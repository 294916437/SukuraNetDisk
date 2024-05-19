<template>
  <div>
    <div class="top">
      <div class="top-op">
        <el-button type="danger" :disabled="selectFileIdList.length == 0" @click="delFileBatch">
          <span class="iconfont icon-del">批量删除</span>
        </el-button>
        <div class="search-panel">
          <!--input输入-->
          <el-input
            clearable
            placeholder="请输入文件名搜索"
            v-model="fileNameFuzzy"
            @keyup.enter="search"
          >
            <template #suffix>
              <i class="iconfont icon-search" @click="search"></i>
            </template>
          </el-input>
        </div>
        <div class="iconfont icon-refresh" @click="loadDataList"></div>
      </div>
      <!---导航-->
      <FolderNavigator ref="navigationRef" @navChange="navChange"></FolderNavigator>
    </div>
    <div class="file-list">
      <FileTable
        ref="dataTableRef"
        :columns="columns"
        :dataSource="tableData"
        :fetch="loadDataList"
        :initFetch="false"
        :options="tableOptions"
        @rowSelected="rowSelected"
      >
        <!--显示文件行详细，包括文件图片、名称、操作图标-->
        <template #fileName="{ index, row }">
          <div class="file-item" @mouseenter="showOp(row)" @mouseleave="cancelshowOp(row)">
            <template v-if="(row.fileType == 3 || row.fileType == 1) && row.status == 2">
              <FileIcon :cover="row.fileCover" :width="32"></FileIcon>
            </template>
            <template v-else>
              <FileIcon v-if="row.folderType == 0" :fileType="row.fileType"></FileIcon>
              <FileIcon v-if="row.folderType == 1" :fileType="0"></FileIcon>
            </template>
            <span class="file-name" :title="row.fileName" v-if="!row.showEdit">
              <span @click="preview(row)">{{ row.fileName }}</span>
              <span v-if="row.status == 0" class="transfer-status">转码中</span>
              <span v-if="row.status == 1" class="transfer-status transfer-fail">转码失败</span>
            </span>
            <!--显示文件系列操作-->>
            <div class="edit-panel" v-if="row.showEdit">
              <el-input
                v-model.trim="row.fileNameReal"
                ref="editNameRef"
                :maxLength="180"
                @keyup.enter="saveNameEdit(index)"
              >
                <template #suffix>{{ row.fileSuffix }}</template>
              </el-input>
              <span
                :class="[`iconfont icon-right1`, row.fileNameReal ? '' : 'not-allow']"
                @click="saveNameEdit(index)"
              ></span>
              <span class="iconfont icon-error" @click="cancelNameEdit(index)"></span>
            </div>
            <span class="op">
              <template v-if="row.showOp && row.fileId && row.status == 2">
                <span
                  class="iconfont icon-download"
                  v-if="row.folderType == 0"
                  @click="download(row)"
                  >下载</span
                >
                <span class="iconfont icon-del" @click="delFile(row)">删除</span>
              </template>
            </span>
          </div>
        </template>
        <!--显示文件具体大小-->
        <template #fileSize="{ index, row }">
          <span v-if="row.fileSize">
            {{ proxy.Utils.size2Str(row.fileSize) }}
          </span>
        </template>
      </FileTable>
    </div>
    <Preview ref="previewRef"></Preview>
  </div>
</template>

<script setup>
import { ref, getCurrentInstance } from "vue";
const { proxy } = getCurrentInstance();
const currentFolder = ref({ fileId: 0 });
const api = {
  loadDataList: "/admin/loadFileList",
  delFile: "/admin/delFile",
  createDownloadUrl: "/admin/createDownloadUrl",
  download: "/api/admin/download",
};
const columns = [
  {
    lable: "文件名",
    prop: "fileName",
    scopedSlots: "fileName",
  },
  {
    lable: "发布人",
    prop: "nickName",
    width: 180,
  },
  {
    lable: "修改时间",
    prop: "lastUpdateTime",
    width: 220,
  },
  {
    lable: "大小",
    prop: "fileSize",
    scopedSlots: "fileSize",
    width: 120,
  },
];
//搜索文件
const search = () => {
  showLoading.value = true;
  loadDataList();
};
const tableData = ref({});
const tableOptions = ref({
  exHeight: 50,
  selectType: "checkbox",
  showIndex: false,
});
const selectFileIdList = ref([]);
const rowSelected = (rows) => {
  selectFileIdList.value = [];
  rows.forEach((item) => {
    selectFileIdList.value.push(item.userId + "_" + item.fileId);
  });
};
const showLoading = ref(false);
const fileNameFuzzy = ref();
const loadDataList = async () => {
  let params = {
    pageNo: tableData.value.pageNo,
    pageSize: tableData.value.pageSize,
    fileNameFuzzy: fileNameFuzzy.value,
    filePid: currentFolder.value.fileId,
  };

  let result = await proxy.Request({
    url: api.loadDataList,
    showLoading: showLoading.value,
    params: params,
  });
  if (!result) {
    return;
  }
  tableData.value = result.data;
};
//监听路由变化
const navChange = (data) => {
  const { curFolder } = data;
  currentFolder.value = curFolder;
  showLoading.value = true;
  loadDataList();
};
//展示操作按钮
const showOp = (row) => {
  tableData.value.list.forEach((element) => {
    element.showOp = false;
  });
  row.showOp = true;
};
const cancelshowOp = (row) => {
  row.showOp = false;
};
//点击文件名后的预览操作
const navigationRef = ref();
const previewRef = ref();
const preview = (data) => {
  if (data.folderType == 1) {
    navigationRef.value.openFolder(data);
  }
  if (data.status != 2) {
    proxy.Message.warning("文件正在转码中");
    return;
  }
  previewRef.value.showPreview(data, 1);
};
//删除单个文件
const delFile = (row) => {
  proxy.Confirm(`你确定要删除${row.fileName}吗？`, async () => {
    let result = await proxy.Request({
      url: api.delFile,
      params: {
        fileIdsAndUserIds: row.userId + "_" + row.fileId,
      },
    });
    if (!result) {
      return;
    }
    loadDataList();
  });
};
//删除多个文件
const delFileBatch = () => {
  if (selectFileIdList.value.length == 0) {
    return;
  }
  proxy.Confirm(`你确定要删除这些文件吗？`, async () => {
    let result = await proxy.Request({
      url: api.delFile,
      params: {
        fileIdsAndUserIds: selectFileIdList.value.join(","),
      },
    });
    if (!result) {
      return;
    }
    loadDataList();
  });
};
//下载文件
const download = async (row) => {
  let result = await proxy.Request({
    url: api.createDownloadUrl + "/" + row.userId + "/" + row.fileId,
  });
  if (!result) {
    return;
  }
  window.location.href = api.download + "/" + result.data;
};
</script>

<style lang="scss" scoped>
@import "/src/assets/file.list.scss";

.file-list {
  margin-top: 10px;
  .file-item {
    .op {
      width: 120px;
    }
  }
}
</style>

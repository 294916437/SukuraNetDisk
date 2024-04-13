<template>
  <div>
    <div class="top">
      <div class="top-op">
        <div class="btn">
          <el-upload
            :show-file-list="false"
            :with-credentials="true"
            :multiple="true"
            :accept="fileAccept"
            :http-request="addFile"
          >
            <el-button type="primary">
              <span class="iconfont icon-upload">上传文件</span>
            </el-button>
          </el-upload>
        </div>
        <el-button type="success" @click="newFolder">
          <span class="iconfont icon-folder-add">新建文件夹</span>
        </el-button>
        <el-button
          type="warning"
          :disabled="selectFileIdList.length == 0"
          @click="moveFolderBatch"
        >
          <span class="iconfont icon-move">批量移动</span>
        </el-button>
        <el-button
          type="danger"
          :disabled="selectFileIdList.length == 0"
          @click="delFileBatch"
        >
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
        :initFetch="true"
        :options="tableOptions"
        @rowSelected="rowSelected"
      >
        <!--显示文件行详细，包括文件图片、名称、操作图标-->
        <template #fileName="{ index, row }">
          <div
            class="file-item"
            @mouseenter="showOp(row)"
            @mouseleave="cancelshowOp(row)"
          >
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
              <span v-if="row.status == 1" class="transfer-status transfer-fail"
                >转码失败</span
              >
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
                <span class="iconfont icon-share1">分享</span>
                <span class="iconfont icon-download" v-if="row.folderType == 0"
                  >下载</span
                >
                <span class="iconfont icon-del" @click="delFile(row)">删除</span>
                <span class="iconfont icon-edit" @click="editFileName(index)"
                  >重命名</span
                >
                <span class="iconfont icon-move" @click="moveFolder(row)">移动</span>
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
    <FolderSelect ref="folderSelectRef" @folderSelect="moveFolderDone"></FolderSelect>
    <Preview ref="previewRef"></Preview>
  </div>
</template>

<script setup>
import { ref, reactive, getCurrentInstance, nextTick, computed } from "vue";
import CategoryInfo from "../../js/CategoryInfo";
const { proxy } = getCurrentInstance();
const emit = defineEmits("addFile");
const addFile = (fileData) => {
  emit("addFile", {
    file: fileData.file,
    filePid: currentFolder.value.fileId,
  });
};
//上传文件后回调函数
const reload = () => {
  showLoading.value = false;
  loadDataList();
};
defineExpose({ reload });
const fileAccept = computed(() => {
  const categoryItem = CategoryInfo[category.value];
  return categoryItem ? categoryItem.accept : "*";
});
//当前目录
const currentFolder = ref({ fileId: 0 });
const api = {
  loadDataList: "/file/loadDataList",
  rename: "/file/rename",
  newFolder: "/file/newFolder",
  getFolderInfo: "/file/getFolderInfo",
  delFile: "/file/delFile",
  changeFileFolder: "/file/changeFileFolder",
  createDownloadUrl: "/file/createDownloadUrl",
  download: "/api/file/download",
};

const columns = [
  {
    lable: "文件名",
    prop: "fileName",
    scopedSlots: "fileName",
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
const showLoading = ref(true);
const fileNameFuzzy = ref();
const category = ref();
const loadDataList = async () => {
  let params = {
    pageNo: tableData.value.pageNo,
    pageSize: tableData.value.pageSize,
    fileNameFuzzy: fileNameFuzzy.value,
    filePid: currentFolder.value.fileId,
    category: category.value,
  };
  if (params.category !== "all") {
    delete params.filePid;
  }
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

//编辑row,控制每次只能新建一次文件夹
const editNameRef = ref();
const editing = ref(false);
//新建文件夹
const newFolder = () => {
  if (editing.value) {
    return;
  }
  tableData.value.list.forEach((element) => {
    element.showEdit = false;
  });
  editing.value = true;
  tableData.value.list.unshift({
    showEdit: true,
    fileType: 0,
    fileId: "",
    filePid: 0,
  });
  nextTick(() => {
    editNameRef.value.focus();
  });
};
//取消新建目录
const cancelNameEdit = (index) => {
  const fileData = tableData.value.list[index];
  if (fileData.fileId) {
    fileData.showEdit = false;
  } else {
    tableData.value.list.splice(index, 1);
    editing.value = false;
  }
};
//确定新建目录
const saveNameEdit = async (index) => {
  const { fileId, filePid, fileNameReal } = tableData.value.list[index];
  if (
    fileNameReal == "" ||
    fileNameReal.indexOf("/") != -1 ||
    fileNameReal.indexOf("\\") != -1
  ) {
    proxy.Message.warning("文件名不能为空或含有斜杠");
    return;
  }
  let url = api.rename;
  if (fileId == "") {
    url = api.newFolder;
  }
  let result = await proxy.Request({
    url: url,
    params: {
      fileId: fileId,
      filePid: filePid,
      fileName: fileNameReal,
    },
  });
  if (!result) {
    return;
  }
  tableData.value.list[index] = result.data;
  editing.value = false;
};
//文件重命名
const editFileName = (index) => {
  if (tableData.value.list[0].fileId == "") {
    tableData.value.list.splice(0, 1);
    index = index - 1;
  }
  tableData.value.list.forEach((element) => {
    element.showEdit = false;
  });
  let currentData = tableData.value.list[index];
  currentData.showEdit = true;
  //编辑文件
  if (currentData.folderType == 0) {
    currentData.fileNameReal = currentData.fileName.substring(
      0,
      currentData.fileName.indexOf(".")
    );
    currentData.fileSuffix = currentData.fileName.substring(
      currentData.fileName.indexOf(".")
    );
  } else {
    currentData.fileNameReal = currentData.fileName;
    currentData.fileSuffix = "";
  }
  editing.value = true;
  nextTick(() => {
    editNameRef.value = focus();
  });
};
//文件删除
const selectFileIdList = ref([]);
const rowSelected = (rows) => {
  selectFileIdList.value = [];
  rows.forEach((item) => {
    selectFileIdList.value.push(item.fileId);
  });
};
//删除单个文件
const delFile = (row) => {
  proxy.Confirm(`你确定要删除${row.fileName}吗？`, async () => {
    let result = await proxy.Request({
      url: api.delFile,
      params: {
        fileIds: row.fileId,
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
        fileIds: selectFileIdList.value.join(","),
      },
    });
    if (!result) {
      return;
    }
    loadDataList();
  });
};
const folderSelectRef = ref();
const currentMoveFile = ref({});
const moveFolder = (data) => {
  currentMoveFile.value = data;
  folderSelectRef.value.showFolderDialog(currentFolder.value.fileId);
};

const moveFolderBatch = () => {
  currentMoveFile.value = {};
  folderSelectRef.value.showFolderDialog(currentFolder.value.fileId);
};
//移动操作(包括单文件和多文件移动)
const moveFolderDone = async (folderId) => {
  if (currentFolder.value.fileId == folderId) {
    proxy.Message.warning("无法移动到当前目录");
    return;
  }
  let fileIdArray = [];
  if (currentMoveFile.value.fileId) {
    fileIdArray.push(currentMoveFile.value.fileId);
  } else {
    fileIdArray = fileIdArray.concat(selectFileIdList.value);
  }
  let result = await proxy.Request({
    url: api.changeFileFolder,
    params: {
      fileIds: fileIdArray.join(","),
      filePid: folderId,
    },
  });
  if (!result) {
    return;
  }
  folderSelectRef.value.close();
  loadDataList();
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
  previewRef.value.showPreview(data, 0);
};

const navChange = (data) => {
  const { categoryId, curFolder } = data;
  currentFolder.value = curFolder;
  category.value = categoryId;
  loadDataList();
};
</script>

<style lang="scss" scoped>
@import "/src/assets/file.list.scss";
</style>

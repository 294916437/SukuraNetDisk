<template>
  <div class="top">
    <el-button type="success" @click="revertBatch" :disabled="selectFileIdList.length == 0">
      <span class="iconfont icon-revert"></span>还原</el-button
    >
    <el-button type="danger" @click="delFileBatch" :disabled="selectFileIdList.length == 0">
      <span class="iconfont icon-del"></span>批量删除</el-button
    >
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
      <template #fileName="{ index, row }">
        <div class="file-item" @mouseenter="showOp(row)" @mouseleave="cancelshowOp(row)">
          <template v-if="(row.fileType == 3 || row.fileType == 1) && row.status != 0">
            <FileIcon :cover="row.fileCover" :width="32"></FileIcon>
          </template>
          <template v-else>
            <FileIcon v-if="row.folderType == 0" :fileType="row.fileType"></FileIcon>
            <FileIcon v-if="row.folderType == 1" :fileType="0"></FileIcon
          ></template>
          <span class="file-name" :title="row.fileName">{{ row.fileName }}</span>
          <span class="op">
            <template v-if="row.showOp">
              <span class="iconfont icon-link" @click="revert(row)">还原</span>
              <span class="iconfont icon-cancel" @click="delFile(row)">彻底删除</span>
            </template>
          </span>
        </div>
      </template>
      <template #fileSize="{ index, row }">
        <span v-if="row.fileSize"> {{ proxy.Utils.size2Str(row.fileSize) }}</span>
      </template>
    </FileTable>
  </div>
</template>

<script setup>
import { ref, getCurrentInstance } from "vue";
const { proxy } = getCurrentInstance();
const api = {
  loadDataList: "/recycle/loadRecycleList",
  delFile: "/recycle/delFile",
  recoverFile: "/recycle/recoverFile",
};

const columns = [
  {
    lable: "文件名",
    prop: "fileName",
    scopedSlots: "fileName",
  },
  {
    lable: "删除时间",
    prop: "recoveryTime",
    width: 220,
  },
  {
    lable: "大小",
    prop: "fileSize",
    scopedSlots: "fileSize",
    width: 120,
  },
];
const tableData = ref({});
const tableOptions = ref({
  exHeight: 50,
  selectType: "checkbox",
});
const loadDataList = async () => {
  let params = {
    pageNo: tableData.value.pageNo,
    pageSize: tableData.value.pageSize,
  };
  let result = await proxy.Request({
    url: api.loadDataList,
    params,
  });
  if (!result) {
    return;
  }
  tableData.value = result.data;
};
//文件删除
const selectFileIdList = ref([]);
const rowSelected = (rows) => {
  selectFileIdList.value = [];
  rows.forEach((item) => {
    selectFileIdList.value.push(item.fileId);
  });
};
const showOp = (row) => {
  tableData.value.list.forEach((item) => {
    item.showOp = false;
  });
  row.showOp = true;
};
const cancelshowOp = (row) => {
  row.showOp = false;
};
//恢复
const revert = async (row) => {
  proxy.Confirm(`确定要还原【${row.fileName}】吗？`, async () => {
    let result = await proxy.Request({
      url: api.recoverFile,
      params: { fileIds: row.fileId },
    });
    if (!result) {
      return;
    }
    loadDataList();
  });
};
//批量恢复
const revertBatch = async (row) => {
  proxy.Confirm("确定要还原这些文件吗？", async () => {
    let result = await proxy.Request({
      url: api.recoverFile,
      params: { fileIds: selectFileIdList.value.join(",") },
    });
    if (!result) {
      return;
    }
    loadDataList();
  });
};
//删除
const emits = defineEmits(["reload"]);
const delFile = async (row) => {
  proxy.Confirm(`确定要彻底删除【${row.fileName}】吗？`, async () => {
    let result = await proxy.Request({
      url: api.delFile,
      params: { fileIds: row.fileId },
    });
    if (!result) {
      return;
    }
    loadDataList();
    emits("reload");
  });
};
//批量删除
const delFileBatch = async (row) => {
  proxy.Confirm("确定要彻底删除这些文件吗？", async () => {
    let result = await proxy.Request({
      url: api.delFile,
      params: { fileId: selectFileIdList.value.join(",") },
    });
    if (!result) {
      return;
    }
    loadDataList();
    emits("reload");
  });
};
</script>

<style lang="scss" scoped>
@import "../../assets/file.list.scss";
.file-list {
  margin-top: 10px;
  .file-item {
    .op {
      width: 150px;
      margin-right: 60px;
    }
  }
}
</style>

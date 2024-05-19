<template>
  <div>
    <div class="top">
      <el-button
        type="primary"
        :disabled="selectFileIdList.length == 0"
        @click="cancelShareBatch"
      >
        <span class="iconfont icon-cancel"></span>
        取消分享
      </el-button>
    </div>

    <div class="file-list">
      <FileTable
        ref="dataTableRef"
        :columns="columns"
        :dataSource="tableData"
        :fetch="loadShareList"
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
                <span class="iconfont icon-link" @click="copy(row)">复制链接</span>
                <span class="iconfont icon-cancel" @click="cancelShare(row)">取消分享</span>
              </template>
            </span>
          </div>
        </template>
        <template #expireTime="{ index, row }">
          {{ row.validType == 3 ? "永久" : row.expireTime }}</template
        >
      </FileTable>
    </div>
  </div>
</template>

<script setup>
import useClipboard from "vue-clipboard3";
const { toClipboard } = useClipboard();
import { ref, getCurrentInstance } from "vue";
const { proxy } = getCurrentInstance();
const api = {
  loadShareList: "/share/loadShareList",
  cancelShare: "/share/cancelShare",
};
const shareUrl = ref(document.location.origin + "/share/");
const columns = [
  {
    lable: "文件名",
    prop: "fileName",
    scopedSlots: "fileName",
  },
  {
    lable: "分享时间",
    prop: "shareTime",
    width: 220,
  },
  {
    lable: "失效时间",
    prop: "expireTime",
    scopedSlots: "expireTime",
    width: 220,
  },
  {
    lable: "浏览次数",
    prop: "viewCount",
    width: 220,
  },
];
//搜索文件
const search = () => {
  showLoading.value = true;
  loadShareList();
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
    selectFileIdList.value.push(item.fileId);
  });
};
const loadShareList = async () => {
  let params = {
    pageNo: tableData.value.pageNo,
    pageSize: tableData.value.pageSize,
  };
  let result = await proxy.Request({
    url: api.loadShareList,
    params,
  });
  if (!result) {
    return;
  }
  tableData.value = result.data;
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
//复制链接
const copy = async (data) => {
  await toClipboard(`链接:${shareUrl.value}${data.shareId} 提取码:${data.code}`);
  proxy.Message.success("复制成功");
};
//取消分享
const cancelshareIdList = ref([]);
const cancelShareBatch = () => {
  if (selectFileIdList.value.length == 0) {
    proxy.Message.warning("请选择要取消分享的文件");
    return;
  }
  cancelshareIdList.value - selectFileIdList.value;
  cancelShareDone();
};
const cancelShare = (row) => {
  cancelshareIdList.value = [row.shareId];
  cancelShareDone();
};
const cancelShareDone = () => {
  proxy.Confirm(`你确定取消分享吗?`, async () => {
    let result = await proxy.Request({
      url: api.cancelShare,
      params: {
        shareIds: cancelshareIdList.value.join(","),
      },
    });
    if (!result) {
      return;
    }
    proxy.Message.success("取消分享成功");
    loadShareList();
  });
};
</script>

<style lang="scss" scoped>
@import "/src/assets/file.list.scss";
.file-list {
  margin-top: 10px;
  .file-item {
    .file-name {
      span {
        cursor: pointer;
        &:hover {
          color: hsl(330, 80%, 80%);
        }
      }
    }
    .op {
      width: 170px;
    }
  }
}
</style>

<template>
  <div>
    <Dialog
      :show="dialogConfig.show"
      :title="dialogConfig.title"
      :buttons="dialogConfig.buttons"
      :width="32"
      :height="22"
      :showCancel="false"
      @close="dialogConfig.show = false"
    >
      <div class="navigation-panel">
        <FolderNavigator
          ref="navigationRef"
          @navChange="navChange"
          :watchPath="false"
        ></FolderNavigator>
      </div>
      <div class="folder-list" v-if="folderList.length > 0">
        <div class="folder-item" v-for="item in folderList" @click="selectFolder(item)">
          <FileIcon :fileType="0"></FileIcon>
          <span class="file-name">{{ item.fileName }}</span>
        </div>
      </div>
      <div v-else class="tips">
        移动到<span>{{ currentFolder.fileName }}</span>
      </div>
    </Dialog>
  </div>
</template>

<script setup>
import { ref, reactive, getCurrentInstance, nextTick } from "vue";
const { proxy } = getCurrentInstance();
const api = {
  loadAllFolder: "/file/loadAllFolder",
};
const dialogConfig = ref({
  show: false,
  title: "移动文件",
  buttons: [
    {
      type: "primary",
      text: "确定",
      click: (e) => {
        folderSelect();
      },
      text: "移动到此",
    },
  ],
});
//父级ID
const filePid = ref("0");
const currentFileIds = ref([]);
const folderList = ref([]);
const currentFolder = ref({});

const loadAllFolder = async () => {
  let result = await proxy.Request({
    url: api.loadAllFolder,
    params: {
      filePid: filePid.value,
      currentFileIds: currentFileIds.value,
    },
  });
  if (!result) {
    return;
  }
  folderList.value = result.data;
};
const close = () => {
  dialogConfig.value.show = false;
};
const showFolderDialog = (currentFolder) => {
  dialogConfig.value.show = true;
  currentFileIds.value = currentFolder;
  loadAllFolder();
};
defineExpose({
  showFolderDialog,
  close,
});
//选择目录
const navigationRef = ref();
const selectFolder = (data) => {
  navigationRef.value.openFolder(data);
};
//确定选择目录
const emit = defineEmits(["folderSelect"]);
const folderSelect = () => {
  emit("folderSelect", filePid.value);
};
//导航改变回调
const navChange = (data) => {
  const { curFolder } = data;
  currentFolder.value = curFolder;
  filePid.value = curFolder.fileId;
  loadAllFolder();
};
</script>

<style lang="scss" scoped>
.navigation-panel {
  padding-left: 10px;
  background: #f1f1f1;
}

.folder-list {
  max-height: calc(100vh - 100px);
  .folder-item {
    cursor: pointer;
    display: flex;
    align-items: center;
    padding: 10px;

    .file-name {
      display: inline-block;
      margin-left: 10px;
    }

    &:hover {
      background: #f8f8f8;
    }
  }
}

.tips {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  margin-top: 2em;
  font-size: 1em;
  span {
    font-weight: bold;
    color: hsl(330, 80%, 80%);
  }
}
</style>

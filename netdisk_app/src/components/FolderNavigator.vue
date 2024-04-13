<template>
  <div class="top-navigation">
    <template v-if="folderList.length > 0">
      <span class="back link" @click="backParent">返回上一级</span>
      <el-divider direction="vertical"></el-divider>
    </template>
    <span v-if="folderList.length == 0" class="all-file">全部文件</span>
    <span v-if="folderList.length > 0" class="link" @click="setCurrentFolder(-1)"
      >全部文件</span
    >
    <template v-for="(item, index) of folderList">
      <span class="iconfont icon-right"></span>
      <span
        class="link"
        v-if="index < folderList.length - 1"
        @click="setCurrentFolder(index)"
        >{{ item.fileName }}</span
      >
      <span v-if="index == folderList.length - 1" class="text">{{ item.fileName }}</span>
    </template>
  </div>
</template>

<script setup>
import { ref, reactive, getCurrentInstance, nextTick, watch } from "vue";
import { useRouter, useRoute } from "vue-router";
const { proxy } = getCurrentInstance();
const router = useRouter();
const route = useRoute();
const props = defineProps({
  watchPath: {
    type: Boolean,
    default: true,
  },
  shareId: {
    type: String,
  },
  adminShow: {
    type: Boolean,
    default: false,
  },
});
const category = ref();
const api = {
  getFolderInfo: "/file/getFolderInfo",
  getFolderInfoShare: "/showShare/getFolderInfo",
  getFolderInfoAdmin: "/admin/getFolderInfo",
};
//所以目录层级
const folderList = ref([]);
//当前目录
const currentFolder = ref({
  fileId: "0",
});
const init = () => {
  folderList.value = [];
  currentFolder.value = { fileId: "0" };
  doCallback();
};
const openFolder = (data) => {
  const { fileId, fileName } = data;
  const folder = {
    fileName: fileName,
    fileId: fileId,
  };
  folderList.value.push(folder);
  currentFolder.value = folder;
  setPath();
};
defineExpose({ openFolder });
//返回上级目录
const backParent = () => {
  let currentIndex = null;
  for (let i = 0; i < folderList.value.length; i++) {
    if (folderList.value[i].fileId == currentFolder.value.fileId) {
      currentIndex = i;
      break;
    }
  }
  setCurrentFolder(currentIndex - 1);
};
//点击导航 设置当前目录
const setCurrentFolder = (index) => {
  if (index == -1) {
    currentFolder.value = { fileId: "0" };
    folderList.value = [];
  } else {
    currentFolder.value = folderList.value(index);
    folderList.value.splice(index + 1, folderList.value.length);
  }
  setPath();
};
const setPath = () => {
  if (!props.watchPath) {
    doCallback();
    return;
  }
  let pathArray = [];
  folderList.value.forEach((item) => {
    pathArray.push(item.fileId);
  });
  router.push({
    path: route.path,
    query: pathArray.length == 0 ? "" : { path: pathArray.join("/") },
  });
};
//获取当前路径的目录
const getNavigationFolder = async (path) => {
  let url = api.getFolderInfo;
  if (props.shareId) {
    url = api.getFolderInfoShare;
  }
  if (props.adminShow) {
    url = api.getFolderInfoAdmin;
  }
  let result = await proxy.Request({
    url: url,
    params: {
      path: path,
      shareId: props.shareId,
    },
  });
  if (!result) {
    return;
  }
  folderList.value = result.data;
};
const emit = defineEmits(["navChange"]);
const doCallback = () => {
  emit("navChange", {
    categoryId: category.value,
    curFolder: currentFolder.value,
  });
};
watch(
  () => route,
  (newVal, oldVal) => {
    if (!props.watchPath) {
      return;
    }
    if (newVal.path.indexOf("/main") === -1) {
      return;
    }
    const path = newVal.query.path;
    category.value = newVal.params.category;
    if (path == undefined) {
      init();
    } else {
      getNavigationFolder(path);
      let pathArray = path.split("/");
      currentFolder.value = {
        fileId: pathArray[pathArray.length - 1],
      };
      doCallback();
    }
  },
  { immediate: true, deep: true }
);
</script>
<style lang="scss" scoped>
.top-navigation {
  display: flex;
  font-size: 13px;
  align-items: center;
  line-height: 40px;
  .all-file {
    font-weight: bold;
  }

  .link {
    color: hsl(330, 80%, 80%);
    cursor: pointer;
    &:hover {
      opacity: 50%;
    }
  }

  .icon-right {
    color: hsl(330, 80%, 80%);
    padding: 0px 5px;
    font-size: 13px;
  }
}
</style>

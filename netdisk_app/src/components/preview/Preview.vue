<template>
  <PreviewImage
    ref="imageViewRef"
    :imageList="[imageUrl]"
    v-if="fileInfo.fileCategory == 3"
  >
  </PreviewImage>
  <Window
    :show="showWindow"
    @close="closeWindow"
    :width="fileInfo.fileCategory == 1 ? 1200 : 800"
    :title="fileInfo.fileName"
    :align="fileInfo.fileCategory == 1 ? 'center' : 'top'"
    v-else
  >
    <PreviewVideo :url="url" v-if="fileInfo.fileCategory == 1"></PreviewVideo>
    <PreviewDoc :url="url" v-if="fileInfo.fileType == 5"></PreviewDoc>
    <PreviewExcel :url="url" v-if="fileInfo.fileType == 6"></PreviewExcel>
    <PreviewPdf :url="url" v-if="fileInfo.fileType == 4"></PreviewPdf>
    <PreviewTxT
      :url="url"
      v-if="fileInfo.fileType == 7 || fileInfo.fileType == 8"
    ></PreviewTxT>
    <PreviewMusic
      :url="url"
      v-if="fileInfo.fileType == 2"
      :fileName="fileInfo.fileName"
    ></PreviewMusic>
    <PreviewDownload
      :downloadUrl="downloadUrl"
      :createDownloadUrl="createDownloadUrl"
      :fileInfo="fileInfo"
      v-if="fileInfo.fileCategory == 5 && fileInfo.fileType != 8"
    ></PreviewDownload>
  </Window>
</template>
<script setup>
import PreviewDownload from "./PreviewDownload.vue";
import PreviewMusic from "./PreviewMusic.vue";
import PreviewTxT from "./PreviewTxT.vue";
import PreviewPdf from "./PreviewPdf.vue";
import PreviewImage from "./PreviewImage.vue";
import PreviewExcel from "./PreviewExcel.vue";
import PreviewDoc from "./PreviewDoc.vue";
import PreviewVideo from "./PreviewVideo.vue";
import { ref, reactive, getCurrentInstance, nextTick, computed } from "vue";
const { proxy } = getCurrentInstance();

const imageUrl = computed(() => {
  return (
    proxy.globalInfo.imageUrl +
    fileInfo.value.fileCover.replaceAll("_.", ".") +
    "?timestamp=" +
    new Date().getTime()
  );
});
const showWindow = ref(false);
const closeWindow = () => {
  showWindow.value = false;
};
const FILE_URL_MAP = {
  0: {
    fileUrl: "/file/getFile/",
    videoUrl: "/file/ts/getVideoInfo",
    createDownloadUrl: "/file/createDownloadUrl",
    downloadUrl: "/api/file/download",
  },
  1: {
    fileUrl: "/admin/getFile",
    videoUrl: "/admin/ts/getVideoInfo",
    createDownloadUrl: "/admin/createDownloadUrl",
    downloadUrl: "/api/admin/download",
  },
  2: {
    fileUrl: "/showShare/getFile",
    videoUrl: "/showShare/getVideoInfo",
    createDownloadUrl: "/showShare/createDownloadUrl",
    downloadUrl: "/showShare/download",
  },
};
const url = ref(null);
const createDownloadUrl = ref(null);
const downloadUrl = ref(null);

const imageViewRef = ref();
const fileInfo = ref({});
const showPreview = (data, showPart) => {
  fileInfo.value = data;
  if (data.fileCategory == 3) {
    nextTick(() => {
      imageViewRef.value.show(0);
    });
  } else if (data.fileCategory != null) {
    showWindow.value = true;
    let _url = FILE_URL_MAP[showPart].fileUrl;
    let _createDownloadUrl = FILE_URL_MAP[showPart].createDownloadUrl;
    let _downloadUrl = FILE_URL_MAP[showPart]._downloadUrl;
    if (data.fileCategory == 1) {
      _url = FILE_URL_MAP[showPart].videoUrl;
    }
    if (showPart == 0) {
      _url = _url + "/" + data.fileId;
      _createDownloadUrl = _createDownloadUrl + "/" + data.fileId;
    }
    url.value = _url;
    createDownloadUrl.value = _createDownloadUrl;
    downloadUrl.value = _downloadUrl;
  }
};
defineExpose({ showPreview });
</script>

<style lang="scss" scoped></style>

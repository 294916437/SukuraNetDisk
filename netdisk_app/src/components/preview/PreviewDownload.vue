<template>
  <div class="others">
    <div class="body-content">
      <fileIcon
        :iconName="fileInfo.fileType == 9 ? 'zip' : 'others'"
        :width="80"
      ></fileIcon>
      <div class="file-name">{{ fileInfo.fileName }}</div>
      <div class="tips">该类型的文件不支持预览，请下载后查看</div>
      <div class="donwload-btn">
        <el-button type="primary" @click="download">
          {{ proxy.Utils.size2Str(fileInfo.fileSize) }}:点击下载
        </el-button>
        <el-button type="primary" @click="downloadUrl"> 下载 </el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, getCurrentInstance, nextTick, onMounted, onUnmounted } from "vue";
const { proxy } = getCurrentInstance();
const props = defineProps({
  createDownloadUrl: {
    type: String,
  },
  downloadUrl: {
    type: String,
  },
  fileInfo: {
    type: Object,
  },
});
const download = async () => {
  let result = await proxy.Request({
    url: props.createDownloadUrl,
  });
  if (!result) {
    return;
  }
  window.location.href = props.downloadUrl + "/" + result.data;
};
</script>

<style lang="scss" scoped>
.others {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  .body-content {
    text-align: center;
    .file-name {
      font-weight: bold;
    }
    .tips {
      color: #999898;
      margin-top: 5px;
      font-size: 13pxS;
    }
    .donwload-btn {
      margin-top: 20px;
      .el-button {
        width: 100px;
        height: 30px;
        font-size: 14px;
      }
    }
  }
}
</style>

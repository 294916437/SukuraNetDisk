<template>
  <div class="image-viewer">
    <el-image-viewer
      :initial-index="previewImageIndex"
      :hide-on-click-modal="true"
      :url-list="imageList"
      :min-scale="scale.min"
      :max-scale="scale.max"
      @close="closeImageView"
      v-if="previewImageIndex != null"
    ></el-image-viewer>
  </div>
</template>
<script setup>
import { ref, reactive, getCurrentInstance, nextTick } from "vue";
const { proxy } = getCurrentInstance();
const props = defineProps({
  imageList: {
    type: Array,
  },
});
const scale = ref({
  min: 0.5,
  max: 1.2,
});
const previewImageIndex = ref(null);
const show = (index) => {
  previewImageIndex.value = index;
};
defineExpose({ show });
const closeImageView = () => {
  previewImageIndex.value = null;
};
</script>

<style lang="scss" scoped>
.image-viewer {
  .el-image-viewer__mask {
    opacity: 0.6;
  }
}
</style>

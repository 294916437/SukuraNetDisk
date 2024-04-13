<template>
  <div ref="player" class="player"></div>
</template>

<script setup>
import DPlayer from "dplayer";
import { ref, reactive, getCurrentInstance, onMounted } from "vue";
const { proxy } = getCurrentInstance();
const props = defineProps({
  url: {
    type: String,
  },
});
const videoInfo = ref({
  video: null,
});
const player = ref();
const initplayer = () => {
  const dp = new DPlayer({
    element: player.value,
    theme: "hsl(330, 80%, 80%)",
    screenshot: true,
    video: {
      url: `/api/${props.url}`,
      type: "customHls",
      customType: {
        customHls: function (video, player) {
          const hls = new Hls();
          hls.loadSource(video.src);
          hls.attachMedia(video);
        },
      },
    },
  });
};
onMounted(() => {
  initplayer();
});
</script>

<style lang="scss" scoped>
.player {
  width: 100%;
  :deep(.dplayer-video-wrap) {
    text-align: center;
    .dplayer-video {
      margin: 0 auto;
      max-height: calc(100vh - 41px);
    }
  }
}
</style>

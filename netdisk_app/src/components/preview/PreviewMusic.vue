<template>
  <div class="music">
    <div class="body-content">
      <div class="cover">
        <img src="" alt="" />
      </div>
      <div ref="playerRef" class="music-player"></div>
    </div>
  </div>
</template>

<script setup>
import APlayer from "APlayer";
import "APlayer/dist/Aplayer.min.css";
import { ref, onMounted, onUnmounted } from "vue";
const props = defineProps({
  url: {
    type: String,
  },
  fileName: {
    type: String,
  },
});
const playerRef = ref();
const player = ref();
onMounted(() => {
  player.value = new APlayer({
    container: playerRef.value,
    preload: "auto",
    audio: {
      url: `/api/${props.url}`,
      cover: "",
      name: `${props.fileName}`,
      artist: "佚名",
      theme: "hsl(330, 80%, 80%)",
    },
  });
});
onUnmounted(() => {
  player.value.destroy();
});
</script>

<style lang="scss" scoped>
.music {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  .body-content {
    text-align: center;
    width: 80%;
    .cover {
      width: 200px;
      margin: 0px auto;
      text-align: center;
      img {
        width: 100%;
      }
    }
  }
}
</style>

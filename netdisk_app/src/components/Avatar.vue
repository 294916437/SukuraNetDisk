<template>
    <span class="avatar" :style="{ width: width + 'px', height: width + 'px' }">
        <img :src="`${proxy.globalInfo.avatarUrl}${userId}?${timestamp}`" v-if="userId" />
    </span>
</template>

<script setup>
import { getCurrentInstance } from "vue"
const { proxy } = getCurrentInstance();
const props = defineProps({
    userId: {
        type: String,
    },
    timestamp: {
        type: Number,
        default: 0,
    },
    width: {
        type: Number,
        default: 40,
    },
})
</script>

<style lang="scss" scoped>
.avatar {
    display: flex;
    border-radius: 50%;
    overflow: hidden;

    img {
        width: 100%;
        object-fit: cover;
    }
}
</style>


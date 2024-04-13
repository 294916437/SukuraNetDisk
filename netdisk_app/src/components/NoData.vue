<template>
    <div class="no-data">
        <div class="msg">{{ msg }}</div>
    </div>
</template>

<script setup>
const props = defineProps({
    msg: {
        type: String,
    },
});
</script>
<style lang="scss" scoped>
.no-data {
    text-align: center;
    padding: 10px 0px;
    .msg {
        font-size: 1.2em;
        font-weight: bold;
        color: #909399;
        margin-top: 10px;
    }
}
</style>


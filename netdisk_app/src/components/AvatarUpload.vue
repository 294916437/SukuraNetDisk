 <template>
  <div class="avatar-upload">
  <div class="avatar-show">
    <template v-if="localFile">
        <img :src="localFile"/>
    </template>
    <template v-else>
        <img :src="`/api/getAvatar/${modelValue.userId}`" />
    </template>
  </div>
</div>
  <div class="select-btn">
    <el-upload
    name="file"
    :show-file-list="false"
    accept=".png,.PNG,.jpg,.JPG,.jpeg,.JPEG,.gif,.GIF,.bmp,.BMP"
    :multiple="false"
    :http-request="uploadImage"
    >
    <el-button type="primary">选择</el-button>
    </el-upload>
  </div>
</template>

<script setup>
import { ref, reactive, getCurrentInstance, nextTick } from "vue"
import { useRouter, useRoute } from "vue-router"
const { proxy } = getCurrentInstance();
const router = useRouter();
const route = useRoute();
const timestamp=ref("");
const props=defineProps({
    modelValue:{
        type:Object,
        default:null,
    },
});
const localFile=ref(null);
const emit=defineEmits();
const uploadImage=async(file)=>{
    file=file.file;
    let img=new FileReader();
    img.readAsDataURL(file);
    img.onload=({target})=>{
        localFile.value=target.result;
    };
    emit("update:modelValue",file);
};
</script>

<style lang="scss" scoped>
.avatar-upload{
    display: flex;
    justify-content: center;
    align-items: end;
    .avatar-show{
        display: flex;
        position: relative;
        width: 100%;
        height: 100%;
        background: hsl(300, 5%, 4%);
        justify-content: center;
        align-items: center;
        overflow: hidden;
        .iconfont{
            font-size: 50px;
            color: hsl(330, 80%, 80%);
        }
        img{
            width: 100%;
            height: 100%;
        }
        .op{
            position: absolute;
            top:80px;
            color: hsl(330, 80%, 80%);
        }
    }
    .select-btn{
        margin-left: 10px;
        vertical-align: bottom;
    }
}
</style>

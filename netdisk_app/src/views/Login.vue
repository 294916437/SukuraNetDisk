<template>
  <div class="login-body">
    <div class="bg"></div>
    <div class="login-panel">
      <el-form
        class="login-register"
        :model="formData"
        :rules="rules"
        ref="formDataRef"
        @submit.prevent
      >
        <div class="login-title">樱花云盘</div>
        <!--输入账号-->
        <el-form-item prop="email">
          <el-input
            size="large"
            clearable
            placeholder="请输入邮箱"
            v-model.trim="formData.email"
            maxLength="150"
          >
            <template #prefix>
              <span class="iconfont icon-account"></span>
            </template>
          </el-input>
        </el-form-item>
        <!--输入密码-->
        <el-form-item prop="password" v-if="opType == 1">
          <el-input
            size="large"
            type="password"
            clearable
            placeholder="请输入密码"
            v-model.trim="formData.password"
            show-password
          >
            <template #prefix>
              <span class="iconfont icon-password"></span>
            </template>
          </el-input>
        </el-form-item>
        <!--注册账号-->
        <div v-if="opType == 0 || opType == 2">
          <el-form-item prop="emailCode">
            <div class="send-email-panel">
              <el-input
                size="large"
                placeholder="请输入邮箱验证码"
                v-model.trim="formData.emailCode"
              >
                <template #prefix>
                  <span class="iconfont icon-checkcode"></span>
                </template>
              </el-input>
              <el-button
                class="send-email-btn"
                type="priamry"
                size="large"
                color="rgb(189, 255, 255,0.6)"
                @click="getEmailCode()"
              >
                获取验证码
              </el-button>
            </div>
          </el-form-item>

          <!--昵称-->

          <el-form-item prop="nickName" v-if="opType == 0">
            <el-input
              clearable
              size="large"
              placeholder="请输入昵称"
              v-model.trim="formData.nickName"
              max-length="20"
            >
              <template #prefix>
                <span class="iconfont icon-account"></span>
              </template>
            </el-input>
          </el-form-item>

          <!--输入密码-->
          <el-form-item prop="registerPassword">
            <el-input
              clearable
              type="password"
              size="large"
              placeholder="请输入密码"
              v-model.trim="formData.registerPassword"
              show-password
            >
              <template #prefix>
                <span class="iconfont icon-password"></span>
              </template>
            </el-input>
          </el-form-item>
          <!--重复输入密码-->
          <el-form-item prop="reRegisterPassword">
            <el-input
              clearable
              type="password"
              size="large"
              placeholder="请再次输入密码"
              v-model.trim="formData.reRegisterPassword"
              show-password
            >
              <template #prefix>
                <span class="iconfont icon-password"></span>
              </template>
            </el-input>
          </el-form-item>
        </div>

        <!--输入验证码-->
        <el-form-item prop="checkCode">
          <div class="check-code-panel">
            <el-input
              size="large"
              clearable
              placeholder="请输入验证码"
              v-model.trim="formData.checkCode"
              @keyup.enter="doSubmit"
            >
              <template #prefix>
                <span class="iconfont icon-checkcode"></span>
              </template>
            </el-input>
            <img :src="checkCodeUrl" class="check-code" @click="changeCheckCode(0)" />
          </div>
        </el-form-item>
        <!-- 下拉框 -->
        <el-form-item v-if="opType == 1">
          <div class="rememberme-panel">
            <el-checkbox v-model="formData.rememberMe">记住我！ </el-checkbox>
          </div>
          <div class="no-account">
            <a href="javascript:void(0)" class="a-link" @click="showPanel(2)"
              >忘记密码？</a
            >
            <a href="javascript:void(0)" class="a-link" @click="showPanel(0)"
              >没有账号?</a
            >
          </div>
        </el-form-item>
        <el-form-item v-if="opType == 2">
          <a href="javascript:void(0)" class="a-link" @click="showPanel(1)">邮箱登陆</a>
        </el-form-item>

        <el-form-item v-if="opType == 0">
          <a href="javascript:void(0)" class="a-link" @click="showPanel(1)">已有账号</a>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" class="op-btn" size="large" @click="doSubmit">
            <span v-if="opType == 0">注册</span>
            <span v-if="opType == 1">登陆</span>
            <span v-if="opType == 2">重置密码</span>
          </el-button>
        </el-form-item>
      </el-form>
    </div>
    <Dialog
      :show="dialogConfigSendMailCode.show"
      :title="dialogConfigSendMailCode.title"
      :buttons="dialogConfigSendMailCode.buttons"
      :width="32"
      :showCancel="false"
      @close="dialogConfigSendMailCode.show = false"
    >
      <el-form
        :model="formDataSendMailCode"
        :rules="rules"
        ref="formDataSendMailCodeRef"
        label-width="80px"
        @submit.prevent
      >
        <!--邮箱验证码-->
        <el-form-item label="邮箱">
          {{ formData.email }}
        </el-form-item>
        <el-form-item label="验证码" prop="checkCode">
          <div class="check-code-panel">
            <el-input
              size="large"
              placeholder="请输入验证码"
              v-model.trim="formDataSendMailCode.checkCode"
            >
              <template #prefix>
                <span class="iconfont icon-checkcode"></span>
              </template>
            </el-input>
            <img
              :src="checkCodeUrlforSendMailCode"
              class="check-code"
              @click="changeCheckCode(1)"
            />
          </div>
        </el-form-item>
      </el-form>
    </Dialog>
  </div>
</template>
<script setup>
import { ref, reactive, getCurrentInstance, nextTick, onMounted } from "vue";
import { useRouter, useRoute } from "vue-router";
import md5 from "js-md5";
const { proxy } = getCurrentInstance();
const router = useRouter();
const route = useRoute();
const api = {
  checkCode: "/api/checkCode",
  sendEmailCode: "/sendEmailCode",
  register: "/register",
  login: "/login",
  resetPwd: "/resetPwd",
};
//操作类型
//操作类型 0:注册  1:登陆 2:重置密码
const opType = ref(1);
const showPanel = (type) => {
  opType.value = type;
  resetForm();
};
onMounted(() => {
  showPanel(1);
});
const checkRePassword = (rule, value, callback) => {
  if (value != formData.value.registerPassword) {
    callback(new Error(rule.message));
  } else {
    callback();
  }
};
const formData = ref({});
const formDataRef = ref();
const rules = {
  email: [
    { required: true, message: "请输入邮箱" },
    { validator: proxy.Verify.email, message: "请输入正确的邮箱" },
  ],
  password: [{ required: true, message: "请输入密码" }],
  emailCode: [{ required: true, message: "请输入邮箱验证码" }],
  nickName: [{ required: true, message: "请输入昵称" }],
  registerPassword: [
    { required: true, message: "请输入密码" },
    {
      validator: proxy.Verify.password,
      message: "密码只能是数字、字母、特殊字符的8-18位组合",
    },
  ],
  reRegisterPassword: [
    { required: true, message: "请再次输入密码" },
    { validator: checkRePassword, message: "两次输入的密码不一致" },
  ],
  checkCode: [{ required: true, message: "请输入图片验证码" }],
};
const checkCodeUrl = ref(api.checkCode);
const checkCodeUrlforSendMailCode = ref(api.checkCode);
const changeCheckCode = (type) => {
  if (type == 0) {
    checkCodeUrl.value =
      api.checkCode + "?type=" + type + "&time=" + new Date().getTime();
  } else {
    checkCodeUrlforSendMailCode.value =
      api.checkCode + "?type=" + type + "&time=" + new Date().getTime();
  }
};
//发送邮箱验证码
const formDataSendMailCode = ref({});
const formDataSendMailCodeRef = ref();
const dialogConfigSendMailCode = reactive({
  show: false,
  title: "发送邮箱验证码",
  buttons: [
    {
      type: "primary",
      text: "发送验证码",
      click: (e) => {
        sendEmailCode();
      },
    },
  ],
});
const getEmailCode = () => {
  formDataRef.value.validateField("email", (valid) => {
    if (!valid) {
      return;
    }

    dialogConfigSendMailCode.show = true;
    nextTick(() => {
      changeCheckCode(1);
      formDataSendMailCodeRef.value.resetFields();
      formDataSendMailCode.value = {
        email: formData.value.email,
      };
    });
  });
};
const sendEmailCode = async () => {
  formDataSendMailCodeRef.value.validate(async (valid) => {
    if (!valid) {
      return;
    }
    const params = Object.assign({}, formDataSendMailCode.value);
    params.type = opType.value == 0 ? 0 : 1;
    //发送Ajax请求
    let result = await proxy.Request({
      url: api.sendEmailCode,
      params: params,
      errorCallback: () => {
        changeCheckCode(1);
      },
    });
    if (!result) {
      return;
    }
    proxy.Message.success("发送验证码成功，请通过邮箱查看");
    dialogConfigSendMailCode.show = false;
  });
};
//重置表单
const resetForm = () => {
  changeCheckCode(0);
  formDataRef.value.resetFields;
  formData.value = {};
  //登录
  if (opType.value == 1) {
    const cookieLoginInfo = proxy.VueCookies.get("loginInfo");
    if (cookieLoginInfo) {
      formData.value = cookieLoginInfo;
    }
  }
};
//登陆、注册、重置密码按钮提交
const doSubmit = () => {
  formDataRef.value.validate(async (valid) => {
    if (!valid) {
      return;
    }
    let params = {};
    Object.assign(params, formData.value);
    //注册  找回密码
    if (opType.value == 0 || opType.value == 2) {
      params.password = params.registerPassword;
      delete params.registerPassword;
      delete params.reRegisterPassword;
    }
    //登录
    if (opType.value == 1) {
      let cookieLoginInfo = proxy.VueCookies.get("loginInfo");
      let cookiePassword = cookieLoginInfo == null ? null : cookieLoginInfo.password;
      if (params.password !== cookiePassword) {
        params.password = md5(params.password);
      }
    }
    let url = null;
    if (opType.value == 0) {
      url = api.register;
    } else if (opType.value == 1) {
      url = api.login;
    } else if (opType.value == 2) {
      url = api.resetPwd;
    }
    let result = await proxy.Request({
      url: url,
      params: params,
      errorCallback: () => {
        changeCheckCode(0);
      },
    });
    if (!result) {
      return;
    }
    //注册返回
    if (opType.value == 0) {
      proxy.Message.success("注册成功，请登录");
      showPanel(1);
    } else if (opType.value == 1) {
      if (params.rememberMe) {
        const loginInfo = {
          email: params.email,
          password: params.password,
          rememberMe: params.rerememberMe,
        };
        proxy.VueCookies.set("loginInfo", loginInfo, "7d");
      } else {
        proxy.VueCookies.remove("loginInfo");
      }
      proxy.Message.success("登录成功");
      //存储cookie
      proxy.VueCookies.set("userInfo", result.data, "2h");
      //重定向到登录页面
      const redirectUrl = route.query.redirectUrl || "/";
      router.push(redirectUrl);
    } else if (opType.value == 2) {
      proxy.Message.success("重置密码成功");
      showPanel(1);
    }
  });
};
</script>
<style lang="scss" scoped>
.login-body {
  display: flex;
  height: 100vh;
  background-size: cover;
  background-image: url(../assets/login.jpg);
  background-attachment: fixed;

  .login-panel {
    width: 430px;
    margin: 0 auto; //登陆框水平居中
    margin-top: calc((100vh - 500px) / 2);

    .login-register {
      padding: 25px;
      background: rgb(255, 211, 217, 0.6);
      border-radius: 5px;

      .login-title {
        text-align: center;
        font-size: 1.8em;
        font-weight: bolder;
        color: hsl(330, 90%, 75%);
      }

      .send-email-panel {
        display: flex;
        width: 100%;
        justify-content: space-between;

        .send-email-btn {
          margin-left: 5px;
        }
      }

      .rememberme-panel {
        width: 100%;
      }

      .no-account {
        width: 100%;
        display: flex;
        justify-content: space-between;
      }

      .op-btn {
        width: 100%;
      }
    }
  }

  .check-code-panel {
    display: flex;
    width: 100;

    .check-code {
      margin-left: 5px;
      cursor: pointer;
    }
  }

  .login-btn-qq {
    display: flex;
    align-items: center;
    justify-content: center;
    margin-top: 20px;
    text-align: center;

    img {
      cursor: pointer;
      margin-left: 10px;
      width: 20px;
    }
  }
}
</style>

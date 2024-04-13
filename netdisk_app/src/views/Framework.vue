<template>
  <div class="framework">
    <div class="header">
      <div class="logo">
        <span class="iconfont icon-pan"></span>
        <div class="name">樱花云盘</div>
      </div>
      <div class="right-panel">
        <el-popover
          :width="750"
          trigger="click"
          v-model:visible="showUploader"
          :offset="20"
          transition="none"
          :hide-after="0"
          :popper-style="{ padding: '0px' }"
        >
          <template #reference>
            <span class="iconfont icon-transfer"></span>
          </template>
          <template #default>
            <Uploader
              ref="uploaderRef"
              @uploadCallback="uploadCallbackHandler"
            ></Uploader>
          </template>
        </el-popover>
        <el-dropdown>
          <div class="user-info">
            <div class="avatar">
              <Avatar
                :userId="userInfo.userId"
                :timestamp="timestamp"
                :width="46"
              ></Avatar>
            </div>
            <span class="nick-name">{{ userInfo.nickName }}</span>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="updateAvatar">修改头像</el-dropdown-item>
              <el-dropdown-item @click="updatePassword">修改密码</el-dropdown-item>
              <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>
    <div class="body">
      <div class="left-sider">
        <div class="menu-list">
          <div
            @click="jump(item)"
            :class="['menu-item', item.menuCode == currentMenu.menuCode ? 'active' : '']"
            v-for="item in menus"
          >
            <div :class="['iconfont', 'icon-' + item.icon]"></div>
            <div class="text">{{ item.name }}</div>
          </div>
        </div>
        <div class="menu-sub-list">
          <div
            @click="jump(sub)"
            :class="['menu-item-sub', currentPath == sub.path ? 'active' : '']"
            v-for="sub in currentMenu.children"
          >
            <span :class="['iconfont', 'icon-' + sub.icon]" v-if="sub.icon"> </span>
            <span class="text">{{ sub.name }}</span>
          </div>
          <div class="tips" v-if="currentMenu && currentMenu.tips">
            {{ currentMenu.tips }}
          </div>
          <div class="space-info">
            <div class="space-title">空间使用</div>
            <div class="percent">
              <el-progress
                :percentage="
                  Math.floor((useSpaceInfo.useSpace / useSpaceInfo.totalSpace) * 10000) /
                  100
                "
                color="hsl(330, 80%, 80%)"
              ></el-progress>
            </div>
            <div class="space-use">
              <div class="use">
                {{ proxy.Utils.size2Str(useSpaceInfo.useSpace) }}/
                {{ proxy.Utils.size2Str(useSpaceInfo.totalSpace) }}
              </div>
              <div class="iconfont icon-refresh" @click="getUseSpace"></div>
            </div>
          </div>
        </div>
      </div>
      <div class="body-content">
        <router-view v-slot="{ Component }">
          <component :is="Component" @addFile="addFile" ref="routerViewRef"> </component>
        </router-view>
      </div>
    </div>
    <UpdateAvatar ref="updateAvatarRef" @updateAvatar="reloadAvatar"></UpdateAvatar>
    <UpdatePassword ref="updatePasswordRef"></UpdatePassword>
  </div>
</template>

<script setup>
import { ref, reactive, getCurrentInstance, nextTick, watch, onMounted } from "vue";
import { useRouter, useRoute } from "vue-router";
import Uploader from "./main/Uploader.vue";
import UpdateAvatar from "./UpdateAvatar.vue";
import UpdatePassword from "./UpdatePassword.vue";
const { proxy } = getCurrentInstance();
const router = useRouter();
const route = useRoute();
const api = {
  logout: "/logout",
  getUseSpace: "getUseSpace",
};
const uploaderRef = ref();
const showUploader = ref(false);
//上传文件
const addFile = (data) => {
  const { file, filePid } = data;
  showUploader.value = true;
  uploaderRef.value.addFile(file, filePid);
};
//上传文件后回调函数刷新文件列表和用户空间
const routerViewRef = ref();
const uploadCallbackHandler = () => {
  nextTick(() => {
    routerViewRef.value.reload();
    getUseSpace();
  });
};
const timestamp = ref(0);
const userInfo = ref(proxy.VueCookies.get("userInfo"));
const menus = [
  {
    icon: "cloude",
    name: "首页",
    menuCode: "main",
    path: "/main/all",
    allShow: true,
    children: [
      {
        icon: "all",
        name: "全部",
        category: "all",
        path: "/main/all",
      },
      {
        icon: "video",
        name: "视频",
        category: "video",
        path: "/main/video",
      },
      {
        icon: "music",
        name: "音频",
        category: "music",
        path: "/main/music",
      },
      {
        icon: "image",
        name: "图片",
        category: "image",
        path: "/main/image",
      },
      {
        icon: "doc",
        name: "文档",
        category: "doc",
        path: "/main/doc",
      },
    ],
  },
  {
    path: "/myshare",
    icon: "share",
    name: "分享",
    menuCode: "share",
    allShow: true,
    children: [
      {
        name: "分享记录",
        path: "/myshare",
      },
    ],
  },
  {
    path: "/recycle",
    name: "回收站",
    menuCode: "recycle",
    tips: "保存10天内删除的文件",
    icon: "del",
    allShow: true,
    children: [
      {
        name: "删除的文件",
        path: "/recycle",
      },
    ],
  },
  {
    path: "/settings/fileList",
    name: "设置",
    menuCode: "settings",
    icon: "settings",
    allShow: false,
    children: [
      {
        name: "用户文件",
        path: "/settings/fileList",
      },
      {
        name: "用户管理",
        path: "/settings/userList",
      },
      {
        name: "系统设置",
        path: "/settings/sysSetting",
      },
    ],
  },
];
const currentMenu = ref({});
const currentPath = ref();
const jump = (data) => {
  if (!data.path || data.menuCode == currentMenu.value.menuCode) {
    return;
  }
  router.push(data.path);
};
const setMenu = (menuCode, path) => {
  const menu = menus.find((item) => {
    return item.menuCode === menuCode;
  });
  currentMenu.value = menu;
  currentPath.value = path;
};
watch(
  () => route,
  (newVal, oldVal) => {
    if (newVal.meta.menuCode) {
      setMenu(newVal.meta.menuCode, newVal.path);
    }
  },
  { immediate: true, deep: true }
);
//修改头像
const updateAvatarRef = ref();
const updateAvatar = () => {
  updateAvatarRef.value.show(userInfo.value);
};
const reloadAvatar = () => {
  userInfo.value = proxy.VueCookies.get("userInfo");
  timestamp.value = new Date().getTime();
};
//修改密码
const updatePasswordRef = ref();
const updatePassword = () => {
  updatePasswordRef.value.show();
};
//退出
const logout = async () => {
  proxy.Confirm("亲亲,你确定要退出吗?", async () => {
    let result = await proxy.Request({
      url: api.logout,
    });
    if (!result) {
      return;
    }
    proxy.VueCookies.remove("userInfo");
    router.push("/login");
  });
};
//使用空间
const useSpaceInfo = ref({
  useSpace: 0,
  totalSpace: 1,
});
const getUseSpace = async () => {
  let result = await proxy.Request({
    url: api.getUseSpace,
    showLoading: false,
  });
  if (!result) {
    return;
  }
  useSpaceInfo.value = result.data;
};
getUseSpace();
</script>

<style lang="scss" scoped>
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  position: relative;
  padding-left: 24px;
  padding-right: 24px;
  height: 56px;
  z-index: 200;
  box-shadow: 0 3px 10px 0 rgb(0 0 0 / 6%);

  .logo {
    display: flex;
    align-items: center;

    .icon-pan {
      font-size: 30px;
      color: hsl(330, 80%, 80%);
    }

    .name {
      font-weight: bold;
      margin-left: 5px;
      font-size: 25px;
      color: hsl(330, 90%, 75%);
    }
  }

  .right-panel {
    display: flex;
    align-items: center;

    .icon-transfer {
      cursor: pointer;
    }

    .user-info {
      display: flex;
      margin-right: 10px;
      align-items: center;
      cursor: pointer;

      .avatar {
        margin: 0 5px 0 15px;
      }

      .nick-name {
        color: hsl(330, 90%, 75%);
      }
    }
  }
}

.body {
  display: flex;

  .left-sider {
    display: flex;
    border-right: 1px solid #f1f2f4;

    .menu-list {
      height: calc(100vh - 56px);
      width: 80px;
      box-shadow: 0 3px 10px rgb(0 0 0/6%);
      border-right: 1px solid #f1f2f4;

      .menu-item {
        font-size: 14px;
        font-weight: bold;
        text-align: center;
        padding: 20px 0px;
        cursor: pointer;

        &:hover {
          background: hsl(300, 60%, 90%);
        }

        .iconfont {
          font-weight: normal;
          font-size: 28px;
        }
      }

      .active {
        .iconfont {
          color: hsl(330, 80%, 80%);
        }

        .text {
          color: hsl(330, 80%, 80%);
        }
      }
    }

    .menu-sub-list {
      position: relative;
      width: 200px;
      padding: 20px 10px 0px;

      .menu-item-sub {
        text-align: center;
        line-height: 40px;
        border-radius: 5px;
        cursor: pointer;

        &:hover {
          background: hsl(300, 40%, 95%);
        }

        .iconfont {
          font-size: 14px;
          margin-right: 20px;
        }

        .text {
          font-size: 13px;
        }
      }

      .active {
        background: hsl(300, 40%, 95%);

        .iconfont {
          color: hsl(330, 80%, 80%);
        }

        .text {
          color: hsl(330, 80%, 80%);
        }
      }

      .tips {
        color: hsl(0, 20%, 85%);
        font-size: 13px;
        margin-top: 10px;
      }

      .space-info {
        position: absolute;
        bottom: 10px;
        width: 100%;
        padding: 5px 5px;
        .space-title {
          font-size: 1.1em;
          margin-bottom: 5px;
        }
        .percent {
          padding-right: 10px;
        }

        .space-use {
          display: flex;
          flex-direction: row;
          margin-top: 5px;
          justify-content: space-around;

          .use {
            flex: 1;
          }

          .iconfont {
            margin-right: 30px;
            color: hsl(330, 80%, 80%);
            cursor: pointer;
          }
        }
      }
    }
  }

  .body-content {
    flex: 1;
    width: 0;
    padding-left: 20px;
  }
}
</style>

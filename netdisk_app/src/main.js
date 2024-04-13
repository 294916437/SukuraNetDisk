import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import ElementPlus from "element-plus";
import "element-plus/dist/index.css";
import "./assets/icon/iconfont.css";
import "./assets/base.scss";
import VueCookies from "vue-cookies";
//引入代码高亮
import HljsVuePlugin from "@highlightjs/vue-plugin";
import "highlight.js/styles/atom-one-light.css";
import "highlight.js/lib/common";
//引入全局JS
import Confirm from "./utils/Confirm";
import Verify from "./utils/Verify.js";
import Message from "./utils/Message";
import Request from "./utils/Request";
import Utils from "./utils/Utils";
//引入自定义组件
import Customeddialog from "./components/Customeddialog.vue";
import Avatar from "./components/Avatar.vue";
import FileTable from "./components/FileTable.vue";
import FileIcon from "./components/FileIcon.vue";
import NoData from "./components/NoData.vue";
import FolderSelect from "./components/FolderSelect.vue";
import FolderNavigator from "./components/FolderNavigator.vue";
import Window from "./components/Window.vue";
import Preview from "./components/preview/Preview.vue";

const app = createApp(App);
app.use(ElementPlus);
app.use(HljsVuePlugin);
app.use(router);

//全局配置自定义组件
app.component("Dialog", Customeddialog);
app.component("Avatar", Avatar);
app.component("FileTable", FileTable);
app.component("FileIcon", FileIcon);
app.component("NoData", NoData);
app.component("FolderSelect", FolderSelect);
app.component("FolderNavigator", FolderNavigator);
app.component("Window", Window);
app.component("Preview", Preview);
//配置全局JS
app.config.globalProperties.Confirm = Confirm;
app.config.globalProperties.Verify = Verify;
app.config.globalProperties.Message = Message;
app.config.globalProperties.Request = Request;
app.config.globalProperties.VueCookies = VueCookies;
app.config.globalProperties.Utils = Utils;
app.config.globalProperties.globalInfo = {
  avatarUrl: "/api/getAvatar/",
  imageUrl: "/api/file/getImage/",
};
app.mount("#app");

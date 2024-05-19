<template>
  <div>
    <div class="top-panel">
      <el-form :model="searchFormData" label-width="80px" @submit.prevent>
        <el-row>
          <el-col :span="4">
            <el-form-item label="用户昵称">
              <el-input
                clearable
                placeholder="支持迷糊搜索"
                v-model.trim="searchFormData.nickNameFuzzy"
                @keyup.nature="loadDataList"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item label="状态">
              <el-select clearable placeholder="请选择状态" v-model="searchFormData.status">
                <el-option :value="1" label="启用"></el-option>
                <el-option :value="2" label="禁用"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-button type="primary" @click="loadDataList">查询</el-button>
          </el-col>
        </el-row>
      </el-form>
    </div>
    <div class="file-list">
      <FileTable
        :columns="columns"
        :dataSource="tableData"
        :fetch="loadDataList"
        :initFetch="true"
        :options="tableOptions"
      >
        <template #avatar="{ index, row }">
          <div class="avatar">
            <Avatar :userId="row.userId" :avatar="row.avatar" :width="45"></Avatar>
          </div>
        </template>
        <template #space="{ index, row }">
          {{ proxy.Utils.size2Str(row.useSpace) }}/{{ proxy.Utils.size2Str(row.totalSpace) }}
        </template>
        <template #status="{ index, row }">
          <span v-if="row.status == 1" style="color: #529b2e">启用</span>
          <span v-if="row.status == 0" style="color: #f56c62">禁用</span>
        </template>
        <template #op="{ index, row }">
          <span class="a-link" @click="updateUserSpace(row)">分配空间</span>
          <el-divider direction="vertical"></el-divider>
          <span class="a-link" @click="updateUserStatus(row)">{{
            row.status == 0 ? "启用" : "禁用"
          }}</span>
        </template>
      </FileTable>
    </div>
    <Dialog
      :show="dialogConfig.show"
      :title="dialogConfig.title"
      :buttons="dialogConfig.buttons"
      :width="35"
      :height="20"
      :showCancel="false"
      @close="dialogConfig.show = false"
    >
      <el-form
        :model="formData"
        :rules="rules"
        ref="formDataRef"
        label-width="80px"
        @submit.prevent
      >
        <!--input输入-->
        <el-form-item label="昵称">{{ formData.nickName }} </el-form-item>
        <!--input输入-->
        <el-form-item label="空间大小" prop="changeSpace">
          <el-input clearable placeholder="请输入空间大小" v-model.trim="formData.changeSpace"
            ><template #suffix>MB</template>
          </el-input>
        </el-form-item>
      </el-form>
    </Dialog>
  </div>
</template>

<script setup>
import { ref, getCurrentInstance, nextTick } from "vue";
const { proxy } = getCurrentInstance();
const api = {
  loadDataList: "/admin/loadUserList",
  updateUserStatus: "/admin/updateUserStatus",
  updateUserSpace: "/admin/updateUserSpace",
};
const columns = [
  {
    lable: "头像",
    prop: "avatar",
    width: 70,
    scopedSlots: "avatar",
  },
  {
    lable: "昵称",
    prop: "nickName",
    width: 140,
  },
  {
    lable: "邮箱",
    prop: "email",
    width: 300,
  },
  {
    lable: "空间使用",
    prop: "space",
    width: 140,
    scopedSlots: "space",
  },
  {
    lable: "加入时间",
    prop: "joinTime",
  },
  {
    lable: "最近登录时间",
    prop: "lastLoginTime",
  },
  {
    lable: "状态",
    prop: "status",
    width: 60,
    scopedSlots: "status",
  },
  {
    lable: "操作",
    prop: "op",
    width: 160,
    scopedSlots: "op",
  },
];
const searchFormData = ref({});

const tableData = ref({});
const tableOptions = ref({
  exHeight: 50,
});
const showLoading = ref(false);
const fileNameFuzzy = ref();
const category = ref();
const loadDataList = async () => {
  let params = {
    pageNo: tableData.value.pageNo,
    pageSize: tableData.value.pageSize,
  };
  Object.assign(params, searchFormData.value);
  let result = await proxy.Request({
    url: api.loadDataList,
    params,
  });
  if (!result) {
    return;
  }
  tableData.value = result.data;
};
const updateUserStatus = (row) => {
  proxy.Confirm("确认要修改用户状态吗？", async () => {
    let result = await proxy.Request({
      url: api.updateUserStatus,
      params: {
        userId: row.userId,
        status: row.status == 1 ? 0 : 1,
      },
    });
    if (!result) {
      return;
    }
    loadDataList();
  });
};
const dialogConfig = ref({
  show: false,
  title: "修改空间大小",
  buttons: [
    {
      type: "primary",
      text: "确定",
      click: (e) => {
        submitForm();
      },
    },
  ],
});
const formData = ref({});
const formDataRef = ref();
const rules = {
  changeSpace: [{ required: true, message: "请输入空间大小" }],
};
const updateUserSpace = (data) => {
  dialogConfig.value.show = true;
  nextTick(() => {
    formDataRef.value.resetFields();
    formData.value = Object.assign({}, data);
  });
};
const submitForm = () => {
  formDataRef.value.validate(async (valid) => {
    if (!valid) {
      return;
    }
    let params = {};
    Object.assign(params, formData.value);
    let result = await proxy.Request({
      url: api.updateUserSpace,
      params,
    });
    if (!result) {
      return;
    }
    dialogConfig.value.show = false;
    proxy.Message.success("操作成功");
    loadDataList();
  });
};
</script>

<style lang="scss" scoped>
.top-panel {
  margin-top: 10px;
}
.file-list {
  .avatar {
    text-align: center;
    width: 50px;
    height: 50px;
    overflow: hidden;
    img {
      width: 100%;
      height: 100%;
    }
  }
  .a-link {
    cursor: pointer;
    &:hover {
      color: #409eff;
    }
  }
}
</style>

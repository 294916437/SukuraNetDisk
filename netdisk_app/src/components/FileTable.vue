<template>
  <div>
    <el-table
      ref="dataTable"
      :data="dataSource.list || []"
      :height="tableHeight"
      :stripe="options.stripe"
      :border="options.border"
      header-row-class-name="table-header-row"
      highlight-current-row
      @row-click="handleRowClick"
      @selection-change="handleSelectionChange"
    >
      <!--文件复选框-->
      <el-table-column
        v-if="options.selectType && options.selectType == 'checkbox'"
        type="selection"
        width="45"
        align="center"
      >
      </el-table-column>
      <!--是否开启文件序号-->
      <el-table-column
        v-if="options.showIndex"
        label="序号"
        type="index"
        width="40"
        align="left"
      >
      </el-table-column>
      <template v-for="(column, index) in columns">
        <template v-if="column.scopedSlots">
          <!--列名-->
          <el-table-column
            :key="index"
            :prop="column.prop"
            :label="column.lable"
            :align="column.align || 'left'"
            :width="column.width"
          >
            <!--各列数据，包括文件名、文件最新更新时间、文件大小-->
            <template #default="scope">
              <slot :name="column.scopedSlots" :index="scope.$index" :row="scope.row">
              </slot>
            </template>
          </el-table-column>
        </template>
        <template v-else>
          <el-table-column
            :key="index"
            :prop="column.prop"
            :label="column.lable"
            :align="column.align || 'left'"
            :width="column.width"
          >
          </el-table-column>
        </template>
      </template>
    </el-table>
    <!--分页-->
    <div class="pagination" v-if="showPagination">
      <el-pagination
        v-if="dataSource.totalCount"
        background
        :total="dataSource.totalCount"
        :page-sizes="[15, 30, 50, 100]"
        :page-size="dataSource.pageSize"
        :current-page-sync="dataSource.pageNo"
        :layout="layout"
        @size-change="handlePageSizeChange"
        @current-change="handlePageNoChange"
        style="text-align: right"
      >
      </el-pagination>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from "vue";
const emit = defineEmits(["rowSelected", "rowClick"]);
const props = defineProps({
  dataSource: Object,
  showPagination: {
    type: Boolean,
    default: true,
  },
  showPageSize: {
    type: Boolean,
    default: true,
  },
  options: {
    type: Object,
  },
  columns: Array,
  fetch: Function,
  initFetch: {
    type: Boolean,
    default: true,
  },
});
const layout = computed(() => {
  return `total,${props.showPageSize ? "sizes" : ""},prev,pager,next,jumper`;
});
const topHeight = 60 + 20 + 30 + 46;
const tableHeight = ref(
  props.options.tableHeight
    ? props.options.tableHeight
    : window.innerHeight - topHeight - props.options.exHeight
);
const init = () => {
  if (props.initFetch && props.fetch) {
    props.fetch();
  }
};
init();
const dataTable = ref();
const clearSelection = () => {
  dataTable.value.clearSelection();
};
const setCurrentRow = (rowKey, rowValue) => {
  let row = props.dataSource.list.find((item) => {
    return item[rowKey] === rowValue;
  });
  dataTable.value.setCurrentRow(row);
};
defineExpose({ setCurrentRow, clearSelection });
const handleRowClick = (row) => {
  emit("rowClick", row);
};
const handleSelectionChange = (row) => {
  emit("rowSelected", row);
};
const handlePageSizeChange = (size) => {
  props.dataSource.pageSize = size;
  props.dataSource.pageNo = 1;
  props.fetch();
};
const handlePageNoChange = (pageNo) => {
  props.dataSource.pageNo = pageNo;
  props.fetch();
};
</script>

<style lang="scss" scoped>
.pagination {
  padding-top: 10px;
  padding-right: 10px;
}
.el-pagination {
  justify-content: right;
}
.el-table {
  :deep(.el-table__cell) {
    padding: 4px 0px;
  }
}
</style>

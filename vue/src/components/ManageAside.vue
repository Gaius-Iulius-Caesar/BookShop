<template>
  <el-menu style="height: 100%;overflow-x: hidden"
           background-color="rgb(48,48,65,86)"
           text-color="#fff"
           active-text-color="#ffd04b"
           :collapse-transition="false"
           :collapse="isCollapse"
           router>
    <div style="height: 60px; line-height: 60px;text-align: center;">
      <img src="../assets/logo.png" alt="" style="width: 20px;position: relative;top: 5px;margin-right: 5px;">
      <b style="color: white" v-show="logoTextShow">后台管理</b>
    </div>

    <el-menu-item index="/manage/managehome">
      <i class="el-icon-house"></i>
      <template slot="title">
        <span slot="title">书店信息</span>
      </template>
    </el-menu-item>

    <el-menu-item index="/manage/inventory">
      <i class="el-icon-s-order"></i>
      <template slot="title">
        <span slot="title">库存数据</span>
      </template>
    </el-menu-item>

    <el-submenu index="/manage/selldata">
      <template slot="title">
        <i class="el-icon-menu"></i>
        <span slot="title">销售数据</span>
      </template>
      <el-menu-item index="/manage/selldata/records">
        <i class="el-icon-sell"></i>
        <span slot="title">记录</span>
      </el-menu-item>
      <el-menu-item index="/manage/selldata/statistics">
        <i class="el-icon-s-marketing"></i>
        <span slot="title">统计</span>
      </el-menu-item>
    </el-submenu>
  </el-menu>
</template>

<script>
export default {
  name: "Aside",
  props: {
    isCollapse: Boolean,
    logoTextShow: Boolean
  },
  methods:{

  }
}
</script>

<style scoped>

</style>
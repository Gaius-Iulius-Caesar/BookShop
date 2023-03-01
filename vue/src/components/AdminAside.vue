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

    <el-menu-item index="/admin/adminhome">
      <i class="el-icon-house"></i>
      <template slot="title">
        <span slot="title">管理员信息</span>
      </template>
    </el-menu-item>
    <el-submenu index="/admin/admincarousel">
      <template slot="title">
        <i class="el-icon-menu"></i>
        <span slot="title">系统管理</span>
      </template>
      <el-menu-item index="/admin/admincarousel">
        <i class="el-icon-video-camera-solid"></i>
        <span slot="title">轮播图管理</span>
      </el-menu-item>
    </el-submenu>
  </el-menu>
</template>

<script>
export default {
  name: "AdminAside",
  props: {
    isCollapse: Boolean,
    logoTextShow: Boolean
  },
}
</script>

<style scoped>

</style>
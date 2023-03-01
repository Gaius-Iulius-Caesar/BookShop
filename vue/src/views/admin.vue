<template>
  <el-container style="min-height: 100vh">
    <el-aside :width="sideWidth + 'px'"
              style="background-color: rgb(238, 241, 246);box-shadow: 2px 0 6px rgb(0 21 41 / 35%)">
      <AdminAside :isCollapse="isCollapse" :logoTextShow="logoTextShow"></AdminAside>
    </el-aside>
    <el-container>
      <el-header style="border-bottom: 1px solid #ccc;">
        <AdminHeader :collapseBtnClass="collapseBtnClass"></AdminHeader>
      </el-header>
      <el-main>
        <!--    表示当前页面的子路由会在 <router-view/> 中展示-->
        <router-view/>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import AdminAside from "@/components/AdminAside";
import AdminHeader from "@/components/AdminHeader";

export default {
  name: "Admin",
  components: {AdminHeader, AdminAside},
  data() {
    return {
      collapseBtnClass: 'el-icon-s-fold',
      isCollapse: false,
      sideWidth: 200,
      logoTextShow: true,
    }
  },
  created() {

  },
  methods: {
    collapse() { //点击收缩按钮触发
      this.isCollapse = !this.isCollapse
      if (this.isCollapse) {
        this.sideWidth = 64
        this.collapseBtnClass = 'el-icon-s-unfold'
        this.logoTextShow = false
      } else {
        this.sideWidth = 200
        this.collapseBtnClass = 'el-icon-s-fold'
        this.logoTextShow = true
      }
    },

  }
}

</script>

<style scoped>

</style>
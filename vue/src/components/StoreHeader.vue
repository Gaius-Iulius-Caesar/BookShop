<template>
  <div>
    <el-menu
        :default-active="'/store/storemain'"
        class="storeGuide"
        mode="horizontal"
        @select="handleSelect"
        background-color="rgb(48,48,65,86)"
        text-color="#fff"
        active-text-color="#ffd04b"
        router>
      <div style="position: absolute;left: 10px;font-size: 30px;color: #ffffff;">
        <el-page-header @back="logout" title="退出登录"></el-page-header>
      </div>
      <el-menu-item index="/store/storemain">首页</el-menu-item>
      <el-menu-item index="/store/storecart">购物车</el-menu-item>
      <el-menu-item index="/store/storeorders">历史订单</el-menu-item>
      <el-menu-item index="/store/storehome">个人中心</el-menu-item>
    </el-menu>
  </div>
</template>

<script>
export default {
  name: "StoreHeader",
  data() {
    return {}
  },
  created() {

  },
  mounted() {

  },
  methods: {
    handleSelect(key, keyPath) {
      console.log(key, keyPath);
    },
    logout() {
      this.$router.push("/login")
      localStorage.removeItem("user")
      this.$message.success("退出成功")
    },
  }
}
</script>

<style scoped>
.storeGuide {
  display: flex;
  justify-content: center;
  align-items: center;
}


</style>
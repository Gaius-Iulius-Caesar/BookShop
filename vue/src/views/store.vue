<template>
  <div>
    <el-container>
      <el-header style="width: 100%;padding: 0;margin: 0">
        <StoreHeader></StoreHeader>
      </el-header>
      <el-main style="width: 100%;padding: 0;margin: 0">
        <!--    表示当前页面的子路由会在 <router-view/> 中展示-->
        <router-view/>
      </el-main>
      <el-footer>
        <StoreFooter></StoreFooter>
      </el-footer>
    </el-container>
  </div>
</template>

<script>
import StoreHeader from "@/components/StoreHeader";
import StoreFooter from "@/components/StoreFooter";

export default {
  name: "store",
  components: {StoreFooter, StoreHeader},
  data() {
    return {}
  },
  created() {

  },
  mounted() {

  },
  methods: {}
}
</script>

<style scoped>


</style>
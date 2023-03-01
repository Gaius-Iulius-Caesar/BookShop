<template>
  <div style="margin-top: 10px;display: flex;justify-content: center">
    <div>
      <el-timeline :reverse="true">
        <el-timeline-item
            v-for="order in ordersData"
            :key="order.id"
            :timestamp="order.createTime">
          <!--放一个卡片-->
          <el-card style="width:500px">
            <div slot="header">
              <span>
                <el-tag size="small" type="success" v-if="order.isSent === 1">已发货 快递单号 {{ order.expressId }}</el-tag>
                <el-tag size="small" type="danger" v-if="order.isSent === 0">未发货</el-tag>
              </span>
              <span style="float: right; padding: 3px 0"><el-tag
                  type="success">金额:{{ order.price * order.count }}元</el-tag></span>
            </div>
            <div style="display: flex;justify-content:space-between">
              <div><img style="width: 90px;height:90px;margin-top: 5px" :src="order.coverurl"/></div>
              <div>
                {{ order.bookname }} ({{ order.author }} 编著)
                <el-tag type="success">{{ order.count }}*{{ order.price }}元</el-tag>
              </div>
            </div>
          </el-card>
        </el-timeline-item>
      </el-timeline>
    </div>
  </div>
</template>

<script>
export default {
  name: "StoreOrders",
  data() {
    return {
      ordersData: [],
      buyUser: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
    };
  },
  mounted() {
    this.request.get("http://localhost:9090/selldata/orders/" + this.buyUser.id).then(res => {
      this.ordersData = res
    })
  }
}
</script>

<style scoped>

</style>
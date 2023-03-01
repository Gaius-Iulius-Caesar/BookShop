<template>
  <div style="display: flex;flex-wrap:wrap;justify-content: center;">
    <div style="width: 100%;margin: 50px">
      <el-steps :active="2" align-center>
        <el-step title="购物车"></el-step>
        <el-step title="下单"></el-step>
        <el-step title="付款"></el-step>
        <el-step title="交易成功, 待配货"></el-step>
      </el-steps>
    </div>

    <div>
      <h1 style="display:block;">订单提交成功咯!!! 快去付款吧!!!</h1>
      <el-card class="box-card" style="margin-bottom: 50px">
        <div slot="header" class="clearfix">
          <span>请选择以下支付方式:</span>
        </div>
        <div class="text item">
          <el-popover
              placement="left"
              width="400"
              trigger="click">
            <img width="200" src="http://localhost:9090/file/77ce34b56116467d80e5de4837977cd9.jpg">
            <el-tag type="success">共计{{ totalPrice }}</el-tag>
            <el-button @click="paysuccess" type="danger" style="margin-left: 10px">付款成功</el-button>
            <el-button slot="reference">支付宝</el-button>
          </el-popover>
          <el-popover
              style="margin-left: 10px"
              placement="right"
              width="400"
              trigger="click">
            <img width="200" src="http://localhost:9090/file/77ce34b56116467d80e5de4837977cd9.jpg">
            <el-tag type="success">共计{{ totalPrice }}</el-tag>
            <el-button @click="paysuccess" type="danger" style="margin-left: 10px">付款成功</el-button>
            <el-button slot="reference">微信</el-button>
          </el-popover>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script>
export default {
  name: "StorePay",
  data() {
    return {
      totalPrice: 0,
      //获取当前用户数据
      buyUser: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
    };
  }, //data-end
  mounted() {
    this.load()
  },
  methods: {
    load() {
      this.request.get("http://localhost:9090/cart/" + this.buyUser.id).then(res => {
        // console.log(res) // 在浏览器控制台中打印
        // 设置总金额
        let sum = 0
        for (let i = 0; i < res.length; i++)
          sum += (res[i]['newPrice'] * res[i]['count'])
        this.totalPrice = sum
      })
    },
    paysuccess() {
      this.request.get("http://localhost:9090/cart/pay-success/" + this.buyUser.id).then(res => {
        if (res.code === "200") {
          this.$message.success("付款成功，请等待商家配货")
          this.$router.push('/store/storecart')
        }
      })
    }
  }
}
</script>

<style scoped>
.text {
  font-size: 14px;
}

.item {
  padding: 18px 0;
}

.box-card {
  width: 480px;
}
</style>
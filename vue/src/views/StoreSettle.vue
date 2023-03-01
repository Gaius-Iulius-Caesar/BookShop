<template>
  <div style="display: flex;flex-wrap:wrap;justify-content: center;">
    <div style="width: 100%;margin: 50px">
      <el-steps :active="1" align-center>
        <el-step title="购物车"></el-step>
        <el-step title="下单"></el-step>
        <el-step title="付款"></el-step>
        <el-step title="交易成功, 待配货"></el-step>
      </el-steps>
    </div>

    <div style="width:600px;">
      <h1 style="align-content: center">请核对订单信息</h1>
      <el-table :data="tableData" border>
        <el-table-column prop="bookname" label="书名" width="80"></el-table-column>
        <el-table-column prop="coverurl" label="图片" width="80">
          <template slot-scope="scope">
            <img :src="scope.row.coverurl" style="width: 70px;height: 70px">
          </template>
        </el-table-column>
        <el-table-column prop="newPrice" label="价格" width="80"></el-table-column>
        <el-table-column prop="count" label="数量" width="160"></el-table-column>
        <el-table-column label="合计" width="80">
          <template slot-scope="scope">
            {{ scope.row.count * scope.row.newPrice }}
          </template>
        </el-table-column>
      </el-table>

      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>共计消费:<el-tag type="danger">{{ totalPrice }}元</el-tag></span>
        </div>
        <div class="text item">
          <el-input placeholder="请输入收货地址" v-model="form.address">
            <template slot="prepend">收货地址</template>
          </el-input>
          <el-input placeholder="联系方式" v-model="form.connection" style="margin-top: 10px">
            <template slot="prepend">联系方式</template>
          </el-input>
          <el-input placeholder="收货人姓名" v-model="form.name" style="margin-top: 10px">
            <template slot="prepend">收货人姓名</template>
          </el-input>
        </div>
      </el-card>

      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span><el-tag>支付方式: 在线支付</el-tag></span>
        </div>
      </el-card>
      <div style="display: flex;justify-content: center;margin-top:20px;margin-bottom: 20px ">
        <el-button type="primary" @click="next">下一步</el-button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "StoreSettle",
  data() {
    return {
      tableData: [],
      totalPrice: 0,
      form:{},
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
        this.tableData = res
        // 设置总金额
        let sum = 0
        for (let i = 0; i < res.length; i++)
          sum += (res[i]['newPrice'] * res[i]['count'])
        this.totalPrice = sum
      })
    },
    next(){
      this.form.buyId = this.buyUser.id
      this.request.post("http://localhost:9090/cart/goods-receive", this.form).then(res => {
        if (res.code === "200") {
          this.$message.success("下单成功")
          this.$router.push('/store/storepay')
        } else {
          this.$message.error(res.msg)
        }
      })
    }
  }
}
</script>

<style scoped>

</style>
<template>
  <div style="display: flex;flex-wrap:wrap;justify-content: center;">
    <div style="width: 100%;margin: 50px">
      <el-steps :active="0" align-center>
        <el-step title="购物车"></el-step>
        <el-step title="下单"></el-step>
        <el-step title="付款"></el-step>
        <el-step title="交易成功, 待配货"></el-step>
      </el-steps>
    </div>
    <div style="position: relative;margin-bottom: 90px">
      <el-table v-if="tableData.length!==0" :data="tableData" border @selection-change="handleSelectionChange"
                style="width: 100%">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="bookId" label="商品编号" width="80"></el-table-column>
        <el-table-column prop="bookname" label="书名" width="200"></el-table-column>
        <el-table-column prop="coverurl" label="封面" width="80">
          <template slot-scope="scope">
            <img :src="scope.row.coverurl" style="width: 70px;height: 70px">
          </template>
        </el-table-column>
        <el-table-column prop="newPrice" label="价格" width="80"></el-table-column>
        <el-table-column prop="count" label="数量" width="160">
          <template slot-scope="scope">
            <el-input-number required size="small" v-model="scope.row.count" :min="1" :max="scope.row.stock"
                             @change="goodsNumberChange(scope.row)" label="数量"></el-input-number>
          </template>
        </el-table-column>
        <el-table-column label="总价" width="80">
          <template slot-scope="scope">
            {{ scope.row.count * scope.row.newPrice }}
          </template>
        </el-table-column>
      </el-table>
      <el-empty description="这里空空如也" v-if="tableData.length===0">
        <el-button type="primary" @click="$router.push('/store/storemain')">快去主页看看吧</el-button>
      </el-empty>
      <div :hidden="tableData.length===0" style="display: inline-block;margin-top: 20px">
        总金额：
        <el-tag>{{ totalPrice }}元</el-tag>
      </div>
      <div style="position: absolute;right: 0;display: inline-block;margin-top: 20px" v-if="tableData.length!==0">
        <el-button type="danger" @click="handleDelBatch" v-if="tableData.length!==0">批量删除</el-button>
        <el-button type="primary" @click="$router.push('/store/storesettle')">结算</el-button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "StoreCart",
  data() {
    return {
      multipleSelection: [],
      tableData: [],
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
        this.tableData = res
        // 设置总金额
        let sum = 0
        for (let i = 0; i < res.length; i++)
          sum += (res[i]['newPrice'] * res[i]['count'])
        this.totalPrice = sum
      })
    },
    goodsNumberChange(cartItem) {
      this.request.post("http://localhost:9090/cart", cartItem).then(res => {
        if (res) {
          this.$message.success("修改数量成功")
          this.load()
        } else {
          this.$message.error("修改数量失败")
        }
      })
    },
    handleSelectionChange(val) { // 选择列表行
      this.multipleSelection = val
    },
    handleDelBatch() {
      let ids = this.multipleSelection.map(v => v.id) // [{}, {}, {}] => [1, 2, 3]
      this.request.post("http://localhost:9090/cart/del/batch", ids).then(res => {
        if (res) {
          this.$message.success("批量删除成功")
          this.load()
        } else {
          this.$message.error("批量删除失败")
        }
      })
    },
  }
}
</script>

<style scoped>

</style>
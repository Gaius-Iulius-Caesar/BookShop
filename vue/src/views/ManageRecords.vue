<template>
  <div>
    <el-table :data="tableData" border stripe :header-cell-class-name="headerBg">
      <el-table-column prop="id" label="订单编号" width="100" sortable></el-table-column>
      <el-table-column prop="buyId" label="顾客ID" width="70"></el-table-column>
      <el-table-column prop="buyNickname" label="顾客昵称" sortable></el-table-column>
      <el-table-column prop="bookId" label="图书编号" width="70"></el-table-column>
      <el-table-column prop="bookname" label="书名" width="200" sortable></el-table-column>
      <el-table-column prop="price" label="支付价格"></el-table-column>
      <el-table-column prop="count" label="数量"></el-table-column>
      <el-table-column prop="isPay" label="支付状态">
        <template slot-scope="scope">
          <el-tag size="small" type="success" v-if="scope.row.isPay === 1">已支付</el-tag>
          <el-tag size="small" type="danger" v-if="scope.row.isPay === 0">未支付</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="isSent" label="发货状态">
        <template slot-scope="scope">
          <el-tag size="small" type="success" v-if="scope.row.isSent === 1">已发货</el-tag>
          <el-tag size="small" type="danger" v-if="scope.row.isSent === 0">未发货</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="订单创建时间" width="240" sortable></el-table-column>
      <el-table-column label="操作" align="center">
        <template slot-scope="scope">
          <el-button type="success" @click="handleSent(scope.row)">配货<i class="el-icon-edit"></i></el-button>
        </template>
      </el-table-column>
    </el-table>
    <div style="padding: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[2, 5, 10, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>

    <el-dialog title="配货" :visible.sync="dialogFormVisible" width="30%">
      <el-form label-width="80px" size="small">
        <el-form-item label="收货地址">
          <el-input v-model="form.address" autocomplete="off" :disabled = true></el-input>
        </el-form-item>
        <el-form-item label="联系方式">
          <el-input v-model="form.connection" autocomplete="off" :disabled = true>></el-input>
        </el-form-item>
        <el-form-item label="收货人">
          <el-input v-model="form.name" autocomplete="off" :disabled = true>></el-input>
        </el-form-item>
        <el-form-item label="物流单号">
          <el-input v-model="form.expressId" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "Records",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 2,
      dialogFormVisible: false,
      form: {},
      headerBg: 'headerBg',
      shopuser: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
    }
  },
  created() {
    this.load()
  },
  methods: {
    // 请求分页查询数据
    load() {
      this.request.get("http://localhost:9090/selldata/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          shopId: this.shopuser.id,
        }
      }).then(res => {
        // console.log(res) // 在浏览器控制台中打印
        this.tableData = res.records
        this.total = res.total
      })
    },
    cancel() { // 对话框取消按钮事件
      this.dialogFormVisible = false
      this.load()
    },
    save() { // 对话框确定按钮事件
      this.request.post("http://localhost:9090/selldata", this.form).then(res => {
        if (res) {
          this.$message.success("保存成功")
          this.dialogFormVisible = false
          this.load()
        } else {
          this.$message.success("保存失败")
        }
      })
    },
    handleSent(row) {
      this.form = row
      this.dialogFormVisible = true
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.load()
    }
  }
}
</script>

<style scoped>

</style>
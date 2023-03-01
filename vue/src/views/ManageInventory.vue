<template>
  <div>
    <div style="margin: 10px 0">
      <el-input style="width: 183px" suffix-icon="el-icon-search" placeholder="请输入书名" v-model="bookname"></el-input>
      <el-button class="ml-5" type="primary" @click=load>搜索</el-button>
      <el-button class="ml-5" type="warning" @click=reset>重置</el-button>
    </div>
    <div style="margin: 10px 0">
      <el-button type="primary" @click="handleAdd">新增库存<i class="el-icon-circle-plus-outline"></i></el-button>
      <el-popconfirm
          class="ml-5"
          confirm-button-text='确定'
          cancel-button-text='我再想想'
          icon="el-icon-info"
          icon-color="red"
          title="确定删除吗？"
          @confirm="handleDelBatch()"
      >
        <el-button type="danger" slot="reference">批量删除<i class="el-icon-remove-outline"></i></el-button>
      </el-popconfirm>
<!--      <el-upload action="http://localhost:9090/inventory/import" style="display: inline-block" :show-file-list="false"
                 accept="xlsx" :on-success="handleImportSuccess">
        <el-button type="primary" class="ml-5">导入<i class="el-icon-bottom"></i></el-button>
      </el-upload>-->
      <el-button type="primary" @click="exp" class="ml-5">导出库存数据<i class="el-icon-top"></i></el-button>
    </div>
    <el-table :data="tableData" border stripe :header-cell-class-name="headerBg"
              @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="ID"></el-table-column>
      <el-table-column prop="bookname" label="书名" width="240"></el-table-column>
      <el-table-column prop="category" label="类别" width="240">
        <template slot-scope="scope">
          <el-tag size="small">{{ scope.row.category }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="stock" label="库存量"></el-table-column>
      <el-table-column prop="price" label="价格"></el-table-column>
      <el-table-column prop="discount" label="折扣"></el-table-column>
      <el-table-column prop="enable" label="上架">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.enable===1 ? true : false" active-color="#13ce66" inactive-color="#ff4949"></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="240" align="center">
        <template slot-scope="scope">
          <el-button type="success" @click="handleEdit(scope.row)">编辑<i class="el-icon-edit"></i></el-button>
          <el-button type="success" @click="handleBookInfo(scope.row)">详情<i class="el-icon-document"></i></el-button>
          <el-popconfirm
              class="ml-5"
              confirm-button-text='确定'
              cancel-button-text='我再想想'
              icon="el-icon-info"
              icon-color="red"
              title="确定删除吗？"
              @confirm="handleDel(scope.row.id)"
          >
            <el-button type="danger" slot="reference">删除<i class="el-icon-remove-outline"></i></el-button>
          </el-popconfirm>
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

    <el-dialog title="库存信息" :visible.sync="dialogFormVisible" width="30%">
      <el-form label-width="80px" size="small">
        <el-form-item label="库存量">
          <el-input v-model="form.stock" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="价格">
          <el-input v-model="form.price" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="折扣">
          <el-input v-model="form.discount" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="是否上架">
          <el-switch v-model="enableFormItem" active-color="#13ce66" inactive-color="#ff4949"></el-switch>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="图书信息" :visible.sync="dialogBookInfoFormVisible" width="30%">
      <div>
        <el-upload
            class="avatar-uploader"
            action="http://localhost:9090/file/upload/"
            :show-file-list="false"
            :on-success="handleCoverSuccess"
        >
          <img v-if="bookInfoForm.coverurl" :src="bookInfoForm.coverurl" class="avatar">
          <i v-else class="el-icon-plus avatar-uploader-icon"></i>
        </el-upload>
      </div>
      <el-descriptions size="mid" :column="1" :colon="false" :contentStyle=contentStyle style="margin-left: 10%;margin-right: 5%">
        <el-descriptions-item label="图书编号">{{ bookInfoForm.id }}</el-descriptions-item>
        <el-descriptions-item label="书名">{{ bookInfoForm.bookname }}</el-descriptions-item>
        <el-descriptions-item label="作者">{{ bookInfoForm.author }}</el-descriptions-item>
        <el-descriptions-item label="简介">{{ bookInfoForm.introduction }}</el-descriptions-item>
        <el-descriptions-item label="出版社">{{ bookInfoForm.press }}</el-descriptions-item>
        <el-descriptions-item label="分类"><el-tag>{{ bookInfoForm.category }}</el-tag></el-descriptions-item>
        <template slot="extra">
          <el-button type="primary" size="small" @click="handleBookInfoEdit" class="el-icon-edit">编辑</el-button>
        </template>
      </el-descriptions>
    </el-dialog>
    <el-dialog title="编辑图书" :visible.sync="dialogBookInfoEditFormVisible" width="30%">
      <el-form label-width="80px" size="small">
        <el-form-item label="书名">
          <el-input v-model="bookInfoEditForm.bookname" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="作者">
          <el-input v-model="bookInfoEditForm.author" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="简介">
          <el-input v-model="bookInfoEditForm.introduction" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="出版社">
          <el-input v-model="bookInfoEditForm.press" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="分类">
          <!--          <el-input v-model="bookInfoEditForm.category" autocomplete="off"></el-input>-->
          <el-select v-model="bookInfoEditForm.category" placeholder="请选择">
            <el-option
                v-for="item in options"
                :key="item"
                :label="item"
                :value="item">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="bookCancel">取 消</el-button>
        <el-button type="primary" @click="bookSave">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>

export default {
  name: "Inventory",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 2,
      bookname: "",
      dialogFormVisible: false,
      dialogBookInfoFormVisible: false,
      dialogBookInfoEditFormVisible: false,
      form: {},
      bookInfoForm: {},
      bookInfoEditForm: {},
      contentStyle: {"font-size": "13px"},
      multipleSelection: [],
      headerBg: 'headerBg',
      shopuser: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      options: [],
      enableFormItem: false
    }
  },
  created() {
    this.load()
    // 获得图书分类信息
    this.request.get("http://localhost:9090/book/category").then(res => { this.options = res})
  },
  methods: {
    // 请求分页查询数据
    load() {
      this.request.get("http://localhost:9090/inventory/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          shopId: this.shopuser.id,
          bookname: this.bookname,
        }
      }).then(res => {
        // console.log(res) // 在浏览器控制台中打印
        this.tableData = res.records
        this.total = res.total
      })
    },
    reset() {
      this.bookname = ""
      this.load()
    },
    handleAdd() {
      this.dialogFormVisible = true
      this.form = {}
    },
    handleDelBatch() {
      let ids = this.multipleSelection.map(v => v.id) // [{}, {}, {}] => [1, 2, 3]
      this.request.post("http://localhost:9090/inventory/del/batch", ids).then(res => {
        if (res) {
          this.$message.success("批量删除成功")
          this.dialogFormVisible = false
          this.load()
        } else {
          this.$message.success("批量删除失败")
        }
      })
    },
    exp() {
      window.open("http://localhost:9090/inventory/export/" + this.shopuser.id)
    },
    handleImportSuccess() {
      this.$message.success("文件导入成功")
      this.load()
    },
    save() { // 对话框确定按钮事件
      this.form.shopId = this.shopuser.id
      this.form.enable = this.enableFormItem ? 1 : 0
      this.request.post("http://localhost:9090/inventory", this.form).then(res => {
        if (res) {
          this.$message.success("保存成功")
          this.dialogFormVisible = false
          this.load()
        } else {
          this.$message.success("保存失败")
        }
      })
    },
    cancel() { // 对话框取消按钮事件
      this.dialogFormVisible = false
      this.load()
    },
    bookSave() {
      // 此函数必定在handleBookInfoEdit之后触发
      // 而在handleBookInfo中this.bookInfoEditForm = this.bookInfoForm = res已设置了id的值
      // 故可以直接使用后端的saveOrUpadate方法
      this.request.post("http://localhost:9090/book", this.bookInfoEditForm).then(res => {
        if (res) {
          this.$message.success("保存成功")
          this.dialogBookInfoFormVisible = false
          this.dialogBookInfoEditFormVisible = false
          this.load()
        } else {
          this.$message.success("保存失败")
        }
      })
    },
    bookCancel() {
      this.dialogBookInfoFormVisible = false
      this.dialogBookInfoEditFormVisible = false
      this.load()
    },
    handleSelectionChange(val) { // 选择列表行
      this.multipleSelection = val
    },
    handleEdit(row) {
      this.form = row
      this.dialogFormVisible = true
    },
    handleBookInfo(row) {
      this.request.get("http://localhost:9090/book/" + row.bookId).then(res => {
        this.bookInfoForm = res // res = Book
        this.dialogBookInfoFormVisible = true
      })
    },
    handleBookInfoEdit() {
      // 此函数必定在handleBookInfo之后触发
      // 而在handleBookInfo中this.bookInfoForm = res已设置了id的值
      // 故可以直接使用后端的saveOrUpadate方法
      this.bookInfoEditForm = this.bookInfoForm
      this.dialogBookInfoEditFormVisible = true
    },
    handleCoverSuccess(res) {
      // 此函数必定在handleBookInfo之后触发
      // 而在handleBookInfo中this.bookInfoForm = res已设置了id的值
      // 故可以直接使用后端的saveOrUpadate方法
      this.bookInfoForm.coverurl = res
      this.request.post("http://localhost:9090/book", this.bookInfoForm).then(res => {
        if (res) {
          this.$message.success("上传成功")
          this.load()
        } else {
          this.$message.success("上传失败")
        }
      })
    },
    handleDel(id) {
      this.request.delete("http://localhost:9090/inventory/" + id).then(res => {
        if (res) {
          this.$message.success("删除成功")
          this.dialogFormVisible = false
          this.load()
        } else {
          this.$message.success("删除失败")
        }
      })
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

<style>
.headerBg {
  background: #eee !important;
}

.avatar-uploader {
  text-align: center;
  padding-bottom: 10px;
}

.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 90px;
  height: 90px;
  line-height: 90px;
  text-align: center;
}

.avatar {
  width: 90px;
  height: 90px;
  display: block;
}
</style>
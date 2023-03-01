<template>
  <div>
    <div style="margin: 10px 0">
      <el-input style="width: 183px" suffix-icon="el-icon-search" placeholder="请输入图片描述" v-model="imageInfo"></el-input>
      <el-button class="ml-5" type="primary" @click=load>搜索</el-button>
      <el-button class="ml-5" type="warning" @click=reset>重置</el-button>
      <el-button type="primary" @click="handleAdd">新增图片<i class="el-icon-circle-plus-outline"></i></el-button>
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
    </div>

    <el-table :data="tableData" border stripe :header-cell-class-name="headerBg"
              @selection-change="handleSelectionChange">
      <el-table-column type="selection" ></el-table-column>
      <el-table-column prop="id" label="ID"></el-table-column>
      <el-table-column prop="imageUrl" label="图片" width="240">
        <template slot-scope="scope">
          <img v-if="scope.row.imageurl" :src="scope.row.imageurl" class="avatar">
        </template>
      </el-table-column>
      <el-table-column prop="imageInfo" label="图片描述" width="240"></el-table-column>
      <el-table-column prop="adminId" label="创建者ID"></el-table-column>
      <el-table-column prop="adminNickname" label="创建者昵称" width="240"></el-table-column>
      <el-table-column prop="enable" label="启用">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.enable===1 ? true : false" active-color="#13ce66"
                     inactive-color="#ff4949"></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="240" align="center">
        <template slot-scope="scope">
          <el-button type="success" @click="handleEdit(scope.row)">编辑<i class="el-icon-edit"></i></el-button>
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
      <div>
        <el-upload
            class="avatar-uploader"
            action="http://localhost:9090/file/upload/"
            :show-file-list="false"
            :on-success="handleImageSuccess"
        >
          <img v-if="form.imageurl" :src="form.imageurl" class="avatar">
          <i v-else class="el-icon-plus avatar-uploader-icon"></i>
        </el-upload>
      </div>
      <el-form label-width="80px" size="small">
        <el-form-item label="图片描述">
          <el-input v-model="form.imageInfo" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="是否启用">
          <el-switch v-model="enableFormItem" active-color="#13ce66" inactive-color="#ff4949"></el-switch>
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
  name: "AdminCarousel",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 2,
      imageInfo: "",
      dialogFormVisible: false,
      form: {},
      enableFormItem: false,
      multipleSelection: [],
      headerBg: 'headerBg',
      shopuser: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
    }
  },
  created() {
    this.load()
  },
  methods: {
    // 请求分页查询数据
    load() {
      this.request.get("http://localhost:9090/carousel/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          imageInfo: this.imageInfo,
        }
      }).then(res => {
        // console.log(res) // 在浏览器控制台中打印
        this.tableData = res.records
        this.total = res.total
      })
    },
    reset() {
      this.imageInfo = ""
      this.load()
    },
    handleAdd() {
      this.dialogFormVisible = true
      this.form = {}
    },
    handleDelBatch() {
      let ids = this.multipleSelection.map(v => v.id) // [{}, {}, {}] => [1, 2, 3]
      this.request.post("http://localhost:9090/carousel/del/batch", ids).then(res => {
        if (res) {
          this.$message.success("批量删除成功")
          this.dialogFormVisible = false
          this.load()
        } else {
          this.$message.success("批量删除失败")
        }
      })
    },
    save() { // 对话框确定按钮事件
      this.form.adminId = this.shopuser.id
      this.form.enable = this.enableFormItem ? 1 : 0
      this.request.post("http://localhost:9090/carousel", this.form).then(res => {
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
    handleEdit(row) {
      this.form = row
      this.dialogFormVisible = true
    },
    handleSelectionChange(val) { // 选择列表行
      this.multipleSelection = val
    },
    handleImageSuccess(res) {
      this.form.adminId = this.shopuser.id
      this.form.imageurl = res
      this.request.post("http://localhost:9090/carousel", this.form).then(res => {
        if (res) {
          this.$message.success("上传成功")
          this.dialogFormVisible = false
          this.load()
        } else {
          this.$message.success("上传失败")
        }
      })
    },
    handleDel(id) {
      this.request.delete("http://localhost:9090/carousel/" + id).then(res => {
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

<style scoped>
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
  width: 220px;
  height: 90px;
  line-height: 90px;
  text-align: center;
}

.avatar {
  width: 220px;
  height: 90px;
  display: block;
}
</style>
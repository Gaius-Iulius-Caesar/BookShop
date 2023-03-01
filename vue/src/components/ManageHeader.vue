<template>
  <div style="line-height: 60px; display: flex">
    <div style="flex: 1;font-size: 20px">
      <span :class="collapseBtnClass" style="cursor: pointer; font-size: 18px " @click="collapse"></span>
      <el-breadcrumb separator="/" style="display: inline-block;margin-left: 10px">
        <el-breadcrumb-item :to="'/manage'">商家后台</el-breadcrumb-item>
        <el-breadcrumb-item>{{ currentPathName }}</el-breadcrumb-item>
        <el-breadcrumb-item>{{ currentSubPathName }}</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <el-dropdown style="width: 150px; cursor: pointer; text-align: right">
      <div style="display: inline-block">
        <img :src="shopuser.avatarurl" alt=""
             style="width: 30px; border-radius: 50%; position: relative; top: 10px; right: 5px">
        <span>{{ shopuser.nickname ? shopuser.nickname : shopuser.username }}</span><i class="el-icon-arrow-down" style="margin-left: 5px"></i>
      </div>
      <el-dropdown-menu slot="dropdown" style="width: 100px; text-align: center">
        <el-dropdown-item style="font-size: 14px; padding: 5px 0">
          <span style="text-decoration: none" @click="handleChangePassword">修改密码</span>
        </el-dropdown-item>
        <el-dropdown-item style="font-size: 14px; padding: 5px 0">
          <span style="text-decoration: none" @click="gotoManageHome">书店信息</span>
        </el-dropdown-item>
        <el-dropdown-item style="font-size: 14px; padding: 5px 0">
          <span style="text-decoration: none" @click="logout">退出</span>
        </el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>

    <el-dialog title="密码修改" :visible.sync="dialogFormVisible" width="30%">
      <el-form label-width="80px" size="small" :model="form" :rules="rules" ref="form">
        <el-form-item label="原密码">
          <el-input v-model="form.oldPassword" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="新密码">
          <el-input v-model="form.newPassword" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="重复密码">
          <el-input v-model="form.repeatePassword" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
        <el-button type="primary" @click="savePassword">确 定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
export default {
  name: "Header",
  props: {
    collapseBtnClass: String,
  },
  watch: {
    '$route': function () {
      let path = localStorage.getItem("currentPath") // 取出当前路由
      if (path === "ManageHome" || path === "Manage") {
        this.currentPathName = "书店信息"
        this.currentSubPathName = ''
      } else if (path === "Inventory") {
        this.currentPathName = "库存数据"
        this.currentSubPathName = ''
      } else if (path === "Records") {
        this.currentPathName = "销售数据"
        this.currentSubPathName = '记录'
      } else if (path === "Statistics") {
        this.currentPathName = "销售数据"
        this.currentSubPathName = '统计'
      }
    }
  },
  data() {
    return {
      currentPathName: '',
      currentSubPathName: '',
      shopuser: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      dialogFormVisible: false,
      form: {},
      rules: {
        oldPassword: [
          {required: true, message: '请输入原密码', trigger: 'blur'},
          {min: 3, message: '长度不少于3位', trigger: 'blur'}
        ],
        newPassword: [
          {required: true, message: '请输入新密码', trigger: 'blur'},
          {min: 3, message: '长度不少于3位', trigger: 'blur'}
        ],
        repeatePassword: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 3, message: '长度不少于3位', trigger: 'blur'}
        ],
      },
    }
  },
  created() {

  },
  methods: {
    collapse() {
      this.$parent.$parent.$parent.$parent.collapse()  // 通过4个 $parent 找到父组件，从而调用其折叠方法
      // this.$emit("asideCollapse")
    },
    handleChangePassword() {
      this.dialogFormVisible = true
      this.form = {}
    },
    cancel() { // 对话框取消按钮事件
      this.dialogFormVisible = false
    },
    savePassword() { // 对话框确定按钮事件--这是处理密码修改的事件
      this.$refs['form'].validate((valid) => {
        if (valid) {
          if (this.form.newPassword !== this.form.repeatePassword) {
            this.$message.error("2次输入的新密码不相同")
            return false
          }
          this.request.get("http://localhost:9090/user/change-password", {
            params: {
              id: this.shopuser.id,
              oldPassword: this.form.oldPassword,
              newPassword: this.form.newPassword,
            }
          }).then(res => {
            if (res.code === '200') {
              this.$message.success("修改成功")
              this.dialogFormVisible = false
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      })
    },
    gotoManageHome() {
      this.$router.push('/manage/managehome')
    },
    logout() {
      this.$router.push("/login")
      localStorage.removeItem("user")
      this.$message.success("退出成功")
    },
  }
}
</script>

<style>

</style>
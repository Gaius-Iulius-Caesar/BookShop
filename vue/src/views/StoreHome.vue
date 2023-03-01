<template>
  <div style="margin: 10px">
    <el-upload
        class="avatar-uploader"
        action="http://localhost:9090/file/upload/"
        :show-file-list="false"
        :on-success="handleAvatarSuccess"
    >
      <img v-if="form.avatarurl" :src="form.avatarurl" class="avatar">
      <i v-else class="el-icon-plus avatar-uploader-icon"></i>
    </el-upload>
    <div style="display:flex; margin-left: 39.5%">
      <el-form ref="form" :model="form" label-width="70px">
        <el-form-item label="ID">
          <el-input v-model="form.id" disabled></el-input>
        </el-form-item>
        <el-form-item label="账号">
          <el-input v-model="form.username" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="姓名">
          <el-input v-model="form.nickname" :disabled="disabled"></el-input>
        </el-form-item>
        <el-form-item label="联系方式">
          <el-input v-model="form.phone" :disabled="disabled"></el-input>
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="form.address" :disabled="disabled"></el-input>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email" :disabled="disabled"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="disabled=!disabled">编辑</el-button>
          <el-button type="primary" @click="save" :disabled="disabled">确认修改</el-button>
          <el-button type="danger" @click="handleChangePassword">修改密码</el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-dialog title="密码修改" :visible.sync="dialogPassFormVisible" width="30%">
      <el-form label-width="80px" size="small" :model="passForm" :rules="rules" ref="pass">
        <el-form-item label="原密码">
          <el-input v-model="passForm.oldPassword" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="新密码">
          <el-input v-model="passForm.newPassword" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="重复密码">
          <el-input v-model="passForm.repeatePassword" autocomplete="off"></el-input>
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
  name: "StoreHome",
  data() {
    return {
      form: {},
      disabled: true,
      buyUser: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      passForm: {},
      dialogPassFormVisible: false,
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
    this.load()
  },
  methods: {
    load() {
      this.request.get("http://localhost:9090/user/" + this.buyUser.id).then(res => {
        if (res.code === "200") {
          this.form = res.data
        } else {
          this.$message.success("获取信息失败")
        }
      })
    },
    handleAvatarSuccess(res) {
      this.request.get("http://localhost:9090/user/save-avatar", {
        params: {
          id: this.buyUser.id,
          avatarurl: res,
        }
      }).then(res => {
        if (res.code === '200') {
          this.form.avatarurl = res.msg // 此msg为avatarurl
          // 重新更新用户信息
          let currentShopUser = JSON.parse(localStorage.getItem("user"))
          currentShopUser.avatarurl = this.form.avatarurl
          localStorage.setItem("user", JSON.stringify(currentShopUser))
          location.reload()
        }
      })
    },
    save() { // 对话框确定按钮事件
      this.form.id = this.buyUser.id
      this.request.post("http://localhost:9090/user", this.form).then(res => {
        if (res) {
          this.$message.success("保存成功")
          this.load()
        } else {
          this.$message.success("保存失败")
        }
      })
    },
    handleChangePassword() {
      this.dialogPassFormVisible = true
      this.passForm = {}
    },
    cancel() { // 对话框取消按钮事件
      this.dialogPassFormVisible = false
    },
    savePassword() { // 对话框确定按钮事件--这是处理密码修改的事件
      this.$refs['pass'].validate((valid) => {
        if (valid) {
          if (this.passForm.newPassword !== this.passForm.repeatePassword) {
            this.$message.error("2次输入的新密码不相同")
            return false
          }
          this.request.get("http://localhost:9090/user/change-password", {
            params: {
              id: this.buyUser.id,
              oldPassword: this.passForm.oldPassword,
              newPassword: this.passForm.newPassword,
            }
          }).then(res => {
            if (res.code === '200') {
              this.$message.success("修改成功")
              this.dialogPassFormVisible = false
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      })
    },
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
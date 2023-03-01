<template>
  <div class="wrapper">
    <div style="margin: 100px auto; background-color: #fff; width: 350px; height: 400px; padding: 20px; border-radius: 10px">
      <div style="margin: 20px 0; text-align: center; font-size: 24px"><b>注册</b></div>
      <el-form :model="user" :rules="rules" ref="user">
        <el-form-item prop="username">
          <el-input placeholder="请输入账号" size="medium" style="margin: 5px 0" prefix-icon="el-icon-user" v-model="user.username"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input placeholder="请输入密码" size="medium" style="margin: 5px 0" prefix-icon="el-icon-lock" show-password v-model="user.password"></el-input>
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input placeholder="请确认密码" size="medium" style="margin: 5px 0" prefix-icon="el-icon-lock" show-password v-model="user.confirmPassword"></el-input>
        </el-form-item>
        <el-form-item style="text-align: center">
          <el-radio v-model="radio" label="1">商家</el-radio>
          <el-radio v-model="radio" label="2">顾客</el-radio>
          <el-radio v-model="radio" label="3">管理员</el-radio>
        </el-form-item>
        <el-form-item style="margin: 10px 0; text-align: center">
          <el-button type="warning" size="small"  autocomplete="off" @click="$router.push('/login')">返回登录</el-button>
          <el-button type="primary" size="small"  autocomplete="off" @click="register" style="margin-left: 20%">确定</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>

export default {
  name: "register",
  data() {
    return {
      radio: "1",
      user: {},
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 3, max: 10, message: '长度在 3 到 5 个字符', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur' }
        ],
      }
    }
  },
  methods: {
    register() {
      this.user.role = parseInt(this.radio)
      this.$refs['user'].validate((valid) => {
        if (valid) {  // 表单校验合法
          if(this.user.password !== this.user.confirmPassword){
            this.$message.error("两次输入的密码不一致")
            return false
          }
          this.request.post("http://localhost:9090/user/register", this.user).then(res => {
            if(res.code === '200') {
              this.$message.success("注册成功")
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      });
    }
  }
}
</script>

<style>
.wrapper {
  height: 100vh;
  background-image: linear-gradient(to bottom right, #6746fc, #fb3f55);
  overflow: hidden;
}
</style>

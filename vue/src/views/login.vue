<template>
  <div class="wrapper">
    <div style="margin: 200px auto; background-color: #fff; width: 350px; height: 350px; padding: 20px; border-radius: 10px">
      <div style="margin: 20px 0; text-align: center; font-size: 24px"><b>登录</b></div>
      <el-form :model="user" :rules="rules" ref="user">
        <el-form-item prop="username">
          <el-input size="medium" style="margin: 10px 0" prefix-icon="el-icon-user" v-model="user.username"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input size="medium" style="margin: 10px 0" prefix-icon="el-icon-lock" show-password
                    v-model="user.password"></el-input>
        </el-form-item>
        <el-form-item style="text-align: center">
          <el-radio v-model="radio" label="1">商家</el-radio>
          <el-radio v-model="radio" label="2">顾客</el-radio>
          <el-radio v-model="radio" label="3">管理员</el-radio>
        </el-form-item>
        <el-form-item style="margin: 10px 0; text-align: center">
          <el-button type="warning" size="small" autocomplete="off" @click="$router.push('/register')">注册</el-button>
          <el-button type="primary" size="small" autocomplete="off" @click="login" style="margin-left: 20%">登录
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>

export default {
  name: "login",
  data() {
    return {
      radio: "1",
      user: {}, // 存储账号、密码、身份
      rules: {
        username: [
          {required: true, message: '请输入用户名', trigger: 'blur'},
          {min: 3, max: 10, message: '长度在 3 到 5 个字符', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur'}
        ],
      }
    }
  },
  methods: {
    login() {
      this.user.role = parseInt(this.radio)
      this.$refs['user'].validate((valid) => {
        // 表单校验合法
        if (valid) {
          this.request.post("http://localhost:9090/user/login", this.user).then(res => {
            if (res.code === '200') {
              // 注意，此处获取到的user应为userDTO
              localStorage.setItem("user", JSON.stringify(res.data))  // 存储用户信息到浏览器
              if (this.radio === "1")
                this.$router.push("/manage")
              else if (this.radio === "2")
                this.$router.push("/store")
              else if (this.radio === "3")
                this.$router.push("/admin")
              this.$message.success({message: "登陆成功", duration: 1200})
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

<template>
  <div style="width: 800px;margin-left: 55px;">
    <el-descriptions class="margin-top" :column="1" :labelStyle=labelStyle :contentStyle=contentStyle border>
      <template slot="extra">
        <el-button type="primary" size="small" @click="handleEdit" class="el-icon-edit">编辑</el-button>
      </template>
      <el-descriptions-item>
        <template slot="label"><i class="el-icon-picture-outline"></i>头像</template>
        <el-upload
            class="avatar-uploader"
            action="http://localhost:9090/file/upload/"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
        >
          <img v-if="form.avatarurl" :src="form.avatarurl" class="avatar">
          <i v-else class="el-icon-plus avatar-uploader-icon"></i>
        </el-upload>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label"><i class="el-icon-user"></i>管理员昵称</template>
        {{ form.nickname }}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label"><i class="el-icon-eleme"></i>邮箱</template>
        {{ form.email }}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label"><i class="el-icon-mobile-phone"></i>手机号</template>
        {{ form.phone }}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label"><i class="el-icon-office-building"></i>联系地址</template>
        {{ form.address }}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label"><i class="el-icon-tickets"></i>备注</template>
        <el-tag size="small">学校</el-tag>
      </el-descriptions-item>
    </el-descriptions>

    <el-dialog title="管理员" :visible.sync="dialogFormVisible" width="30%">
      <el-form label-width="90px" size="small">
        <el-form-item label="管理员昵称">
          <el-input v-model="form.nickname" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="form.phone" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="联系地址">
          <el-input v-model="form.address" autocomplete="off"></el-input>
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
  name: "AdminHome",
  data() {
    return {
      labelStyle: {
        "height": "70px",
        "width": "150px",
        "font-size": "14px"
      },
      contentStyle: {
        "font-size": "17px"
      },
      form: {},
      adminUser: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      dialogFormVisible: false,
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      this.request.get("http://localhost:9090/user/" + this.adminUser.id).then(res => {
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
          id: this.adminUser.id,
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
    handleEdit() {
      this.dialogFormVisible = true
    },
    cancel() { // 对话框取消按钮事件
      this.dialogFormVisible = false
      this.load()
    },
    save() { // 对话框确定按钮事件
      this.form.id = this.adminUser.id
      this.request.post("http://localhost:9090/user", this.form).then(res => {
        if (res) {
          this.$message.success("保存成功")
          this.dialogFormVisible = false
          this.load()
        } else {
          this.$message.success("保存失败")
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
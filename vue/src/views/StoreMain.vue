<template>
  <div>
    <div>
      <el-carousel :interval="2000" type="card" height="200px">
        <el-carousel-item v-for="item in carouselData" :key="item.id">
          <img :src="item.imageurl" :alt="item.id" style="width: 100%; height: 100%">
        </el-carousel-item>
      </el-carousel>
    </div>
    <!--搜索框和查找按钮-->
    <div class="centerShow">
      <el-input v-model="bookname" type="text" prefix-icon="el-icon-search" style="width: 600px;margin-right: 10px"
                placeholder="请输入请您要查找的商品"/>
      <el-button type="primary" icon="el-icon-search" @click="load">查找</el-button>
    </div>
    <!--分类导航-->
    <div class="centerShow">
      <el-tabs v-model="category" @tab-click="load">
        <el-tab-pane label="综合" name=""></el-tab-pane>
        <el-tab-pane :label="item" :name="item" v-for="(item) in  categoryData" :key="item"></el-tab-pane>
      </el-tabs>
    </div>

    <!-- 显示查询结果 -->
    <div class="centerShow">
      <el-card style="width: 250px;margin: 10px" v-for="item in goods" :key="item.id"
      >
        <div>
          <div style="width: 100%;height:200px;display: flex;justify-content: center">
            <img :src="item.coverurl" alt="item.name" style="width: 200px;height: 200px;border-radius: 20px">
          </div>
          <div class="body">
            <el-tag class="tagClass">{{ item.bookname }}</el-tag>
            <el-tag class="tagClass">作者:{{ item.author }}</el-tag>
            <el-tag class="tagClass"> 库存:{{ item.stock }}</el-tag>
            <el-tag class="tagClass" style="text-decoration:line-through ">原价:{{ item.price }}</el-tag>
            <el-tag class="tagClass"> 现价:{{ (item.discount * item.price).toFixed(2) }}</el-tag>
            <div style="margin-top: 10px">
              <el-button type="mini" @click="checkGoodsDetail(item)">查看</el-button>
              <el-button type="mini" @click="putIntoShoppingCart(item)">加入购物车</el-button>
            </div>
          </div>
        </div>
      </el-card>
    </div>

    <!--  显示商品详情-diag-->
    <el-dialog title="图书信息" :visible.sync="dialogBookInfoFormVisible" width="30%">
      <el-descriptions size="mid" :column="1" :colon="false" :contentStyle=contentStyle
                       style="margin-left: 10%;margin-right: 5%">
        <el-descriptions-item label="供货商">{{ bookInfoForm.shopNickname }}</el-descriptions-item>
        <el-descriptions-item label="书名">{{ bookInfoForm.bookname }}</el-descriptions-item>
        <el-descriptions-item label="作者">{{ bookInfoForm.author }}</el-descriptions-item>
        <el-descriptions-item label="简介">{{ bookInfoForm.introduction }}</el-descriptions-item>
        <el-descriptions-item label="出版社">{{ bookInfoForm.press }}</el-descriptions-item>
        <el-descriptions-item label="分类">
          <el-tag>{{ bookInfoForm.category }}</el-tag>
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "StoreMain",
  data() {
    return {
      carouselData: [],
      goods: [],
      bookname: "",
      category: "0",
      categoryData: [],
      bookInfoForm: {},
      dialogBookInfoFormVisible: false,
      contentStyle: {"font-size": "13px"},
      buyUser: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
    }
  },
  created() {
    this.load()
    // 获得图书分类信息
    this.request.get("http://localhost:9090/book/category").then(res => {
      this.categoryData = res
    })
  },
  mounted() {
    this.carouselLoad()
  },
  methods: {
    carouselLoad() {
      this.request.get("http://localhost:9090/carousel/play").then(res => {
        // console.log(res) // 在浏览器控制台中打印
        if (res.code === "200") {
          this.carouselData = res.data;
        } else
          this.$message.error("获取轮播图数据失败")
      })
    },
    load() {
      this.request.get("http://localhost:9090/inventory/goods", {
        params: {
          bookname: this.bookname,
          category: this.category
        }
      }).then(res => {
        // console.log(res) // 在浏览器控制台中打印
        this.goods = res
      })
    },
    checkGoodsDetail(item) {
      this.bookInfoForm = item
      this.dialogBookInfoFormVisible = true
    },
    putIntoShoppingCart(item) {
      let form = {}
      form.buyId = this.buyUser.id
      form.inventoryId = item.id
      this.request.post("http://localhost:9090/cart", form).then(res => {
        if(res)
          this.$message.success("购物车修改成功")
        else
          this.$message.error("购物车修改失败")
      })
    }
  }
}
</script>

<style scoped>
.centerShow {
  margin-top: 10px;
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
}
</style>
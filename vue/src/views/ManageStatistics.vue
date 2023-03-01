<template>
  <div>
    <el-row>
      <el-col :span="12">
        <div id="lineAndBar" style="width: 500px;height: 400px"></div>
      </el-col>
      <el-col :span="12">
        <div id="pie" style="width: 500px;height: 400px"></div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts';

export default {
  name: "Statistics",
  data() {
    return {
      shopuser: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
    }
  },
  mounted() { // 页面元素渲染之后触发
    // 折线与柱状复合图
    let lineAndBarOption = {
      title: {
        text: '本年度销量走势',
        // subtext: 'Fake Data',
        left: 'center'
      },
      xAxis: {
        type: 'category',
        data: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月','十月','十一月','十二月']
      },
      yAxis: {
        type: 'value'
      },
      series: [
        {
          data: [150, 230, 224, 218, 135, 147, 260],
          type: 'line'
        },
        {
          data: [150, 230, 224, 218, 135, 147, 260],
          type: 'bar'
        }]
    };
    this.request.get("http://localhost:9090/echarts/lineandbar/" + this.shopuser.id).then(res => {
      if(res.code === "200"){
        lineAndBarOption.series[0].data = res.data
        lineAndBarOption.series[1].data = res.data
        const lineAndBarDom = document.getElementById('lineAndBar');
        const lineAndBarChart = echarts.init(lineAndBarDom);
        lineAndBarChart.setOption(lineAndBarOption);
      }
      else
        this.$message.error("获取数据失败")
    })
    // 饼状图
    let pieOption = {
      title: {
        text: '各种类图书销售占比',
        // subtext: 'Fake Data',
        left: 'center'
      },
      tooltip: {
        trigger: 'item'
      },
      legend: {
        orient: 'vertical',
        left: 'left'
      },
      series: [
        {
          name: '种类',
          type: 'pie',
          radius: '50%',
          data: [],
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }
      ]
    };
    this.request.get("http://localhost:9090/echarts/pie/" + this.shopuser.id).then(res => {
      if(res.code === "200"){
        pieOption.series[0].data = res.data
        const pieDom = document.getElementById('pie');
        const pieChart = echarts.init(pieDom);
        pieChart.setOption(pieOption);
      }
      else
        this.$message.error("获取数据失败")
    })
  }
}
</script>

<style scoped>

</style>
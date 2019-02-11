## 能够定位、查询天气的天气预报Demo

app架构：

- LocationProvider
  - LocationFinder 位置查找
  - LocationList 城市列表
  - mAdapter 城市列表RecyclerView
- WeatherFragment
  - WeatherFragment 存放天气详情
- WeatherRequest
  - NetClient 访问MySQL得到城市列表
  - PureNetUtil 引用的网络工具库
  - Query 调用天气api
- WeatherStructure
  - WeatherDetail 存放每一天的天气
  - WeatherTotal 存放获取到的所有数据
- MainWindow 主程序



效果框架：

- 天气界面使用```viewpaget+fragment+tablayout```联动
- 城市列表使用```RecyclerView+SearchView```
- 主界面为```RelativeLayout```

后端框架：

- Gson解析传值

- EasyPermission 权限管理

- butterknife(目前10.0的版本貌似不兼容Android Studio 3.3+Gradle 4.10.1)


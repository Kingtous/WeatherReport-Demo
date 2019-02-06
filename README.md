## 一个能够定位、查询天气的app-Demo

app架构：

- LocationProvider
  - LocationFinder
  - LocationList

- WeatherFragment
  - WeatherFragment
- WeatherRequest
  - NetClient
  - PureNetUtil(引用)
  - Query
  - WeatherDetail
  - WeatherTotal
- MainWindow主程序



效果框架：

- 天气界面使用```viewpaget+fragment+tablayout```联动

- 城市列表使用```listview```展现

  

后端框架：

- Gson解析传值

- butterknife(目前10.0的版本貌似不兼容Android Studio 3.3+Gradle 4.10.1)

  

TODO:

- RecyclerView带点击事件
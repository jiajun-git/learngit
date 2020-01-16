# Layui

[官方文档](https://www.layui.com/doc/)

### 1.layui日历控件设置选择日期不能超过当前日期

```html
	layui.use('laydate', function() {
			var laydate = layui.laydate;
			laydate.render({
				elem : '#begin',//指定元素\
                type: 'datetime',//图形化选择时间\
				max : getNowFormatDate() //设置最大时间\
			});
			laydate.render({
				elem : '#end',
                type: 'datetime',
				max : getNowFormatDate()
			});
		});
	
	function getNowFormatDate() {
		var date = new Date();
		var seperator1 = "-";
		var seperator2 = ":";
		var month = date.getMonth() + 1;
		var strDate = date.getDate();
		if (month >= 1 && month <= 9) {
			month = "0" + month;
		}
		if (strDate >= 0 && strDate <= 9) {
			strDate = "0" + strDate;
		}
		var currentdate = date.getFullYear() + seperator1 + month
				+ seperator1 + strDate + " " + date.getHours() + seperator2
				+ date.getMinutes() + seperator2 + date.getSeconds();
		return currentdate;
	}
```

### 2.表格自动渲染

所谓的自动渲染，即：在一段 table 容器中配置好相应的参数，由 table 模块内部自动对其完成渲染，而无需你写初始的渲染方法。其特点在上文也有阐述。你需要关注的是以下三点：

1) 带有 *class="layui-table"* 的 ** 标签。
2) 对标签设置属性 *lay-data=""* 用于配置一些基础参数
3) 在 * 标签中设置属性*lay-data=""*用于配置表头信息

按照上述的规范写好table原始容器后，只要你加载了layui 的 table 模块，就会自动对其建立动态的数据表格。

```html
<table class="layui-table" lay-data="{height:315, url:'/demo/table/user/', page:true, id:'test'}" lay-filter="test">
  <thead>
    <tr>
      <th lay-data="{field:'id', width:80, sort: true}">ID</th>
      <th lay-data="{field:'username', width:80}">用户名</th>
      <th lay-data="{field:'sex', width:80, sort: true}">性别</th>
      <th lay-data="{field:'city'}">城市</th>
      <th lay-data="{field:'sign'}">签名</th>
      <th lay-data="{field:'experience', sort: true}">积分</th>
      <th lay-data="{field:'score', sort: true}">评分</th>
      <th lay-data="{field:'classify'}">职业</th>
      <th lay-data="{field:'wealth', sort: true}">财富</th>
    </tr>
  </thead>
</table>
```

### 3.时间控件laydate

```html
 layui.use('laydate', function(){
  var laydate = layui.laydate;

  //执行一个laydate实例
  laydate.render({
    elem: '#startTime' //指定元素\
    ,type: 'datetime'
    ,max : getNowFormatDate()
  });

  laydate.render({
    elem: '#endTime' //指定元素\
    ,type: 'datetime'
    ,max : getNowFormatDate()
  });
});


独立版的laydate:

laydate.render({
elem: '#birthday'
})
```


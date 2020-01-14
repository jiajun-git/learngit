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

# python

#### 1.概念

解释型语言，解释器逐行解释每一句源代码，速度比编译型语言慢。

完全面向对象，拥有一份强大的标准库，python社区提供了大量的第三方模块

行首不能有空格      unexpected indent

交互式运行，输入python  >>>    exit()或ctl+d退出

#### 2.注释

#单行注释         同时可以在代码后面添加单行注释
   多行注释   用一对连续的三个引号（单引号和双引号都可以）‘’‘   ’‘’

#### 3.运算符

python中*运算符还可以用于字符串，计算结果就是字符串重复指定次数的结果
例如：“-” * 10          -->"----------"

#### 4.在python中定义变量

在python中定义变量时是不需要指定变量的类型的，解释器会根据等号右边的数据自动推导出类型
数字型和非数字型（字符串、元组、字典）
True对应的数字是1   False对应的数字是0

#### 5.input键盘输入       

输入的内容都是字符串类型

#### 6.字符串操作

**strip**可用于去除字符串中首尾的特殊字符，包括空格、换行符等     a=a.strip()

​      str = "00000003210Runoob01230000000"; 
​      print str.strip( '0' );  # 去除首尾字符 0
​      str2 = "   Runoob      ";   # 去除首尾空格
​      print str2.strip();

`.replace(``'"'``,'')``#去除所有的双引号`

**split** 按指定分隔符**分割**字符串
​         test="Alex Jobs Tim Jack"
​         test1=test.split(" ")
​         print(test1)
​         ['Alex', 'Jobs', 'Tim', 'Jack']   

​     txt = "Google#Runoob#Taobao#Facebook"

​    第二个参数为 1，返回两个参数列表
​    x = txt.split("#", 1)
​    print x

​    ['Google', 'Runoob#Taobao#Facebook']

**join** 按指定分隔符拼接字符串
​         print(test1)
​         ['Alex', 'Jobs', 'Tim', 'Jack']
​         print(' '.join(test1))
​         Alex Jobs Tim Jack
**format**
​        message="Hi {0}, Let's {1}"     #0,1必须按顺序，相当于索引
​        print(message.format('Jack','Python'))
​        Hi Jack, Let's Python
输出格式化字符串  print("格式化字符串" % (变量1，变量2...))
​        %s   字符串
​        %d   十进制整数
​        %f    浮点数
​        %%   输出%
​                例子：print("单价为：%d" %price)
​                            print("单价为：%.2f" %price)   # %.2f表示显示小数点后面2位

**python开发中，Tab和空格不要混用，要么全是空格，要么全是Tab**

#### 7.if语句

​      if 要判断得条件：
​         条件成立语句
​      else：
​         不成立语句



#### 8.编码函数

对于单个字符的编码，Python提供了`ord()`函数获取字符的整数表示，`chr()`函数把编码转换为对应的字符：

```
>>> ord('A')
65
>>> ord('中')
20013
>>> chr(66)
'B'
>>> chr(25991)
'文'
```



#### 9.格式化字符串

在Python中，采用的格式化方式和C语言是一致的，用`%`实现，举例如下：

```
>>> 'Hello, %s' % 'world'
'Hello, world'
>>> 'Hi, %s, you have $%d.' % ('Michael', 1000000)
'Hi, Michael, you have $1000000.'
```

`%`运算符就是用来格式化字符串的。在字符串内部，`%s`表示用字符串替换，`%d`表示用整数替换，有几个`%?`占位符，后面就跟几个变量或者值，顺序要对应好。如果只有一个`%?`，括号可以省略。

如果你不太确定应该用什么，`%s`永远起作用，它会把任何数据类型转换为字符串：

```
>>> 'Age: %s. Gender: %s' % (25, True)
'Age: 25. Gender: True'
```



#### 10.列表、字典

Python内置的一种数据类型是列表：list。list是一种有序的集合，可以随时添加和删除其中的元素。

```
>>> classmates = ['Michael', 'Bob', 'Tracy']
>>> classmates
['Michael', 'Bob', 'Tracy']
```

另一种有序列表叫元组：tuple。tuple和list非常类似，但是tuple一旦初始化就不能修改，比如同样是列出同学的名字：

```
>>> classmates = ('Michael', 'Bob', 'Tracy')
```



字典是另一种可变容器模型，且可存储任意类型对象。字典的每个键值 key=>value 对用冒号 : 分割，每个键值对之间用逗号 , 分割，整个字典包括在花括号 {} 中 ,格式如下所示：

```sh
d = {key1 : value1, key2 : value2 }
>>>dict = {'a': 1, 'b': 2, 'b': '3'} 
>>> dict['b'] 
'3' 
>>> dict 
{'a': 1, 'b': '3'}
键一般是唯一的，如果重复最后的一个键值对会替换前面的，值不需要唯一。值可以取任何数据类型，但键必须是不可变的，如字符串，数字或元组
```

```sh
#字典作为参数传递
dic={'abc':123,'aaa':333,'wer':334}
def text_dic(**dd):
    for a,b in dd.items():# a 代表键 ，b代表值
        print(a,b)
text_dic(**dic)
```

#### 11.循环

Python的循环有两种，一种是for...in循环，依次把list或tuple中的每个元素迭代出来，看例子：

```
names = ['Michael', 'Bob', 'Tracy']
for name in names:
    print(name)

Michael
Bob
Tracy
```

第二种循环是while循环，只要条件满足，就不断循环，条件不满足时退出循环。比如我们要计算100以内所有奇数之和，可以用while循环实现：

```
sum = 0
n = 99
while n > 0:
    sum = sum + n
    n = n - 2
print(sum)
```

`break`语句可以在循环过程中直接退出循环，而`continue`语句可以提前结束本轮循环，并直接开始下一轮循环。这两个语句通常都*必须*配合`if`语句使用。



#### 12.def函数

在Python中，定义一个函数要使用`def`语句，依次写出函数名、括号、括号中的参数和冒号`:`，然后，在缩进块中编写函数体，函数的返回值用`return`语句返回。

我们以自定义一个求绝对值的`my_abs`函数为例：

```
def my_abs(x):
    if x >= 0:
        return x
    else:
        return -x
print(my_abs(-99))
```

**空函数**

如果想定义一个什么事也不做的空函数，可以用`pass`语句：

```
def nop():
    pass
```

`pass`语句什么都不做，那有什么用？实际上`pass`可以用来作为占位符，比如现在还没想好怎么写函数的代码，就可以先放一个`pass`，让代码能运行起来。



#### 13.Python的with...as的用法



#### 14.open()

python open() 函数用于打开一个文件，创建一个 file 对象，相关的方法才可以调用它进行读写。

| r    | 以只读方式打开文件。文件的指针将会放在文件的开头。这是默认模式。 |
| ---- | ------------------------------------------------------------ |
| rb   | 以二进制格式打开一个文件用于只读。文件指针将会放在文件的开头。这是默认模式。 |
| r+   | 打开一个文件用于读写。文件指针将会放在文件的开头。           |
| rb+  | 以二进制格式打开一个文件用于读写。文件指针将会放在文件的开头。 |
| w    | 打开一个文件只用于写入。如果该文件已存在则打开文件，并从开头开始编辑，即原有内容会被删除。如果该文件不存在，创建新文件。 |
| wb   | 以二进制格式打开一个文件只用于写入。如果该文件已存在则打开文件，并从开头开始编辑，即原有内容会被删除。如果该文件不存在，创建新文件。 |
| w+   | 打开一个文件用于读写。如果该文件已存在则打开文件，并从开头开始编辑，即原有内容会被删除。如果该文件不存在，创建新文件。 |
| wb+  | 以二进制格式打开一个文件用于读写。如果该文件已存在则打开文件，并从开头开始编辑，即原有内容会被删除。如果该文件不存在，创建新文件。 |
| a    | 打开一个文件用于追加。如果该文件已存在，文件指针将会放在文件的结尾。也就是说，新的内容将会被写入到已有内容之后。如果该文件不存在，创建新文件进行写入。 |
| ab   | 以二进制格式打开一个文件用于追加。如果该文件已存在，文件指针将会放在文件的结尾。也就是说，新的内容将会被写入到已有内容之后。如果该文件不存在，创建新文件进行写入。 |
| a+   | 打开一个文件用于读写。如果该文件已存在，文件指针将会放在文件的结尾。文件打开时会是追加模式。如果该文件不存在，创建新文件用于读写。 |
| ab+  | 以二进制格式打开一个文件用于追加。如果该文件已存在，文件指针将会放在文件的结尾。如果该文件不存在，创建新文件用于读写。 |

**file对象方法**

- **file.read([size])**：size 未指定则返回整个文件，如果文件大小 >2 倍内存则有问题，f.read()读到文件尾时返回""(空字串)。 
-  **file.readline()**：返回一行。
-   **file.readlines([size])** ：返回包含size行的列表, size 未指定则返回全部行。
-   **for line in f: print line** ：通过迭代器访问。
-   **f.write("hello\n")**：如果要写入字符串以外的数据,先将他转换为字符串。
-   **f.tell()**：返回一个整数,表示当前文件指针的位置(就是到文件头的比特数)。
-   **f.seek(偏移量,[起始位置])**：用来移动文件指针。
  -  偏移量: 单位为比特，可正可负
  -   起始位置: 0 - 文件头, 默认值; 1 - 当前位置; 2 - 文件尾
-  **f.close()** 关闭文件

#### 15.正则表达式

- .                    匹配任意字符（不包括换行符）
- ^                    匹配开始位置，多行模式下匹配每一行的开始
- $                    匹配结束位置，多行模式下匹配每一行的结束
- \*                    匹配前一个元字符0到多次
- \+                    匹配前一个元字符1到多次
- ?                    匹配前一个元字符0到1次
- {m,n}                匹配前一个元字符m到n次
- \\                   转义字符，跟在其后的字符将失去作为特殊元字符的含义，例如\\.只能匹配.，不能再匹配任意字符
- []                   字符集，一个字符的集合，可匹配其中任意一个字符
- |                    逻辑表达式 或 ，比如 a|b 代表可匹配 a 或者 b
- (...)                分组，默认为捕获，即被分组的内容可以被单独取出，默认每个分组有个索引，从 1 开始，按照"("的顺序决定索引值
- (?iLmsux)            分组中可以设置模式，iLmsux之中的每个字符代表一个模式,用法参见 模式 I
- (?:...)              分组的不捕获模式，计算索引时会跳过这个分组
- (?P<name>...)        分组的命名模式，取此分组中的内容时可以使用索引也可以使用name
- (?P=name)            分组的引用模式，可在同一个正则表达式用引用前面命名过的正则
- (?#...)              注释，不影响正则表达式其它部分,用法参见 模式 I
- (?=...)              顺序肯定环视，表示所在位置右侧能够匹配括号内正则
- (?!...)              顺序否定环视，表示所在位置右侧不能匹配括号内正则
- (?<=...)             逆序肯定环视，表示所在位置左侧能够匹配括号内正则
- (?<!...)             逆序否定环视，表示所在位置左侧不能匹配括号内正则
- (?(id/name)yes|no)   若前面指定id或name的分区匹配成功则执行yes处的正则，否则执行no处的正则
- \number              匹配和前面索引为number的分组捕获到的内容一样的字符串
- \A                   匹配字符串开始位置，忽略多行模式
- \Z                   匹配字符串结束位置，忽略多行模式
- \b                   匹配位于单词开始或结束位置的空字符串
- \B                   匹配不位于单词开始或结束位置的空字符串
- \d                   匹配一个数字， 相当于 [0-9]
- \D                   匹配非数字,相当于 [^0-9]
- \s                   匹配任意空白字符， 相当于 [ \t\n\r\f\v]
- \S                   匹配非空白字符，相当于 [^ \t\n\r\f\v]
- \w                   匹配数字、字母、下划线中任意一个字符， 相当于 [a-zA-Z0-9_]
- \W                   匹配非数字、字母、下划线中的任意字符，相当于 [^a-zA-Z0-9_]



#### 16.怎么快速的对列表进行去重

以下的几种情况结果是一样的，去重之后顺序会改变:

```
ids = [1,2,3,3,4,2,3,4,5,6,1]
news_ids = []
for id in ids:
    if id not in news_ids:
        news_ids.append(id)
print news_ids
```

**或用set**

```
ids = [1,4,3,3,4,2,3,4,5,6,1]
ids = list(set(ids))
```

 **使用itertools.grouby**

```
import itertools
ids = [1,4,3,3,4,2,3,4,5,6,1]
ids.sort()
it = itertools.groupby(ids)
for k, g in it:
    print k
```

**或者使用删除元素索引的方法对列表去重，并且不改变原列表的顺序**

```sh
python for删除的时候会往前移(垃圾回收机制)，未遍历到的后一个占了前一个被删除的"位置"，导致这个数不会被遍历到，而使最后的结果错误.局部变量在栈内存中存在,当for循环语句结束,那么变量会及时被gc(垃圾回收器)及时的释放掉,不浪费空间；如果使用循环之后还想去访问循环语句中控制那个变量,使用while循环。

所以使用while循环删除nums中的Val(的下标)

nums = [1,2,3,3,4,2,3,4,5,6,1]
val = 3
while val in nums:
      nums.pop(nums.index(val))
print nums
return len(nums)
```



#### 17.根据值来寻找对应的键

```sh
Python字典中的键是唯一的，但不同的键可以对应同样的值，比如说uid，可以是1001。id同样可以是1001。这样的话通过值来获取指定的键，就不止一个！而且也并不太好处理。这里同样提供两种思路来处理。
方法一：
>>> dct = {'Name': 'Alice', 'Age': 18, 'uid': 1001, 'id': 1001}
>>> def get_key1(dct, value):
...     return list(filter(lambda k:dct[k] == value, dct))
>>> get_key1(dct, 1001)
['id', 'uid']

方法二：
>>> dct = {'Name': 'Alice', 'Age': 18, 'uid': 1001, 'id': 1001}
>>> def get_key2(dct, value):
...     return [k for (k,v) in dct.items() if v == value]
>>> get_key2(dct, 1001)
['id', 'uid']
>>> get_key2(dct, 1000)
[]
上面的两种方法可以很方便高效的来处理字典中按值来找键的问题
```



#### 18.find方法

```sh
>>>info = 'abca'
>>> print info.find('a')    # 从下标0开始，查找在字符串里第一个出现的子串，返回结果：0
0
>>> print info.find('a',1)  # 从下标1开始，查找在字符串里第一个出现的子串：返回结果3
3
>>> print info.find('3')    # 查找不到返回-1
-1
>>>
```

#### 19.判断ipv4地址为同一网段

```sh
https://www.codeleading.com/article/24711208980/

class ipSunetRoute(object):
 
    ##将IP地址转为二进制
    def ipToBinary(self,ip):
        '''ip address transformat into binary
        Argv:
            ip: ip address
        Return:
            binary
        '''
        ip_num = ip.split('.')
        x = 0
        
        ##IP地址是点分十进制，例如：192.168.1.33，共32bit
        ##第1节（192）向前移24位，第2节（168）向前移16位
        ##第3节（1）向迁移8位，第4节（33）不动
        ##然后进行或运算，得出数据
        for i in range(len(ip_num)):
            num = int(ip_num[i]) << (24 - i*8)
            x = x | num
 
        brnary = str(bin(x).replace('0b',''))
        return brnary
    
    ##将子网掩码转为二进制
    def maskToBinary(self,mask):
        '''netmask change, example: 24 or 255.255.255.0 change binary
        Argv:
            mask: netmask, example:24 or 255.255.255.0
        Return:
            binary
        '''
        mask_list = str(mask).split('.')
        
        ##子网掩码有两种表现形式，例如：/24或255.255.255.0
        if len(mask_list) == 1:
            ##生成一个32个元素均是0的列表
            binary32 = []
            for i in range(32):
                binary32.append('0')
 
            ##多少位子网掩码就是连续多少个1
            for i in range(int(mask)):
                binary32[i] = '1'
            
            binary = ''.join(binary32)
            
        ##输入的子网掩码是255.255.255.0这种点分十进制格式
        elif len(mask_list) == 4:
            binary = self.ipToBinary(mask)
 
        return binary
 
    ##判断IP地址是否属于这个网段
    def ipInSubnet(self,ip,subnet):
        '''
        Argv:
            ip: ip address,example:1.1.1.1
            subnet: subnet,example:1.1.1.0/24,or 1.1.1.0/255.255.255.0
        Return:
            False or True
        '''
        subnet_list = subnet.split('/')
        networt_add = subnet_list[0]
        network_mask = subnet_list[1]
 
        ##原来的得出的二进制数据类型是str，转换数据类型
        ip_num = int(self.ipToBinary(ip),2)
        subnet_num = int(self.ipToBinary(networt_add),2)
        mask_bin = int(self.maskToBinary(network_mask),2)
        
        ##IP和掩码与运算后比较
        if (ip_num & mask_bin) == (subnet_num & mask_bin):
            return True
        else:
            return False
```

```sh
      ##将/24的掩码转换位255.255.255.0这样点分十进制格式
    def maskChange(self,mask):
        '''netmask change, example: 24 change 255.255.255.0
        Argv:
            mask: 0 <= 32
        Return:
            netmask is ip, example 255.255.255.0
        '''
        mask = int(mask)
        binary32 = []
        for i in range(32):
            binary32.append('0')
        
        for i in range(mask):
            binary32[i] = '1'
            
        binary_ip = []
        for i in range(4):
            binary_ = ''.join(binary32[i * 8:i * 8 + 8])
            decimal = str(int(binary_, 2))
            binary_ip.append(decimal)
        netmask = '.'.join(binary_ip)
        return netmask
        
    ##和ipToBinary差不多
    def ipToNum(self,ip):
        '''ip address change binary
        Argv:
            ip: ip address,example:1.1.1.1
        Return:
            neumber
        '''
        ip_num = ip.split('.')
        ip = []
        for x in ip_num:
            ip.append(int(x))
        ip_num = ip[0]<<24 | ip[1]<<16 | ip[2]<<8 | ip[3]
        return ip_num
        
    ##和maskChange结合在一起，就与maskToBinary差不多，
    def maskToNum(self,mask):
        '''netmask change binary
        Argv:
            ip: netmask,example: 24 or 255.255.255.0
        Return:
            neumber
        '''
        mask_list = str(mask).split('.')
        if len(mask_list) == 1:
            mask = self.maskChange(int(mask))
        mask_num = self.ipToNum(mask)
        return mask_num
```

#### 20.enumerate() 函数

```sh
enumerate() 函数用于将一个可遍历的数据对象(如列表、元组或字符串)组合为一个索引序列，同时列出数据和数据下标，一般用在 for 循环当中。
>>>seasons = ['Spring', 'Summer', 'Fall', 'Winter']
>>> list(enumerate(seasons))
[(0, 'Spring'), (1, 'Summer'), (2, 'Fall'), (3, 'Winter')]
>>> list(enumerate(seasons, start=1))       # 下标从 1 开始
[(1, 'Spring'), (2, 'Summer'), (3, 'Fall'), (4, 'Winter')]

for循环
>>>seq = ['one', 'two', 'three']
>>> for i, element in enumerate(seq):
...     print i, element
... 
0 one
1 two
2 three
```

#### 21.字符串大小写转换

```sh
以下代码演示了如何将字符串转换为大写字母，或者将字符串转为小写字母等：
str = "www.runoob.com"
print(str.upper())          # 把所有字符中的小写字母转换成大写字母
print(str.lower())          # 把所有字符中的大写字母转换成小写字母
print(str.capitalize())     # 把第一个字母转化为大写字母，其余小写
print(str.title())          # 把每个单词的第一个字母转化为大写，其余小写 
执行以上代码输出结果为：
WWW.RUNOOB.COM
www.runoob.com
Www.runoob.com
Www.Runoob.Com

```


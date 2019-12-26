### Linux命令积累

## 1.rpm

  －ivh：安装显示安装进度--install--verbose--hash 

在rpm -ivh后面加入 --force --nodeps 来忽略软件包及文件的冲突，不验证套件依赖性

  －Uvh：升级软件包--Update； 

  －qpl：列出RPM软件包内的文件信息[Query Package list]； 

  －qpi：列出RPM软件包的描述信息[Query Package install package(s)]； 

  －qf：查找指定文件属于哪个RPM软件包[Query File]；

  －Va：校验所有的RPM软件包，查找丢失的文件[View Lost]；

  －e：删除包

**rpm 的一点简单用法；**  我们除了软件包管理器以外，还能通过rpm 命令来安装；是不是所有的软件包都能通过rpm 命令来安装呢？不是的，文件以.rpm 后缀结尾的才行；有时我们在一些网站上找到file.rpm ，都要用 rpm 来安装；  

一）初始化rpm 数据库；**  通过rpm 命令查询一个rpm 包是否安装了，也是要通过rpm 数据库来完成的；所以我们要经常用下面的两个命令来初始化rpm 数据库；

```
[root@localhost beinan]# rpm --initdb 
[root@localhost beinan]# rpm --rebuilddb 注：这个要花好长时间；
```

注：这两个参数是极为有用，有时rpm 系统出了问题，不能安装和查询，大多是这里出了问题；

  **二）RPM软件包管理的查询功能：** 

语法：rpm -q 软件名

  三）删除一个rpm 包； 

​    [root@localhost beinan]#rpm -e 软件包名 



## 2.linux如何查看所有的用户和组信息

    一、cat /etc/passwd   查看所有的用户信息
    
    二、cat /etc/passwd|grep 用户名，用于查找某个用户
    
    三、cat /etc/group查看所有组信息
    
    四、cat /etc/group|grep 组名，用于查找某个用户组
## 3.-z $1 是一个判断表达式，用于判断$1的值是否为空字符串

   若为空，则结果为true；否则为false。



## 4.shell log重定向到文件

bash for.sh &> e.txt 
或者 
bash for.sh 1> e.txt 2>&1 
或者 
bash for.sh 2> e.txt 1>&2

将正确信息和错误信息同时添加到e.txt中：这种方法只能覆盖式追加 



## 5.查看dns

cat /etc/resolv.conf

nameserver 1xx.xxx.xx.xxx
nameserver 1xx.xxx.xx.xxx
nameserver 1xx.xxx.xx.xxx

DNS 是计算机域名系统 (Domain Name System 或 Domain Name Service) 的缩写，它是由域名**解析器**和**域名服务器**组成的。通过它可以把你需要访问的网址找到然后把信息送到你电脑上



## 6.shell脚本

```sh
#例子：
#!/bin/sh
a="hello world!"
num=2
echo "a is : $a num is : ${num}nd"

运行结果： a is : hello world! num is : 2nd
```

https://www.cnblogs.com/handsomecui/p/5869361.html

```sh
数组定义法1：
arr=(1 2 3 4 5) # 注意是用空格分开，不是逗号！！
遍历（For循环法）：
for var in ${arr[@]};
do
    echo $var
done
```

```sh
#shell中#*,##*,#*,##*,% *,%% *的含义及用法
介绍下Shell中的${}、##和%%使用范例，本文给出了不同情况下得到的结果。
假设定义了一个变量为：
代码如下:
file=/dir1/dir2/dir3/my.file.txt
可以用${ }分别替换得到不同的值：
${file#*/}：删掉第一个 / 及其左边的字符串：dir1/dir2/dir3/my.file.txt
${file##*/}：删掉最后一个 /  及其左边的字符串：my.file.txt
${file#*.}：删掉第一个 .  及其左边的字符串：file.txt
${file##*.}：删掉最后一个 .  及其左边的字符串：txt
${file%/*}：删掉最后一个  /  及其右边的字符串：/dir1/dir2/dir3
${file%%/*}：删掉第一个 /  及其右边的字符串：(空值)
${file%.*}：删掉最后一个  .  及其右边的字符串：/dir1/dir2/dir3/my.file
${file%%.*}：删掉第一个  .   及其右边的字符串：/dir1/dir2/dir3/my
记忆的方法为：
# 是 去掉左边（键盘上#在 $ 的左边）
%是去掉右边（键盘上% 在$ 的右边）
单一符号是最小匹配；两个符号是最大匹配
${file:0:5}：提取最左边的 5 个字节：/dir1
${file:5:5}：提取第 5 个字节右边的连续5个字节：/dir2
也可以对变量值里的字符串作替换：
${file/dir/path}：将第一个dir 替换为path：/path1/dir2/dir3/my.file.txt
${file//dir/path}：将全部dir 替换为 path：/path1/path2/path3/my.file.txt
```

## 7.virsh的详细命令解析

hostname 得到主机名字
hostnamectl set-hostname controller  修改主机名
suspend （demo） 挂起虚拟机 
resume（demo） 回复虚拟机的suspend状态 



## 8.sout

sort将文件的每一行作为一个单位，相互比较，比较原则是从首字符向后，依次按ASCII码值进行比较，最后将他们按升序输出
-u 选项它的作用很简单，就是在输出行中去除重复行
-b   忽略每行前面开始出的空格字符。
-c   检查文件是否已经按照顺序排序。
-d   排序时，处理英文字母、数字及空格字符外，忽略其他的字符。
-f   排序时，将小写字母视为大写字母。
-i   排序时，除了040至176之间的ASCII字符外，忽略其他的字符。
-m   将几个排序好的文件进行合并。
-M   将前面3个字母依照月份的缩写进行排序。
-n   依照数值的大小排序。
-o<输出文件>   将排序后的结果存入指定的文件。
-r   以相反的顺序来排序。
-t<分隔字符>   指定排序时所用的栏位分隔字符。
+<起始栏位>-<结束栏位>   以指定的栏位来排序，范围由起始栏位到结束栏位的前一栏位



## 9.去除行首空格和tab

行首： echo "  abs  ssa " | sed -r 's/^\s*|\s*$//g'

行尾： sed 's/[ \t]*$//g' 



## 10.tr命令

```
echo "${ping_ip_list[@]}" | tr ' ' '\n' | sort -u | tr '\n' ' ')
```

tr ' ' '\n' 将空格转成换行

tr '\n' ' ' 将换行转换为空格



## 11.ssh

```
-1：强制使用ssh协议版本1；
-2：强制使用ssh协议版本2；
-4：强制使用IPv4地址；
-6：强制使用IPv6地址；
-A：开启认证代理连接转发功能；
-a：关闭认证代理连接转发功能；
-b：使用本机指定地址作为对应连接的源ip地址；
-C：请求压缩所有数据；
-F：指定ssh指令的配置文件；
-f：后台执行ssh指令；
-g：允许远程主机连接主机的转发端口；
-i：指定身份文件；
-l：指定连接远程服务器登录用户名；
-N：不执行远程指令；
-o：指定配置选项；
-p：指定远程服务器上的端口；
-q：静默模式；
-X：开启X11转发功能；
-x：关闭X11转发功能；
-y：开启信任X11转发功能。
```



## 12.cat命令：查看 组合文件

cat a.txt：查看文件的内容

cat a.txt >> b.txt：把a文件的内容组合到b文件内容的末尾

cat -n a.txt：查看文件并给文件标上行号



## 13.sed

```sh
sed [option] 'command' input_file

-n 使用安静silent模式。在一般sed的用法中，所有来自stdin的内容一般都会被列出到屏幕上。但如果加上-n参数后，则只有经过sed特殊处理的那一行(或者动作)才会被列出来
-e 直接在指令列模式上进行 sed 的动作编辑
-f 直接将 sed 的动作写在一个文件内，-f filename则可以执行filename内的sed命令
-r 让sed命令支持扩展的正则表达式(默认是基础正则表达式)
-i 直接修改读取的文件内容，而不是由屏幕输出

a ：新增， a 的后面可以接字串，而这些字串会在新的一行出现(目前的下一行)～
c ：取代， c 的后面可以接字串，这些字串可以取代 n1,n2 之间的行！
d ：删除，因为是删除啊，所以 d 后面通常不接任何咚咚；
i ：插入， i 的后面可以接字串，而这些字串会在新的一行出现(目前的上一行)；
p ：打印，亦即将某个选择的数据印出。通常 p 会与参数 sed -n 一起运行～
s ：取代，可以直接进行取代的工作哩！通常这个 s 的动作可以搭配正规表示法！例如 1,20s/old/new/g 就是

#替换文件中的字符串
sed  -i 's/要被取代的字串/新的字串/g' file    g代表全局选项，没有g只替换所有行的第一个匹配项

sed '1,$a\add one' test.txt 从第一行到最后一行所有行后追加"add one"字符串行
sed '/first/a\add one' test.txt 在匹配到first行追加"add one"字符串行

sed '1,$c\add one' test.txt 从第一行到最后一行所有行替换为"add one"字符串行
sed '/first/c\add one' test.txt 将匹配到first行替换为"add one"字符串行
sed -i '/xxx/s/aaa/fff/g' file    --表示针对文件，找出包含xxx的行，并将其中的aaa替换为fff
sed -i '3s/aaa/fff/' file         --表示针对file文件中的第三行，将其中的aaa替换为fff

sed -i'4,$d' test.txt 从第四行到最后一行全部删除
#打印文本指定行 p
sed -n '46,345p' test.txt

#删除指定行
sed -e '/abc/d'  a.txt //删除a.txt中含"abc"的行，但不改变a.txt文件本身，操作之后的结果在终端显示
sed -i '/abc/d'  a.txt   //直接删除a.txt文件中的adc行
删除第N行：
sed -i 'Nd' filename 
删除第M到N行：
sed -i 'M,Nd' filename

sed '/^first.*end$/s/line/text/g' test.txt 匹配以first开头end结尾的所有行，然后将line全部替换为text

# 特定字符串的行后插入新行 sed -i '/特定字符串/a 新行字符串' ab.txt  
```

```sh
sed -e 's/bin/BIN/g' -e's/nologin/NOLOGIN/g' passwd
sed -i 's/sh.cn.ao/rnd.ki.sw/g' ${WORKSPACE}/image/utils/artifactory.py
sed -i 's/sh.cn.ao/rnd.ki.sw/g' ${WORKSPACE}/image/files/rhel7.4-base.repo.j2
sed -i '/^proxy/d' ${WORKSPACE}/image/files/rhel7.4-base.repo.j2
sed -i '/proxy/d' ${WORKSPACE}/image/group_vars/bmc
```



## 14.linux >和>>

linux中经常会用到将内容输出到某文件当中，只需要在执行命令后面加上>或者>>号即可进入操作。

大于号：将一条命令执行结果（标准输出，或者错误输出，本来都要打印到屏幕上面的）重定向其它输出设备（文件，打开文件操作符，或打印机等等）

小于号：命令默认从键盘获得的输入，改成从文件，或者其它打开文件以及设备输入

/     >>是追加内容

/     >是覆盖原有内容

示例：


bogon:Desktop wenxuechao$ echo 'abc' > test.txt
bogon:Desktop wenxuechao$ echo '123' >> test.txt
执行效果，第一句命令会在桌面创建个test.txt的文件，并且将abc写到文件中。
第二句命令，会在文件下方，再次写入内容。

<小于号

mysql -u root -p -h test < test.sql 导入数据



## 15.ln命令

将某个文件链接到一个文件上
ln  -f / -n/ -s  SourceFile [ TargetFile ]
将一个或多个文件链接到一个目录上
ln -f / -n/ -s  SourceFile ... TargetDirectory

-f 促使 ln 命令替换掉任何已经存在的目的路径。如果目的路径已经存在，而没有指定 -f 标志，ln 命令不会创建新的链接，而是向标准错误写一条诊断消息并继续链接剩下的 SourceFiles。
-n 指定，如果链接是一个现有的文件，那么不要覆盖文件的内容。 -f 标志重设了这个标志。这是缺省的行为。 
-s 促使 ln 命令创建符号链接。符号链接中包含了它所链接的文件的名字。当对链接执行打开操作的时候，会使用到引用文件。对符号链接的 stat 调用会返回链接的目标文件；必须完成lstat 调用来获取链接的信息。可以使用 readlink 调用来读取符号链接的内容。符号链接可能跨越文件系统，指向目录。
注意：当为 -s 标志指定 SourceFile 参数的时候，必须使用绝对路径。如果没有指明绝对路径，那么当 SourceFile 和 TargetFile 参数位于不同的目录中的时候，可能会发生意外的结果。在创建符号链接之前，不需要存在源文件



## 16. 清空文件内容

1. /[dev](https://www.baidu.com/s?wd=dev&tn=SE_PcZhidaonwhc_ngpagmjz&rsv_dl=gh_pc_zhidao)/null表示空设备，这里就是把日志记录到空设备里，就是不记录日志

   ```)
   $ : > filename #其中的 : 是一个占位符, 不产生任何输出.
   
   　　$ > filename
   
   　　$ echo “” > filename
   
   　　$ echo /dev/null > filename
   
   　　$ echo > filename
   
   　　$ cat /dev/null > filename
   
       $ cp /dev/null filename 
   ```


## 17.切换成root用户

sudo -s/-i



## 18.查看keystore命令

/opt/jre/default/bin/keytool -v -list -keystore xxxxxxxx.keystore

/opt/jre/default/bin/  为java路径



## 19.chmod和chown

chmod语法：

将rwx看成是二进制的数，有用1表示，没有用0表示，那么rwx r-x r--就可以表示成：111 101 100,将其转换成为一个十进制数就是：754

\+ 表示添加权限

\- 表示删除权限

= 重置权限

修改文件夹 后面加一个-R

chown语法：

```
-rw-r--r--. 1 root root 0 Aug  6 09:17 file
```

root root表示file文件的所属用户组为root，所有者为root。

```
chown [选项]... [所有者][:[组]] 文件...
```

journalctl -xf -u crond  | grep -i cert 

## 20.grep

```sh
ps -fe|grep pktgen | grep -vE '(grep|/bin/bash)'

－v：显示不包含匹配文本的所有行。反向查找。
-E 使用扩展正则表达式

-i :  搜索时候忽略大小写

grep -i Fec /bmsccontents/ncm/mdf-up.conf 
```



## 21.top命令

```sh
top 然后按1
top - 08:50:32 up  3:16,  0 users,  load average: 16.58, 15.08, 13.53
Tasks:  46 total,   2 running,  44 sleeping,   0 stopped,   0 zombie
%Cpu0  : 28.1 us, 20.5 sy,  0.0 ni, 50.7 id,  0.0 wa,  0.0 hi,  0.7 si,  0.0 st
%Cpu1  : 27.0 us, 18.1 sy,  0.0 ni, 53.4 id,  0.7 wa,  0.0 hi,  0.7 si,  0.0 st
%Cpu2  : 15.6 us, 16.3 sy,  0.0 ni, 67.4 id,  0.0 wa,  0.0 hi,  0.7 si,  0.0 st
%Cpu3  : 23.9 us, 26.3 sy,  0.0 ni, 48.8 id,  0.3 wa,  0.0 hi,  0.7 si,  0.0 st
%Cpu4  : 20.0 us, 13.2 sy,  0.0 ni, 66.1 id,  0.4 wa,  0.0 hi,  0.4 si,  0.0 st
%Cpu5  : 24.8 us, 16.9 sy,  0.0 ni, 57.3 id,  0.6 wa,  0.0 hi,  0.3 si,  0.0 st
```

## 22.查看内存

```sh
free -h
# free -h
              total        used        free      shared  buff/cache   available
Mem:           9.7G        5.6G        310M        265M        3.8G        3.3G
Swap:          1.0G        7.4M        1.0G

```

## 23.查看cpu，numa个数等

```sh
lscpu
```

## 24.修改时间和时区

```sh
#设置时区
timedatectl set-timezone Asia/Shanghai
#修改时间
timedatectl set-time 2015-11-20
timedatectl set-time '16:10:40 2015-11-20'
#将日期设置为2017年11月3日
[root@linux-node ~]# date -s 11/03/17       或者 date -s '2015-01-14 12:00:00'  
#将时间设置为14点20分50秒
[root@linux-node ~]# date -s 14:20:50
#将时间设置为2017年11月3日14点16分30秒（MMDDhhmmYYYY.ss）
[root@linux-node ~]# date 1103141617.30

date +%s      转换当前时间为毫秒
date -d "2019-04-24 10:04:00" +%s      转换指定时间为毫秒
date -d "@1565943129910"       将毫秒时间转换为时间
网站转换 https://tool.chinaz.com/Tools/unixtime.aspx

#显示硬件时间：
hwclock --show
#设置硬件时间：
hwclock --set --date ‘08/02/2012 12:00:00’
#将硬件时间同步到系统时间：
hwclock --hctosys
#将系统时间同步到硬件时间
hwclock --systohc
#强制把系统时间写入CMOS：
clock -w

systemctl stop chronyd
systemctl disable chronyd
卸载chrony yum erase chrony
```

## 25.查看有没有实际卡

```sh
[root@compute2 ~]# lspci | grep Eth

```

## 26.tee命令

```sh
#即将输出打到屏幕上又会将输出写入文件内
tee [Options]…… [File]……
-a,--append:不覆盖，而是追加输出到指定的文件中
-i,--ignore-interrupts:忽略中断信息
--help:显示帮助信息并退出
--version:显示版本信息并退出

echo 'asdff' | tee -a Example.txt 
```

## 27.yum

```sh
yum list installed 列出所有已经安装的软件包
yum list updates 列出需要更新的软件包
yum update packageName 更新此软件包
yum update 更新所有列在yum list updates更新列表中的软件包
#卸载软件
yum remove packageName
yum erase packageName
其中remove是只卸载软件，保留配置文件和数据文件，erase是卸载软件并删除其相关的文件
#查找有什么软件包能提供给定的值   例子：比如查询什么软件包提供 dig 命令
[root@linux-node1 ~]# yum provides dig
32:bind-utils-9.9.4-29.el7_2.3.x86_64 : Utilities for querying DNS name servers

yum makecache  使之生效
yum search file 查找
yum info file
```

## 28.手动清alarm

```sh
fmsendmessage -c <modlue> <code> <resourceid>
```

## 29.批量删除进程

```sh
kill -9 `ps -ef |grep xxx|awk '{print $2}' ` 
包含xxx的行，输出pid的列
例如：kill -9 `ps -ef |grep java|awk '{print $2}'

ps -ef | grep xxx | grep -v root | awk '{print $2}' | xargs kill -9  
grep -v这个参数的作用是排除某个字符。所以这里排除了root执行的命令。之后也利用awk找到pid这一列。
最后的xargs是从标准输出获取参数并执行命令的程序，即从前面的命令获取输出作为参数来执行下一个命令。
```

## 30.强制修改密码

```sh
To expire the current password and force user to set a new password use the below command:

# passwd -e [username]

#查看密码过期时间
chage -l admin
其他命令用chage -l 查看
```

## 31.命令ping不通gateway

```sh
iptables -I OUTPUT -d 10.136.21.1  -j DROP

解除：
iptables -D OUTPUT -d 10.136.21.1  -j DROP
```




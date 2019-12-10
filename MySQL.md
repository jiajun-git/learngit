## MySQL

### 1.相互关系

```sh
MySQL为Oracle旗下的产品.
使用MySQL时，不同的表还可以使用不同的数据库引擎。如果你不知道应该采用哪种引擎，记住总是选择InnoDB就好了.

MariaDB
由MySQL的创始人创建的一个开源分支版本，使用XtraDB引擎。
Aurora
由Amazon改进的一个MySQL版本，专门提供给在AWS托管MySQL用户，号称5倍的性能提升。
PolarDB
由Alibaba改进的一个MySQL版本，专门提供给在阿里云托管的MySQL用户，号称6倍的性能提升。

在命令提示符下输入mysql -u root -p    密码：sjj123
如果没有启动，进入d盘mysql目录下，bin目录下  net start mysql     #启动mysql服务
再用mysql -u root -p
exit 退出
```

### 2.主键、外键

```sh
数据库主键，指的是一个列或多列的组合，其值能唯一地标识表中的每一行，通过它可强制表的实体完整性。
主键可以用来表示一个精确定位的特定的行，如果没有主键，你就无法精准定位一条记录是否就是你要的相关行记录，这样就会导致更新或删除表中特定的行很困难。
创建一张含主键的表course
CREATE TABLE course(id INT(11) PRIMARY KEY AUTO_INCREMENT,course_name VARCHAR(30));

外键表示了两个关系之间的相关联系。以另一个关系的外键作主关键字的表被称为主表，具有此外键的表被称为主表的从表。外键又称作外关键字。外键用于与另一张表的关联。是能确定另一张表记录的字段，保持数据的一致性、完整性。
ALTER TABLE students
ADD CONSTRAINT fk_class_id
FOREIGN KEY (class_id)
REFERENCES classes (id);
其中，外键约束的名称fk_class_id可以任意，FOREIGN KEY (class_id)指定了class_id作为外键，REFERENCES classes (id)指定了这个外键将关联到classes表的id列（即classes表的主键）。

要删除一个外键约束，也是通过ALTER TABLE实现的：
ALTER TABLE students
DROP FOREIGN KEY fk_class_id;

```

### 3.索引

```sh
一个索引是存储的表中一个特定列的值数据结构（最常见的是B-Tree）。索引是在表的列上创建。所以，要记住的关键点是索引包含一个表中列的值，并且这些值存储在一个数据结构中。请记住记住这一点：索引是一种数据结构.
B-Tree 是最常用的用于索引的数据结构。因为它们是时间复杂度低， 查找、删除、插入操作都可以可以在对数时间内完成。另外一个重要原因存储在B-Tree中的数据是有序的.
哈希表是另外一种你可能看到用作索引的数据结构-这些索引通常被称为哈希索引。使用哈希索引的原因是，在寻找值时哈希表效率极高.哈希表是无顺的数据结构，对于很多类型的查询语句哈希索引都无能为力。举例来说，假如你想要找出所有小于40岁的员工。你怎么使用使用哈希索引进行查询？这不可行，因为哈希表只适合查询键值对-也就是说查询相等的查询.

数据库索引同时存储了指向表中的相应行的指针。指针是指一块内存区域， 该内存区域记录的是对硬盘上记录的相应行的数据的引用。因此，索引中除了存储列的值，还存储着一个指向在行数据的索引。当这个SQL （SELECT * FROM Employee WHERE Employee_Name = ‘Jesus’ ）运行时，数据库会检查在查询的列上是否有索引。

#在Employee_Name列上创建索引的SQL如下
CREATE INDEX name_index
ON Employee (Employee_Name).

CREATE [索引类型] INDEX 索引名称
ON 表名(列名)

优点：
    1.大大加快数据的检索速度;   
    2.创建唯一性索引，保证数据库表中每一行数据的唯一性;   
    3.加速表和表之间的连接;   
    4.在使用分组和排序子句进行数据检索时，可以显著减少查询中分组和排序的时间。
缺点：
　　1.索引需要占用数据表以外的物理存储空间
　　2.创建索引和维护索引要花费一定的时间
　　3.当对表进行更新操作时，索引需要被重建，这样降低了数据的维护速度。
```

### 4.查询数据

```sh
SELECT * FROM classes;

#条件查询
SELECT * FROM students WHERE score >= 80 AND/OR gender = 'M';
SELECT * FROM students WHERE NOT class_id = 2;  不符合该条件的查询

#排序
SELECT id, name, gender, score FROM students ORDER BY score;       升序
SELECT id, name, gender, score FROM students ORDER BY score DESC;  倒序

#分页查询
第一页，每页3行记录：
SELECT id, name, gender, score FROM students ORDER BY score DESC LIMIT 3 OFFSET 0;
第二页：
SELECT id, name, gender, score FROM students ORDER BY score DESC LIMIT 3 OFFSET 3;
在MySQL中，LIMIT 15 OFFSET 30还可以简写成LIMIT 30, 15。使用LIMIT <M> OFFSET <N>分页时，随着N越来越大，查询效率也会越来越低。

#聚合查询
SELECT COUNT(*) FROM students;
SUM  计算某一列的合计值，该列必须为数值类型
AVG  计算某一列的平均值，该列必须为数值类型
MAX  计算某一列的最大值
MIN  计算某一列的最小值
SELECT AVG(score) average FROM students WHERE gender = 'M';   其中average为查询列的取别名

#分组
SELECT COUNT(*) num FROM students GROUP BY class_id;

#多表查询
SELECT * FROM students, classes;    SELECT * FROM <表1> <表2>;
SELECT
    students.id sid,
    students.name,
    students.gender,
    students.score,
    classes.id cid,
    classes.name cname
FROM students, classes;

#连接查询

```

### 5.其他实用语句

```sh
SHOW DATABASES;

#创建、删除数据库
CREATE DATABASE test;
DROP DATABASE test;

#对一个数据库进行操作时，要首先将其切换为当前数据库：
USE test;

#列出表
SHOW TABLES;

#查看表结构，列出建表语句
DESC students;
SHOW CREATE TABLE students;

#创建、删除表
 CREATE TABLE `students` (
        `id` bigint(20) NOT NULL AUTO_INCREMENT,
        `class_id` bigint(20) NOT NULL,   
        `name` varchar(100) NOT NULL,        
        `gender` varchar(1) NOT NULL,              
        `score` int(11) NOT NULL,               
         PRIMARY KEY (`id`)           
     ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 

DROP TABLE students;

#如果要给students表新增一列birth
ALTER TABLE students ADD COLUMN birth VARCHAR(10) NOT NULL;
#要修改birth列，例如把列名改为birthday，类型改为VARCHAR(20)
ALTER TABLE students CHANGE COLUMN birth birthday VARCHAR(20) NOT NULL;
#删除列
ALTER TABLE students DROP COLUMN birthday;

#希望插入一条新记录（INSERT），但如果记录已经存在，就先删除原记录，再插入新记录
REPLACE INTO students (id, class_id, name, gender, score) VALUES (1, 1, '小明', 'F', 99);
#希望插入一条新记录（INSERT），但如果记录已经存在，就更新该记录
INSERT INTO students (id, class_id, name, gender, score) VALUES (1, 1, '小明', 'F', 99) ON DUPLICATE KEY UPDATE name='小明', gender='F', score=99;
若id=1的记录不存在，INSERT语句将插入新记录，否则，当前id=1的记录将被更新，更新的字段由UPDATE指定

#若id=1的记录不存在，INSERT语句将插入新记录，否则，不执行任何操作
INSERT IGNORE INTO students (id, class_id, name, gender, score) VALUES (1, 1, '小明', 'F', 99);

#快照
对class_id=1的记录进行快照，并存储为新表students_of_class1:
CREATE TABLE students_of_class1 SELECT * FROM students WHERE class_id=1;

```

### 6.事务

```sh
这种把多条语句作为一个整体进行操作的功能，被称为数据库事务。数据库事务可以确保该事务范围内的所有操作都可以全部成功或者全部失败。如果事务失败，那么效果就和没有执行这些SQL一样，不会对数据库数据有任何改动。

A：Atomic，原子性，将所有SQL作为原子工作单元执行，要么全部执行，要么全部不执行；
C：Consistent，一致性，事务完成后，所有数据的状态都是一致的，即A账户只要减去了100，B账户则必定加上了100；
I：Isolation，隔离性，如果有多个事务并发执行，每个事务作出的修改必须与其他事务隔离；
D：Duration，持久性，即事务完成后，对数据库数据的修改被持久化存储。

BEGIN;
UPDATE accounts SET balance = balance - 100 WHERE id = 1;
UPDATE accounts SET balance = balance + 100 WHERE id = 2;
COMMIT;
COMMIT是指提交事务，即试图把事务内的所有SQL所做的修改永久保存

希望主动让事务失败，这时，可以用ROLLBACK回滚事务，整个事务会失败
BEGIN;
UPDATE accounts SET balance = balance - 100 WHERE id = 1;
UPDATE accounts SET balance = balance + 100 WHERE id = 2;
ROLLBACK;

#隔离级别
Isolation Level 脏读（Dirty Read）不可重复读（Non Repeatable Read） 幻读（Phantom Read）
Read Uncommitted   Yes                Yes                       Yes
Read Committed      -                 Yes                       Yes
Repeatable Read     -                  -                        Yes
Serializable        -                  -                         -

Read Uncommitted是隔离级别最低的一种事务级别。在这种隔离级别下，一个事务会读到另一个事务更新后但未提交的数据，如果另一个事务回滚，那么当前事务读到的数据就是脏数据，这就是脏读（Dirty Read）。
Read Committed隔离级别下，不可重复读是指，在一个事务内，多次读同一数据，在这个事务还没有结束时，如果另一个事务恰好修改了这个数据，那么，在第一个事务中，两次读取的数据就可能不一致。
Repeatable Read隔离级别下，幻读（Phantom Read）是指，在一个事务中，第一次查询某条记录，发现没有，但是，当试图更新这条不存在的记录时，竟然能成功，并且，再次读取同一条记录，它就神奇地出现了。
Serializable是最严格的隔离级别。在Serializable隔离级别下，所有事务按照次序依次执行，因此，脏读、不可重复读、幻读都不会出现。虽然Serializable隔离级别下的事务具有最高的安全性，但是，由于事务是串行执行，所以效率会大大下降，应用程序的性能会急剧降低。如果没有特别重要的情景，一般都不会使用Serializable隔离级别。

#默认级别
如果没有指定隔离级别，数据库就会使用默认的隔离级别。在MySQL中，如果使用InnoDB，默认的隔离级别是Repeatable Read。

#指定级别的语句
SET TRANSACTION ISOLATION LEVEL READ COMMITTED;
BEGIN;
UPDATE students SET name = 'Bob' WHERE id = 1;
COMMIT;
```


## git

### 2.创建版本库

```sh
$ mkdir learngit
$ cd learngit
$ git init
Initialized empty Git repository in /Users/michael/learngit/.git/

vi readme.txt
Git is a version control system.
Git is free software.

$ git config --global user.email "sjiaj1995@163.com"
$  git config --global user.name "jiajun-git"

$ git add readme.txt      用命令git add告诉Git，把文件添加到仓库
$ git commit -m "wrote a readme file"   -m后面输入的是本次提交的说明，可以输入任意内容
#git commit命令执行成功后会告诉你，1 file changed：1个文件被改动（我们新添加的readme.txt文件）；2 insertions：插入了两行内容（readme.txt有两行内容）。
[master (root-commit) eaadf4e] wrote a readme file
 1 file changed, 2 insertions(+)
 create mode 100644 readme.txt
 
 $ git status
On branch master
nothing to commit, working tree clean
git status命令可以让我们时刻掌握仓库当前的状态
```

### 3.版本回退命令

```sh
$ git log
commit 095b6e227c1fa4ec2d3db62b60b750ef2fe7bb14 (HEAD -> master)
Author: jiajun-git <sjiaj1995@163.com>
Date:   Sun Sep 8 18:43:45 2019 +0800

#准备把readme.txt回退到上一个版本，也就是add distributed的那个版本
#首先，Git必须知道当前版本是哪个版本，在Git中，用HEAD表示当前版本，也就是最新的提交1094adb...，上一个版本就是HEAD^，上上一个版本就是HEAD^^，当然往上100个版本写100个^比较容易数不过来，所以写成HEAD~100。
$ git reset --hard HEAD^
HEAD is now at e475afc add distributed

#想再回去怎么办？办法其实还是有的，只要上面的命令行窗口还没有被关掉，你就可以顺着往上找啊找啊，找到那个append GPL的commit id是1094adb...，于是就可以指定回到未来的某个版本，版本号没必要写全，前几位就可以了，Git会自动去找。当然也不能只写前一两位，因为Git可能会找到多个版本号，就无法确定是哪一个了。
$ git reset --hard  38a3775e
HEAD is now at 38a3775 distributed
#如果关掉了命令行窗口，也有办法找到版本号，Git提供了一个命令git reflog用来记录你的每一次命令：
$ git reflog
38a3775 (HEAD -> master) HEAD@{0}: reset: moving to 38a3775e
095b6e2 HEAD@{1}: reset: moving to HEAD^
38a3775 (HEAD -> master) HEAD@{2}: commit: distributed
095b6e2 HEAD@{3}: commit (initial): wrote a readme file

Git在内部有个指向当前版本的HEAD指针，当你回退版本的时候，Git仅仅是把HEAD从指向append GPL：
┌────┐
│HEAD│
└────┘
   │
   └──> ○ append GPL
        │
        ○ add distributed
        │
        ○ wrote a readme file
改为指向add distributed：
┌────┐
│HEAD│
└────┘
   │
   │    ○ append GPL
   │    │
   └──> ○ add distributed
        │
        ○ wrote a readme file
然后顺便把工作区的文件更新了。所以你让HEAD指向哪个版本号，你就把当前版本定位在哪。
```



### 4.工作区和暂存区

```sh
工作区（Working Directory）
就是你在电脑里能看到的目录，比如我的learngit文件夹就是一个工作区：

版本库（Repository）
工作区有一个隐藏目录.git，这个不算工作区，而是Git的版本库。
Git的版本库里存了很多东西，其中最重要的就是称为stage（或者叫index）的暂存区，还有Git为我们自动创建的第一个分支master，以及指向master的一个指针叫HEAD。
```

![git-repo](https://www.liaoxuefeng.com/files/attachments/919020037470528/0)

```sh
第一步是用git add把文件添加进去，实际上就是把文件修改添加到暂存区；
第二步是用git commit提交更改，实际上就是把暂存区的所有内容提交到当前分支。
因为我们创建Git版本库时，Git自动为我们创建了唯一一个master分支，所以，现在，git commit就是往master分支上提交更改。
#如果要提交到版本库一定要先git add ,在git commit ,只有提交到暂存区才能到版本库
```

```sh
#撤销修改：
$ git checkout -- readme.txt
让这个文件回到最近一次git commit或git add时的状态。
把readme.txt文件在工作区的修改全部撤销，这里有两种情况：
一种是readme.txt自修改后还没有被放到暂存区，现在，撤销修改就回到和版本库一模一样的状态；
一种是readme.txt已经添加到暂存区后，又作了修改，现在，撤销修改就回到添加到暂存区后的状态。
#即，此命令只能撤销git add 或者git commit之前的工作区的内容

#如果git add后没有git commit ,想撤销git add，还可以用reset命令
$ git reset HEAD readme.txt
所以git reset命令既可以回退版本，也可以把暂存区的修改回退到工作区。
```

```sh
#删除文件
用命令git rm删掉，并且git commit：
$ git rm test.txt
rm 'test.txt'
$ git commit -m "remove test.txt"
[master d46f35e] remove test.txt
 1 file changed, 1 deletion(-)
 delete mode 100644 test.txt
```

 

### 5.远程仓库

```sh
第1步：创建SSH Key。在用户主目录下，看看有没有.ssh目录，如果有，再看看这个目录下有没有id_rsa和id_rsa.pub这两个文件，如果已经有了，可直接跳到下一步。如果没有，打开Shell（Windows下打开Git Bash），创建SSH Key：
$ ssh-keygen -t rsa -C "sjiaj1995@163.com"
Generating public/private rsa key pair.
Enter file in which to save the key (/c/Users/84614/.ssh/id_rsa):
Created directory '/c/Users/84614/.ssh'.
Enter passphrase (empty for no passphrase):
Enter same passphrase again:
Your identification has been saved in /c/Users/84614/.ssh/id_rsa.
Your public key has been saved in /c/Users/84614/.ssh/id_rsa.pub.
The key fingerprint is:
SHA256:sq/xi7PY9uImrvTvX9D9buaI1JVU+rWrg9jIi4tZ02o sjiaj1995@163.com
The key's randomart image is:
+---[RSA 2048]----+
|                .|
|               o |
|              o .|
|         . . . oo|
|      . S . . o..|
|       o.. . o  .|
|  .   oo..* o .. |
| . ..oOE+* + ++  |
|  .o+XX@Boo .=+  |
+----[SHA256]-----+

第2步：登陆GitHub，打开“Account settings”，“SSH Keys”页面：
然后，点“Add SSH Key”，填上任意Title，在Key文本框里粘贴id_rsa.pub文件的内容：
```


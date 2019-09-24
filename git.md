## git

### 1.基础知识和安装

```sh
git由c语言编写，分布式版本控制系统，开源
分布式版本控制系统根本没有“中央服务器”，每个人的电脑上都是一个完整的版本库，这样，你工作的时候，就不需要联网了，因为版本库就在你自己的电脑上
```

```sh
#安装
https://git-scm.com/downloads
安装完成后，还需要最后一步设置，在命令行输入：
$ git config --global user.name "Your Name"
$ git config --global user.email "email@example.com"
因为Git是分布式版本控制系统，所以，每个机器都必须自报家门：你的名字和Email地址。
注意`git config`命令的`--global`参数，用了这个参数，表示你这台机器上所有的Git仓库都会使用这个配置，当然也可以对某个仓库指定不同的用户名和Email地址。
对某个仓库指定用户名和Email：
$ git config user.name "Your Name"
$ git config user.email "email@example.com"
还可用$ git config user.name、$ git config user.email查看你的用户名和邮箱
```

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

```sh
#添加远程仓库
在github上创建learngit仓库
在本地的learngit仓库下运行命令：
$ git remote add origin git@github.com:jiajun-git/learngit.git
远程库的名字就是origin，这是Git默认的叫法，也可以改成别的，但是origin这个名字一看就知道是远程库。
$ git push -u origin master
The authenticity of host 'github.com (13.250.177.223)' can't be established.
RSA key fingerprint is SHA256:nThbg6kXUpJWGl7E1IGOCspRomTxdCARLviKw6E5SY8.
Are you sure you want to continue connecting (yes/no)? yes
Warning: Permanently added 'github.com,13.250.177.223' (RSA) to the list of known hosts.
Enumerating objects: 9, done.
Counting objects: 100% (9/9), done.
Delta compression using up to 8 threads
Compressing objects: 100% (6/6), done.
Writing objects: 100% (9/9), 712 bytes | 89.00 KiB/s, done.
Total 9 (delta 2), reused 0 (delta 0)
remote: Resolving deltas: 100% (2/2), done.
To github.com:jiajun-git/learngit.git
 * [new branch]      master -> master
Branch 'master' set up to track remote branch 'master' from 'origin'.

之后提交不用再加-u参数：
git push origin master
```

```sh
#从远程库克隆并且上传新的代码
在指定目录下git bash
git clone git@github.com:jiajun-git/gitskills.git
或者点击github上的clone按钮，复制https地址
git clone https://github.com/jiajun-git/gitskills.git

克隆下来之后如果需要更改用户名和邮箱见第二节
然后可进行修改、git add、git commint、git push origin master

Git支持多种协议，默认的git://(git@github.com:jiajun-git/gitskills.git)使用ssh，但也可以使用https等其他协议。使用https除了速度慢以外，还有个最大的麻烦是每次推送都必须输入口令,原生git协议速度最快
```

### 6.分支

```sh
#创建与合并分支
首先，我们创建dev分支，然后切换到dev分支：
$ git checkout -b dev
Switched to a new branch 'dev'

git checkout命令加上-b参数表示创建并切换，相当于以下两条命令：
$ git branch dev
$ git checkout dev
Switched to branch 'dev'

然后，用git branch命令查看当前分支：
$ git branch
* dev
  master
git branch命令会列出所有分支，当前分支前面会标一个*号。
然后，我们就可以在dev分支上正常提交，比如对readme.txt做个修改，加上一行：
Creating a new branch is quick.
然后提交：
$ git add readme.txt 
$ git commit -m "branch test"
[dev b17d20e] branch test
 1 file changed, 1 insertion(+)
现在，dev分支的工作完成，我们就可以切换回master分支：
$ git checkout master
Switched to branch 'master'
```

![git-br-on-master](https://www.liaoxuefeng.com/files/attachments/919022533080576/0)

```sh
我们把dev分支的工作成果合并到master分支上：
$ git merge dev
Updating d46f35e..b17d20e
Fast-forward
 readme.txt | 1 +
 1 file changed, 1 insertion(+)
#git merge命令用于合并指定分支到当前分支
因为创建、合并和删除分支非常快，所以Git鼓励你使用分支完成某个任务，合并后再删掉分支，这和直接在master分支上工作效果是一样的，但过程更安全。
#删除分支
$ git branch -d dev
Deleted branch dev (was b17d20e).

切换分支使用git checkout <branch>，而前面讲过的撤销修改则是git checkout -- <file>，同一个命令，有两种作用，确实有点令人迷惑。
实际上，切换分支这个动作，用switch更科学。因此，最新版本的Git提供了新的git switch命令来切换分支：
创建并切换到新的dev分支，可以使用：
$ git switch -c dev
直接切换到已有的master分支，可以使用：
$ git switch master
```

![git-br-ff-merge](https://www.liaoxuefeng.com/files/attachments/919022412005504/0)

```sh
#合并分支时，Git会用Fast forward模式，但这种模式下，删除分支后，会丢掉分支信息。如果要强制禁用Fast forward模式，Git就会在merge时生成一个新的commit，这样，从分支历史上就可以看出分支信息。下面我们实战一下--no-ff方式的git merge：
$ git merge --no-ff -m "merge with no-ff" dev
Merge made by the 'recursive' strategy.
 readme.txt | 1 +
 1 file changed, 1 insertion(+)
因为本次合并要创建一个新的commit，所以加上-m参数，把commit描述写进去。
#git merge和git merge --no-ff的区别
$ git log --graph   （git merge）
* commit 6266234a8faec97e6b72541c5bedc931c4aa0aad (HEAD -> master, dev)
| Author: jiajun-git <sjiaj1995@163.com>
| Date:   Tue Sep 24 21:20:18 2019 +0800
|
|     no off branch dev
|
*   commit b0189f2a3c8d094fcb7f323d94ae69c0fbdc9c0b

$ git log --graph    （git merge --no-ff）
*   commit 057ece5feed9292797c69ff63de1a4b71956984b (HEAD -> master)
|\  Merge: 6266234 d5f493d
| | Author: jiajun-git <sjiaj1995@163.com>
| | Date:   Tue Sep 24 21:24:08 2019 +0800
| |
| |     merge with no-ff
| |
| * commit d5f493d4eb391a4fcf337e6a463bba7fd3040d6b (dev)
|/  Author: jiajun-git <sjiaj1995@163.com>
|   Date:   Tue Sep 24 21:23:40 2019 +0800
|
|       no off branch
|
* commit 6266234a8faec97e6b72541c5bedc931c4aa0aad
| Author: jiajun-git <sjiaj1995@163.com>
| Date:   Tue Sep 24 21:20:18 2019 +0800
|
|     no off branch dev

#此模式下图如下：
```

![1569331114923](C:\Users\84614\AppData\Roaming\Typora\typora-user-images\1569331114923.png)



```sh
#解决冲突
$ git checkout master
Switched to branch 'master'
Your branch is ahead of 'origin/master' by 1 commit.
  (use "git push" to publish your local commits)

$ vi readme.txt

$ git add readme.txt

$ git commint -m "branch master"
git: 'commint' is not a git command. See 'git --help'.

The most similar command is
        commit

$ git commit -m "branch master"
[master 6a4ce52] branch master
 1 file changed, 1 insertion(+)

$ git merge dev
Auto-merging readme.txt
CONFLICT (content): Merge conflict in readme.txt
Automatic merge failed; fix conflicts and then commit the result.

vi readme.txt
Git is a version control system.
Git is free software.
Git is a distributed version control system.
git commit
git new
Creating a new branch is quick.
<<<<<<< HEAD
git branch master.
=======
git branch dev.
>>>>>>> dev
~

#手动修改冲突的内容，再提交
$ git add readme.txt
$ git commit -m "fix conflict"

#git log --graph命令可以看到分支合并图。
$ git log --graph --pretty=oneline --abbrev-commit
*   b0189f2 (HEAD -> master) fix conflict
|\
| * ea7b684 (dev) branch dev
* | 6a4ce52 branch master
|/
* b6fef88 new branch
* 075f551 (origin/master) five step
* b1055ae first step
* 525faf7 new commit
* 21e43e1 git knowledge01
* 234e97e git knowledge
* fce4e8f commit
* 38a3775 distributed
* 095b6e2 wrote a readme file![git-br-conflict-merged](https://www.liaoxuefeng.com/files/attachments/919023031831104/0)
```

![1569330325537](C:\Users\84614\AppData\Roaming\Typora\typora-user-images\1569330325537.png)

```sh


```




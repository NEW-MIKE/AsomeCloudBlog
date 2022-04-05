### 一，建立维护与仓库的连接：

#### 1，多个账户下，建立仓库对自己的本地的信任以及自动关联相关commit账户：

以下生成两个账户用于不同的仓库下面，在这个过程中会提示你输入用户名:
（1）`ssh-keygen -t rsa -C “youremail@gmail.com”`生成了一对秘钥`id_rsa`和`id_rsa.pub` ， 保存在了`~/.ssh`文件夹内。

（2）然后将名字进行更改：work_rsa 和work_rsa.pub 然后再通过另外一个邮箱生成新的密钥：
（3）然后，你需要再用另一个邮箱生成一对 private 的秘钥`ssh-keygen -t rsa -C “private_email@gmail.com”`。这时候要注意重命名，否则会覆盖上面的密钥文件。假设我们生成了一对新的秘钥`private` 和`private.pub`。
（4）将相应的密钥配置到对应的账户下面去。
（5）修改配置文件：编辑`~/.ssh/config`文件。如果该文件不存在的话，直接创建一个就好。里面的内容如下：

```text
# 公共
Host github_public
Hostname ssh.github.com
IdentityFile ~/.ssh/id_rsa
port 22

#个人
Host github_private
Hostname ssh.github.com
IdentityFile ~/.ssh/private
port 22

# 公共
Host github_mike
UpdateHostKeys yes
Hostname ssh.github.com
IdentityFile ~/.ssh/new_mike
port 22

#个人
Host github_isa
Hostname ssh.github.com
IdentityFile ~/.ssh/isa
port 22

Host gitee.com
HostName gitee.com
PreferredAuthentications publickey
IdentityFile ~/.ssh/gitee_rsa

Host gitlab.com
HostName gitlab.com
PreferredAuthentications publickey
IdentityFile ~/.ssh/gitlab_rsa
```

网上大部分教程的配置文件中`Hostname`都是`github.com`，配置成那样是不能正常SSH访问的。但是是否可以通过HTTPS来进行访问呢。
（6）git bash 之后，进行测试：

```console
ssh -T git@github_public
ssh -T git@github_private
ssh  -vvv -T git@github_mike //可以打印出所有的问题点
```

`@`后面的主机名为上面配置文件中填写的`Host`选项，如果都能正常返回如下信息，就说明配置正常。

```text
Hi xxx! You've successfully authenticated, but GitHub does not provide shell access.
```

同样的方式你就可以配置更多的以SSH登录的不同git用户。
（7）配置本地仓库属性（PS：在这里应该是通过email完成对于用户名的映射关系，待验证）：首先要删除GIT配置的全局用户名和邮件地址，接着在每个项目仓库中按照需求设置不同的`user.name`和 `user.email`：

```text
# 删除全局设置
git config --global --unset user.name
git config --global --unset user.email
查看用户名：git config user.name查看密码：git config user.password查看邮
# 添加本地设置
# 设置为私有仓库的GitHub账号邮箱和公有账号的GitHub邮箱。配置了这些之后，我们就能够成功的从远程拉取仓库了，拉取之后，cd到仓库目录下，配置该仓库使用的用户名和邮箱：就是说，跑到仓库下面去进行设置就可以了。

git config --local user.name xxxx
git config --local user.email xxx
```

（8）在实际的仓库中，设置建立远程连接：之后在添加远程仓库的时候，把`github.com` 修改成`github_public` 或者`github_private`就好了（即上述文件中的HOST选项），用上面两个 Host 名称来代替原来的`github.com` ，（**这一步很重要**）如：

```text
git remote add origin git@github_public:xxx/example.git # public user
git remote add origin git@github_private:xxx/example.git # priavate user
ps：设置的时候，也是要如此来进行，如果更新了自己的密钥，这里需要重新设置一下，也就是更新
git remote set-url origin git@github_mike:NEW-MIKE/Blog2Me.git
```

其实，上面的配置文件的意思就是，按照你设置远程仓库时使用不同的 Host 名称，查找`~/.ssh/config`文件，使用不同的 SSH 文件连接到 GitHub。这样你就将是以不同的身份访问 GitHub 仓库。

### 二，从公有的库里面拉取新的库，开辟新的分支，并且提交自己的分支，删除自己的分支；

查看当前分支名称：

git branch | grep "*"

首先克隆一份原仓库到本地进行操作：

```bash
git clone xxxxxxx.git
```

创建并切换到`main`：

```bash
git checkout -b main
```

推送`main`：

```bash
git push origin main
```

然后自己就可以在自己的分支下面进行工作了

删除本地`master`：

```bash
git branch -d master
```

删除远程`master`：

```bash
git push origin :master
```

在自己分支上工作了，在需要把别的分支新的内容同步到自己的分支的时候：
多重分支的合并，如果两个分支都是我自己控制的，一般我们这样做的目的是为了确保在向远程分支推送时能保持提交历史的整洁——例如向某个其他人维护的项目贡献代码时。 在这种情况下，你首先在自己的分支里进行开发，当开发完成时你需要先将你的代码变基到 `origin/master` 上，然后再向主项目提交修改。 这样的话，该项目的维护者就不再需要进行整合工作，只需要快进合并便可。
切换或者创建我自己的分支：git checkout experiment
将我开发好了的提交多次的内容，且经过了测试的内容，变基到主分支：git rebase master
如果我是项目的主要控制人，在收到了别人变基的操作申请之后，进行快速合并操作：

```console
git checkout master
$ git merge experiment//此处的意义在于合并其他的分支到当前分支。
```

在进行合并之前，codereview git diff --name-status <commit> <commit>

合码冲突的地方，可以进行选择性的选择代码的存在，一个一个的选择。最终合入代码

推送的时候是 git push -u origin xxxx    git push -u origin master 如果当前分支与多个主机存在追踪关系，则可以使用 -u 参数指定一个默认主机，这样后面就可以不加任何参数使用git push，

然后到仓库去可以看到从远程库的master分支向上游仓库的master分支申请提交代码
Able to merge代表你的代码与上游代码没有冲突，可以提交
然后点击 create pull request，进入下面页面：等待拥有者审批

### 三，自己对于开发状态的管理：

1，管理我的commit：

git log  查看提交历史git log --oneline 以精简模式显示查看提交历史git log -p  查看指定文件的提交历史git blame  一列表方式查看指定文件的提交历史

回滚之后查看最近几次的提交记录：git reflog 结合git reset --hard commit_id 就可以在任意的版本之间进行任意的跳动了。

然后如果想要利用不同的回滚的方式来进行回滚：
git reset –-soft 目标版本号 /以及想要回退的文件      可以把版本库上的提交回退到暂存区，修改记录保留
git reset –-mixed 目标版本号 可以把版本库上的提交回退到工作区，修改记录保留，那么在这里，我就可以直接回滚了。
git reset –-hard  可以把版本库上的提交彻底回退，修改的记录全部revert。

有时候我们提交完了才发现漏掉了几个文件没有添加，或者提交信息写错了。 此时，可以运行带有 `--amend` 选项的提交命令来重新提交：

```console
$ git commit --amend
```

2，暂存区管理add管理：

git status  查看当前工作区暂存区变动，git status -s  查看当前工作区暂存区变动，概要信息git status  --show-stash 查询工作区中是否有stash(暂存的文件)

对于当次，我不想要暂存的文件：git reset HEAD <file>...，这样子，就可以从我的暂存区将某个文件移交出来。

3，管理我的工作区

如果我想要撤销我的某个文件的修改：git checkout file 

git stash  把当前的工作隐藏起来 等以后恢复现场后继续工作git stash list 显示保存的工作进度列表git stash pop stash@{num} 恢复工作进度到工作区git stash show ：显示做了哪些改动git stash drop stash@{num} ：删除一条保存的工作进度git stash clear 删除所有缓存的stash。

### 综述：

维持git 的更新和追踪github的政策的变化

git add . 会把本地所有untrack的文件都加入暂存区，并且会根据.[gitignore](https://so.csdn.net/so/search?q=gitignore&spm=1001.2101.3001.7020)做过滤，但是git add * 会忽略.gitignore把任何文件都加入

绿色代理github：把以下的所有代理地址记录下来：

1. 打开https://github.com.ipaddress.com/  如下图：
   2.打开https://fastly.net.ipaddress.com/github.global.ssl.fastly.net#ipinfo 如下图：
   3.打开https://github.com.ipaddress.com/assets-cdn.github.com 如下图：
   4.打开电脑的hosts文件，把下列的东东写在最后，然后保存即可

2. 140.82.113.4(图1的IP Address) github.com  
   199.232.69.194(图2的IP Address) github.global.ssl.fastly.net 
   185.199.108.153(图3的IP Address)  assets-cdn.github.com 
   185.199.109.153(图3的IP Address)  assets-cdn.github.com 
   185.199.110.153(图3的IP Address)  assets-cdn.github.com 
   185.199.111.153(图3的IP Address)  assets-cdn.github.com

   目标位置就是我们的hosts文件的位置地址了，一般都是C:\Windows\System32\drivers\etc\HOSTS
   在终端在输以下指令刷新DNS（需要权限）在命令行窗口输入"ipconfig /flushdns"执行，刷新本地的DNS缓存数据

   ```
   sudo killall -HUP mDNSResponder;say DNS cache has been flushed
   ```

至此，git自由了。还有其他的算法自由，操作系统自由等等需要去实现，以及股票自由。


如何将git log导出来来呢：git log --reverse --pretty=oneline > log.txt
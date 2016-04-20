## GitHub 更新自己Fork的代码

> *By 刘杰辉 ( Jeffrey Lau ) [ [GitHub](https://github.com/jeffreylau7) ] [ [CSDN](http://blog.csdn.net/jeffreylau7) ]*

这标题真有点抽象，我这来解析一下具体的应用场景吧。

一天在github上看到一个项目A很不错，就fork了项目A到自己的仓库里面，这个项目在自己仓库命名为项目a，但过了几天，看到项目A有了更新，加了一些新功能，但自己仓库中的项目a却没有跟着项目A来更新，这下怎么办？如何才能把项目a同步项目A的新更新？

### 定义具体的情景

用户A 在github上有一个项目叫项目A，用户B Fork了项目A在自己的仓库叫项目A，项目A有更新，添加了新功能，用户B想自己项目A也跟着用户A项目A也更新一下。

##### 1. 用户B克隆项目项目A到本地

    git clone https://github.com/userB/projectA.git
    
    cd projectA


##### 2. 添加项目A的远程分支到本地

    git remote add projectA https://github.com/userA/projectA.git

    git remote -v
    origin https://github.com/userB/projectA.git (fetch)
    origin https://github.com/userB/projectA.git (push)
    projectA_userA https://github.com/userA/projectA.git (fetch)
    projectA_userA https://github.com/userA/projectA.git (push)

##### 3. fetch projectA 到本地

    git fetch projectA_userA

##### 4. 合并两个版本

    git merge projectA_userA/master

##### 5. 把本地合并的代码提交到github上

    git push origin master


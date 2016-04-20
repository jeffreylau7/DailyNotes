## RecyclerView 的什么？

> *By 刘杰辉 ( Jeffrey Lau ) [ [GitHub](https://github.com/jeffreylau7) ] [ [CSDN](http://blog.csdn.net/jeffreylau7) ]*

RecyclerView 是ListView、GridView的升级融合版，提供更好的通用性和更多的自由度，在日常开发中扮演着相当重要的角色，在展示文字图片列表时可以使用RecyclerView 来实现，所以需要重点学习。

## 概述

RecyclerView 在 android.support.v7 包中，下面来看看Android提供哪些支持包。

- Android Support v4:  这个包是为了照顾1.6及更高版本而设计的，这个包是使用最广泛的，eclipse新建工程时，都默认带有了。
- Android Support v7:  这个包是为了考虑照顾2.1及以上版本而设计的，但不包含更低，故如果不考虑1.6,我们可以采用再加上这个包，另外注意，v7是要依赖v4这个包的，即，两个得同时被包含。
- Android Support v13:这个包的设计是为了android 3.2及更高版本的，一般我们都不常用，平板开发中能用到。



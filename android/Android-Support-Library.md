*Jeffrey Lau*

## Android Support Library 译文

> 原文地址：http://developer.android.com/tools/support-library/index.html
> 
> Android Support Library
> 
> Android 支持库

Android Support Library包是一个代码库的集合，这个集合提供Android framework API的向下兼容版本，并提供原本只通过库（译者注：这个库指新版本的库）的API才能使用的功能。每一个Support Library是针对一个特定的Android API级别向下兼容的。这种设计意味着你的应用可以使用库（译者注：这个库指新版本的库）的功能，并且可以和运行Android1.6（API level 4）或以上版本的设备兼容。

这个指南提供了一些信息，这些信息包括利用Support Library可以使用哪些功能，如何在你的开发环境中使用这些功能，以及库的发布管理。

## 概述 ##

对于应用开发者来说，在Android项目中引入Support Library是一种最佳做法，这依赖于你的应用设置的目标平台版本的范围和它用到的API。利用库提供的功能可以帮助你提升应用的外观，提升系统性能并扩大应用的用户使用范围。如果你使用了Android code template（代码模板）工具，你会注意到所有的应用模板默认包含了一个或多个Support Library。

每一个Support Library设定了一个目标的基础Android API级别，并且提供了不通的功能集合。为了高效地使用这些库，需要着重考虑一下你想使用哪些功能，并且理解每一个库在不同的Android API级别都支持哪些功能。开始前，回顾一下Support Library Features指南。然后，进入Support Library Setup主题学习一下如何将Support Library合并到你的应用。想要了解更多关于Support Library API的细节，查看API参考中的android.support包。

> 原文地址：http://developer.android.com/tools/support-library/features.html

在Android Support Library包中，包含了几个可以被引入到你应用的库。每一个库支持一个特定的Android平台版本范围和功能集合。

这个指南解释了Support Library提供的重要功能和版本，这将帮助你决定你的应用选择哪一个功能和版本。总的来说，我们推荐引入v4 support和 v7 appcompat的库，因为它们支持的Android版本很广泛，并为推荐的用户界面样式提供API。

想要使用任何下面的库，你必须将库文件下载到你安装的Android SDK中。在Support Library Setup中根据指引下载Support Library，来完成这一步骤。你必须采取额外的措施来引入一个特殊的Support Library到你的应用中。查看下面每一个库章节的末尾来获取重要信息，这些信息阐述了如何将库引入你的应用中。

## v4 Support Library ##

这个库是为Android 1.6（API level 4）和更高版本设计的。相对于其他库，他包含的API集合最大，包括对应用组件、用户界面功能、辅助功能、数据处理、网络连接和编程工具的支持。这里有一些包含在v4库中的关键类：（此处偷个懒，省去关键类的翻译500字^_^）

App Components
Fragment - Adds support encapsulation of user interface and functionality with Fragments, enabling applications provide layouts that adjust between small and large-screen devices.
NotificationCompat - Adds support for rich notification features.
LocalBroadcastManager - Allows applications to easily register for and receive intents within a single application without broadcasting them globally.
User Interface
ViewPager - Adds a ViewGroup that manages the layout for the child views, which the user can swipe between.
PagerTitleStrip - Adds a non-interactive title strip, that can be added as a child of ViewPager.
PagerTabStrip - Adds a navigation widget for switching between paged views, that can also be used with ViewPager.
DrawerLayout - Adds support for creating a Navigation Drawer that can be pulled in from the edge of a window.
SlidingPaneLayout - Adds widget for creating linked summary and detail views that appropriately adapt to various screen sizes.
Accessibility
ExploreByTouchHelper - Adds a helper class for implementing accessibility support for custom views.
AccessibilityEventCompat - Adds support for AccessibilityEvent. For more information about implementing accessibility, see Accessibility.
AccessibilityNodeInfoCompat - Adds support for AccessibilityNodeInfo.
AccessibilityNodeProviderCompat - Adds support for AccessibilityNodeProvider.
AccessibilityDelegateCompat - Adds support for View.AccessibilityDelegate.
Content
Loader - Adds support for asynchronous loading of data. The library also provides concrete implementations of this class, including CursorLoader and AsyncTaskLoader.
FileProvider - Adds support for sharing of private files between applications.
还有很多其他API包含在这个库中。在API引用中查看 android.support.v4包，以获取关于v4 Support Library API的全部和详细的信息。
原文：There are many other APIs included in this library. For complete, detailed information about the v4 Support Library APIs, see the  android.support.v4 package in the API reference.



在你下载了Android Support Library以后，这个库放在<sdk>/extras/android/support/v4/目录下。这个库不包含用户界面资源。按照adding libraries without resources的说明来将这个库引入你的应用项目。
原文：This library is located in the <sdk>/extras/android/support/v4/ directory after you download the Android Support Libraries. This library does not contain user interface resources. To include it in your application project, follow the instructions for adding libraries without resources.



这个库的Gradle建造脚本依赖标识符如下：
原文：The Gradle build script dependency identifier for this library is as follows:

com.android.support:support-v4:18.0.+  

这个依赖符号指定了发布版本为18.0.0或更高。
原文：This dependency notation specifies the release version 18.0.0 or higher.

## v7 Support Library  ##

有几个库是为Android 2.1（API level 7）和更高版本设计的。这些库提供指定的功能集合，并且可以相互独立地引入你的应用。
原文：There are several libraries designed to be used with Android 2.1 (API level 7) and higher. These libraries provide specific feature sets and can be included in your application independently from each other.

v7 appcompat library
这个库添加了对Action Bar用户界面 design pattern（设计样式）的支持。
原文：This library adds support for the Action Bar user interface design pattern.

注意：这个库依赖于v4 Support Library。如果你在使用Ant或者Eclipse，确保要将v4 Support Library作为这个库classpath的一部分引入。
原文：Note: This library depends on the v4 Support Library. If you are using Ant or Eclipse, make sure you include the v4 Support Library as part of this library's classpath.

这里有一些包含在v7 appcompat库中的关键类：（此处偷个懒，省去关键类的翻译200字^_^） 
原文：Here are a few of the key classes included in the v7 appcompat library:

ActionBar - Provides an implementation of the action bar user interface pattern. For more information on using the Action Bar, see the Action Bar developer guide.
ActionBarActivity - Adds an application activity class that must be used as a base class for activities that uses the Support Library action bar implementation.
ShareActionProvider - Adds support for a standardized sharing action (such as email or posting to social applications) that can be included in an action bar.
在你下载了Android Support Library以后，这个库放在<sdk>/extras/android/support/v7/appcompat/ 目录下。这个库包含用户界面资源。按照 adding libraries with resources 的说明来将这个库引入你的应用项目。

原文：This library is located in the <sdk>/extras/android/support/v7/appcompat/ directory after you download the Android Support Libraries. This library contains user interface resources. To include it in your application project, follow the instructions for  adding libraries with resources.

这个库的Gradle建造脚本依赖标识符如下： 
原文：The Gradle build script dependency identifier for this library is as follows:

[plain] view plaincopy在CODE上查看代码片派生到我的代码片
com.android.support:appcompat-v7:18.0.+  

这个依赖符号指定了发布版本为18.0.0或更高。
原文：This dependency notation specifies the release version 18.0.0 or higher.

v7 gridlayout library 
这个库添加了对 GridLayout类的支持，这个类允许你使用矩形单元的网格来排列用户界面元素。在API引用中查看android.support.v7.widget 包，以获取关于v7 gridlayout Support Library API的详细信息。

原文：This library adds support for the GridLayout class, which allows you to arrange user interface elements using a grid of rectangular cells. For detailed information about the v7 gridlayout library APIs, see the android.support.v7.widget package in the API reference.

在你下载了Android Support Library以后，这个库放在<sdk>/extras/android/support/v7/gridlayout/ 目录下。这个库包含用户界面资源。按照adding libraries with resources 的说明来将这个库引入你的应用项目。 

原文：This library is located in the <sdk>/extras/android/support/v7/gridlayout/ directory after you download the Android Support Libraries. This library contains user interface resources. To include it in your application project, follow the instructions for adding libraries with resources.

这个库的Gradle建造脚本依赖标识符如下： 
原文：The Gradle build script dependency identifier for this library is as follows:

com.android.support:gridlayout-v7:18.0.+  

这个依赖符号指定了发布版本为18.0.0或更高。
原文：This dependency notation specifies the release version 18.0.0 or higher.

v7 mediarouter library 
这个库提供了MediaRouter, MediaRouteProvider和相关的支持Google Cast developer preview（谷歌投影）（译者注：谷歌投影是一种屏幕分享技术，可以将小屏幕的智能设备分享到大的屏幕上，如智能手机到电视，但是详情请看这里，或这里1，或这里2）开发者预览的媒体类。

原文：This library provides MediaRouter, MediaRouteProvider, and related media classes that support the Google Cast developer preview.

总的来说，在v7 mediarouter库中的API提供了一种控制手段，它可以控制从当前设备到扩展屏幕、扬声器和其他目的设备的媒体通道和数据流的路径选择。这个库包含了一些API，这些API包含发行具有应用特性的媒体路径provider，发现和选择目的设备，检查媒体状态和其他一些功能。在API引用中查看 android.support.v7.media 包，以获取关于v7 mediarouter Support Library API的详细信息。

原文：In general, the APIs in the v7 mediarouter library provide a means of controlling the routing of media channels and streams from the current device to external screens, speakers, and other destination devices. The library includes APIs for publishing app-specific media route providers, for discovering and selecting destination devices, for checking media status, and more. For detailed information about the v7 mediarouter library APIs, see the  android.support.v7.media package in the API reference.

在你下载了Android Support Library以后，这个库放在<sdk>/extras/android/support/v7/mediarouter/ 目录下。这个库包含用户界面资源。它是以一个从属于v7 appcompat库的库项目的方式提供给开发者使用的，所以当你在创建项目的时候，你需要在build path中同时引入这两个库。按照 adding libraries with resources 的说明以获取更多如何创建项目的信息。  如果你使用Eclipse/ADT开发，要确保同时引入android-support-v7-mediarouter.jar和android-support-v7-appcompat.jar文件。

原文：The v7 mediarouter library is located in the <sdk>/extras/android/support/v7/mediarouter/ directory after you download the Android Support Library. It's provided as a library project with a dependency on the v7 appcompat library, so you'll need to include both libraries in your build path when setting up your project. For more information on how to set up your project, follow the instructions in adding libraries with resources. If you are developing in Eclipse/ADT, make sure to include both the android-support-v7-mediarouter.jar and android-support-v7-appcompat.jar files.

如果你使用的是Android Studio，你只需要指定Gradle建造脚本依赖标识符com.android.support:support-v7-mediarouter:<revision>，这个标识符的可用库的修正版本最低是18.0.0。例如：

com.android.support:mediarouter-v7:18.0.+  

原文：If you are using Android Studio, all you need to do is specify the Gradle build script dependency identifier com.android.support:support-v7-mediarouter:<revision>, where "18.0.0" is the minimum revision at which the library is available. For example:

Tip：在Support Library r18中介绍的v7 mediarouter库API受制于Support Library的后续修正变化。当前，我们推荐你只在关于Google Cast developer preview时使用这个库。

原文：Tip:The v7 mediarouter library APIs introduced in Support Library r18 are subject to change in later revisions of the Support Library. At this time, we recommend using the library only in connection with the Google Cast developer preview.

## v8 Support Library  ##
这个库是为Android（API level 8）或更高版本的使用设计的。它加入了对  RenderScript 计算架构的支持。这些API包含在 android.support.v8.renderscript包中。你应该能意识到介绍包含这些API的应用程序的步骤与其他支持库的API有很大不同。查看  RenderScript开发者指南，以获取更多关于这些API的信息。

原文：This library is designed to be used with Android (API level 8) and higher. It adds support for the  RenderScript computation framework. These APIs are included in the android.support.v8.renderscript package. You should be aware that the steps for including these APIs in your application is very different from other support library APIs. For more information about using these APIs in your application, see the RenderScript developer guide.

注意：利用Support Library使用RenderScript，这是被Android Eclipse插件和Ant build工具支持的。目前它不支持使用Android Studio或者基于Gradle构建。

原文：Note: Use of RenderScript with the support library is supported with the Android Eclipse plugin and Ant build tools. It isnot currently supported with Android Studio or Gradle-based builds.

## v13 Support Library  ##
这个库是为Android 3.2（API level 13）或更高版本的使用设计的。它添加了对  Fragment用户界面样式（利用FragmentCompat类）和额外的fragment支持类的支持。查看  Fragment开发者指南查阅更多关于fragment的信息。在API引用中查看android.support.v13包，以获取关于v13 Support Library API的详细信息。

原文：This library is designed to be used for Android 3.2 (API level 13) and higher. It adds support for the  Fragment user interface pattern with the (FragmentCompat) class and additional fragment support classes. For more information about fragments, see the Fragments developer guide. For detailed information about the v13 Support Library APIs, see the android.support.v13 package in the API reference.

在你下载了Android Support Library以后，这个库放在 <sdk>/extras/android/support/v13/  目录下。这个库不包含用户界面资源。按照 adding libraries without resources的说明来将这个库引入你的应用项目。

原文：This library is located in the <sdk>/extras/android/support/v13/ directory after you download the Android Support Libraries. This library does not contain user interface resources. To include it in your application project, follow the instructions for adding libraries without resources.

这个库的Gradle建造脚本依赖标识符如下：  
原文：The Gradle build script dependency identifier for this library is as follows:

[plain] view plaincopy在CODE上查看代码片派生到我的代码片
com.android.support:support-v13:18.0.+  

这个依赖符号指定了发布版本为18.0.0或更高。
原文：This dependency notation specifies the release version 18.0.0 or higher.

原文地址：http://developer.android.com/tools/support-library/setup.html

如何在你的开发项目中安装Android Support Library，取决于你要使用哪些功能，以及你要在你的应用中支持哪个范围的Android平台版本。

原文：How you setup the Android Support Libraries in your development project depends on what features you want to use and what range of Android platform versions you want to support with your application.
这个文档指导你下载Support Library包，并向你的开发环境中添加库。

原文：This document guides you through downloading the Support Library package and adding libraries to your development environment.

下载Support Library
Android Support Library包是作为Android SDK的补充下载提供的，并且在AndroidSDK Manager中是可用的。遵循以下说明来获取Support Library文件。

原文：The Android Support Library package is provided as a supplemental download to the Android SDK and is available through the Android SDK Manager. Follow the instructions below to obtain the Support Library files.

通过SDK Manager获取Support Library：

1.打开Android SDK Manager

2.在SDK Manager窗口，滚动到Package List的末尾，找到Extra文件夹，如果需要的话打开文件夹显示它的内容。

3.选择Android Support Library项目。

注意：如果你使用的是Android Studio开发，选择并安装Android Support Repository项目而不是Android Support Library项目。

4.点击Install packages按钮。



下载完成后，SDK会将Support Library文件安装到你已经存在的Android SDK目录下。库文件位于SDK的如下子目录：<sdk>/extras/android/support/目录。

原文：After downloading, the tool installs the Support Library files to your existing Android SDK directory. The library files are located in the following subdirectory of your SDK: <sdk>/extras/android/support/ directory.


选择Support Library

在添加一个Support Library到你的项目之前，想好你要包含哪些功能，以及你要支持的最低Android版本是多少。查看Support Library Features以获取更多关于不同库提供的功能的信息。

原文：Before adding a Support Library to your application, decide what features you want to include and the lowest Android versions you want to support. For more information on the features provided by the different libraries, see Support Library Features.

添加Support Library


为了使用Support Library，你必须在开发环境中修改应用项目的classpath依赖关系。并且你必须为将要使用的每一个Support Library都执行一下上述步骤。
原文：In order to use a Support Library, you must modify your application's project's classpath dependencies within your development environment. You must perform this procedure for each Support Library you want to use.
除了可编译的代码类，一些Support Library包含了资源，比如图片或XML文件。例如，v7 appcompat和 v7 gridlayout库包含一些资源。

原文：Some Support Libraries contain resources beyond compiled code classes, such as images or XML files. For example, thev7 appcompat and v7 gridlayout libraries include resources.
如果你不确定一个库是否包含资源，查看Support Library Features页面。下面的章节描述了如何添加一个包含或不包含资源的Support Library到你的应用项目中。

原文：If you are not sure if a library contains resources, check the Support Library Features page. The following sections describe how to add a Support Library with or without resources to your application project.


添加不带资源的library


添加一个不包含资源的Support Library到你的应用项目：
原文：To add a Support Library without resources to your application project:
利用Eclipse
确保你已经利用SDK Manager下载了Android Support Library 。
在你的项目的根目录下创建一个libs/目录。
从你的Android SDK安装目录（例如，<sdk>/extras/android/support/v4/android-support-v4.jar）下拷贝JAR文件到你项目的libs/目录下。
右键点击JAR文件并选择Build Path > Add to Build Path。
利用Android Studio
确保你已经利用SDK Manager下载了Android Support Repository 。
打开项目中的 build.gradle文件。
添加Support Library到dependencies部分。例如，如果添加v4 Support Library，加入下面一行：

dependencies {  
    ...  
    compile "com.android.support:support-v4:18.0.+"  
}  

添加带资源的library


添加一个包含资源（例如带有Action Bar的v7 appcompat）的Support Library到你的应用项目：
原文：To add a Support Library with resources (such as v7 appcompat for action bar) to your application project:

利用Eclipse
创建一个基于support library代码的 library project：
确保你已经利用 SDK Manager下载了Android Support Library。
创建一个library项目并且确保需要的JAR文件包含在了项目的build path中：
选择File > Import。
选择Existing Android Code Into Workspace 并点击Next。
浏览SDK安装目录，并进入Support Library目录下。例如，如果你要添加appcompat项目，浏览 <sdk>/extras/android/support/v7/appcompat/。
点击Finish引入项目。对于v7 appcompat项目，你将看到一个标题为android-support-v7-appcompat的新项目。
在新项目中，展开libs/ 文件夹，右键点击每一个.jar文件，并选择Build Path > Add to Build Path。例如，当创建v7 appcompat项目时，同时将android-support-v4.jar和android-support-v7-appcompat.jar文件添加到build path中。
右键点击library项目文件夹并选择Build Path > Configure Build Path。
在Order and Export选项中，在刚刚添加到build path中的.jar文件上打勾，这时这些文件成为项目可用的了并依赖于这个library项目。例如，appcompat项目要求同时导出android-support-v4.jar和android-support-v7-appcompat.jar文件。
去掉Android Dependencies上的对勾。
点击OK完成设置。
现在你拥有了一个包含你选择的Support Library的library项目，你可以在一个或多个应用项目中利用这个Support Library。
原文：You now have a library project for your selected Support Library that you can use with one or more application projects.

向你的应用工程（译者注：需要加入Support Library的工程）添加库：
原文：Add the library to your application project:
在项目浏览器中右键单击你的项目，选择Properties。
在左边的分类面板中，选择Android。
在Library面板中，点击Add。
选择库项目，然后点击OK。例如，appcompat项目会在列表中显示为android-support-v7-appcompat。
在properties窗口中，点击OK。
利用Android Studio
确保你利用SDK Manager下载了Android Support Repository。
打开项目中的build.gradle文件。
将support library功能项目标识符添加到dependencies部分。例如，为了包含appcompat项目，需要将compile "com.android.support:appcompat-v7:18.0.+"添加到dependencies部分。如下：
[plain] view plaincopy在CODE上查看代码片派生到我的代码片
dependencies {  
    ...  
    compile "com.android.support:appcompat-v7:18.0.+"  
}  
使用Support Library的API
对目前framework API提供支持的Support Library类，一般都与framework中的类名相同，但是位于android.support类包中，或者有一个*Compat后缀。

原文：Support Library classes that provide support for existing framework APIs typically have the same name as framework class but are located in the android.support class packages, or have a *Compat suffix.
警告：当使用Support Library中的类的时候，确定你从适当的包中引入这些类。例如，当应用ActionBar类的时候：

当使用Support Library时：android.support.v7.app.ActionBar
当只为API 11或更高级别开发的时候：android.app.ActionBar
原文：Caution: When using classes from the Support Library, be certain you import the class from the appropriate package. For example, when applying the ActionBar class:
android.support.v7.app.ActionBar when using the Support Library.
android.app.ActionBar when developing only for API level 11 or higher.
注意：当包含了Support Library在你的项目中后，我们强烈推荐你使用ProGuard工具准备你的应用APK发布。除了保护你的源代码外，ProGuard工具还可以从你包含到应用中的任何库中去除无用的类，这样可以使你的应用大小在下载的时候尽可能的小。要获取更多信息，请查看 ProGuard。
原文：Note: After including the Support Library in your application project, we strongly recommend using the ProGuard tool to prepare your application APK for release. In addition to protecting your source code, the ProGuard tool also removes unused classes from any libraries you include in your application, which keeps the download size of your application as small as possible. For more information, see ProGuard.

在Android开发者training classes、guides和示例中，提供了一些使用Support Library功能的更多指南。查看API参考中的android.support包，以获取更多关于单个Support Library类和方法的信息。

原文：Further guidance for using some Support Library features is provided in the Android developer training classes, guides and samples. For more information about the individual Support Library classes and methods, see the android.support packages in the API reference.

Manifest声明的改变

如果你正在利用Support Library增加目前应用的向下兼容性到一个Android API的早期版本，确保更新你应用的manifest。特别的，你要更新manifest中<uses-sdk>标签的android:minSdkVersion元素，改成一个新的，更低的版本号，如下：
原文：If you are increasing the backward compatibility of your existing application to an earlier version of the Android API with the Support Library, make sure to update your application's manifest. Specifically, you should update the android:minSdkVersion element of the <uses-sdk> tag in the manifest to the new, lower version number, as shown below:
<uses-sdk  
    android:minSdkVersion="7"  
    android:targetSdkVersion="17" />  

这个改变通知Google Play你的应用可以被安装在Android 2.1（API level 7）或更高的版本的设备中。
原文：This change tells Google Play that your application can be installed on devices with Android 2.1 (API level 7) and higher.

注意：如果你在项目中包含了v4 support和v7 appcompat库，你需要指定最小SDK版本为7（而不是4）。你引入到应用的最高级别的support library决定了它可以操作的最低的API版本。（译者注：这句话是指当你同时引入了多个包的情况，比如，v7比v4版本更高，所以可以操作的最小SDK版本是7）

原文：Note: If you are including the v4 support and v7 appcompat libraries in your application, you should specify a minimum SDK version of "7" (and not "4"). The highest support library level you include in your application determines the lowest API version in which it can operate.

代码示例
每一个Support Library都包含了代码示例来帮助你开始使用support API。代码示例包含在从SDK Manager的下载中，并被放在了Android SDK安装目录中，如下所列：
原文：Each Support Library includes code samples to help you get started using the support APIs. The code is included in the download from the SDK Manager and is placed inside the Android SDK installation directory, as listed below:

4v Samples: <sdk>/extras/android/support/samples/Support4Demos/
7v Samples: <sdk>/extras/android/support/samples/Support7Demos/
13v Samples: <sdk>/extras/android/support/samples/Support13Demos/
App Navigation: <sdk>/extras/android/support/samples/SupportAppNavigation/


1, Android Support V4, V7, V13是什么？
本质上就是三个java library。

2, 为什么要有support库？
如果在低版本Android平台上开发一个应用程序，而应用程序又想使用高版本才拥有的功能，就需要使用Support库。

3, 三个Support 库的区别和作用是什么？
Android Support v4 是最早（2011年4月份）实现的库。用在Android1.6 (API lever 4)或者更高版本之上。它包含了相对V4, V13大的多的功能。
例如：Fragment，NotificationCompat，LoadBroadcastManager，ViewPager，PageTabAtrip，Loader，FileProvider 等。
详细API 参考 http://developer.android.com/reference/android/support/v4/app/package-summary.html
Android Support v7: 这个包是为了考虑Android2.1(API level 7) 及以上版本而设计的，但是v7是要依赖v4这个包的，也就是如果要使用，两个包得同时 被引用。
 v7支持了Action Bar。
Android Support v13:这个包的设计是为了android 3.2及更高版本的，一般我们都不常用，平板开发中能用到。


4, 如何使用？
首先要保证Android Support Library 被安装

然后在工程中增加（例如 support-v4 Library）

在ADT中需要按照以下步骤：
1、右击当前工程，查找Properties
2、选择Java Build Path
3、选择Libraries tab，点击右边面板的Add External JARs按钮
4、选择android-support-v4.jar文件，这一文件的常见路径为:YOUR_DRIVE\android-sdks\extras\android\support\v4\android-support-v4.jar
5、完成添加后，选择Order and Export标签，确认即可。


5 v4、v7、v13 的例子代码
4v : <sdk>/extras/android/support/samples/Support4Demos/
7v : <sdk>/extras/android/support/samples/Support7Demos/
13v : <sdk>/extras/android/support/samples/Support13Demos/
App Navigation: <sdk>/extras/android/support/samples/SupportAppNavigation/

Android Support v4、v7、v13的区别和应用场景

N久未做android了，以前做的时候，2.2才刚出来，现在android都更新到了4.3了，而从前一段时间android各个sdk版本市场占有率 来看，1.6、2.1还是占有一定的市场,故在有些时候，我们还是得要考虑兼容这些版本。

google提供了Android Support Library package 系列的包来保证来高版本sdk开发的向下兼容性，即我们用4.x开发时，在1.6等版本上，可以使用高版本的有些特性，如fragement,ViewPager等，下面，简单说明下这几个版本间的区别：

Android Support v4:  这个包是为了照顾1.6及更高版本而设计的，这个包是使用最广泛的，eclipse新建工程时，都默认带有了。

Android Support v7:  这个包是为了考虑照顾2.1及以上版本而设计的，但不包含更低，故如果不考虑1.6,我们可以采用再加上这个包，另外注意，v7是要依赖v4这个包的，即，两个得同时被包含。

Android Support v13  :这个包的设计是为了android 3.2及更高版本的，一般我们都不常用，平板开发中能用到。

有这么一个问题：
开发中ADT新建项目的时候，会自动帮你将v4.jar绑定加入到你的项目中，但是你无法查看v4.jar的源码，怎样能够查看其中的源码？
解决方案是：
首先,你先点击项目中的右键，进入Project properties中，选择LibraryTab列，将其中的Android Dependencies  remove掉
然后,在SDK的目录下去寻找/extras\android\support\v4下的jar包 ，然后添加到其中去
最后，便可以按以往的步骤绑定源码了，右键jar包，attach  sourch  file 源码文件进入，源码文件的位置在选中即可


Android Support兼容包详解
http://stormzhang.com/android/2015/03/29/android-support-library/

背景
来自于知乎上邀请回答的一个问题Android中AppCompat和Holo的一个问题？, 看来很多人还是对这些兼容包搞不清楚，那么干脆写篇博客吧.

Support Library
我们都知道Android一些SDK比较分裂，为此google官方提供了Android Support Library package 系列的包来保证高版本sdk开发的向下兼容性, 所以你可能经常看到v4，v7，v13这些数字，首先我们就来理清楚这些数字的含义，以及它们之间的区别。

support-v4
用在API lever 4(即Android 1.6)或者更高版本之上。它包含了相对更多的内容，而且用的更为广泛，例如：Fragment，NotificationCompat，LoadBroadcastManager，ViewPager，PageTabStrip，Loader，FileProvider 等

Gradle引用方法：

compile 'com.android.support:support-v4:21.0.3'
support-v7
这个包是为了考虑API level 7(即Android 2.1)及以上版本而设计的，但是v7是要依赖v4这个包的，v7支持了Action Bar以及一些Theme的兼容。

Gradle引用方法:

compile 'com.android.support:appcompat-v7:21.0.3'
support-v13
这个包的设计是为了API level 13(即Android 3.2)及更高版本的，一般我们都不常用，平板开发中能用到，这里就不过多介绍了。

Theme
我们来介绍下各种Theme的概念。

Hoho Theme
在4.0之前Android可以说是没有设计可言的，在4.0之后推出了Android Design，从此Android在设计上有了很大的改善，而在程序实现上相应的就是Holo风格，所以你看到有类似 Theme.Holo.Light、 Theme.Holo.Light.DarkActionBar 就是4.0的设计风格，但是为了让4.0之前的版本也能有这种风格怎么办呢？这个时候就不得不引用v7包了，所以对应的就有 Theme.AppCompat.Light、 Theme.AppCompat.Light.DarkActionBar，如果你的程序最小支持的版本是4.0，那么可以不用考虑v7的兼容。

Material Design Theme
今年的5.0版本，Android推出了Material Design的概念，这是在设计上Android的又一大突破。对应的程序实现上就有 Theme.Material.Light、 Theme.Material.Light.DarkActionBar等，但是这种风格只能应用在在5.0版本的手机，如果在5.0之前应用Material Design该怎么办呢？同样的引用appcompat-v7包，这个时候的Theme.AppCompat.Light、 Theme.AppCompat.Light.DarkActionBar就是相对应兼容的Material Design的Theme。

注意事项
gradle引用appcompat-v7包的时候就不需要引用v4了，因为v7里默认包含了v4包；

compile ‘com.android.support:appcompat-v7:21.0.3’ 中的21代表API level 21推出的兼容包，所以如果你引用的是21之前的版本，则默认这些Theme.AppCompat.Light是Holo风格的，从21开始的版本默认是Material风格

使用appcompat之后，你的所有的Activity应该继承自ActionBarActivity，而ActionBarActivity继承自FragmentActivity，所以放心的使用Fragment；

最后，相信已经讲的很清楚了，大家有问题可直接博客留言。如果英语好的，可直接移步官方最权威的解释
https://developer.android.com/tools/support-library/features.html

## 进入和退出 Activity 的动画

### 应用场景

1. 当用 startActivity 启动一个新的 Activity 
2. 当前的 Activity finish 

### 如何使用

overridePendingTransition(R.anim.enter_anim, R.anim.exit_anim);

在 startActivity finish 之后使用，可以自定义新 Activity 的进入前台的动画效果，和当前 Activity 进入后台的动画效果

### 例子


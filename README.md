# Camera2Record
Camera2 api实现点击拍照，长按录制。

更多介绍参考简书地址：http://www.jianshu.com/p/f8c694a4fb57

# 一、先看一下效果图：
![capture.gif](http://upload-images.jianshu.io/upload_images/3318087-d1fe85e84137e988.gif?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![record.gif](http://upload-images.jianshu.io/upload_images/3318087-a9b84904f9e2e81f.gif?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

# 二、3步集成到自己的项目中：
**1.在AndroidManifest.xml申明所需要的权限：**
（注：请确保进入Camera2的时候已经拥有这三项权限了，Android6.0需要动态去申请权限）
```
   <uses-permission android:name="android.permission.CAMERA" />
   <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
   <uses-permission android:name="android.permission.RECORD_AUDIO" />
```
**2.在project的build.gradle和app的build.gradle下分别申明如下代码：**

project的build.gradle：
```
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }//这句代码
    }
}
```
app的build.gradle：
```
dependencies {
    compile 'com.github.CKTim:Camera2Record:v1.0.0'
}
```
**3.打开Camera2：**

因为每个人对拍完照或者录完像后的处理都不一样，所以这里我采用拍完跳转activity的方式，将拍照录像后的地址传递给了下一个activity，当然这个activty界面逻辑什么的都是由你自己去编写的，你可以对获取到的图片视频地址进行你需要的编辑，例如再次压缩或者重拍等操作：
getIntent().getStringExtra(Camera2Config.INTENT_PATH_SAVE_PIC);//获取图片地址
getIntent().getStringExtra(Camera2Config.INTENT_PATH_SAVE_VIDEO);//获取视频地址

如下用法:
```
    //配置Camera2相关参数，
    Camera2Config.RECORD_MAX_TIME = 10;//最长录制时间
    Camera2Config.RECORD_MIN_TIME=2;//最短录制时间
    Camera2Config.RECORD_PROGRESS_VIEW_COLOR=R.color.colorAccent;//录制进度条颜色
    Camera2Config.PREVIEW_MAX_HEIGHT=1000;////最大高度预览尺寸，默认大于1000的第一个
    Camera2Config.PATH_SAVE_VIDEO=;//视频保存地址，注意需要以/结束，例如Camera2/
    Camera2Config.PATH_SAVE_PIC=;图片保存地址，注意需要以/结束，例如Camera2/
    Camera2Config.ENABLE_CAPTURE=true;//是否开启拍照功能
    Camera2Config.ENABLE_RECORD=true;//是否开启录像功能
    //拍完照需要跳转的activity，这个activity自己编写，可以获取到保存的视频或者图片地址
    Camera2Config.ACTIVITY_AFTER_CAPTURE = Camera2RecordFinishActivity.class;
    btnOpenCamera2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //进入Camera2界面
                Camera2RecordActivity.start(MainActivity.this);
            }
        });
```

# QQChatHistoryExporter
手机QQ聊天记录导出，在现有项目基础上增加对语音消息和转发聊天记录的支持，不支持群聊导出，下载
[本体](https://github.com/ZhangJun2017/QQChatHistoryExporter/releases/download/v1.2/QQChatHistoryExporter_v1.2.jar) [附加包](https://github.com/ZhangJun2017/QQChatHistoryExporter/releases/download/v1.1/QQChatHistoryExporter_v1.1_extra.zip)
，需要Java环境

## 使用方法
* 通过root或者其他途径取得
    >数据库文件
    >```
    >/data/data/com.tencent.mobileqq/databases/QQ号.db
    >/data/data/com.tencent.mobileqq/databases/slowtable_QQ号.db
    >```
    >
    >密钥
    >```
    >/data/data/com.tencent.mobileqq/files/kc
    >```

* 如果需要导出图片
    >将此文件夹复制到资源目录下
    >```
    >内置储存/Android/data/com.tencent.mobileqq/Tencent/MobileQQ/chatpic
    >```

* 如果需要导出语音
    >将此文件夹复制到资源目录下
    >```
    >内置储存/Android/data/com.tencent.mobileqq/Tencent/MobileQQ/QQ号/ptt
    >```
    >并解压附加包内的 `silk_v3_decoder.exe` `ffmpeg.exe` 到资源目录下

* 在命令提示符内执行 `java -Dfile.encoding=UTF-8 -jar 本体jar路径` 

* 填入`数据库路径`，`资源路径`，`输出路径`，`密钥文件的内容`，`自己的QQ号`和`导出对象的QQ号`

* 等待导出。将自动复制图片、转发的聊天记录和语音消息，意外中断的话可以手动再次执行输出目录下的 `exop.bat`

* 如果需要显示QQ自带表情
    >解压附加包内的 `emotion` 文件夹到输出目录下 `assets` 文件夹内
    
## 说明
导出的文件以 `QQ号.html` 命名，迁移时也要一并包括 `assets` 文件夹

***
**感谢 [Yiyiyimu/QQ-History-Backup](https://github.com/Yiyiyimu/QQ-History-Backup) 及其所基于的项目，本项目核心部分和附加包内QQ自带表情资源完全来自该项目**

**感谢 [kn007/silk-v3-decoder](https://github.com/kn007/silk-v3-decoder) 和 [FFmpeg/FFmpeg](https://github.com/FFmpeg/FFmpeg) ，附加包内解码语音消息所需的可执行文件 `silk_v3_decoder.exe` `ffmpeg.exe` 来自这两个项目**

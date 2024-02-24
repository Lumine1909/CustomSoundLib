# CustomSoundLib
Add custom sounds in game (resource pack)


Config:

```
settings:
  force-resource-pack: false
  resource-pack-port: 25567 #内部端口 用于启动分发服务器
  resource-url: "http://127.0.0.1:25567" #外部下载url
pack:
  description: "Resource pack for the sounds used in this server" #资源包介绍
  sounds: #手动添加的文件应在此栏目下标注
    music1:
      file: "music.ogg" #文件名 默认放在插件文件夹目录下
      name: "testmusic.ogg" #在资源包中的文件名 用于游戏
      path: "music/test" #资源包中的文件路径 用于定位
      key: "music.test" #此音效的事件 用于播放

```

API usage:
```
SoundManager manager = Bukkit.getServicesManager().getRegistration(SoundManager.class).getProvider();
manager.addSound(new File(getDataFolder(), "test.ogg"), "test1.ogg", "music/new", "music.new");
```

Details:
[TestSoundPlugin.zip](https://github.com/Lumine1909/CustomSoundLib/files/14391594/TestSoundPlugin.zip)

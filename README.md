# 《坦克动荡》游戏功能实现

## 一、需求与目标

《坦克动荡》是一款十分经典的2D坦克战斗游戏，是作者与伙伴的童年回忆，但原版坦克动荡拓展性不高，且仅支持单机双人模式，因此作者希望在复刻这款游戏的功能上添加新的功能与玩法。

增加的功能主要有：人机对战模式，道具系统，血条显示，自定义键位

## 二、项目架构

```structure
.
├── src                                             # 项目源代码
    ├──images                                       # 使用图片
    ├──map                                          # 预设地图（加速初次加载）
    ├──Bullet.java                                  # 子弹类                                  
    ├──BulletManager.java                           # 子弹管理
    ├──EndPanel.java                                # 结束界面
    ├──GamePanel.java                               # 游戏界面                                   
    ├──HomePanel.java                               # 开始界面
    ├──Item.java                                    # 道具父类
    ├──Main.java                                    # 主入口
    ├──Screen.java                                  # 主框架
    ├──Tank.java                                    # 坦克类
    ├──TankPanel.java                               # 坦克操控接口（可接入user、ai）
    ├──TileManager.java                             # 地块管理器
    ├──ImageLoader.java                             # 图片加载器
    ├──User.java                                    # 玩家 1
    └──User2.java                                   # 玩家 2
├── .vscode                                         # 使用 vscode 开发
├── README.md                                       # 项目中文介绍
└── README-EN.md                                    # 项目英文介绍
```

## 三、开发流程

### 1. 开发工具和环境

使用 **Visual Studio Code** 进行开发，环境为 **jdk 21.0.8**

### 2. 开发前准备

#### 2.1 确定大小

以 48 像素 * 48 像素为一个单元

使用 $2,304(48 * 16 * 3) * 1728(48 * 12 * 3)$ 大小的窗口.

#### 2.2 获取资源

所有的图片来自 [opengameart](https://opengameart.org/) 和 [pixel editor](https://pixieditor.net/)

### 3. 开发模块

## 进展里程碑

v0.7:实现原版游戏功能（单机双人PVP模式）

## 四、快速开始

### 1. 下载安装

若配置了jdk 21.0.8 及以上版本（低版本可能会有兼容问题，但作者尽量避免使用高版本特性）的 JAVA 环境，可以克隆本仓库运行

若没有，请下载仓库中的 demo.zip ，解压后根据安装说明即可

### 2. 玩法

目前仅支持单机双人模式，

player 1:WASD 对应上左下右，空格发射

player 2:↑←↓→ 对应上左下右，回车键发射

子弹可反弹，每个坦克有 5 滴血

只剩一辆坦克时游戏结束，该坦克胜利，结算页面持续3s后回到主页面

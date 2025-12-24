# 《Tank-Strike》

<div align="center">

[**简体中文**](./README.md) | [**English**](./README_En.md)

</div>

## 一、快速开始

### 1. 下载安装

前提条件：Java8及以上

```bash
# 1.克隆项目
git clone https://github.com/rippor911/Personal-Tank-Strike.git

# 2.进入源代码目录
cd Personal-Tank-Strike/src

# 3.编译与运行（也可以在IDE中完成）
javac -encoding UTF-8 Main.java
java Main
```

### 2. 玩法

目前支持单机双人模式，人机对战模式，如果是单机双人模式键位如下：

player 1:WASD 对应上左下右，空格发射

player 2:↑←↓→ 对应上左下右，回车键发射

子弹可反弹，每个坦克初始有 5 滴血，可拾取回血、加速道具

只剩一辆坦克时游戏结束，该坦克胜利，结算页面持续3s后回到主页面

## 二、项目简介

《坦克动荡》是一款十分经典的2D坦克战斗游戏，是作者与伙伴的童年回忆，因此作者希望在复刻这款游戏的功能上添加新的功能与玩法。

增加的功能主要有：可扩展的道具系统，血条显示

```structure
src/  
├── Main.java                    # 程序入口 (Entry Point)
└── com/  
    ├── Screen.java              # 窗口管理 (JFrame)，负责分辨率适配
    ├── GamePanel.java           # 游戏主引擎，包含 60FPS 刷新循环与状态管理
    ├── HomePanel.java           # 主菜单界面 (UI)，处理模式选择与记录查看
    ├── EndPanel.java            # 结算界面，显示胜负结果
    ├── Tank.java                # 坦克实体核心类 (Model)，处理属性与基础动作
    ├── TileManager.java         # 地图渲染器，负责绘制地形
    ├── ShowHeart.java           # UI 组件，实时显示玩家生命值
    ├── controller/              # 控制器层 (Controller)
    │   ├── TankPanel.java       # 坦克控制接口
    │   ├── User1.java           # 玩家1 键盘监听控制器
    │   ├── User2.java           # 玩家2 键盘监听控制器
    │   └── UserAI.java          # 电脑 AI 核心逻辑 (A* 寻路与决策)
    ├── item/                    # 物品与投射物 (Entity)
    │   ├── Bullet.java          # 子弹实体
    │   ├── Item.java            # 道具实体 (如增益buff)
    │   ├── BulletManager.java   # 子弹对象池与生命周期管理
    │   └── ItemManager.java     # 道具生成与交互管理
    └── util/                    # 工具类 (Utils)
        ├── ImageLoader.java     # 资源加载器 (读取图片)
        ├── MazeGenerator.java   # 地图生成算法 (Procedural Generation)
        ├── Logger.java          # 文件 I/O，用于读写记录
        ├── images/              # 游戏贴图资源
        └── record.txt           # 记录存储文件
```

## 三、鸣谢

所有的图片来自 [opengameart](https://opengameart.org/) 和 [pixel editor](https://pixieditor.net/)
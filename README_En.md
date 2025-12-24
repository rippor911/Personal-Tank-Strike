这是为您翻译好的英文版 `README_En.md` 内容。保持了原有的格式和布局，并将代码结构图中的中文注释也进行了翻译，以便国际用户阅读。

```markdown
# Tank-Strike

<div align="center">

[**简体中文**](./README.md) | [**English**](./README_En.md)

</div>

## 1. Quick Start

### 1. Installation

**Prerequisites:** Java 8 or higher.

```bash
# 1. Clone the repository
git clone https://github.com/rippor911/Personal-Tank-Strike.git

# 2. Navigate to source directory
cd Personal-Tank-Strike/src

# 3. Compile and Run (Can also be done in an IDE)
javac -encoding UTF-8 Main.java
java Main
```

### 2. Gameplay

Currently, the game supports **Local PvP (Player vs Player)** and **PvE (Player vs AI)** modes.

**Controls for Local PvP:**

*   **Player 1:** `W`, `A`, `S`, `D` to Move, `Space` to Shoot.
*   **Player 2:** `↑`, `←`, `↓`, `→` (Arrow Keys) to Move, `Enter` to Shoot.

**Mechanics:**
*   Bullets ricochet off walls.
*   Each tank starts with **5 HP**.
*   Items (Health/Speed buffs) spawn randomly and can be picked up.

**Game Over:**
The game ends when only one tank remains. The surviving tank wins. The result screen displays for 3 seconds before returning to the main menu.

## 2. Introduction

"Tank-Strike" is a tribute to the classic flash game "Tank Trouble". It holds childhood memories for the author and their friends. The goal of this project is to recreate the classic gameplay while adding new features and mechanics.

**New Features:** An extensible item system and real-time HP display.

```text
src/  
├── Main.java                    # Entry Point
└── com/  
    ├── Screen.java              # Window Management (JFrame), handles resolution scaling
    ├── GamePanel.java           # Main Game Engine, includes 60FPS loop & state management
    ├── HomePanel.java           # Main Menu UI, handles mode selection & records
    ├── EndPanel.java            # Game Over Screen, displays results
    ├── Tank.java                # Tank Entity Model, handles properties & basic actions
    ├── TileManager.java         # Map Renderer, responsible for drawing terrain
    ├── ShowHeart.java           # UI Component, displays player HP
    ├── controller/              # Controller Layer
    │   ├── TankPanel.java       # Tank Control Interface
    │   ├── User1.java           # Player 1 Keyboard Listener
    │   ├── User2.java           # Player 2 Keyboard Listener
    │   └── UserAI.java          # Computer AI Core Logic (A* Pathfinding & Decision Making)
    ├── item/                    # Items & Projectiles (Entities)
    │   ├── Bullet.java          # Bullet Entity
    │   ├── Item.java            # Item Entity (e.g., Buffs)
    │   ├── BulletManager.java   # Bullet Object Pool & Lifecycle Management
    │   └── ItemManager.java     # Item Generation & Interaction Management
    └── util/                    # Utils
        ├── ImageLoader.java     # Resource Loader (Images)
        ├── MazeGenerator.java   # Map Generation Algorithm (Procedural Generation)
        ├── Logger.java          # File I/O for record keeping
        ├── images/              # Game Image Assets
        └── record.txt           # Record Storage File
```

## 3. Acknowledgments

All images are from [opengameart](https://opengameart.org/) and [pixel editor](https://pixieditor.net/).
```
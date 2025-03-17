# KoukeNeko-Lobby Plugin

## 🌟 Introduction

KoukeNeko-Lobby is a powerful Minecraft plugin that provides a complete lobby system for your server. It protects your lobby world from player interactions, offers a teleportation system, and enhances player experience with special effects.

## 🔧 Features

* ✅ Lobby world protection (block break, place, interactions)
* ✅ Player protection from damage in the lobby
* ✅ Anti-explosion protection in the lobby world
* ✅ Minecart collision protection
* ✅ Automatic player status effects in the lobby (regeneration, speed, saturation)
* ✅ Simple teleportation system with /lobby command
* ✅ Easy lobby point setting with /setlobby
* ✅ Fully customizable via configuration file

## 📂 Installation

1. Place the plugin (KoukeNeko-Lobby.jar) into your server's plugins folder.
2. Restart your server to activate the plugin.
3. The plugin will generate a default configuration file.
4. Use /setlobby to set the lobby teleportation point.

## ⚙️ Configuration

Example configuration (config.yml):

```yaml
prefix: "&7[&b&l🕹️&7]&f" # Message prefix

# Lobby system settings
lobby_system:
  # Lobby world name
  lobby_world: world_lobby

  # Permission to bypass lobby restrictions
  lobby_permission: koukeneko.lobby.bypass

  # Allow block breaking in lobby
  allow_break: false

  # Allow block placing in lobby
  allow_place: false

  # Allow interactions in lobby
  allow_use: false

  # Allow attacking entities in lobby
  allow_attack: false

  # Allow explosions in lobby
  allow_explosion: false

  # Allow minecart collisions
  allow_vehicle_collision: false

  # Effect settings
  effects:
    # Enable lobby effects
    enabled: true
    # Effect refresh interval (ticks, 20 = 1 second)
    interval: 200
    # Effect duration (ticks)
    duration: 220
    # Enable specific effects
    regeneration: true
    speed: true
    saturation: true

  # Lobby location settings
  enable_lobby_location: false
  lobby_location:
    world: world_lobby
    x: 0.0
    y: 100.0
    z: 0.0
    yaw: 0.0
    pitch: 0.0

# Message settings
messages:
  only_player: "&c只有玩家才可以使用"
  no_permission: "&c你沒有權限執行此命令"
  lobby_set: "&a大廳位置已設定"
  lobby_not_enabled: "&c大廳位置未啟用，請聯絡管理員"
  lobby_not_set: "&c大廳位置未設定，請聯絡管理員"
  teleport_success: "&a傳送至大廳"
  config_reloaded: "&a設定文件已重新載入！"
```

## 🚀 Commands

* `/setlobby` (Permission: koukeneko.lobby.admin)
  + Sets the current location as the lobby teleportation point.

* `/lobby` (Aliases: hub, spawn)
  + Teleports the player to the lobby.

* `/knlobbyreload` (Aliases: koukenekoreload, Permission: koukeneko.lobby.admin)
  + Reloads the plugin configuration.

## 🔑 Permissions

| Permission | Description |
| --- | --- |
| koukeneko.lobby.admin | Access to administrative commands |
| koukeneko.lobby.bypass | Bypass lobby world restrictions |
| koukeneko.lobby.notify | Receive notifications about lobby events |

## 🌟 簡介

KoukeNeko-Lobby 是一款功能強大的 Minecraft 插件，為您的伺服器提供完整的大廳系統。它能保護您的大廳世界免受玩家互動的影響，提供傳送系統，並通過特殊效果增強玩家體驗。

## 🔧 功能特色

* ✅ 大廳世界保護（方塊破壞、放置、互動）
* ✅ 玩家在大廳中的傷害保護
* ✅ 大廳世界中的爆炸保護
* ✅ 礦車碰撞保護
* ✅ 大廳中的自動玩家狀態效果（再生、速度、飽食度）
* ✅ 簡單的傳送系統，使用 /lobby 指令
* ✅ 使用 /setlobby 輕鬆設定大廳點
* ✅ 通過配置文件完全可自訂

## 📂 安裝方法

1. 將插件 (KoukeNeko-Lobby.jar) 放入伺服器的 plugins 資料夾。
2. 重新啟動伺服器以啟用插件。
3. 插件將會產生預設設定檔。
4. 使用 /setlobby 設定大廳傳送點。

## 🚀 指令

* `/setlobby` (權限: koukeneko.lobby.admin)
  + 設定當前位置為大廳傳送點。

* `/lobby` (別名: hub, spawn)
  + 將玩家傳送到大廳。

* `/knlobbyreload` (別名: koukenekoreload, 權限: koukeneko.lobby.admin)
  + 重新載入插件配置。

## 🔑 許可權

| 權限 | 說明 |
| --- | --- |
| koukeneko.lobby.admin | 存取管理指令的權限 |
| koukeneko.lobby.bypass | 繞過大廳世界限制的權限 |
| koukeneko.lobby.notify | 接收關於大廳事件的通知 |

🚀 Enjoy your game! 祝你遊戲愉快!

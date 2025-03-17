package dev.doeshing.koukeNekoLobby;

import dev.doeshing.koukeNekoLobby.core.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;

/**
 * 大廳系統管理器 - 處理大廳世界的保護和功能
 */
public class LobbyManager implements Listener {

    private final KoukeNekoLobby plugin;
    private final MessageManager messageManager;

    // 大廳相關設定
    private String lobbyWorld;
    private String lobbyPermission;
    private boolean allowBreak;
    private boolean allowPlace;
    private boolean allowUse;
    private boolean allowAttack;
    private boolean allowVehicleCollision;
    private boolean allowExplosion;
    private boolean enableLobbyLocation;
    private Location lobbyLocation;

    // 效果設定
    private boolean effectsEnabled;
    private int effectsInterval;
    private int effectsDuration;
    private boolean effectRegeneration;
    private boolean effectSpeed;
    private boolean effectSaturation;

    /**
     * 建構子
     * @param plugin 插件實例
     */
    public LobbyManager(KoukeNekoLobby plugin) {
        this.plugin = plugin;
        this.messageManager = plugin.getMessageManager();
        loadSettings();
        startEffectsTask();
    }

    /**
     * 重新載入設定
     */
    public void reload() {
        loadSettings();
    }

    /**
     * 從設定檔載入大廳設定
     */
    private void loadSettings() {
        // 基本設定
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("lobby_system");
        
        if (config == null) {
            plugin.getLogger().warning("找不到 lobby_system 設定節點！");
            return;
        }
        
        this.lobbyWorld = config.getString("lobby_world", "world");
        this.lobbyPermission = config.getString("lobby_permission", "koukeneko.lobby.bypass");
        this.allowBreak = config.getBoolean("allow_break", false);
        this.allowPlace = config.getBoolean("allow_place", false);
        this.allowUse = config.getBoolean("allow_use", false);
        this.allowAttack = config.getBoolean("allow_attack", false);
        this.allowVehicleCollision = config.getBoolean("allow_vehicle_collision", false);
        this.allowExplosion = config.getBoolean("allow_explosion", false);
        this.enableLobbyLocation = config.getBoolean("enable_lobby_location", false);

        // 效果設定
        ConfigurationSection effectsConfig = config.getConfigurationSection("effects");
        if (effectsConfig != null) {
            this.effectsEnabled = effectsConfig.getBoolean("enabled", true);
            this.effectsInterval = effectsConfig.getInt("interval", 200);
            this.effectsDuration = effectsConfig.getInt("duration", 220);
            this.effectRegeneration = effectsConfig.getBoolean("regeneration", true);
            this.effectSpeed = effectsConfig.getBoolean("speed", true);
            this.effectSaturation = effectsConfig.getBoolean("saturation", true);
        }

        // 大廳位置
        if (config.contains("lobby_location")) {
            ConfigurationSection locConfig = config.getConfigurationSection("lobby_location");
            if (locConfig != null) {
                String worldName = locConfig.getString("world", "world");
                World world = Bukkit.getWorld(worldName);
                double x = locConfig.getDouble("x", 0.0);
                double y = locConfig.getDouble("y", 64.0);
                double z = locConfig.getDouble("z", 0.0);
                float yaw = (float) locConfig.getDouble("yaw", 0.0);
                float pitch = (float) locConfig.getDouble("pitch", 0.0);
                
                if (world != null) {
                    this.lobbyLocation = new Location(world, x, y, z, yaw, pitch);
                } else {
                    plugin.getLogger().warning("找不到大廳世界: " + worldName);
                }
            }
        }
    }

    /**
     * 開始效果任務
     */
    private void startEffectsTask() {
        if (!effectsEnabled) return;
        
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            World world = Bukkit.getWorld(lobbyWorld);
            if (world == null) return;
            
            for (Player player : world.getPlayers()) {
                if (effectRegeneration) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, effectsDuration, 255, true, false));
                }
                if (effectSpeed) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, effectsDuration, 0, true, false));
                }
                if (effectSaturation) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, effectsDuration, 255, true, false));
                }
            }
        }, 0L, effectsInterval);
    }

    /**
     * 設定大廳位置
     * @param location 新的大廳位置
     */
    public void setLobbyLocation(Location location) {
        this.lobbyLocation = location;
        enableLobbyLocation = true;
        
        // 更新設定檔
        String basePath = "lobby_system.lobby_location";
        plugin.getConfig().set(basePath + ".world", location.getWorld().getName());
        plugin.getConfig().set(basePath + ".x", location.getX());
        plugin.getConfig().set(basePath + ".y", location.getY());
        plugin.getConfig().set(basePath + ".z", location.getZ());
        plugin.getConfig().set(basePath + ".yaw", location.getYaw());
        plugin.getConfig().set(basePath + ".pitch", location.getPitch());
        plugin.getConfig().set("lobby_system.enable_lobby_location", true);
        plugin.saveConfig();
    }

    /**
     * 取得大廳位置
     * @return 大廳位置
     */
    public Location getLobbyLocation() {
        return lobbyLocation;
    }

    /**
     * 檢查大廳位置是否啟用
     * @return 是否啟用
     */
    public boolean isLobbyLocationEnabled() {
        return enableLobbyLocation;
    }

    /* ============ 以下為事件監聽，保護大廳世界 ============ */

    /**
     * 取消大廳世界中玩家受到的傷害事件
     */
    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player victim = (Player) event.getEntity();
        if (victim.getWorld().getName().equalsIgnoreCase(lobbyWorld)) {
            event.setCancelled(true);
        }
    }

    /**
     * 取消大廳世界中玩家破壞方塊的事件 (無權限者)
     */
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (player.getWorld().getName().equalsIgnoreCase(lobbyWorld)) {
            if (!player.hasPermission(lobbyPermission) && !allowBreak) {
                event.setCancelled(true);
            }
        }
    }

    /**
     * 取消大廳世界中玩家放置方塊的事件 (無權限者)
     */
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (player.getWorld().getName().equalsIgnoreCase(lobbyWorld)) {
            if (!player.hasPermission(lobbyPermission) && !allowPlace) {
                event.setCancelled(true);
            }
        }
    }

    /**
     * 取消大廳世界中玩家互動的事件 (無權限者)
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        // 如果是合成台就放行
        if (event.getClickedBlock() != null && event.getClickedBlock().getType().toString().equals("CRAFTING_TABLE")) {
            return;
        }

        if (player.getWorld().getName().equalsIgnoreCase(lobbyWorld)) {
            if (!player.hasPermission(lobbyPermission) && !allowUse) {
                event.setCancelled(true);
            }
        }
    }

    /**
     * 取消大廳世界中玩家攻擊其他實體的事件 (無權限者)
     */
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Player attacker = null;
        
        if (event.getDamager() instanceof Player) {
            attacker = (Player) event.getDamager();
        } else if (event.getDamager() instanceof org.bukkit.entity.Projectile) {
            org.bukkit.entity.Projectile projectile = (org.bukkit.entity.Projectile) event.getDamager();
            ProjectileSource shooter = projectile.getShooter();
            if (shooter instanceof Player) {
                attacker = (Player) shooter;
            }
        }
        
        if (attacker != null && attacker.getWorld().getName().equalsIgnoreCase(lobbyWorld)) {
            if (!attacker.hasPermission(lobbyPermission) && !allowAttack) {
                event.setCancelled(true);
            }
        }
    }

    /**
     * 處理大廳世界中爆炸事件
     */
    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if (event.getLocation().getWorld().getName().equalsIgnoreCase(lobbyWorld)) {
            if (!allowExplosion) {
                event.setCancelled(true);
                // 將詳細事件通知通知送給有權限的人，包含位置訊息
                Location location = event.getLocation();
                messageManager.broadcastMessage(
                        "&c大廳世界中的爆炸事件已被取消，位置: &7" + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ(),
                        "koukeneko.lobby.notify"
                );
            }
        }
    }

    /**
     * 處理礦車與生物碰撞的事件
     */
    @EventHandler(ignoreCancelled = true)
    public void onVehicleEntityCollision(VehicleEntityCollisionEvent event) {
        if (event.getVehicle() instanceof Minecart minecart
                && event.getEntity() instanceof LivingEntity entity
                && minecart.getWorld().getName().equalsIgnoreCase(lobbyWorld)) {

            if (!allowVehicleCollision) {
                final var oldVelocity = minecart.getVelocity().clone();
                event.setCancelled(true);

                // 通知管理員有礦車與生物碰撞包含位置訊息
                Location location = minecart.getLocation();
                messageManager.broadcastMessage(
                        "&c大廳世界中的礦車與生物碰撞事件已被取消，位置: &7" + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ(),
                        "koukeneko.lobby.notify"
                );

                // 下2個 Tick 蓋回速度
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    minecart.setVelocity(oldVelocity);

                    // 也把生物往旁邊推一下，避免一直佔在礦車前面
                    // 根據需求可調整推力大小
                    var pushDirection = minecart.getLocation().toVector().subtract(entity.getLocation().toVector()).normalize();
                    // 讓生物被輕微推開
                    entity.setVelocity(pushDirection.multiply(-0.5D));
                }, 2L);
            }
        }
    }
}

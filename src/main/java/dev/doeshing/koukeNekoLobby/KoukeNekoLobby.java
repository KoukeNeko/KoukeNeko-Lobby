package dev.doeshing.koukeNekoLobby;

import dev.doeshing.koukeNekoLobby.commands.LobbyCommand;
import dev.doeshing.koukeNekoLobby.commands.ReloadCommand;
import dev.doeshing.koukeNekoLobby.commands.SetLobbyCommand;
import dev.doeshing.koukeNekoLobby.core.CommandSystem;
import dev.doeshing.koukeNekoLobby.core.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * KoukeNekoLobby 插件主類
 */
public final class KoukeNekoLobby extends JavaPlugin {

    private MessageManager messageManager;
    private LobbyManager lobbyManager;

    @Override
    public void onEnable() {
        // 保存默認配置文件
        saveDefaultConfig();
        
        // 初始化訊息管理器
        messageManager = new MessageManager(this);
        
        // 初始化大廳管理器
        lobbyManager = new LobbyManager(this);
        
        // 註冊事件監聽器
        Bukkit.getPluginManager().registerEvents(lobbyManager, this);

        // 使用 CommandSystem 註冊指令
        CommandSystem commandSystem = new CommandSystem(this);
        commandSystem.registerCommand("setlobby", new SetLobbyCommand(this), "koukeneko.lobby.admin", "設定大廳位置", "/setlobby", "sl");
        commandSystem.registerCommand("lobby", new LobbyCommand(this), "", "傳送至大廳", "/lobby", "hub");
        commandSystem.registerCommand("koukenekolobbyreload", new ReloadCommand(this), "koukeneko.lobby.admin", "重載插件設定", "/knlobbyreload", "knlobbyreload");
        
        getLogger().info("KoukeNekoLobby 插件已啟用！");
    }

    @Override
    public void onDisable() {
        getLogger().info("KoukeNekoLobby 插件已停用！");
    }
    
    /**
     * 取得訊息管理器
     * @return 訊息管理器
     */
    public MessageManager getMessageManager() {
        return messageManager;
    }
    
    /**
     * 取得大廳管理器
     * @return 大廳管理器
     */
    public LobbyManager getLobbyManager() {
        return lobbyManager;
    }
}

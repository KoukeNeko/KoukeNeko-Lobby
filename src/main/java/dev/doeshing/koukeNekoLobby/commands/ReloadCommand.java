package dev.doeshing.koukeNekoLobby.commands;

import dev.doeshing.koukeNekoLobby.KoukeNekoLobby;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * 重載插件設定指令
 */
public class ReloadCommand implements CommandExecutor {

    private final KoukeNekoLobby plugin;

    public ReloadCommand(KoukeNekoLobby plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // 檢查權限
        if (!sender.hasPermission("koukeneko.lobby.admin")) {
            plugin.getMessageManager().sendConfigMessage(sender, "no_permission");
            return true;
        }
        
        // 重新載入配置文件
        plugin.reloadConfig();
        
        // 重新載入訊息前綴
        plugin.getMessageManager().loadPrefix();
        
        // 重新載入大廳設定
        plugin.getLobbyManager().reload();
        
        // 發送重載完成訊息
        plugin.getMessageManager().sendConfigMessage(sender, "config_reloaded");
        return true;
    }
}

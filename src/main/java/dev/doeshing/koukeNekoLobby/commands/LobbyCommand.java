package dev.doeshing.koukeNekoLobby.commands;

import dev.doeshing.koukeNekoLobby.KoukeNekoLobby;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * 傳送至大廳指令
 */
public class LobbyCommand implements CommandExecutor {
    
    private final KoukeNekoLobby plugin;

    public LobbyCommand(KoukeNekoLobby plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            plugin.getMessageManager().sendConfigMessage(sender, "only_player");
            return true;
        }
        
        Player player = (Player) sender;
        
        // 檢查大廳位置是否啟用
        if (!plugin.getLobbyManager().isLobbyLocationEnabled()) {
            plugin.getMessageManager().sendConfigMessage(player, "lobby_not_enabled");
            return true;
        }
        
        // 檢查大廳位置是否存在
        Location lobbyLocation = plugin.getLobbyManager().getLobbyLocation();
        if (lobbyLocation == null) {
            plugin.getMessageManager().sendConfigMessage(player, "lobby_not_set");
            return true;
        }
        
        // 傳送玩家
        player.teleport(lobbyLocation);
        plugin.getMessageManager().sendConfigMessage(player, "teleport_success");
        return true;
    }
}

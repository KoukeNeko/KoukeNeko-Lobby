package dev.doeshing.koukeNekoLobby.commands;

import dev.doeshing.koukeNekoLobby.KoukeNekoLobby;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * 設定大廳位置指令
 */
public class SetLobbyCommand implements CommandExecutor {
    
    private final KoukeNekoLobby plugin;

    public SetLobbyCommand(KoukeNekoLobby plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            plugin.getMessageManager().sendConfigMessage(sender, "only_player");
            return true;
        }
        
        Player player = (Player) sender;
        if (!player.hasPermission("koukeneko.lobby.admin")) {
            plugin.getMessageManager().sendConfigMessage(player, "no_permission");
            return true;
        }
        
        // 設定大廳位置
        plugin.getLobbyManager().setLobbyLocation(player.getLocation());
        plugin.getMessageManager().sendConfigMessage(player, "lobby_set");
        return true;
    }
}

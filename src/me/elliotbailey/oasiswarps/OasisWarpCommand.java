package me.elliotbailey.oasiswarps;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class OasisWarpCommand implements CommandExecutor {

    private OasisWarps plugin;
    public OasisWarpCommand(OasisWarps plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender p, Command cmd, String label, String[] args) {
        String cmdName = cmd.getName().toLowerCase();

        if (!cmdName.equals("oasiswarps")) return false;

        if (args.length == 0 || !args[0].toLowerCase().equals("reload")) {

            p.sendMessage(Util.format("&3======== &b&lOasisWarps &3========"));
            p.sendMessage(Util.format("&7» &b/oasiswarps help&3: Display this message"));
            p.sendMessage(Util.format("&7» &b/oasiswarps reload&3: Reload the plugin's config file"));
            p.sendMessage(Util.format("&7» &b/warp <warp> [user]&3: Teleport yourself or a user to a warp"));
            p.sendMessage(Util.format("&7» &b/warps [page]&3: View a list of all warps, with an optional page choice"));
            p.sendMessage(Util.format("&7» &b/setwarp <warp>&3: Set a warp"));
            p.sendMessage(Util.format("&7» &b/delwarp <warp>&3: Delete a warp"));

        } else {

            if (p.hasPermission("oasiswarps.reload")) {
                plugin.reloadConfig();
                plugin.saveConfig();

                plugin.getLogger().info(Util.format("Config reloaded"));
                p.sendMessage(Util.format(plugin.getConfig().getString("messages.plugin-reload")));
            } else {
                p.sendMessage(Util.format(plugin.getConfig().getString("messages.no-permission")));
            }

        }

        return true;

    }

}

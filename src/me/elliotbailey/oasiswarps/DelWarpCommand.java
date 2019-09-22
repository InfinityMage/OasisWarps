package me.elliotbailey.oasiswarps;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

public class DelWarpCommand implements CommandExecutor {

    private OasisWarps plugin;
    public DelWarpCommand(OasisWarps plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String cmdName = cmd.getName().toLowerCase();

        if (!cmdName.equals("delwarp")) return false;

        if(!(sender instanceof Player)) {
            sender.sendMessage(Util.format("&cThis command cannot be used in the console!"));
            return true;
        }

        Player p = (Player) sender;

        if (p.hasPermission("oasiswarps.delwarp")) {

            if (args.length != 1) {
                p.sendMessage(Util.format(plugin.getConfig().getString("messages.incorrect-usage").replaceAll("\\{USAGE\\}","/delwarp <warp name>")));
            } else {

                String warpName = args[0].toLowerCase();

                if(warpName.replaceAll("[a-z0-9_]", "").length() > 0) {
                    p.sendMessage(Util.format(plugin.getConfig().getString("messages.illegal-char")));
                    return true;
                }

                if (plugin.getWarps().contains(warpName)) {

                    if (plugin.getWarps().getString(warpName+".owner").equals(p.getUniqueId().toString())) {
                        plugin.getWarps().set(warpName, null);
                        try {
                            plugin.getWarps().save(plugin.warpDataFile);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        p.sendMessage(Util.format(plugin.getConfig().getString("messages.warp-deleted").replaceAll("\\{WARP\\}", warpName)));
                    } else {

                        if (p.hasPermission("oasiswarps.delwarp.others")) {
                            plugin.getWarps().set(warpName, null);
                            try {
                                plugin.getWarps().save(plugin.warpDataFile);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            p.sendMessage(Util.format(plugin.getConfig().getString("messages.warp-deleted").replaceAll("\\{WARP\\}", warpName)));
                        } else {
                            p.sendMessage(Util.format(plugin.getConfig().getString("messages.no-permission-action").replaceAll("\\{ACTION\\}", "delete other player's warps")));
                        }

                    }

                }

            }

        } else {
            p.sendMessage(Util.format(plugin.getConfig().getString("messages.no-permission")));
        }

        return true;

    }

}

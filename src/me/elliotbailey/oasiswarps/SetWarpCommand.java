package me.elliotbailey.oasiswarps;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

public class SetWarpCommand implements CommandExecutor {

    private OasisWarps plugin;
    public SetWarpCommand(OasisWarps plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String cmdName = cmd.getName().toLowerCase();

        if(!cmdName.equals("setwarp")) return false;

        if(!(sender instanceof Player)) {
            sender.sendMessage(Util.format("&cThis command cannot be used in the console!"));
            return true;
        }

        Player p = (Player) sender;

        if (p.hasPermission("oasiswarps.setwarp")) {

            if(args.length != 1) {
                p.sendMessage(Util.format(plugin.getConfig().getString("messages.incorrect-usage").replaceAll("\\{USAGE\\}","/setwarp <warp name>")));
            } else {

                String warpName = args[0].toLowerCase();

                if(warpName.replaceAll("[a-z0-9_]", "").length() > 0) {
                    p.sendMessage(Util.format(plugin.getConfig().getString("messages.illegal-char")));
                    return true;
                }

                if (warpName.length() > plugin.getConfig().getInt("max-length")) {
                    p.sendMessage(Util.format(plugin.getConfig().getString("messages.long-name").replaceAll("\\{MAX\\}", String.valueOf(plugin.getConfig().getInt("max-length")))));
                    return true;
                }
                if (warpName.length() < plugin.getConfig().getInt("min-length")) {
                    p.sendMessage(Util.format(plugin.getConfig().getString("messages.short-name").replaceAll("\\{MIN\\}", String.valueOf(plugin.getConfig().getInt("min-length")))));
                    return true;
                }

                if (!plugin.getWarps().contains(warpName)) {
                    plugin.getWarps().set(warpName+".owner", p.getUniqueId().toString());
                    plugin.getWarps().set(warpName+".world", p.getWorld().getName());
                    plugin.getWarps().set(warpName+".x", p.getLocation().getBlockX()+0.5);
                    plugin.getWarps().set(warpName+".y", p.getLocation().getBlockY());
                    plugin.getWarps().set(warpName+".z", p.getLocation().getBlockZ()+0.5);
                    plugin.getWarps().set(warpName+".yaw", p.getLocation().getYaw());
                    plugin.getWarps().set(warpName+".pitch", p.getLocation().getPitch());
                    p.sendMessage(Util.format(plugin.getConfig().getString("messages.warp-created").replaceAll("\\{WARP\\}", warpName)));
                    try {
                        plugin.getWarps().save(plugin.warpDataFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    p.sendMessage(Util.format(plugin.getConfig().getString("messages.already-exists")));
                }

            }

        } else {
            p.sendMessage(Util.format(plugin.getConfig().getString("messages.no-permission")));
        }

        return true;
    }

}

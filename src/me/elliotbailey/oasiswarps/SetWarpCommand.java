package me.elliotbailey.oasiswarps;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;

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

                // Check for illegal characters in warp name
                // Only accepted characters are alphanumeric and underscore
                if(warpName.replaceAll("[a-z0-9_]", "").length() > 0) {
                    p.sendMessage(Util.format(plugin.getConfig().getString("messages.illegal-char")));
                    return true;
                }

                // Check for maximum warp name length
                if (warpName.length() > plugin.getConfig().getInt("max-length")) {
                    p.sendMessage(Util.format(plugin.getConfig().getString("messages.long-name").replaceAll("\\{MAX\\}", String.valueOf(plugin.getConfig().getInt("max-length")))));
                    return true;
                }

                // Check for minimum warp name length
                if (warpName.length() < plugin.getConfig().getInt("min-length")) {
                    p.sendMessage(Util.format(plugin.getConfig().getString("messages.short-name").replaceAll("\\{MIN\\}", String.valueOf(plugin.getConfig().getInt("min-length")))));
                    return true;
                }

                // Add each permission limit from config file to an array
                ArrayList<String> permissionLimits = new ArrayList<String>();
                for (String key : plugin.getConfig().getKeys(true)) {
                    String[] key_yaml_parts = key.split("\\.");
                    if (key_yaml_parts[0].equals("setwarp-limits") && key_yaml_parts.length == 2) {
                        permissionLimits.add(key_yaml_parts[1]);
                    }
                }

                // Create array of all of the player's warps
                ArrayList<String> playerWarps = new ArrayList<String>();
                for (String warp : plugin.getWarps().getKeys(true)) {
                    String[] warp_yaml_parts = warp.split("\\.");
                    if (warp_yaml_parts.length == 2) {
                        if (warp_yaml_parts[1].equals("owner")) {
                            if (plugin.getWarps().getString(warp_yaml_parts[0]+"."+warp_yaml_parts[1]).equals(p.getUniqueId().toString()))
                            playerWarps.add(warp_yaml_parts[0]);
                        }
                    }

                }

                // Find the maximum number of warps the player is allowed to have
                Integer playerMaxWarps = 0;

                for (String perm : permissionLimits) {
                    if (p.hasPermission("oasiswarps.limit."+perm)) {
                        if (plugin.getConfig().getInt("setwarp-limits." + perm) > playerMaxWarps) playerMaxWarps = plugin.getConfig().getInt("setwarp-limits." + perm);
                    }
                }

                if (!(playerWarps.size() >= playerMaxWarps) || p.hasPermission("oasiswarps.limit.bypass")) {

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

                } else {
                    p.sendMessage(Util.format(plugin.getConfig().getString("messages.limit-reached")));
                }

            }

        } else {
            p.sendMessage(Util.format(plugin.getConfig().getString("messages.no-permission")));
        }

        return true;
    }

}

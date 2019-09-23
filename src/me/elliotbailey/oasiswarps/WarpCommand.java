package me.elliotbailey.oasiswarps;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

public class WarpCommand implements CommandExecutor {

    private OasisWarps plugin;
    public WarpCommand(OasisWarps plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String cmdName = cmd.getName().toLowerCase();

        if (!cmdName.equals("warp")) return false;

        if (!(sender instanceof Player)) {
            sender.sendMessage(Util.format("&cThis command cannot be used in the console!"));
            return true;
        }

        Player p = (Player) sender;

        if (p.hasPermission("oasiswarps.warp")) {

            if(args.length != 1 && args.length != 2) {
                p.sendMessage(Util.format(plugin.getConfig().getString("messages.incorrect-usage").replaceAll("\\{USAGE\\}","/warp <warp name> [player]")));
            } else {
                String warpName = args[0].toLowerCase();

                // Normal warp
                if(args.length == 1) {
                    if (plugin.getWarps().contains(warpName)) {

                        // Warp safety check
                        if (plugin.getConfig().getBoolean("safe-warp")) {

                            Boolean warpSafe = true;

                            Location warpLocation = new Location(
                                    plugin.getServer().getWorld(plugin.getWarps().getString(warpName+".world")),
                                    plugin.getWarps().getDouble(warpName+".x"),
                                    Math.ceil(plugin.getWarps().getDouble(warpName+".y")),
                                    plugin.getWarps().getDouble(warpName+".z")
                            );

                            // Check for 2 air/unsolid blocks at warp with a solid block below
                            if (warpLocation.add(0, 1, 0).getBlock().getType().isSolid() || warpLocation.getBlock().getType().toString() == "WATER") warpSafe = false;
                            warpLocation.subtract(0, 1, 0);
                            if (warpLocation.getBlock().getType().isSolid()) warpSafe = false;
                            if (!warpLocation.subtract(0, 1, 0).getBlock().getType().isSolid() && !warpLocation.getBlock().getType().toString().contains("CARPET") && !warpLocation.subtract(0, 1, 0).getBlock().getType().isSolid() && !warpLocation.subtract(0, 1, 0).getBlock().getType().isSolid()) warpSafe = false;
                            warpLocation.add(0, 3, 0);

                            String[] dangerous_blocks = {"LAVA","CACTUS","SWEET_BERRY_BUSH","WITHER_ROSE","MAGMA_BLOCK","COBWEB","CAMPFIRE","FIRE"};

                            for (Double[] loc : Util.safeWarpLocations()) {
                                for (String block : dangerous_blocks) {
                                    if (warpLocation.add(loc[0], loc[1], loc[2]).getBlock().getType().toString().equals(block)) {
                                        warpSafe = false;
                                    }
                                    warpLocation.subtract(loc[0], loc[1], loc[2]);
                                }
                            }

                            if (!warpSafe) {
                                p.sendMessage(Util.format(plugin.getConfig().getString("messages.unsafe")));
                                return true;
                            }

                        }

                        p.sendMessage(Util.format(plugin.getConfig().getString("messages.warp-tp").replaceAll("\\{WARP\\}", warpName)));

                        p.teleport(new Location(
                                plugin.getServer().getWorld(plugin.getWarps().getString(warpName+".world")),
                                plugin.getWarps().getDouble(warpName+".x"),
                                plugin.getWarps().getDouble(warpName+".y"),
                                plugin.getWarps().getDouble(warpName+".z"),
                                plugin.getWarps().getInt(warpName+".yaw"),
                                plugin.getWarps().getInt(warpName+".pitch")
                        ), PlayerTeleportEvent.TeleportCause.PLUGIN);

                    } else {
                        p.sendMessage(Util.format(plugin.getConfig().getString("messages.no-exist")));
                    }
                }

                // Warping a specific person
                if(args.length == 2) {

                    if(p.hasPermission("oasiswarps.warp.others")) {

                        Player otherPlayer = p.getServer().getPlayer(args[1]);
                        if (otherPlayer != null) {

                            p.sendMessage(Util.format(plugin.getConfig().getString("messages.warp-tp-other").replaceAll("\\{WARP\\}", warpName).replaceAll("\\{USER\\}", otherPlayer.getDisplayName())));
                            otherPlayer.sendMessage(Util.format(plugin.getConfig().getString("messages.warp-tp").replaceAll("\\{WARP\\}", warpName)));

                            otherPlayer.teleport(new Location(
                                    plugin.getServer().getWorld(plugin.getWarps().getString(warpName+".world")),
                                    plugin.getWarps().getDouble(warpName+".x"),
                                    plugin.getWarps().getDouble(warpName+".y"),
                                    plugin.getWarps().getDouble(warpName+".z"),
                                    plugin.getWarps().getInt(warpName+".yaw"),
                                    plugin.getWarps().getInt(warpName+".pitch")
                            ), PlayerTeleportEvent.TeleportCause.PLUGIN);

                        } else {
                            p.sendMessage(Util.format(plugin.getConfig().getString("messages.not-online")));
                        }

                    } else {
                        p.sendMessage(Util.format(plugin.getConfig().getString("messages.no-permission-action").replaceAll("\\{ACTION\\}", "warp other players")));
                    }

                }

            }

        } else {
            p.sendMessage(Util.format(plugin.getConfig().getString("messages.no-permission")));
        }

        return true;

    }

}

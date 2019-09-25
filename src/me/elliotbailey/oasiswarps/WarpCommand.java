package me.elliotbailey.oasiswarps;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class WarpCommand implements CommandExecutor, Listener {

    private OasisWarps plugin;
    public WarpCommand(OasisWarps plugin) {
        this.plugin = plugin;
    }

    private Boolean isLocationSafe(Location location) {

        Boolean warpSafe = true;

        // Check for 2 air/unsolid blocks at warp with a solid block below
        if (location.add(0, 1, 0).getBlock().getType().isSolid() || location.getBlock().getType().toString() == "WATER") warpSafe = false;
        location.subtract(0, 1, 0);
        if (location.getBlock().getType().isSolid()) warpSafe = false;
        if (!location.subtract(0, 1, 0).getBlock().getType().isSolid() && !location.getBlock().getType().toString().contains("CARPET") && !location.subtract(0, 1, 0).getBlock().getType().isSolid() && !location.subtract(0, 1, 0).getBlock().getType().isSolid()) warpSafe = false;
        location.add(0, 3, 0);

        String[] dangerous_blocks = {"LAVA","CACTUS","SWEET_BERRY_BUSH","WITHER_ROSE","MAGMA_BLOCK","COBWEB","CAMPFIRE","FIRE"};

        for (Double[] loc : Util.safeLocations()) {
            for (String block : dangerous_blocks) {
                if (location.add(loc[0], loc[1], loc[2]).getBlock().getType().toString().equals(block)) {
                    warpSafe = false;
                }
                location.subtract(loc[0], loc[1], loc[2]);
            }
        }

        if (warpSafe) return true;
        else return false;

    }

    private void tpToWarp(Player p, String warpName) {
        p.teleport(new Location(
                plugin.getServer().getWorld(plugin.getWarps().getString(warpName+".world")),
                plugin.getWarps().getDouble(warpName+".x"),
                plugin.getWarps().getDouble(warpName+".y"),
                plugin.getWarps().getDouble(warpName+".z"),
                plugin.getWarps().getInt(warpName+".yaw"),
                plugin.getWarps().getInt(warpName+".pitch")
        ), PlayerTeleportEvent.TeleportCause.PLUGIN);

        p.sendMessage(Util.format(plugin.getConfig().getString("messages.warp-tp").replaceAll("\\{WARP\\}", warpName)));

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

                        int timeLeft = TimeManager.getCooldown(p.getUniqueId());
                        if (timeLeft == 0 || p.hasPermission("oasiswarps.bypass.cooldown")) {

                            if (plugin.getConfig().getBoolean("safe-warp")) {

                                Location warpLocation = new Location(
                                        plugin.getServer().getWorld(plugin.getWarps().getString(warpName+".world")),
                                        plugin.getWarps().getDouble(warpName+".x"),
                                        Math.ceil(plugin.getWarps().getDouble(warpName+".y")),
                                        plugin.getWarps().getDouble(warpName+".z")
                                );

                                if (!isLocationSafe(warpLocation)) {
                                    p.sendMessage(Util.format(plugin.getConfig().getString("messages.warp-unsafe")));
                                    return true;
                                }

                            }

                            if (!(p.hasPermission("oasiswarps.bypass.delay")) && plugin.getConfig().getBoolean("warp-delay")) {

                                p.sendMessage(Util.format(plugin.getConfig().getString("messages.warp-tp-delay").replaceAll("\\{DELAY\\}", String.valueOf(plugin.getConfig().getInt("warp-delay")))));

                                TimeManager.addTeleporting(p.getUniqueId());

                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        if (TimeManager.isTeleporting(p.getUniqueId())) {
                                            tpToWarp(p, warpName);
                                            TimeManager.removeTeleporting(p.getUniqueId());
                                        }
                                    }
                                }.runTaskLater(plugin, plugin.getConfig().getInt("warp-delay-time")*20);

                            } else {
                                tpToWarp(p, warpName);
                            }

                            TimeManager.setCooldown(p.getUniqueId(), plugin.getConfig().getInt("warp-cooldown"));
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    int timeLeft = TimeManager.getCooldown(p.getUniqueId());
                                    TimeManager.setCooldown(p.getUniqueId(), --timeLeft);
                                    if(timeLeft == 0){
                                        this.cancel();
                                    }
                                }
                            }.runTaskTimer(this.plugin, 20, 20);
                        } else {
                            p.sendMessage(Util.format(plugin.getConfig().getString("messages.warp-cooldown").replaceAll("\\{COOLDOWN\\}", String.valueOf(timeLeft))));
                        }

                    } else {
                        p.sendMessage(Util.format(plugin.getConfig().getString("messages.warp-no-exist")));
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

    @EventHandler
    public void move(PlayerMoveEvent e) {

        if (e.getTo().getBlockX() != e.getFrom().getBlockX() || e.getTo().getBlockY() != e.getFrom().getBlockZ() || e.getTo().getBlockZ() != e.getFrom().getBlockZ()) {
            if (TimeManager.isTeleporting(e.getPlayer().getUniqueId())) {
                TimeManager.removeTeleporting(e.getPlayer().getUniqueId());
                e.getPlayer().sendMessage(Util.format(plugin.getConfig().getString("messages.warp-cancelled")));
            }

        }

    }

}
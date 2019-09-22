package me.elliotbailey.oasiswarps;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collections;

public class ListWarpsCommand implements CommandExecutor {

    private OasisWarps plugin;
    public ListWarpsCommand(OasisWarps plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender p, Command cmd, String label, String[] args) {
        String cmdName = cmd.getName().toLowerCase();

        if (!cmdName.equals("warps")) return false;

        if (p.hasPermission("oasiswarps.listwarps")) {

            ArrayList<String> warpList = new ArrayList<String>();

            for (String warp : plugin.getWarps().getKeys(true)) {
                if(!warp.contains(".")) warpList.add(warp);
            }

            String warp_list = "&bWarps: &f";

            Collections.sort(warpList);

            for (String x : warpList) warp_list += x + ", ";

            p.sendMessage(Util.format(warp_list.substring(0, warp_list.length() - 2)));

            return true;

        } else {
            p.sendMessage(Util.format(plugin.getConfig().getString("messages.no-permission")));
        }

        return true;

    }

}

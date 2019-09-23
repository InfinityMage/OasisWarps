package me.elliotbailey.oasiswarps;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;

public class ListWarpsCommand implements CommandExecutor {

    private OasisWarps plugin;
    public ListWarpsCommand(OasisWarps plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String cmdName = cmd.getName().toLowerCase();

        if (!cmdName.equals("warps")) return false;

        if (!(sender instanceof Player)) {
            sender.sendMessage(Util.format("&cThis command cannot be used in the console!"));
            return true;
        }

        Player p = (Player) sender;

        if (p.hasPermission("oasiswarps.listwarps")) {

            ArrayList<String> warpList = new ArrayList<String>();

            for (String warp : plugin.getWarps().getKeys(true)) {
                if(!warp.contains(".")) warpList.add(warp);
            }

            String warp_list = "";

            Collections.sort(warpList);

            ArrayList<String> pages = new ArrayList<String>();

            Integer page_counter = 0;

            for (int i = 0; i < warpList.size(); i++) {
                if (plugin.getWarps().getString(warpList.get(i) +".owner").equals(p.getUniqueId().toString())) {
                    warp_list += plugin.getConfig().getString("messages.warp-owned-color")+ warpList.get(i) + plugin.getConfig().getString("messages.warp-color")+", ";
                } else {
                    warp_list += warpList.get(i) + plugin.getConfig().getString("messages.warp-color")+", ";
                }
                page_counter += 1;
                if (page_counter == plugin.getConfig().getInt("warp-list-amount")) {
                    pages.add(warp_list);
                    warp_list = "";
                }
            }

            if (!warp_list.equals("")) pages.add(warp_list);

            if (warpList.size() == 0) p.sendMessage(Util.format(plugin.getConfig().getString("messages.warp-list-none")));
            else {
                Integer page_choice = 1;
                if (args.length == 0) page_choice = 1;
                else {
                    try {
                        page_choice = Integer.parseInt(args[0]);
                    } catch (NumberFormatException | NullPointerException nfe) {
                        p.sendMessage(Util.format(plugin.getConfig().getString("messages.page-not-number")));
                    }
                }

                if (!(page_choice > pages.size())) {
                    p.sendMessage(Util.format(plugin.getConfig().getString("messages.page-info").replaceAll("\\{PAGE\\}", String.valueOf(page_choice)).replaceAll("\\{MAX_PAGE\\}", String.valueOf(pages.size())).replaceAll("\\{WARPS\\}", String.valueOf(warpList.size()))));
                    p.sendMessage(Util.format(pages.get(page_choice-1).substring(0, pages.get(page_choice-1).length() - 2)));
                } else {
                    p.sendMessage(Util.format(plugin.getConfig().getString("messages.page-no-exist")));
                }

            }

            return true;

        } else {
            p.sendMessage(Util.format(plugin.getConfig().getString("messages.no-permission")));
        }

        return true;

    }

}

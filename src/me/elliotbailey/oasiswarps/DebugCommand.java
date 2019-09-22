package me.elliotbailey.oasiswarps;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DebugCommand implements CommandExecutor {

    private OasisWarps plugin;
    public DebugCommand(OasisWarps plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String cmdName = cmd.getName().toLowerCase();

        if (!cmdName.equals("debug")) return false;

        if (!(sender instanceof Player)) {
            sender.sendMessage(Util.format("&cThis command cannot be used in the console!"));
            return true;
        }

        Player p = (Player) sender;

        p.sendMessage(p.getLocation().add(0,1,0).getBlock().getType().toString() + p.getLocation().add(0,1,0).getBlock().getType().isSolid());
        p.sendMessage(p.getLocation().getBlock().getType().toString() + p.getLocation().getBlock().getType().isSolid());
        p.sendMessage(p.getLocation().subtract(0, 1, 0).getBlock().getType().toString() + p.getLocation().subtract(0,1,0).getBlock().getType().isSolid());

        return true;

    }
}

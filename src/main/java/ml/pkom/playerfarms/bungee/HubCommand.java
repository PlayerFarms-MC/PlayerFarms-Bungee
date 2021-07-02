package ml.pkom.playerfarms.bungee;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class HubCommand extends Command{

    public HubCommand(){
        super("hub", "playerfarms.command.hub");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        ProxiedPlayer player = (ProxiedPlayer) sender;
        if (player.getServer().getInfo().getName().equalsIgnoreCase("hub")) {
            player.sendMessage(
                    new ComponentBuilder("既にあなたはロビーに接続されています。").color(ChatColor.RED).create());
            return;
        }
        player.connect(ProxyServer.getInstance().getServerInfo("hub"));
    }
}

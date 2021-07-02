package ml.pkom.playerfarms.bungee;

import java.io.IOException;
import java.io.InputStream;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.UUID;
import java.util.regex.Pattern;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Plugin;

public class PlayerFarms extends Plugin {
    public static final String MOD_ID = "playerfarms-bungee"; // MODID
    public static final String MOD_NAME = "PlayerFarms-Bungee"; // MOD名
    public static final String MOD_VER = "1.0.0"; // MODバージョン
    public static final String MOD_AUT = "Pitan"; // MOD開発者

    private static PlayerFarms playerFarms;
    private static int port = 25564;

    @Override
    public void onEnable() {
        getProxy().getPluginManager().registerCommand(this, new HubCommand());
        playerFarms = this;
        socketServer();
        getLogger().info("PlayerFarms-Bungee is enabled!");
        
    }

    public void socketServer(){
        new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(port);
                Socket socket = serverSocket.accept();
                byte[] data = new byte[4096];
                InputStream in = socket.getInputStream();
                int readSize = in.read(data);

                data = Arrays.copyOf(data, readSize);
                String dataStr = new String(data, "UTF-8");
                in.close();
                serverSocket.close();
                socketProc(dataStr);
            } catch (BindException e) {
                socketServer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void socketProc(String string) {
        // getProxy().getLogger().info(string);
        socketServer();
        String[] procs = string.split(Pattern.quote("-|-"));
        if(procs.length < 1){
            String[] fixProcs = {string};
            procs = fixProcs;
        }
        for (String proc : procs) {
            String args[] = proc.split(",");
            // getProxy().getLogger().info(proc);
            // connect,[playeruuid],[server名]
            if (args[0].equalsIgnoreCase("connect")) {
                getProxy().getPlayer(UUID.fromString(args[1]))
                        .connect(ProxyServer.getInstance().getServerInfo(args[2]));
            }
            // broadcast,[メッセージ]
            if (args[0].equalsIgnoreCase("broadcast")) {
                getProxy().broadcast(new TextComponent(args[1]));
            }
            // send,[playeruuid],[メッセージ]
            if (args[0].equalsIgnoreCase("send")) {
                getProxy().getPlayer(UUID.fromString(args[1])).sendMessage(new TextComponent(args[2]));
            }
            // addServer,[サーバー名(Player名)],[ポート],[motd]
            if (args[0].equalsIgnoreCase("addServer")) {
                String motd = "";
                if (args.length >= 4) {
                    motd = args[3];
                }else {
                    motd = args[1];
                }
                getProxy().getLogger().info("Starting Server \"" + args[1] + "\" (port:" + args[2] + ",motd:" + motd + ")");
                getProxy().getServers().put(args[1], getProxy().constructServerInfo(args[1],
                        InetSocketAddress.createUnresolved("localhost", Integer.parseInt(args[2])), motd, false));
            }
            if (args[0].equalsIgnoreCase("removeServer")) {
                getProxy().getServers().remove(args[1]);
            }
        }
    }

    public static PlayerFarms getInstance() {
        return playerFarms;
    }

}
package com.github.mrgeotech;

import net.minestom.server.MinecraftServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ConfigHandler {

    private static Map<InetSocketAddress, Integer> ips;
    private static String key;

    public static void init() throws IOException {
        File file = new File(Paths.get("").toFile(), "vanilla-ips.txt");
        if (file.createNewFile()) {
            System.out.println("No vanilla ips found! File has been created! Please add ips to vanilla servers that you have running with the connector plugin!");
            MinecraftServer.stopCleanly();
            return;
        }
        ips = new HashMap<>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            key = reader.readLine();
            while (reader.ready()) {
                String[] ip = reader.readLine().split(":");
                ips.put(new InetSocketAddress(ip[0], Integer.parseInt(ip[1])), 0);
            }
            if (ips.size() < 1) throw new NullPointerException();
        } catch (Exception e) {
            System.err.println("An error occurred reading your vanilla ips! Please check that there is at least one ip and that they follow the format of '<address>:<port>'!");
            MinecraftServer.stopCleanly();
        }
    }

    public static InetSocketAddress getIP() {
        InetSocketAddress lowest = ips.keySet().iterator().next();
        for (InetSocketAddress address : ips.keySet()) {
            if (ips.get(lowest) > ips.get(address))
                lowest = address;
        }
        return lowest;
    }

    public static void removeTask(InetSocketAddress address) {
        ips.put(address, ips.get(address) - 1);
    }

    public static String getKey() {
        return key;
    }

}

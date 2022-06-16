package com.github.mrgeotech;

import net.minestom.server.MinecraftServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ConfigUtils {

    private static Map<InetSocketAddress, Integer> ips;
    private static String key;

    public static boolean init() {
        try {
            File file = new File(System.getProperty("user.dir"), "vanilla-ips.txt");
            if (file.createNewFile()) {
                System.out.println("No vanilla ips found! File has been created! Please add ips to vanilla servers that you have running with the connector plugin!");
                MinecraftServer.stopCleanly();
                return true;
            }
            ips = new HashMap<>();
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                key = reader.readLine();
                while (reader.ready()) {
                    String[] ip = reader.readLine().split(":");
                    ips.put(new InetSocketAddress(ip[0], Integer.parseInt(ip[1])), 0);
                }
                System.out.println("Found ips: " + Arrays.toString(ips.keySet().toArray(new InetSocketAddress[0])));
                if (ips.size() < 1)
                    return false;
            } catch (Exception e) {
                System.err.println("An error occurred reading your vanilla ips! Please check that there is at least one ip and that they follow the format of '<address>:<port>'!");
                MinecraftServer.stopCleanly();
            }
            for (Map.Entry<InetSocketAddress, Integer> entry : ips.entrySet()) {
                if (!testIP(entry.getKey())) {
                    System.err.println("IP " + entry.getKey().toString() + " either is unable to be connected to or your key is configured incorrectly!");
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static InetSocketAddress getIP() {
        InetSocketAddress lowest = ips.keySet().iterator().next();
        for (InetSocketAddress address : ips.keySet()) {
            if (ips.get(lowest) > ips.get(address))
                lowest = address;
        }
        ips.put(lowest, ips.get(lowest) + 1);
        return lowest;
    }

    public static void removeTask(InetSocketAddress address) {
        ips.put(address, ips.get(address) - 1);
    }

    public static boolean testIP(InetSocketAddress address) {
        try {
            SocketChannel channel = SocketChannel.open(address);

            byte[] bytes = key.getBytes(StandardCharsets.UTF_8);
            ByteBuffer buffer = ByteBuffer.allocate(bytes.length + 5);
            buffer.put((byte) 0x03); // 1 byte
            buffer.putInt(bytes.length); // 4 bytes
            buffer.put(bytes); // bytes.length bytes
            buffer.flip();
            channel.write(buffer);

            buffer.clear();

            buffer = ByteBuffer.allocate(1);
            channel.read(buffer);
            buffer.position(0);

            return buffer.get() == (byte) 0x04;
        } catch (Exception e) {
            return false;
        }
    }

}

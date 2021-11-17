package com.github.mrgeotech;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;

public class ChannelManager implements Runnable {

    private Selector selector;
    private ServerSocketChannel server;
    private HashMap<SocketAddress, String[]> requests;
    private SpigotLink main;

    public ChannelManager(SpigotLink main) throws IOException {
        this.main = main;
        requests = new HashMap<>();

        selector = Selector.open();

        server = ServerSocketChannel.open();
        server.bind(new InetSocketAddress(20000));
        server.configureBlocking(false);

        int ops = server.validOps();
        server.register(selector, ops);
    }
    
    @Override
    public void run() {
        try {
            selector.select(10);
            Iterator<SelectionKey> i = selector.selectedKeys().iterator();

            while (i.hasNext()) {
                System.out.println("Next");
                SelectionKey key = i.next();

                if (key.isAcceptable()) {
                    System.out.println("Client accepted!");
                    SocketChannel client = server.accept();
                    client.configureBlocking(false);
                    client.register(selector, SelectionKey.OP_READ);
                } else if (key.isReadable()) {
                    System.out.println("Reading from client!");
                    read(key);
                } else if (key.isWritable()) {
                    System.out.println("Writing to client!");
                    write(key);
                }

                i.remove();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        try {
            server.close();
            selector.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void read(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();

        ByteBuffer buffer = ByteBuffer.allocate(2048);
        channel.read(buffer);

        String[] request = new String(buffer.array()).trim().split(",");

        requests.put(channel.getRemoteAddress(), request);

        channel.register(selector, SelectionKey.OP_WRITE);
    }

    private void write(SelectionKey key) throws IOException {
        System.out.println("Writing");
        SocketChannel channel = (SocketChannel) key.channel();

        String[] request = requests.get(channel.getRemoteAddress());

        channel.register(selector, SelectionKey.OP_CONNECT);

        if (request[0].equalsIgnoreCase("c")) {
            System.out.println("c");
            Chunk chunk = loadChunks(Integer.parseInt(request[1]), Integer.parseInt(request[2]));

            System.out.println("Async");
            Bukkit.getScheduler().runTaskAsynchronously(this.main, () -> {
                StringBuilder output = new StringBuilder();

                while (!chunk.isLoaded()) {}

                for (int x = 0; x < 16; x++) {
                    for (int y = 0; y < 256; y++) {
                        for (int z = 0; z < 16; z++) {
                            output.append(x).append(";").append(y).append(";").append(z).append(";").append(chunk.getBlock(x, y, z).getType().getKey().getKey()).append(";");
                        }
                    }
                }

                output.deleteCharAt(output.length() - 1);

                byte[] bytes = output.toString().getBytes(StandardCharsets.UTF_8);
                System.out.println(bytes.length);
                ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
                buffer.put(bytes);
                buffer.flip();
                try {
                    channel.write(buffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } else if (request[0].equalsIgnoreCase("b")) {
            Chunk chunk = loadChunks(Integer.parseInt(request[1]), Integer.parseInt(request[2]));

            Bukkit.getScheduler().runTaskAsynchronously(this.main, () -> {
                StringBuilder output = new StringBuilder();

                for (int x = 0; x < 16; x++) {
                    for (int z = 0; z < 16; z++) {
                        output.append(chunk.getWorld().getBiome(x, 65, z).ordinal());
                    }
                }

                ByteBuffer buffer = ByteBuffer.allocate(2048);
                byte[] bytes = output.toString().getBytes(StandardCharsets.UTF_8);
                for (int i = 0; i < bytes.length; i += 2048) {
                    System.out.println(i);
                    buffer.put(buffer);
                    buffer.flip();
                    try {
                        channel.write(buffer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private Chunk loadChunks(int x, int z) {
        Chunk chunk;
        for (int offX = -1; offX <= 1; offX++) {
            for (int offZ = -1; offZ <= 1; offZ++) {
                chunk = Bukkit.getWorld("world").getChunkAt(x - offX, z - offZ);
                chunk.load();
                Bukkit.getScheduler().runTaskLater(this.main, (Runnable) chunk::unload, 1000);
            }
        }
        return Bukkit.getWorld("world").getChunkAt(x, z);
    }
}

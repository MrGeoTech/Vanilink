package com.github.mrgeotech;

import com.github.luben.zstd.Zstd;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Material;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ChannelManager implements Runnable {

    private final Selector selector;
    private final ServerSocketChannel server;
    private final HashMap<SocketAddress, Integer[]> requests;
    private final SpigotLink main;

    public ChannelManager(SpigotLink main) throws IOException {
        this.main = main;
        requests = new HashMap<>();

        selector = Selector.open();

        server = ServerSocketChannel.open();
        server.bind(new InetSocketAddress(main.getPort()));
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

        ByteBuffer buffer = ByteBuffer.allocate(9);
        channel.read(buffer);

        buffer.position(0);

        byte packetId = buffer.get();
        if (packetId != 0x01) return;

        int chunkX = buffer.getInt();
        int chunkZ = buffer.getInt();

        while (buffer.hasRemaining())
            buffer.get();

        requests.put(channel.getRemoteAddress(), new Integer[] {chunkX, chunkZ});

        channel.register(selector, SelectionKey.OP_WRITE);
    }

    private void write(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();

        Integer[] request = requests.get(channel.getRemoteAddress());
        int chunkX = request[0];
        int chunkZ = request[1];

        channel.register(selector, SelectionKey.OP_CONNECT);

        Chunk chunk = loadChunks(chunkX, chunkZ);

        Bukkit.getScheduler().runTaskAsynchronously(this.main, () -> {
            // Waiting till the chunk is loaded
            while (!chunk.isLoaded()) {}
            ChunkSnapshot snapshot = chunk.getChunkSnapshot();

            try {
                // Creating output streams for easier manipulation
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                DataOutputStream outputStream = new DataOutputStream(byteArrayOutputStream);

                List<String> chunkBlockData = new ArrayList<>();
                Material prevBlock = Material.AIR;

                for (int x = 0; x < 16; x++) {
                    for (int z = 0; z < 16; z++) {
                        for (int y = -64; y < 320; y++) {
                            Material type = snapshot.getBlockType(x, y, z);
                            if (type.equals(prevBlock))
                                chunkBlockData.add("*");
                            else
                                chunkBlockData.add((prevBlock = type).toString().toLowerCase());
                        }
                    }
                }

                // Adding data to be sent to stream
                for (String name : chunkBlockData) {
                    if (name.equals("*"))
                        outputStream.writeInt(0);
                    else {
                        byte[] bytes = name.getBytes(StandardCharsets.UTF_8);
                        outputStream.writeInt(bytes.length);
                        outputStream.write(bytes);
                    }
                }

                // Compressing the data
                byte[] rawData = byteArrayOutputStream.toByteArray();
                byte[] rawDataCompressed = Zstd.compress(rawData);

                // Putting everything into the buffer
                ByteBuffer buffer = ByteBuffer.allocate(rawDataCompressed.length + 9);
                buffer.put((byte) 0x02); // Packet ID
                buffer.putInt(rawDataCompressed.length);
                buffer.putInt(rawData.length);
                buffer.put(rawDataCompressed);
                buffer.flip();

                // Dumping the buffer into the socket channel
                while (buffer.hasRemaining())
                    channel.write(buffer);
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private Chunk loadChunks(int x, int z) {
        Chunk chunk = Objects.requireNonNull(Bukkit.getWorld("world")).getChunkAt(x, z);
        chunk.load();
        Bukkit.getScheduler().runTaskLater(this.main, (Runnable) chunk::unload, 1000);
        return chunk;
    }
}

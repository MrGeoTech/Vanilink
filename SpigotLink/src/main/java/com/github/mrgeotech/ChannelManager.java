package com.github.mrgeotech;

import com.github.luben.zstd.Zstd;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;

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

    private Selector selector;
    private ServerSocketChannel server;
    private HashMap<SocketAddress, Integer[]> requests;
    private SpigotLink main;

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

        byte packetId = buffer.get();
        int chunkX = buffer.getInt();
        int chunkZ = buffer.getInt();

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

                // Writing chunk sections bitmask
                BitSet chunkSections = new BitSet(16);
                for (int section = 0; section < 16; section++)
                    chunkSections.set(section, !snapshot.isSectionEmpty(section));
                outputStream.write(chunkSections.toByteArray());

                // Writing chunk section data
                for (int section = 0; section < 16; section++) {
                    if (!snapshot.isSectionEmpty(section)) continue;

                    //
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
        Chunk chunk = Bukkit.getWorld("world").getChunkAt(x, z);
        chunk.load();
        Bukkit.getScheduler().runTaskLater(this.main, (Runnable) chunk::unload, 1000);
        return chunk;
    }
}

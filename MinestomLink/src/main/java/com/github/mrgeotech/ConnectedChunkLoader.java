package com.github.mrgeotech;

import com.github.luben.zstd.Zstd;
import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.ChunkGenerator;
import net.minestom.server.instance.ChunkPopulator;
import net.minestom.server.instance.batch.ChunkBatch;
import net.minestom.server.instance.block.Block;
import net.minestom.server.world.biomes.Biome;
import org.jetbrains.annotations.NotNull;
import org.jglrxavpok.hephaistos.mca.LongCompactorKt;
import org.jglrxavpok.hephaistos.nbt.*;

import javax.annotation.Nonnull;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ConnectedChunkLoader implements ChunkGenerator {

    @Override
    public void generateChunkData(@NotNull ChunkBatch batch, int chunkX, int chunkZ) {
        InetSocketAddress address = ConfigHandler.getIP();
        System.out.println("Connecting to server " + address.getHostName() + ":" + address.getPort() + "...");

        try {
            SocketChannel channel = SocketChannel.open(address);
            channel.configureBlocking(true);

            // Building the chunk request packet and sending it
            ByteBuffer buffer = ByteBuffer.allocate(9);
            buffer.put((byte) 0x01); // 1 byte
            buffer.putInt(chunkX); // 4 bytes
            buffer.putInt(chunkZ); // 4 bytes
            buffer.flip();
            channel.write(buffer);

            System.out.println("Chunk (" + chunkX + ", " + chunkZ + ") sent!");

            // Reading the chunk data
            buffer = ByteBuffer.allocate(9);
            channel.read(buffer);

            buffer.position(0);

            byte packetId = buffer.get();
            if (packetId != (byte) 0x02) return;

            int compressedPayloadLength = buffer.getInt();
            int uncompressedPayloadLength = buffer.getInt();

            // Filling the buffer with all the packet's contents
            buffer = ByteBuffer.allocate(compressedPayloadLength);
            channel.read(buffer);

            byte[] compressedOutput = buffer.array();
            byte[] output = new byte[uncompressedPayloadLength];

            Zstd.decompress(output, compressedOutput);

            // Getting data streams for easier use of the data
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(output);
            DataInputStream inputStream = new DataInputStream(byteArrayInputStream);

            // Closing the socket and clearing the buffer
            channel.close();
            buffer.clear();

            // Reading the block data
            int paletteLength = inputStream.readInt();
            List<String> chunkBlockData = new ArrayList<>();
            for (int j = 0; j < paletteLength; j++) {
            }

            // Iterator makes getting the items from list easier
            ListIterator<String> chunkDataIterator = chunkBlockData.listIterator();
            Block prevBlock = Block.AIR;

            // Going through batch and setting blocks to their type
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    for (int y = -64; y < 320; y++) {
                        int sLength = inputStream.readInt();
                        if (sLength == 0) {
                            batch.setBlock(x, y, z, prevBlock);
                        } else {
                            byte[] sRaw = inputStream.readNBytes(sLength);
                            batch.setBlock(x, y, z, prevBlock = Objects.requireNonNull(
                                    Block.fromNamespaceId("minecraft:" + (new String(sRaw, StandardCharsets.UTF_8)))));
                        }
                        /* h
                        String blockType = chunkDataIterator.next();
                        try {
                            if (blockType.equals("*"))
                                batch.setBlock(x, y, z, prevBlock);
                            else
                                batch.setBlock(x, y, z, prevBlock = Objects.requireNonNull(Block.fromNamespaceId(blockType)));
                        } catch (NullPointerException e) {
                            e.printStackTrace(System.err);
                            System.err.println(blockType);
                        }*/
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Chunk loaded!");
        ConfigHandler.removeTask(address);
    }

    @Override
    public void fillBiomes(Biome[] biomes, int chunkX, int chunkZ) {
        Arrays.fill(biomes, MinecraftServer.getBiomeManager().getById(0));
    }

    @Override
    public List<ChunkPopulator> getPopulators() {
        return null;
    }

}

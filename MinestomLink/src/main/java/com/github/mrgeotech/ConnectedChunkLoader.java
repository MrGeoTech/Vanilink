package com.github.mrgeotech;

import com.github.luben.zstd.Zstd;
import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.ChunkGenerator;
import net.minestom.server.instance.ChunkPopulator;
import net.minestom.server.instance.batch.ChunkBatch;
import net.minestom.server.instance.block.Block;
import net.minestom.server.world.biomes.Biome;
import org.jglrxavpok.hephaistos.mca.LongCompactorKt;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;
import org.jglrxavpok.hephaistos.nbt.NBTException;
import org.jglrxavpok.hephaistos.nbt.NBTReader;
import org.jglrxavpok.hephaistos.nbt.NBTString;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.*;

public class ConnectedChunkLoader implements ChunkGenerator {

    @Override
    public void generateChunkData(ChunkBatch batch, int chunkX, int chunkZ) {
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

            // TODO: Clean up all this code

            // Reading the chunk data
            buffer = ByteBuffer.allocate(9);
            channel.read(buffer);

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

            // Getting the chunk sections
            BitSet chunkSections = BitSet.valueOf(inputStream.readNBytes(2));

            for (int i = 0; i < 16; i++) {
                if (!chunkSections.get(i)) {
                    // Continue if there is no chunk section at i
                    continue;
                }

                int yOffset = i * 16;

                // Reading the palette data
                int paletteLength = inputStream.readInt();
                List<NBTCompound> palette = new ArrayList<>();
                for (int j = 0; j < paletteLength; j++) {
                    int nbtLength = inputStream.readInt();
                    byte[] nbtRaw = inputStream.readNBytes(nbtLength);
                    NBTCompound nbt = (NBTCompound) new NBTReader(new ByteArrayInputStream(nbtRaw), false).read();
                    palette.add(nbt);
                }

                // Reading the block state data
                int blockStatesLength = inputStream.readInt();
                long[] compactedBlockStates = new long[blockStatesLength];
                for (int j = 0; j < blockStatesLength; j++) {
                    compactedBlockStates[j] = inputStream.readLong();
                }

                int sizeInBits = compactedBlockStates.length * 64 / 4096;
                int[] blockStates = LongCompactorKt.unpack(compactedBlockStates, sizeInBits);

                for (int y = 0; y < 16; y++) {
                    for (int z = 0; z < 16; z++) {
                        for (int x = 0; x < 16; x++) {
                            int pos = y * 16 * 16 + z * 16 + x;
                            NBTCompound compound = palette.get(blockStates[pos]);
                            Block block = getBlockFromCompound(compound);
                            batch.setBlock(x, y + yOffset, z, block);
                        }
                    }
                }
            }
        } catch (IOException | NBTException e) {
            e.printStackTrace();
        }
        System.out.println("Chunk loaded!");
        ConfigHandler.removeTask(address);
    }

    private Block getBlockFromCompound(NBTCompound compound) {
        String name = compound.getString("Name");
        if (name == null) return null;
        if (name == "minecraft:air") return null;

        NBTCompound properties = compound.getCompound("Properties");
        if (properties == null) properties = new NBTCompound();

        HashMap<String, String> propertiesMap = new HashMap<>();
        properties.iterator().forEachRemaining(pair -> {
            propertiesMap.put(pair.component1(), ((NBTString) pair.component2()).getValue());
        });

        return Block.fromNamespaceId(name).withProperties(propertiesMap);
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

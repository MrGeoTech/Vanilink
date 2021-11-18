package com.github.mrgeotech;

import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.ChunkGenerator;
import net.minestom.server.instance.ChunkPopulator;
import net.minestom.server.instance.batch.ChunkBatch;
import net.minestom.server.instance.block.Block;
import net.minestom.server.world.biomes.Biome;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ConnectedChunkLoader implements ChunkGenerator {

    @Override
    public void generateChunkData(ChunkBatch batch, int chunkX, int chunkZ) {
        InetSocketAddress address = ConfigHandler.getIP();
        try {
            System.out.println("Getting chunk at " + chunkX + "," + chunkZ + "!");
            System.out.println("Connecting to server...");
            SocketChannel channel = SocketChannel.open(address);
            channel.configureBlocking(true);
            System.out.println("Connected to server!");

            ByteBuffer buffer = ByteBuffer.allocate(2048);
            buffer.put((ConfigHandler.getKey() + ";c," + chunkX + "," + chunkZ).getBytes(StandardCharsets.UTF_8));
            buffer.flip();
            channel.write(buffer);

            String output = "";

            System.out.println("Getting chunk data!");

            buffer = ByteBuffer.allocate(1000000);
            while (channel.read(buffer) > 0) {
                output += new String(buffer.array()).trim();
            }

            System.out.println("Closing channel!");

            channel.close();
            buffer.clear();

            System.out.println(output.getBytes(StandardCharsets.UTF_8).length);
            System.out.println(output.substring(output.length() - 10));
            output = output.replace("!", "");

            String[] blocksAsStrings = output.split(";");

            for (int i = 0; i < blocksAsStrings.length - 1; i += 4) {
                try {
                    batch.setBlock(Integer.parseInt(blocksAsStrings[i]),
                            Integer.parseInt(blocksAsStrings[i + 1]),
                            Integer.parseInt(blocksAsStrings[i + 2]),
                            Objects.requireNonNull(Block.fromNamespaceId(blocksAsStrings[i + 3])));
                } catch (NullPointerException e) {
                    batch.setBlock(Integer.parseInt(blocksAsStrings[i]),
                            Integer.parseInt(blocksAsStrings[i + 1]),
                            Integer.parseInt(blocksAsStrings[i + 2]),
                            Objects.requireNonNull(Block.AIR));
                    System.out.println(blocksAsStrings[i + 3]);
                    blocksAsStrings[i + 3] = blocksAsStrings[i + 3].replaceAll("[^\\d.]", "");
                    System.out.println(blocksAsStrings[i + 3]);
                    i--;
                } catch (NumberFormatException e) {
                    System.out.println(i);
                    i -= 5;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            System.out.println("Chunk received!");
        } catch (IOException e) {
            e.printStackTrace();
        }
        ConfigHandler.removeTask(address);
    }

    @Override
    public void fillBiomes(Biome[] biomes, int chunkX, int chunkZ) {
        /*
        try {
            System.out.println("Connecting to server...");
            SocketChannel channel = SocketChannel.open(address);
            Biome[] biomeList = new Biome[biomes.length];
            Arrays.fill(biomeList, MinecraftServer.getBiomeManager().getById(0));
            channel.configureBlocking(true);
            System.out.println("Connected to server!");

            ByteBuffer buffer = ByteBuffer.allocate(2048);
            buffer.put(("b," + chunkX + "," + chunkZ).getBytes());
            buffer.flip();
            channel.write(buffer);

            String output = "";

            System.out.println("Getting biome data!");

            buffer.clear();
            while (channel.read(buffer) > 0) {
                output += new String(buffer.array()).trim();
            }

            System.out.println("Closing channel!");

            channel.close();
            buffer.clear();

            String[] biomesAsStrings = output.split(",");

            for (int i = 0; i < biomesAsStrings.length; i++) {
                try {
                    biomeList[i] = MinecraftServer.getBiomeManager().getById(Integer.parseInt(biomesAsStrings[i]));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            biomes = biomeList;

            System.out.println("Biomes received!");
        } catch (IOException e) {
            e.printStackTrace();
            Arrays.fill(biomes, MinecraftServer.getBiomeManager().getById(0));
        }*/
        Arrays.fill(biomes, MinecraftServer.getBiomeManager().getById(0));
    }

    @Override
    public List<ChunkPopulator> getPopulators() {
        return null;
    }

}

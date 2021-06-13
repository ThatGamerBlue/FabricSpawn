package com.thatgamerblue.fabric.spawn;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

public class SpawnConfig
{
	private final Gson GSON = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
	private ConfigObject config;

	public BlockPos getSpawnPos()
	{
		return new BlockPos(config.x, config.y, config.z);
	}

	public RegistryKey<World> getWorld(MinecraftServer server)
	{
		return server.getWorldRegistryKeys().stream().filter(key -> key.getValue().toString().equals(config.world)).findFirst().orElse(null);
	}

	public Vec2f getFacing()
	{
		return new Vec2f(config.pitch, config.yaw);
	}

	public void setSpawnPos(BlockPos pos) throws IOException
	{
		config.x = pos.getX();
		config.y = pos.getY();
		config.z = pos.getZ();
		saveConfig();
	}

	public void setWorld(RegistryKey<World> world) throws IOException
	{
		config.world = world.getValue().toString();
		saveConfig();
	}

	public void setFacing(Vec2f facing) throws IOException
	{
		config.pitch = facing.x;
		config.yaw = facing.y;
		saveConfig();
	}

	public void saveConfig() throws IOException
	{
		Path configPath = FabricLoader.getInstance().getConfigDir().resolve("fabricspawn").resolve("config.json");
		Files.createDirectories(configPath.resolve(".."));
		Files.writeString(configPath, GSON.toJson(config == null ? new ConfigObject() : config));
	}

	public void loadConfig() throws IOException
	{
		Path configPath = FabricLoader.getInstance().getConfigDir().resolve("fabricspawn").resolve("config.json");

		if (!Files.exists(configPath))
		{
			saveConfig();
		}

		config = GSON.fromJson(String.join("\n", Files.readAllLines(configPath)), ConfigObject.class);
	}

	private static class ConfigObject
	{
		public int x = 0;
		public int y = 0;
		public int z = 0;
		public String world = "minecraft:overworld";
		public float yaw = 0.0f;
		public float pitch = 0.0f;
	}
}

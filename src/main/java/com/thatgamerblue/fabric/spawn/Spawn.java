package com.thatgamerblue.fabric.spawn;

import com.mojang.brigadier.CommandDispatcher;
import java.io.IOException;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;

public class Spawn implements DedicatedServerModInitializer
{
	private static final SpawnConfig config = new SpawnConfig();

	@Override
	public void onInitializeServer()
	{
		try
		{
			config.loadConfig();
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
		CommandRegistrationCallback.EVENT.register(this::registerCommands);
	}

	private void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, boolean isDedicated)
	{
		SpawnCommand.register(dispatcher, config);
		SetSpawnCommand.register(dispatcher, config);
	}
}

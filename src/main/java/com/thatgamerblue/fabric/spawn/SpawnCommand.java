package com.thatgamerblue.fabric.spawn;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import static net.minecraft.server.command.CommandManager.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

public class SpawnCommand
{
	public static SpawnConfig config;

	public static void register(CommandDispatcher<ServerCommandSource> dispatcher, SpawnConfig config)
	{
		SpawnCommand.config = config;
		dispatcher.register(
			literal("spawn")
				.executes(scs -> execute(scs.getSource()))
		);
	}

	public static int execute(ServerCommandSource scs)
	{
		MinecraftServer server = scs.getMinecraftServer();

		if (!(scs.getEntity() instanceof ServerPlayerEntity))
		{
			return 1;
		}

		RegistryKey<World> destinationWorldKey = config.getWorld(server);
		ServerWorld destWorld = server.getWorld(destinationWorldKey);

		if (destWorld == null)
		{
			scs.sendFeedback(Text.of("Spawn: Destination world invalid"), false);
			return 1;
		}

		BlockPos target = config.getSpawnPos();
		Vec2f facing = config.getFacing();
		((ServerPlayerEntity) scs.getEntity()).teleport(destWorld, target.getX() + 0.5d, target.getY() + 0.0d, target.getZ() + 0.5d, facing.y, facing.x);

		return 1;
	}
}

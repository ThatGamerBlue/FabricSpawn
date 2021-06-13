package com.thatgamerblue.fabric.spawn;

import com.mojang.brigadier.CommandDispatcher;
import java.io.IOException;
import static net.minecraft.server.command.CommandManager.literal;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

public class SetSpawnCommand
{
	private static SpawnConfig config;

	public static void register(CommandDispatcher<ServerCommandSource> dispatcher, SpawnConfig config)
	{
		SetSpawnCommand.config = config;
		dispatcher.register(
			literal("setspawn")
				.requires(scs -> scs.hasPermissionLevel(2))
				.executes(scs -> execute(scs.getSource()))
		);
	}

	private static int execute(ServerCommandSource scs)
	{
		if (scs.getEntity() == null)
		{
			return 1;
		}

		RegistryKey<World> destWorldKey = scs.getWorld().getRegistryKey();
		BlockPos entityPos = scs.getEntity().getBlockPos();
		BlockPos target = new BlockPos(entityPos.getX(), entityPos.getY(), entityPos.getZ());
		Vec2f facing = new Vec2f(scs.getEntity().getPitch(), scs.getEntity().getYaw());

		try
		{
			config.setWorld(destWorldKey);
			config.setSpawnPos(target);
			config.setFacing(facing);
		}
		catch (IOException ex)
		{
			throw new RuntimeException(ex);
		}

		scs.getWorld().setSpawnPos(target, facing.y);
		scs.getMinecraftServer().getGameRules().get(GameRules.SPAWN_RADIUS).set(0, scs.getMinecraftServer());

		scs.sendFeedback(Text.of("Spawn set!"), false);

		return 1;
	}
}

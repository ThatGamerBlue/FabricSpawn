package com.thatgamerblue.fabric.spawn.mixin;

import com.thatgamerblue.fabric.spawn.SpawnCommand;
import java.util.Optional;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin
{
	@Inject(at = @At("TAIL"), method = "respawnPlayer(Lnet/minecraft/server/network/ServerPlayerEntity;Z)Lnet/minecraft/server/network/ServerPlayerEntity;", locals = LocalCapture.CAPTURE_FAILHARD)
	public void postRespawnPlayer(ServerPlayerEntity player, boolean alive, CallbackInfoReturnable<ServerPlayerEntity> cir,
		// locals
		BlockPos blockPos, float f, boolean bl, ServerWorld serverWorld, Optional optional2, ServerWorld serverWorld2, ServerPlayerEntity serverPlayerEntity)
	{
		if (serverPlayerEntity.getBlockPos().equals(SpawnCommand.config.getSpawnPos()) &&
			serverWorld2.getRegistryKey().getValue().toString().equals(SpawnCommand.config.getWorld(serverPlayerEntity.getServer()).getValue().toString()))
		{
			// jank fix for MC-200092
			SpawnCommand.execute(serverPlayerEntity.getCommandSource());
		}
	}
}

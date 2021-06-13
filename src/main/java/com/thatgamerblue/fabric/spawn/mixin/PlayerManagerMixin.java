package com.thatgamerblue.fabric.spawn.mixin;

import com.thatgamerblue.fabric.spawn.SpawnCommand;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin
{
	@Inject(at = @At("TAIL"), method = "respawnPlayer(Lnet/minecraft/server/network/ServerPlayerEntity;Z)Lnet/minecraft/server/network/ServerPlayerEntity;")
	public void postRespawnPlayer(ServerPlayerEntity player, boolean alive, CallbackInfoReturnable<ServerPlayerEntity> cir)
	{
		if (player.getBlockPos().equals(SpawnCommand.config.getSpawnPos()))
		{
			// jank fix for MC-200092
			SpawnCommand.execute(player.getCommandSource());
		}
	}
}

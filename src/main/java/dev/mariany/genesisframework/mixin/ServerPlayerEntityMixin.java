package dev.mariany.genesisframework.mixin;

import com.mojang.authlib.GameProfile;
import dev.mariany.genesisframework.stat.GFStats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {
    public ServerPlayerEntityMixin(World world, GameProfile profile) {
        super(world, profile);
    }

    /**
     * Increment Hostile Kills Stat.
     */
    @Inject(method = "updateKilledAdvancementCriterion", at = @At(value = "HEAD"))
    public void injectUpdateKilledAdvancementCriterion(Entity entityKilled, DamageSource damageSource, CallbackInfo ci) {
        if (entityKilled instanceof HostileEntity) {
            this.incrementStat(GFStats.HOSTILE_KILLS.value());
        }
    }
}

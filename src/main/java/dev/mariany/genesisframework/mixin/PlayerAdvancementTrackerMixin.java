package dev.mariany.genesisframework.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.mariany.genesisframework.advancement.criterion.GFCriteria;
import dev.mariany.genesisframework.age.Age;
import dev.mariany.genesisframework.age.AgeEntry;
import dev.mariany.genesisframework.age.AgeManager;
import dev.mariany.genesisframework.age.AgeShareManager;
import dev.mariany.genesisframework.config.ConfigHandler;
import dev.mariany.genesisframework.gamerule.GFGamerules;
import dev.mariany.genesisframework.packet.clientbound.UpdateAgeItemUnlocksPayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(PlayerAdvancementTracker.class)
public class PlayerAdvancementTrackerMixin {
    @Shadow
    private ServerPlayerEntity owner;

    /**
     * Prevent earning an age's advancement criterion if the parent age is not complete.
     */
    @Inject(method = "grantCriterion", at = @At(value = "HEAD"), cancellable = true)
    public void injectGrantCriterion(
            AdvancementEntry advancement,
            String criterionName,
            CallbackInfoReturnable<Boolean> cir
    ) {
        AgeManager ageManager = AgeManager.getInstance();
        Optional<AgeEntry> optionalAgeEntry = ageManager.find(advancement);

        if (optionalAgeEntry.isPresent()) {
            AgeEntry ageEntry = optionalAgeEntry.get();
            Age age = ageEntry.getAge();
            Optional<Identifier> parentIdentifier = age.parent();

            if (age.requiresParent() && parentIdentifier.isPresent()) {
                Optional<AgeEntry> parent = ageManager.get(parentIdentifier.get());

                if (parent.isPresent() && !parent.get().isDone(owner)) {
                    cir.setReturnValue(false);
                }
            }
        }
    }

    /**
     * Triggers ${@link GFCriteria#OPEN_ADVANCEMENT_TAB}, updates client age item unlocks state, and shares the age advancement with other players.
     */
    @WrapOperation(method = "grantCriterion", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancement/PlayerAdvancementTracker;onStatusUpdate(Lnet/minecraft/advancement/AdvancementEntry;)V"))
    public void wrapOnStatusUpdate(
            PlayerAdvancementTracker playerAdvancementTracker,
            AdvancementEntry advancement,
            Operation<Void> original
    ) {
        GFCriteria.OBTAIN_ADVANCEMENT.trigger(owner, advancement);

        AgeManager ageManager = AgeManager.getInstance();
        Optional<AgeEntry> optionalAge = ageManager.find(advancement);

        if (optionalAge.isPresent()) {
            ServerPlayNetworking.send(owner, new UpdateAgeItemUnlocksPayload(ageManager.getAllItemUnlocks(owner)));

            MinecraftServer server = owner.getServer();

            if (server != null) {
                if (server.getGameRules().getBoolean(GFGamerules.SHARED_AGE_PROGRESSION)) {
                    AgeShareManager ageShareManager = AgeShareManager.getServerState(server);

                    if (ConfigHandler.getConfig().teamBasedAgeSharing) {
                        ageShareManager.shareWithTeam(owner, optionalAge.get());
                    } else {
                        ageShareManager.shareWithServer(server, optionalAge.get());
                    }
                }
            }
        }

        original.call(playerAdvancementTracker, advancement);
    }
}

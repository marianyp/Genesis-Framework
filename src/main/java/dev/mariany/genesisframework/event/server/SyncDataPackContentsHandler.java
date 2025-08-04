package dev.mariany.genesisframework.event.server;

import dev.mariany.genesisframework.age.AgeManager;
import dev.mariany.genesisframework.instruction.InstructionManager;
import dev.mariany.genesisframework.packet.clientbound.UpdateAgeItemUnlocksPayload;
import dev.mariany.genesisframework.packet.clientbound.UpdateInstructionsPayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;

public class SyncDataPackContentsHandler {
    public static void onSyncDataPackContents(ServerPlayerEntity player, boolean joined) {
        ServerPlayNetworking.send(player, new UpdateAgeItemUnlocksPayload(
                AgeManager.getInstance().getAllItemUnlocks(player))
        );

        ServerPlayNetworking.send(player, new UpdateInstructionsPayload(
                InstructionManager.getInstance().getInstructionAdvancementIds())
        );
    }
}

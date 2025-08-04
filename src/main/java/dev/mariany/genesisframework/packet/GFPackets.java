package dev.mariany.genesisframework.packet;

import dev.mariany.genesisframework.packet.clientbound.NotifyAgeLockedPayload;
import dev.mariany.genesisframework.packet.clientbound.UpdateAgeItemUnlocksPayload;
import dev.mariany.genesisframework.packet.clientbound.UpdateInstructionsPayload;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.RegistryByteBuf;

public class GFPackets {
    public static void register() {
        clientbound(PayloadTypeRegistry.playS2C());
        serverbound(PayloadTypeRegistry.playC2S());
    }

    private static void clientbound(PayloadTypeRegistry<RegistryByteBuf> registry) {
        registry.register(UpdateAgeItemUnlocksPayload.ID, UpdateAgeItemUnlocksPayload.CODEC);
        registry.register(UpdateInstructionsPayload.ID, UpdateInstructionsPayload.CODEC);
        registry.register(NotifyAgeLockedPayload.ID, NotifyAgeLockedPayload.CODEC);
    }

    private static void serverbound(PayloadTypeRegistry<RegistryByteBuf> registry) {
    }
}

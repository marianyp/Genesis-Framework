package dev.mariany.genesisframework.client;

import dev.mariany.genesisframework.client.age.ClientAgeManager;
import dev.mariany.genesisframework.client.instruction.ClientInstructionManager;
import dev.mariany.genesisframework.packet.clientbound.ClientboundPackets;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;

@Environment(EnvType.CLIENT)
public class GFClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientboundPackets.init();

        ClientPlayConnectionEvents.INIT.register(GFClient::cleanup);
        ClientPlayConnectionEvents.DISCONNECT.register(GFClient::cleanup);
    }

    private static void cleanup(
            ClientPlayNetworkHandler clientPlayNetworkHandler,
            MinecraftClient minecraftClient
    ) {
        ClientAgeManager.getInstance().reset();
        ClientInstructionManager.getInstance().reset();
    }
}

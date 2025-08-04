package dev.mariany.genesisframework.event.server;

import dev.mariany.genesisframework.GenesisFramework;
import dev.mariany.genesisframework.age.AgeShareManager;
import dev.mariany.genesisframework.gamerule.GFGamerules;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class ServerPlayConnectionHandler {
    public static void onJoin(
            ServerPlayNetworkHandler serverPlayNetworkHandler,
            PacketSender packetSender,
            MinecraftServer server
    ) {
        ServerPlayerEntity serverPlayer = serverPlayNetworkHandler.player;
        AgeShareManager ageShareManager = AgeShareManager.getServerState(server);

        if (server.getGameRules().getBoolean(GFGamerules.SHARED_AGE_PROGRESSION)) {
            GenesisFramework.LOGGER.info("Preparing to apply shared ages to {}", serverPlayer.toString());
            ageShareManager.applySharedAges(serverPlayer);
        }
    }
}

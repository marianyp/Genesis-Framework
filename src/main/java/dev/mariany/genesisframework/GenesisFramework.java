package dev.mariany.genesisframework;

import dev.mariany.genesisframework.advancement.criterion.GFCriteria;
import dev.mariany.genesisframework.component.GFComponentTypes;
import dev.mariany.genesisframework.config.ConfigHandler;
import dev.mariany.genesisframework.event.block.UseBlockHandler;
import dev.mariany.genesisframework.event.server.ServerPlayConnectionHandler;
import dev.mariany.genesisframework.event.server.SyncDataPackContentsHandler;
import dev.mariany.genesisframework.event.server.advancement.BeforeAdvancementsLoadHandler;
import dev.mariany.genesisframework.event.server.advancement.ServerAdvancementEvents;
import dev.mariany.genesisframework.event.server.command.CommandRegistrationHandler;
import dev.mariany.genesisframework.gamerule.GFGamerules;
import dev.mariany.genesisframework.item.GFItems;
import dev.mariany.genesisframework.packet.GFPackets;
import dev.mariany.genesisframework.sound.GFSoundEvents;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenesisFramework implements ModInitializer {
    public static final String MOD_ID = "genesisframework";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static Identifier id(String resource) {
        return Identifier.of(MOD_ID, resource);
    }

    @Override
    public void onInitialize() {
        ConfigHandler.loadConfig();

        GFPackets.register();
        GFComponentTypes.bootstrap();
        GFSoundEvents.bootstrap();
        GFCriteria.bootstrap();
        GFItems.bootstrap();

        UseBlockCallback.EVENT.register(UseBlockHandler::onUseBlock);
        ServerAdvancementEvents.BEFORE_ADVANCEMENTS_LOAD.register(BeforeAdvancementsLoadHandler::beforeAdvancementsLoad);
        ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register(SyncDataPackContentsHandler::onSyncDataPackContents);
        ServerPlayConnectionEvents.JOIN.register(ServerPlayConnectionHandler::onJoin);
        CommandRegistrationCallback.EVENT.register(CommandRegistrationHandler::onRegister);

        GFGamerules.bootstrap();
    }
}
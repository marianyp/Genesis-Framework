package dev.mariany.genesisframework.server.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import dev.mariany.genesisframework.advancement.AdvancementHelper;
import dev.mariany.genesisframework.age.Age;
import dev.mariany.genesisframework.age.AgeEntry;
import dev.mariany.genesisframework.age.AgeManager;
import dev.mariany.genesisframework.age.AgeShareManager;
import dev.mariany.genesisframework.registry.GFRegistryKeys;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.RegistryKeyArgumentType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class AgeCommand {
    private static final DynamicCommandExceptionType AGE_NOT_FOUND_EXCEPTION = new DynamicCommandExceptionType(
            id -> Text.stringifiedTranslatable("genesisframework.age.ageNotFound", id)
    );

    public static void register(
            CommandDispatcher<ServerCommandSource> dispatcher
    ) {
        dispatcher.register(
                CommandManager.literal("age")
                        .requires(CommandManager.requirePermissionLevel(2))
                        .then(CommandManager.literal("cache")
                                .then(CommandManager.literal("clear")
                                        .then(CommandManager.literal("global")
                                                .executes(
                                                        context ->
                                                                executeClearCache(
                                                                        context.getSource(), true
                                                                )
                                                )
                                        )
                                        .then(CommandManager.literal("teams")
                                                .executes(
                                                        context ->
                                                                executeClearCache(
                                                                        context.getSource(), false
                                                                )
                                                )
                                        )
                                )
                        )
                        .then(CommandManager.literal("give")
                                .then(CommandManager.argument("targets", EntityArgumentType.players()).then(
                                                CommandManager.argument(
                                                                "age",
                                                                RegistryKeyArgumentType.registryKey(GFRegistryKeys.AGE)
                                                        )
                                                        .executes(context ->
                                                                executeGive(
                                                                        EntityArgumentType.getPlayers(
                                                                                context,
                                                                                "targets"
                                                                        ),
                                                                        getAgeEntry(context)
                                                                )
                                                        )
                                        )
                                )
                        )
                        .then(CommandManager.literal("take")
                                .then(CommandManager.argument("targets", EntityArgumentType.players()).then(
                                                CommandManager.argument(
                                                                "age",
                                                                RegistryKeyArgumentType.registryKey(GFRegistryKeys.AGE)
                                                        )
                                                        .executes(context ->
                                                                executeTake(
                                                                        EntityArgumentType.getPlayers(
                                                                                context,
                                                                                "targets"
                                                                        ),
                                                                        getAgeEntry(context)
                                                                )
                                                        )
                                        )
                                )
                        )
        );
    }

    private static int executeClearCache(ServerCommandSource source, boolean global) {
        ServerWorld world = source.getWorld();
        MinecraftServer server = world.getServer();
        AgeShareManager ageShareManager = AgeShareManager.getServerState(server);

        int cleared = ageShareManager.clear(global);

        source.sendFeedback(
                () -> Text.translatable("commands.genesisframework.age.clear", cleared),
                true
        );

        return 1;
    }

    private static int executeGive(Collection<ServerPlayerEntity> targets, AgeEntry ageEntry) {
        return AgeShareManager.progressPlayersToAge(targets, ageEntry);
    }

    private static int executeTake(Collection<ServerPlayerEntity> targets, AgeEntry ageEntry) {
        int success = 0;

        for (ServerPlayerEntity player : targets) {
            if (takeAge(player, ageEntry)) {
                ++success;
            }
        }

        return success;
    }

    private static boolean takeAge(ServerPlayerEntity player, AgeEntry ageEntry) {
        AdvancementEntry advancementEntry = ageEntry.getAdvancementEntry();
        boolean removed = AdvancementHelper.revokeAdvancement(player, advancementEntry);

        List<AgeEntry> children = AgeManager.getInstance().getAges().stream().filter(ageEntry1 ->
                ageEntry1.getParentAdvancementId()
                        .map(parentId -> parentId.equals(ageEntry.getId()))
                        .orElse(false)
        ).toList();

        for (AgeEntry child : children) {
            removed = removed || takeAge(player, child);
        }

        return removed;
    }

    private static AgeEntry getAgeEntry(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        RegistryKey<Age> registryKey = RegistryKeyArgumentType.getKey(
                context,
                "age",
                GFRegistryKeys.AGE,
                AGE_NOT_FOUND_EXCEPTION
        );
        Optional<AgeEntry> optionalAgeEntry = AgeManager.getInstance().get(registryKey.getValue());

        if (optionalAgeEntry.isEmpty()) {
            throw AGE_NOT_FOUND_EXCEPTION.create(registryKey.getValue());
        } else {
            return optionalAgeEntry.get();
        }
    }
}

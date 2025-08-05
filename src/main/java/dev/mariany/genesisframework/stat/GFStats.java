package dev.mariany.genesisframework.stat;

import dev.mariany.genesisframework.GenesisFramework;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;

public class GFStats {
    public static final RegistryEntry.Reference<Identifier> HOSTILE_KILLS = register(
            "hostile_kills",
            StatFormatter.DEFAULT
    );

    private static RegistryEntry.Reference<Identifier> register(String name, StatFormatter formatter) {
        Identifier id = GenesisFramework.id(name);
        Stats.CUSTOM.getOrCreateStat(id, formatter);
        return Registry.registerReference(Registries.CUSTOM_STAT, id, id);
    }

    public static void bootstrap() {
        GenesisFramework.LOGGER.info("Registering Stats for " + GenesisFramework.MOD_ID);
    }
}

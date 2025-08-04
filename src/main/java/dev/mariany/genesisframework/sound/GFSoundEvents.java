package dev.mariany.genesisframework.sound;

import dev.mariany.genesisframework.GenesisFramework;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class GFSoundEvents {
    public static final SoundEvent UI_TOAST_INSTRUCTIONS_COMPLETE = register("ui.toast.instructions_complete");

    private static SoundEvent register(String id) {
        return register(GenesisFramework.id(id));
    }

    private static SoundEvent register(Identifier id) {
        return register(id, id);
    }

    private static SoundEvent register(Identifier id, Identifier soundId) {
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(soundId));
    }

    private static RegistryEntry.Reference<SoundEvent> registerReference(String id) {
        return registerReference(GenesisFramework.id(id));
    }

    private static RegistryEntry.Reference<SoundEvent> registerReference(Identifier id) {
        return registerReference(id, id);
    }

    private static RegistryEntry.Reference<SoundEvent> registerReference(Identifier id, Identifier soundId) {
        return Registry.registerReference(Registries.SOUND_EVENT, id, SoundEvent.of(soundId));
    }

    public static void bootstrap() {
        GenesisFramework.LOGGER.info("Registering Sound Events for " + GenesisFramework.MOD_ID);
    }
}

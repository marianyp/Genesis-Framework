package dev.mariany.genesisframework.registry;

import dev.mariany.genesisframework.age.Age;
import dev.mariany.genesisframework.instruction.Instruction;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class GFRegistryKeys {
    public static final RegistryKey<Registry<Age>> AGE = ofVanilla("age");
    public static final RegistryKey<Registry<Instruction>> INSTRUCTION = ofVanilla("instruction");

    private static <T> RegistryKey<Registry<T>> ofVanilla(String id) {
        return RegistryKey.ofRegistry(Identifier.ofVanilla(id));
    }
}

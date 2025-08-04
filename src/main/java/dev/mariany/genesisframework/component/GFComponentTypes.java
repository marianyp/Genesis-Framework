package dev.mariany.genesisframework.component;

import dev.mariany.genesisframework.GenesisFramework;
import dev.mariany.genesisframework.age.Age;
import dev.mariany.genesisframework.registry.GFRegistryKeys;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

import java.util.List;
import java.util.function.UnaryOperator;

public class GFComponentTypes {
    public static final ComponentType<List<RegistryKey<Age>>> AGES = register(
            "ages",
            builder -> builder.codec(RegistryKey.createCodec(GFRegistryKeys.AGE).listOf()
            ).cache()
    );

    private static <T> ComponentType<T> register(String id, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(
                Registries.DATA_COMPONENT_TYPE,
                GenesisFramework.id(id), builderOperator.apply(ComponentType.builder()).build()
        );
    }

    public static void bootstrap() {
        GenesisFramework.LOGGER.info("Registering Component Types for " + GenesisFramework.MOD_ID);
    }
}

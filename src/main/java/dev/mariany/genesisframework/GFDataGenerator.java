package dev.mariany.genesisframework;

import dev.mariany.genesisframework.datagen.GFInstructionProvider;
import dev.mariany.genesisframework.datagen.GFModelProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import org.jetbrains.annotations.Nullable;

public class GFDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(GFModelProvider::new);
        pack.addProvider(GFInstructionProvider::new);
    }

    @Override
    public @Nullable String getEffectiveModId() {
        return GenesisFramework.MOD_ID;
    }
}

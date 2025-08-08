package dev.mariany.genesisframework.advancement.criterion;

import dev.mariany.genesisframework.GenesisFramework;
import net.minecraft.advancement.criterion.Criterion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class GFCriteria {
    public static final CompleteTrialSpawnerCriteria COMPLETE_TRIAL_SPAWNER_ADVANCEMENT = register(
            "complete_trial_spawner",
            new CompleteTrialSpawnerCriteria()
    );
    public static final OpenAdvancementTabCriteria OPEN_ADVANCEMENT_TAB = register(
            "open_advancement_tab",
            new OpenAdvancementTabCriteria()
    );

    public static <T extends Criterion<?>> T register(String name, T criterion) {
        return Registry.register(Registries.CRITERION, GenesisFramework.id(name), criterion);
    }

    public static void bootstrap() {
        GenesisFramework.LOGGER.info("Registering Criteria for " + GenesisFramework.MOD_ID);
    }
}

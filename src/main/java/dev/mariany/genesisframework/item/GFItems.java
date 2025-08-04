package dev.mariany.genesisframework.item;

import dev.mariany.genesisframework.GenesisFramework;
import dev.mariany.genesisframework.component.GFComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Rarity;

import java.util.List;
import java.util.function.Function;

public class GFItems {
    public static final Item AGE_BOOK = register(
            "age_book", AgeBookItem::new, new Item.Settings()
                    .maxCount(1)
                    .rarity(Rarity.EPIC)
                    .component(GFComponentTypes.AGES, List.of())
    );

    private static Item register(String name, Function<Item.Settings, Item> factory, Item.Settings settings) {
        RegistryKey<Item> itemKey = keyOf(name);
        Item item = factory.apply(settings.registryKey(itemKey));
        Registry.register(Registries.ITEM, itemKey, item);

        return item;
    }

    private static RegistryKey<Item> keyOf(String id) {
        return RegistryKey.of(RegistryKeys.ITEM, GenesisFramework.id(id));
    }

    public static void bootstrap() {
        GenesisFramework.LOGGER.info("Registering Items for " + GenesisFramework.MOD_ID);
    }
}

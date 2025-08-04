package dev.mariany.genesisframework.item;

import dev.mariany.genesisframework.GenesisFramework;
import dev.mariany.genesisframework.advancement.AdvancementHelper;
import dev.mariany.genesisframework.age.Age;
import dev.mariany.genesisframework.age.AgeEntry;
import dev.mariany.genesisframework.age.AgeManager;
import dev.mariany.genesisframework.component.GFComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;

public class AgeBookItem extends Item {
    public AgeBookItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);

        List<RegistryKey<Age>> ages = itemStack.getOrDefault(GFComponentTypes.AGES, List.of());
        itemStack.decrementUnlessCreative(1, player);

        if (ages.isEmpty()) {
            return ActionResult.FAIL;
        }

        if (player instanceof ServerPlayerEntity serverPlayer) {
            AgeManager ageManager = AgeManager.getInstance();

            for (RegistryKey<Age> ageKey : ages) {
                Optional<AgeEntry> optionalAgeEntry = ageManager.get(ageKey.getValue());

                if (optionalAgeEntry.isPresent()) {
                    AgeEntry ageEntry = optionalAgeEntry.get();
                    AdvancementHelper.giveAdvancement(serverPlayer, ageEntry.getAdvancementEntry());
                } else {
                    GenesisFramework.LOGGER.error("Invalid age: {}", ageKey);
                }
            }

            player.incrementStat(Stats.USED.getOrCreateStat(this));
        }

        return ActionResult.SUCCESS;
    }
}

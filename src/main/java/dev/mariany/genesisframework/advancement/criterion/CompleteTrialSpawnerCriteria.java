package dev.mariany.genesisframework.advancement.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class CompleteTrialSpawnerCriteria extends AbstractCriterion<CompleteTrialSpawnerCriteria.Conditions> {
    @Override
    public Codec<Conditions> getConditionsCodec() {
        return Conditions.CODEC;
    }

    public void trigger(ServerPlayerEntity player, boolean ominous) {
        this.trigger(player, conditions -> conditions.matches(ominous));
    }

    public record Conditions(Optional<LootContextPredicate> player, boolean expectOminous)
            implements AbstractCriterion.Conditions {
        public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                                EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC
                                        .optionalFieldOf("player")
                                        .forGetter(Conditions::player),
                                Codec.BOOL.fieldOf("ominous")
                                        .forGetter(Conditions::expectOminous)
                        )
                        .apply(instance, Conditions::new)
        );

        public static AdvancementCriterion<Conditions> create(boolean ominous) {
            return create(null, ominous);
        }

        public static AdvancementCriterion<Conditions> create(
                @Nullable LootContextPredicate playerPredicate,
                boolean ominous
        ) {
            return GFCriteria.COMPLETE_TRIAL_SPAWNER_ADVANCEMENT.create(
                    new Conditions(Optional.ofNullable(playerPredicate), ominous)
            );
        }

        public boolean matches(boolean ominous) {
            return ominous == expectOminous;
        }
    }
}

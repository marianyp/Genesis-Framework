package dev.mariany.genesisframework.advancement.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class OpenAdvancementTabCriteria extends AbstractCriterion<OpenAdvancementTabCriteria.Conditions> {
    @Override
    public Codec<Conditions> getConditionsCodec() {
        return Conditions.CODEC;
    }

    public void trigger(ServerPlayerEntity player, Identifier tab) {
        this.trigger(player, conditions -> conditions.matches(tab));
    }

    public record Conditions(Optional<LootContextPredicate> player, Optional<Identifier> advancementId)
            implements AbstractCriterion.Conditions {
        public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                                EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC
                                        .optionalFieldOf("player")
                                        .forGetter(Conditions::player),
                                Identifier.CODEC.optionalFieldOf("advancement")
                                        .forGetter(Conditions::advancementId)
                        )
                        .apply(instance, Conditions::new)
        );

        public static AdvancementCriterion<Conditions> create() {
            return create(null, null);
        }

        public static AdvancementCriterion<Conditions> create(Identifier tab) {
            return create(null, tab);
        }

        public static AdvancementCriterion<Conditions> create(
                @Nullable LootContextPredicate playerPredicate,
                @Nullable Identifier tab
        ) {
            return GFCriteria.OPEN_ADVANCEMENT_TAB.create(
                    new Conditions(
                            Optional.ofNullable(playerPredicate),
                            Optional.ofNullable(tab)
                    )
            );
        }

        public boolean matches(Identifier tab) {
            return advancementId.map(identifier -> identifier.equals(tab)).orElse(true);
        }
    }
}

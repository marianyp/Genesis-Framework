package dev.mariany.genesisframework.advancement.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ObtainAdvancementCriterion extends AbstractCriterion<ObtainAdvancementCriterion.Conditions> {
    @Override
    public Codec<Conditions> getConditionsCodec() {
        return Conditions.CODEC;
    }

    public void trigger(ServerPlayerEntity player, AdvancementEntry advancementEntry) {
        this.trigger(player, conditions -> conditions.matches(advancementEntry));
    }

    public record Conditions(Optional<LootContextPredicate> player, Identifier advancementId)
            implements AbstractCriterion.Conditions {
        public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                                EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC.optionalFieldOf("player").forGetter(Conditions::player),
                                Identifier.CODEC.fieldOf("advancement").forGetter(Conditions::advancementId)
                        )
                        .apply(instance, Conditions::new)
        );

        public static AdvancementCriterion<Conditions> create(Identifier advancementId) {
            return create(null, advancementId);
        }

        public static AdvancementCriterion<Conditions> create(@Nullable LootContextPredicate playerPredicate, Identifier advancementId) {
            return GFCriteria.OBTAIN_ADVANCEMENT.create(new Conditions(Optional.ofNullable(playerPredicate), advancementId));
        }

        public boolean matches(AdvancementEntry advancementEntry) {
            return this.advancementId.equals(advancementEntry.id());
        }
    }
}

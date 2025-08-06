package dev.mariany.genesisframework.packet.clientbound;

import dev.mariany.genesisframework.GenesisFramework;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.recipe.Ingredient;

import java.util.List;

public record UpdateLockedItems(List<Ingredient> items) implements CustomPayload {
    public static final Id<UpdateLockedItems> ID = new Id<>(
            GenesisFramework.id("update_locked_items"));
    public static final PacketCodec<RegistryByteBuf, UpdateLockedItems> CODEC = PacketCodec.tuple(
            Ingredient.PACKET_CODEC.collect(PacketCodecs.toList()), UpdateLockedItems::items, UpdateLockedItems::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}

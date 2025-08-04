package dev.mariany.genesisframework.packet.clientbound;

import dev.mariany.genesisframework.GenesisFramework;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record NotifyAgeLockedPayload(String itemTranslation, String ageTranslation,
                                     boolean clickInteraction) implements CustomPayload {
    public static final Id<NotifyAgeLockedPayload> ID = new Id<>(
            GenesisFramework.id("notify_age_locked"));
    public static final PacketCodec<RegistryByteBuf, NotifyAgeLockedPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, NotifyAgeLockedPayload::itemTranslation,
            PacketCodecs.STRING, NotifyAgeLockedPayload::ageTranslation,
            PacketCodecs.BOOLEAN, NotifyAgeLockedPayload::clickInteraction,
            NotifyAgeLockedPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}

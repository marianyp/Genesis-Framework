package dev.mariany.genesisframework.mixin;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.mariany.genesisframework.age.AgeEntry;
import dev.mariany.genesisframework.age.AgeManager;
import dev.mariany.genesisframework.registry.GFRegistryKeys;
import net.minecraft.command.CommandSource;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;

@Mixin(ServerCommandSource.class)
public class ServerCommandSourceMixin {
    @Inject(method = "listIdSuggestions", at = @At(value = "HEAD"), cancellable = true)
    public void injectListIdSuggestions(
            RegistryKey<? extends Registry<?>> registryRef,
            CommandSource.SuggestedIdType suggestedIdType,
            SuggestionsBuilder builder,
            CommandContext<?> context,
            CallbackInfoReturnable<CompletableFuture<Suggestions>> cir
    ) {
        if (registryRef == GFRegistryKeys.AGE) {
            CompletableFuture<Suggestions> suggestions = CommandSource.suggestIdentifiers(
                    AgeManager.getInstance().getAges().stream().map(AgeEntry::getId),
                    builder
            );

            cir.setReturnValue(suggestions);
        }
    }
}

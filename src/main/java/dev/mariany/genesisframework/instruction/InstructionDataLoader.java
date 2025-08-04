package dev.mariany.genesisframework.instruction;

import com.google.common.collect.ImmutableMap;
import dev.mariany.genesisframework.config.ConfigHandler;
import dev.mariany.genesisframework.registry.GFRegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;

import java.util.Map;

public class InstructionDataLoader extends JsonDataLoader<Instruction> {
    public InstructionDataLoader(RegistryWrapper.WrapperLookup registries) {
        super(registries, Instruction.CODEC, GFRegistryKeys.INSTRUCTION);
    }

    @Override
    protected void apply(Map<Identifier, Instruction> map, ResourceManager manager, Profiler profiler) {
        if (ConfigHandler.getConfig().enableInstructions) {
            InstructionManager instructionManager = InstructionManager.getInstance();
            instructionManager.clear();

            ImmutableMap.Builder<Identifier, InstructionEntry> builder = ImmutableMap.builder();

            map.forEach((id, instruction) -> builder.put(id, new InstructionEntry(id, instruction)));

            builder.buildOrThrow().values().forEach(instructionManager::add);
        }
    }
}

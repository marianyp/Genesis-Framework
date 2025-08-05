package dev.mariany.genesisframework.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.mariany.genesisframework.GenesisFramework;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigHandler {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File("config/genesis-framework.json5");
    private static GFConfig config = new GFConfig();

    public static GFConfig getConfig() {
        return config;
    }

    public static void loadConfig() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                config = GSON.fromJson(reader, GFConfig.class);
            } catch (IOException error) {
                GenesisFramework.LOGGER.error("Failed to load config: {}", error.getMessage());
            }
        }

        saveConfig();
    }

    private static void saveConfig() {
        try {
            if (CONFIG_FILE.getParentFile().mkdirs()) {
                GenesisFramework.LOGGER.info("Creating parent directory for {} config", GenesisFramework.MOD_ID);
            }

            try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
                GSON.toJson(config, writer);
            }
        } catch (IOException error) {
            GenesisFramework.LOGGER.error("Failed to save config: {}", error.getMessage());
        }
    }
}

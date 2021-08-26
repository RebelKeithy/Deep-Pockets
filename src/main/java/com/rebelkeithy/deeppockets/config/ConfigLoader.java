package com.rebelkeithy.deeppockets.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;

public class ConfigLoader {
    private static final String CONFIG_DIR = FabricLoader.getInstance().getConfigDir() + "\\";

    public Config loadConfigFile(String filename) {
        Config config;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            config = gson.fromJson(new FileReader(new File(CONFIG_DIR, filename)), Config.class);
        } catch (FileNotFoundException e) {
            System.out.println("Generating config file for mod: toughness bars.");
            config = new Config();
        }
        saveConfigFile(filename, config);
        return config;
    }

    public void saveConfigFile(String filename, Config config) {
        try {
            var writer = new FileWriter(CONFIG_DIR + filename);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(config, writer);
            writer.flush();
            writer.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}

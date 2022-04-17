package cc.carm.lib.configuration;

import cc.carm.lib.configuration.spigot.SpigotConfigProvider;

import java.io.File;
import java.io.IOException;

public class EasyConfiguration {


    public static SpigotConfigProvider from(File file, String source) {
        SpigotConfigProvider provider = new SpigotConfigProvider(file);
        try {
            provider.initializeFile(source);
            provider.initializeConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return provider;
    }

    public static SpigotConfigProvider from(File file) {
        return from(file, file.getName());
    }

    public static SpigotConfigProvider from(String fileName) {
        return from(fileName, fileName);
    }

    public static SpigotConfigProvider from(String fileName, String source) {
        return from(new File(fileName), source);
    }


}

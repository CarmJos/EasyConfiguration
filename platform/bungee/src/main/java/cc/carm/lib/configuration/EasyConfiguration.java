package cc.carm.lib.configuration;

import cc.carm.lib.configuration.bungee.BungeeConfigProvider;

import java.io.File;
import java.io.IOException;

public class EasyConfiguration {

    public static BungeeConfigProvider from(File file, String source) {
        BungeeConfigProvider provider = new BungeeConfigProvider(file);
        try {
            provider.initializeFile(source);
            provider.initialize();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return provider;
    }

    public static BungeeConfigProvider from(File file) {
        return from(file, file.getName());
    }

    public static BungeeConfigProvider from(String fileName) {
        return from(fileName, fileName);
    }

    public static BungeeConfigProvider from(String fileName, String source) {
        return from(new File(fileName), source);
    }


}

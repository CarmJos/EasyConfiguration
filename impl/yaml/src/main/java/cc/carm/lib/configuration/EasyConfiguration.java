package cc.carm.lib.configuration;

import cc.carm.lib.configuration.yaml.YamlConfigProvider;

import java.io.File;
import java.io.IOException;

public class EasyConfiguration {

    public static YamlConfigProvider from(File file, String source) {
        YamlConfigProvider provider = new YamlConfigProvider(file);
        try {
            provider.initializeFile(source);
            provider.initializeConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return provider;
    }

    public static YamlConfigProvider from(File file) {
        return from(file, file.getName());
    }

    public static YamlConfigProvider from(String fileName) {
        return from(fileName, fileName);
    }

    public static YamlConfigProvider from(String fileName, String source) {
        return from(new File(fileName), source);
    }


}

package cc.carm.lib.configuration;

import cc.carm.lib.configuration.yaml.YAMLConfigProvider;

import java.io.File;
import java.io.IOException;

public class EasyConfiguration {

    public static YAMLConfigProvider from(File file, String source) {
        YAMLConfigProvider provider = new YAMLConfigProvider(file);
        try {
            provider.initializeFile(source);
            provider.initializeConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return provider;
    }

    public static YAMLConfigProvider from(File file) {
        return from(file, file.getName());
    }

    public static YAMLConfigProvider from(String fileName) {
        return from(fileName, fileName);
    }

    public static YAMLConfigProvider from(String fileName, String source) {
        return from(new File(fileName), source);
    }

}

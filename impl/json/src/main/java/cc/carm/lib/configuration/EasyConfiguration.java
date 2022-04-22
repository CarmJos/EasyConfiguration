package cc.carm.lib.configuration;

import cc.carm.lib.configuration.json.JSONConfigProvider;

import java.io.File;
import java.io.IOException;

public class EasyConfiguration {

    public static JSONConfigProvider from(File file, String source) {
        JSONConfigProvider provider = new JSONConfigProvider(file);
        try {
            provider.initializeFile(source);
            provider.initializeConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return provider;
    }

    public static JSONConfigProvider from(File file) {
        return from(file, file.getName());
    }

    public static JSONConfigProvider from(String fileName) {
        return from(fileName, fileName);
    }

    public static JSONConfigProvider from(String fileName, String source) {
        return from(new File(fileName), source);
    }

}

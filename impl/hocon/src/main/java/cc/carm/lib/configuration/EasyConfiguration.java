package cc.carm.lib.configuration;

import cc.carm.lib.configuration.hocon.HOCONFileConfigProvider;

import java.io.File;
import java.io.IOException;

public class EasyConfiguration {
    public static HOCONFileConfigProvider from(File file, String source) {
        HOCONFileConfigProvider provider = new HOCONFileConfigProvider(file);
        try {
            provider.initializeFile(source);
            provider.initializeConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return provider;
    }

    public static HOCONFileConfigProvider from(File file) {
        return from(file, file.getName());
    }

    public static HOCONFileConfigProvider from(String fileName) {
        return from(fileName, fileName);
    }

    public static HOCONFileConfigProvider from(String fileName, String source) {
        return from(new File(fileName), source);
    }
}

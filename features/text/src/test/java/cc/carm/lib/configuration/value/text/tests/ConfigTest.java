package cc.carm.lib.configuration.value.text.tests;


import cc.carm.lib.configuration.source.ConfigurationHolder;
import cc.carm.lib.configuration.source.yaml.YAMLConfigFactory;
import cc.carm.lib.configuration.value.text.tests.conf.AppMessages;
import org.junit.Test;

public class ConfigTest {


    public static final String[] WEBSITES = new String[]{
            "https://carm.cc",
            "https://www.baidu.com",
            "https://www.google.com"
    };


    @Test
    public void test() {

        ConfigurationHolder<?> holder = YAMLConfigFactory.from("target/messages.yml").build();

        holder.initialize(AppMessages.class);

        System.out.println("--------------------------");

        AppMessages.WELCOME.prepare()
                .placeholders("Carm")
                .insert("guidance")
                .insert("websites", WEBSITES)
                .to(System.out);

        System.out.println("--------------------------");

        AppMessages.NOT_AVAILABLE.sendTo(System.out);

        System.out.println("--------------------------");

        AppMessages.NO_PERMISSION.sendTo(System.out);

        System.out.println("--------------------------");

    }


}

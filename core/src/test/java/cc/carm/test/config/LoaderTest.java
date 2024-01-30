package cc.carm.test.config;

import cc.carm.lib.configuration.adapter.ValueAdapterRegistry;
import cc.carm.lib.configuration.annotation.ConfigPath;
import cc.carm.lib.configuration.Configuration;
import cc.carm.lib.configuration.source.ConfigurationProvider;
import cc.carm.lib.configuration.loader.ConfigurationLoader;
import cc.carm.lib.easyoptions.OptionHolder;
import org.junit.Test;

public class LoaderTest {

    @Test
    public void test() throws Exception {
        ConfigurationProvider<TestSource> provider = new ConfigurationProvider<>(new TestSource(), new ConfigurationLoader(), new ValueAdapterRegistry(), new OptionHolder());

        ConfigurationLoader loader = new ConfigurationLoader();
        loader.load(provider, ROOT.class);
    }

    interface ROOT extends Configuration {

        interface SUB extends Configuration {


        }


        @ConfigPath(root = true)
        interface EXTERNAL extends Configuration {


        }

        @ConfigPath("NO")
        interface YES extends Configuration {


        }

    }
}
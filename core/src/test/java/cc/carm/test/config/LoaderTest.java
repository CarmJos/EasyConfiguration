package cc.carm.test.config;

import cc.carm.lib.configuration.adapter.ValueAdapterRegistry;
import cc.carm.lib.configuration.annotation.ConfigPath;
import cc.carm.lib.configuration.Configuration;
import cc.carm.lib.configuration.source.ConfigurationProvider;
import cc.carm.lib.configuration.source.loader.ConfigurationLoader;
import cc.carm.lib.configuration.source.option.ConfigurationOptionHolder;
import org.junit.Test;

import java.util.concurrent.ConcurrentHashMap;

public class LoaderTest {

    @Test
    public void test() throws Exception {
        ConfigurationProvider<TestSource> provider = new ConfigurationProvider<>(new TestSource(), new ConfigurationLoader(), new ValueAdapterRegistry(), new ConfigurationOptionHolder(), new ConcurrentHashMap<>());

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
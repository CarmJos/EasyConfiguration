package config.source;

import cc.carm.lib.configuration.core.ConfigurationRoot;
import cc.carm.lib.configuration.core.annotation.ConfigPath;
import cc.carm.lib.configuration.core.value.ConfigValue;
import cc.carm.lib.configuration.yaml.value.ConfiguredSerializable;
import config.model.AbstractModel;
import config.model.TestModel;

@ConfigPath(root = true)
public class ImplConfiguration extends ConfigurationRoot {

    public static final ConfigValue<? extends AbstractModel> TEST = ConfiguredSerializable.of(TestModel.class, TestModel.random());


}

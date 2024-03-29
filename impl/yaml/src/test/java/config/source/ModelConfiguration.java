package config.source;

import cc.carm.lib.configuration.core.ConfigurationRoot;
import cc.carm.lib.configuration.core.annotation.ConfigPath;
import cc.carm.lib.configuration.core.annotation.HeaderComment;
import cc.carm.lib.configuration.core.value.ConfigValue;
import cc.carm.lib.configuration.core.value.type.ConfiguredList;
import cc.carm.lib.configuration.core.value.type.ConfiguredMap;
import cc.carm.lib.configuration.core.value.type.ConfiguredSectionMap;
import cc.carm.lib.configuration.demo.tests.model.AbstractModel;
import cc.carm.lib.configuration.yaml.value.ConfiguredSerializable;
import config.model.AnyModel;
import config.model.SomeModel;

@HeaderComment("以下内容用于测试序列化")
@ConfigPath("model-test")
public class ModelConfiguration extends ConfigurationRoot {

    public static final ConfigValue<? extends AbstractModel> SOME_MODEL = ConfiguredSerializable.of(
            SomeModel.class, SomeModel.random()
    );

    public static final ConfigValue<? extends AbstractModel> ANY_MODEL = ConfiguredSerializable.of(
            AnyModel.class, AnyModel.random()
    );

    public static final ConfiguredList<AnyModel> MODELS = ConfiguredList.builderOf(AnyModel.class)
            .fromMap()
            .parseValue(AnyModel::deserialize).serializeValue(AnyModel::serialize)
            .defaults(AnyModel.random(), AnyModel.random(), AnyModel.random())
            .build();

    public static final ConfiguredSectionMap<String, AnyModel> MODEL_MAP = ConfiguredMap.builderOf(String.class, AnyModel.class)
            .asLinkedMap().fromSection()
            .parseValue(v -> new AnyModel(v.getString("name", "EMPTY"), v.getBoolean("state", false)))
            .serializeValue(AnyModel::serialize)
            .defaults(m -> {
                m.put("a", AnyModel.random());
                m.put("b", AnyModel.random());
            })
            .build();

}

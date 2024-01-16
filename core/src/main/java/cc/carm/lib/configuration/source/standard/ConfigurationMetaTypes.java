package cc.carm.lib.configuration.source.standard;

import cc.carm.lib.configuration.annotation.ConfigPath;
import cc.carm.lib.easyannotation.AnnotatedMetaType;

public interface ConfigurationMetaTypes {

    AnnotatedMetaType<ConfigPath, String> PATH = AnnotatedMetaType.of(ConfigPath.class, ConfigPath::value);

    AnnotatedMetaType<ConfigPath, Boolean> ROOT = AnnotatedMetaType.of(ConfigPath.class, ConfigPath::root);

}

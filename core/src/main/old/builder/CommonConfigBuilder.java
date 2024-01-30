package cc.carm.lib.configuration.builder;

public abstract class CommonConfigBuilder<T, B extends CommonConfigBuilder<T, B>>
        extends AbstractConfigBuilder<T, B, ConfigurationProvider<?>> {

    protected CommonConfigBuilder() {
        super(ConfigurationProvider.class);
    }

}

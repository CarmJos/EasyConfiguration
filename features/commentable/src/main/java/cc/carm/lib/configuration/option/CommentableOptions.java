package cc.carm.lib.configuration.option;

import cc.carm.lib.configuration.source.option.ConfigurationOption;

public interface CommentableOptions {

//    /**
//     * Whether to keep modified comments in configuration,
//     * that means we only set comments for values that are not exists in configuration.
//     */
//    ConfigurationOption<Boolean> KEEP_COMMENTS = ConfigurationOption.of(true);

    /**
     * Whether to comment values name that are not exists in configuration and no default value offered.
     * <br>If true, a value without default value is not exists in configuration, we will comment its name,
     * <p>e.g. a value named "foo" without default value will be put as:
     * <blockquote><pre>
     * # Value comments
     * # foo:
     * </pre></blockquote>
     */
    ConfigurationOption<Boolean> COMMENT_EMPTY_VALUE = ConfigurationOption.of(false);


//    /**
//     * The resource path of the comment file.
//     * <p>
//     * Use this option to specify the path of the comment file,
//     * which will be used to load comments for the configuration.
//     * </p>
//     */
//    ConfigurationOption<String> COMMENT_RESOURCE = ConfigurationOption.of(String.class, "config-comments");


}

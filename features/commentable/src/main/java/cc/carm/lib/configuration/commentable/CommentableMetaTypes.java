package cc.carm.lib.configuration.commentable;

import cc.carm.lib.configuration.annotation.FooterComment;
import cc.carm.lib.configuration.annotation.HeaderComment;
import cc.carm.lib.configuration.annotation.InlineComment;
import cc.carm.lib.configuration.source.ConfigurationHolder;
import cc.carm.lib.configuration.source.loader.ConfigurationInitializer;
import cc.carm.lib.configuration.source.meta.ConfigurationMetadata;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public interface CommentableMetaTypes {

    /**
     * Configuration's {@link HeaderComment}
     */
    ConfigurationMetadata<List<String>> HEADER_COMMENTS = ConfigurationMetadata.of(Collections.emptyList());

    /**
     * Configuration's footer comments
     */
    ConfigurationMetadata<List<String>> FOOTER_COMMENTS = ConfigurationMetadata.of(Collections.emptyList());

    /**
     * Configuration's {@link InlineComment}
     */
    ConfigurationMetadata<String> INLINE_COMMENT = ConfigurationMetadata.of();


    static void register(@NotNull ConfigurationHolder<?> provider) {
        register(provider.initializer());
    }

    static void register(@NotNull ConfigurationInitializer initializer) {
        initializer.registerAnnotation(
                HeaderComment.class, HEADER_COMMENTS,
                a -> Arrays.asList(a.value())
        );
        initializer.registerAnnotation(
                FooterComment.class, FOOTER_COMMENTS,
                a -> Arrays.asList(a.value())
        );
        initializer.registerAnnotation(InlineComment.class, INLINE_COMMENT, InlineComment::value);
    }

}

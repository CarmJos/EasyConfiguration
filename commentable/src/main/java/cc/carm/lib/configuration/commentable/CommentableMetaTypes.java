package cc.carm.lib.configuration.commentable;

import cc.carm.lib.configuration.annotation.HeaderComment;
import cc.carm.lib.configuration.annotation.InlineComment;
import cc.carm.lib.easyannotation.AnnotatedMetaType;

import java.util.Arrays;
import java.util.List;

public interface CommentableMetaTypes {

    /**
     * Configuration's {@link HeaderComment}
     */
    AnnotatedMetaType<HeaderComment, List<String>> HEADER_COMMENT = AnnotatedMetaType.of(
            HeaderComment.class, h -> h.value().length == 0 ? null : Arrays.asList(h.value())
    );

    /**
     * Configuration's {@link InlineComment}
     */
    AnnotatedMetaType<InlineComment, String> INLINE_COMMENT = AnnotatedMetaType.of(
            InlineComment.class, c -> c.value().isEmpty() ? null : c.value()
    );

}

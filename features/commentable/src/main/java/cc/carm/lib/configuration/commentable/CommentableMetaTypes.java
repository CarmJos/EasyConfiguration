package cc.carm.lib.configuration.commentable;

import cc.carm.lib.configuration.annotation.HeaderComment;
import cc.carm.lib.configuration.annotation.InlineComment;
import cc.carm.lib.configuration.value.meta.ValueMetaType;

import java.util.Collections;
import java.util.List;

public interface CommentableMetaTypes {

    /**
     * Configuration's {@link HeaderComment}
     */
    ValueMetaType<List<String>> HEADER_COMMENTS = ValueMetaType.of(Collections.emptyList());

    /**
     * Configuration's {@link InlineComment}
     */
    ValueMetaType<String> INLINE_COMMENT_VALUE = ValueMetaType.of();

}

package cc.carm.lib.configuration.commentable;

import cc.carm.lib.configuration.annotation.HeaderComment;
import cc.carm.lib.configuration.annotation.InlineComment;
import cc.carm.lib.configuration.meta.PathMetadata;

import java.util.Collections;
import java.util.List;

public interface CommentableMetaTypes {

    /**
     * Configuration's {@link HeaderComment}
     */
    PathMetadata<List<String>> HEADER_COMMENTS = PathMetadata.of(Collections.emptyList());

    /**
     * Configuration's {@link InlineComment}
     */
    PathMetadata<String> INLINE_COMMENT_VALUE = PathMetadata.of();

}

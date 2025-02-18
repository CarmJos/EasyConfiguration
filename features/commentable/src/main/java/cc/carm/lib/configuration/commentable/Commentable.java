package cc.carm.lib.configuration.commentable;

import cc.carm.lib.configuration.source.ConfigurationHolder;
import cc.carm.lib.configuration.source.loader.ConfigurationInitializer;
import cc.carm.lib.configuration.source.option.StandardOptions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public class Commentable {

    private Commentable() {
        throw new UnsupportedOperationException();
    }

    public static void registerMeta(@NotNull ConfigurationInitializer initializer) {
        CommentableMeta.register(initializer);
    }

    public static @Nullable String getInlineComment(@NotNull ConfigurationHolder<?> holder, @NotNull String path) {
        String comment = getInlineComment(holder, path, null);
        if (comment != null) return comment;

        String sep = String.valueOf(holder.option(StandardOptions.PATH_SEPARATOR));

        // If the comment is not found, try to get the comment from the parent section
        String[] keys = path.split(Pattern.quote(sep));
        if (keys.length == 1) return null;

        // Try every possible parent key&child key combination
        for (int i = 1; i < keys.length; i++) {
            String parentKey = String.join(sep, Arrays.copyOfRange(keys, 0, i));
            String childKey = String.join(sep, Arrays.copyOfRange(keys, i, keys.length));
            comment = getInlineComment(holder, parentKey, childKey);
            if (comment != null) return comment;
        }
        return null;
    }

    public static @Nullable String getInlineComment(@NotNull ConfigurationHolder<?> holder, @NotNull String path, @Nullable String sectionKey) {
        Map<String, String> pathComment = holder.metadata(path).get(CommentableMeta.INLINE);
        if (pathComment == null || pathComment.isEmpty()) return null;
        if (sectionKey == null) return pathComment.get(null);
        for (Map.Entry<String/*regex*/, String/*content*/> entry : pathComment.entrySet()) {
            if (entry.getKey() == null) continue;
            if (Objects.equals(entry.getKey(), sectionKey)) return entry.getValue();
            Pattern pattern = Pattern.compile(entry.getKey().replace(".", "\\.").replace("*", "(.*)"));
            if (pattern.matcher(sectionKey).matches()) return entry.getValue();
        }
        return null;
    }

    public static @Nullable List<String> getHeaderComments(@NotNull ConfigurationHolder<?> holder, @Nullable String path) {
        return holder.metadata(path).get(CommentableMeta.HEADER);
    }

    public static @Nullable List<String> getFooterComments(@NotNull ConfigurationHolder<?> holder, @Nullable String path) {
        return holder.metadata(path).get(CommentableMeta.FOOTER);
    }

}

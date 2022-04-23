package cc.carm.lib.configuration.core.source;

import cc.carm.lib.configuration.core.annotation.ConfigComment;
import cc.carm.lib.configuration.core.value.ConfigValue;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public class ConfigCommentInfo {

    public static @NotNull ConfigCommentInfo DEFAULT_INFO = of(new String[0], true, false);

    protected final @NotNull String[] comments;
    protected final boolean startWrap;
    protected final boolean endWrap;

    public ConfigCommentInfo(@NotNull String[] comments, boolean startWrap, boolean endWrap) {
        this.comments = comments;
        this.startWrap = startWrap;
        this.endWrap = endWrap;
    }

    public @NotNull String[] getComments() {
        return comments;
    }

    public boolean endWrap() {
        return endWrap;
    }

    public boolean startWrap() {
        return startWrap;
    }

    public static @NotNull ConfigCommentInfo of(@NotNull String[] comments, boolean startWrap, boolean endWrap) {
        return new ConfigCommentInfo(comments, startWrap, endWrap);
    }

    public static @NotNull ConfigCommentInfo defaults() {
        return DEFAULT_INFO;
    }

    @Contract("!null->!null")
    public static @Nullable ConfigCommentInfo fromAnnotation(@Nullable ConfigComment comment) {
        if (comment == null) return null;
        else return new ConfigCommentInfo(comment.value(), comment.startWrap(), comment.endWrap());
    }

    public static @NotNull ConfigCommentInfo fromValue(@NotNull ConfigValue<?> value) {
        return Optional.ofNullable(value.getComments()).orElse(defaults());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConfigCommentInfo that = (ConfigCommentInfo) o;
        return startWrap == that.startWrap && endWrap == that.endWrap && Arrays.equals(getComments(), that.getComments());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(startWrap, endWrap);
        result = 31 * result + Arrays.hashCode(getComments());
        return result;
    }

    @Override
    public String toString() {
        return "ConfigCommentInfo{" +
                "comments=" + Arrays.toString(comments) +
                ", startWrap=" + startWrap +
                ", endWrap=" + endWrap +
                '}';
    }
}

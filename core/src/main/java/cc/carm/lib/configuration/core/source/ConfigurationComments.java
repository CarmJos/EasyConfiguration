package cc.carm.lib.configuration.core.source;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;

public abstract class ConfigurationComments {

    protected final @NotNull Map<String, List<String>> headerComments = new HashMap<>();
    protected final @NotNull Map<String, String> inlineComments = new HashMap<>();

    protected @NotNull Map<String, List<String>> getHeaderComments() {
        return headerComments;
    }

    protected @NotNull Map<String, String> getInlineComments() {
        return inlineComments;
    }

    public void setHeaderComments(@Nullable String path, @Nullable List<String> comments) {

        if (comments == null) {
            getHeaderComments().remove(path);
        } else {
            getHeaderComments().put(path, comments);
        }
    }


    public void setInlineComment(@NotNull String path, @Nullable String comment) {
        if (comment == null) {
            getInlineComments().remove(path);
        } else {
            getInlineComments().put(path, comment);
        }
    }

    @Nullable
    @Unmodifiable
    public List<String> getHeaderComment(@Nullable String path) {
        return Optional.ofNullable(getHeaderComments().get(path)).map(Collections::unmodifiableList).orElse(null);
    }

    public @Nullable String getInlineComment(@NotNull String path) {
        return getInlineComments().get(path);
    }


}

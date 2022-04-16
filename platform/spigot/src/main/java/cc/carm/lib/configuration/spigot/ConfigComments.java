package cc.carm.lib.configuration.spigot;

import cc.carm.lib.configuration.commented.CommentsProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ConfigComments implements CommentsProvider {

    Map<String, String[]> comments = new HashMap<>();

    protected Map<String, String[]> getComments() {
        return comments;
    }

    public void set(@NotNull String path, @NotNull String... comments) {
        if (comments.length == 0) {
            getComments().remove(path);
        } else {
            getComments().put(path, comments);
        }
    }

    public @Nullable String[] get(@NotNull String path) {
        return getComments().get(path);
    }

    @Override
    public String[] apply(String s) {
        return get(s);
    }

}

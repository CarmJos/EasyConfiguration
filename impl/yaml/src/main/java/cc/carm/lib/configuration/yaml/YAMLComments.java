package cc.carm.lib.configuration.yaml;

import org.bspfsystems.yamlconfiguration.configuration.ConfigurationSection;
import org.bspfsystems.yamlconfiguration.file.FileConfiguration;
import org.bspfsystems.yamlconfiguration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static cc.carm.lib.configuration.yaml.YAMLConfigProvider.SEPARATOR;

public class YAMLComments {

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

    public @Nullable String buildHeaderComments(@Nullable String path, @NotNull String indents) {
        List<String> comments = getHeaderComment(path);
        if (comments == null || comments.size() == 0) return null;

        StringJoiner joiner = new StringJoiner("\n");
        for (String comment : comments) {
            if (comment.length() == 0) joiner.add(" ");
            else joiner.add(indents + "# " + comment);
        }
        return joiner + "\n";
    }

    /**
     * ??????????????????????????????????????????????????????????????????
     * ???????????????????????????????????? tchristofferson/ConfigUpdater ?????????
     *
     * @param source ???????????????
     * @param writer ???????????????
     * @throws IOException ??????????????????????????????
     */
    public void writeComments(@NotNull YamlConfiguration source, @NotNull BufferedWriter writer) throws IOException {
        FileConfiguration temp = new YamlConfiguration(); // ???????????????????????????????????????

        String configHeader = buildHeaderComments(null, "");
        if (configHeader != null) writer.write(configHeader);

        for (String fullKey : source.getKeys(true)) {
            Object currentValue = source.get(fullKey);

            String indents = getIndents(fullKey);
            String headerComments = buildHeaderComments(fullKey, indents);
            String inlineComment = getInlineComment(fullKey);

            if (headerComments != null) writer.write(headerComments);

            String[] splitFullKey = fullKey.split("[" + SEPARATOR + "]");
            String trailingKey = splitFullKey[splitFullKey.length - 1];

            if (currentValue instanceof ConfigurationSection) {
                ConfigurationSection section = (ConfigurationSection) currentValue;
                writer.write(indents + trailingKey + ":");
                if (inlineComment != null && inlineComment.length() > 0) {
                    writer.write(" # " + inlineComment);
                }
                if (!section.getKeys(false).isEmpty()) {
                    writer.write("\n");
                } else {
                    writer.write(" {}\n");
                    if (indents.length() == 0) writer.write("\n");
                }
                continue;
            }

            temp.set(trailingKey, currentValue);
            String yaml = temp.saveToString();
            temp.set(trailingKey, null);

            yaml = yaml.substring(0, yaml.length() - 1);

            if (inlineComment != null && inlineComment.length() > 0) {
                if (yaml.contains("\n")) {
                    // section???????????????????????? InlineComment ??????????????????
                    String[] splitLine = yaml.split("\n", 2);
                    yaml = splitLine[0] + " # " + inlineComment + "\n" + splitLine[1];
                } else {
                    // ?????????????????????????????????????????????
                    yaml += " # " + inlineComment;
                }
            }

            writer.write(indents + yaml.replace("\n", "\n" + indents) + "\n");
            if (indents.length() == 0) writer.write("\n");
        }

        writer.close();
    }

    /**
     * ???????????????????????????
     * ??????????????????????????? tchristofferson/ConfigUpdater ?????????
     *
     * @param key ???
     * @return ?????????????????????
     */
    protected static String getIndents(String key) {
        String[] splitKey = key.split("[" + YAMLConfigProvider.SEPARATOR + "]");
        return IntStream.range(1, splitKey.length).mapToObj(i -> "  ").collect(Collectors.joining());
    }

}

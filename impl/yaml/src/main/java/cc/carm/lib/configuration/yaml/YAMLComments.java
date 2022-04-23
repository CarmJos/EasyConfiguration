package cc.carm.lib.configuration.yaml;

import cc.carm.lib.configuration.core.source.ConfigCommentInfo;
import org.bspfsystems.yamlconfiguration.configuration.ConfigurationSection;
import org.bspfsystems.yamlconfiguration.file.FileConfiguration;
import org.bspfsystems.yamlconfiguration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static cc.carm.lib.configuration.yaml.YAMLConfigProvider.SEPARATOR;

public class YAMLComments {

    Map<String, ConfigCommentInfo> comments = new HashMap<>();

    protected Map<String, ConfigCommentInfo> getComments() {
        return comments;
    }

    public void set(@Nullable String path, @Nullable ConfigCommentInfo comments) {
        if (comments == null) {
            getComments().remove(path);
        } else {
            getComments().put(path, comments);
        }
    }

    public @NotNull ConfigCommentInfo get(@Nullable String path) {
        return getComments().getOrDefault(path, ConfigCommentInfo.defaults());
    }

    public @Nullable String buildComments(@NotNull String indents, @Nullable String path) {
        ConfigCommentInfo comments = get(path);
        if (!String.join("", comments.getComments()).isEmpty()) {
            String prefix = comments.startWrap() ? "\n" : "";
            String suffix = comments.endWrap() ? "\n" : "";
            StringJoiner joiner = new StringJoiner("\n", prefix, suffix);
            for (String comment : comments.getComments()) {
                if (comment.length() == 0) joiner.add(" ");
                else joiner.add(indents + "# " + comment);
            }
            return joiner + "\n";
        } else {
            return comments.startWrap() || comments.endWrap() ? "\n" : null;
        }
    }

    /**
     * 从一个文件读取配置并写入注释到某个写入器中。
     * 该方法的部分源代码借鉴自 tchristofferson/ConfigUpdater 项目。
     *
     * @param source 源配置文件
     * @param writer 配置写入器
     * @throws IOException 当写入发生错误时抛出
     */
    public void writeComments(@NotNull YamlConfiguration source, @NotNull BufferedWriter writer) throws IOException {
        FileConfiguration temp = new YamlConfiguration(); // 该对象用于临时记录配置内容

        for (String fullKey : source.getKeys(true)) {
            String indents = getIndents(fullKey);
            String comment = buildComments(indents, fullKey);
            if (comment != null) writer.write(comment);

            Object currentValue = source.get(fullKey);

            String[] splitFullKey = fullKey.split("[" + SEPARATOR + "]");
            String trailingKey = splitFullKey[splitFullKey.length - 1];

            if (currentValue instanceof ConfigurationSection) {
                writer.write(indents + trailingKey + ":");
                if (!((ConfigurationSection) currentValue).getKeys(false).isEmpty()) {
                    writer.write("\n");
                } else {
                    writer.write(" {}\n");
                }
                continue;
            }

            temp.set(trailingKey, currentValue);
            String yaml = temp.saveToString();
            yaml = yaml.substring(0, yaml.length() - 1).replace("\n", "\n" + indents);
            String toWrite = indents + yaml + "\n";
            temp.set(trailingKey, null);

            writer.write(toWrite);
        }

        String endComment = buildComments("", null);
        if (endComment != null) writer.write(endComment);

        writer.close();
    }

    /**
     * 得到一个键的缩进。
     * 该方法的源代码来自 tchristofferson/ConfigUpdater 项目。
     *
     * @param key 键
     * @return 该键的缩进文本
     */
    protected static String getIndents(String key) {
        String[] splitKey = key.split("[" + YAMLConfigProvider.SEPARATOR + "]");
        return IntStream.range(1, splitKey.length).mapToObj(i -> "  ").collect(Collectors.joining());
    }

}

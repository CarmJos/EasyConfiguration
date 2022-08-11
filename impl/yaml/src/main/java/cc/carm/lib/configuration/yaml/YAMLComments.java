package cc.carm.lib.configuration.yaml;

import cc.carm.lib.configuration.core.source.ConfigurationComments;
import org.bspfsystems.yamlconfiguration.configuration.ConfigurationSection;
import org.bspfsystems.yamlconfiguration.file.FileConfiguration;
import org.bspfsystems.yamlconfiguration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static cc.carm.lib.configuration.yaml.YAMLConfigProvider.SEPARATOR;

public class YAMLComments extends ConfigurationComments {

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
     * 从一个文件读取配置并写入注释到某个写入器中。
     * 该方法的部分源代码借鉴自 tchristofferson/ConfigUpdater 项目。
     *
     * @param source 源配置文件
     * @param writer 配置写入器
     * @throws IOException 当写入发生错误时抛出
     */
    public void writeComments(@NotNull YamlConfiguration source, @NotNull BufferedWriter writer) throws IOException {
        FileConfiguration temp = new YamlConfiguration(); // 该对象用于临时记录配置内容

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
                    // section为多行内容，需要 InlineComment 加在首行末尾
                    String[] splitLine = yaml.split("\n", 2);
                    yaml = splitLine[0] + " # " + inlineComment + "\n" + splitLine[1];
                } else {
                    // 其他情况下就直接加载后面就好。
                    yaml += " # " + inlineComment;
                }
            }

            writer.write(indents + yaml.replace("\n", "\n" + indents) + "\n");
            if (indents.length() == 0) writer.write("\n");
        }

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

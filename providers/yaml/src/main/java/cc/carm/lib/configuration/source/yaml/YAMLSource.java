package cc.carm.lib.configuration.source.yaml;

import cc.carm.lib.configuration.commentable.CommentableMeta;
import cc.carm.lib.configuration.option.CommentableOptions;
import cc.carm.lib.configuration.source.ConfigurationHolder;
import cc.carm.lib.configuration.source.file.FileConfigSource;
import cc.carm.lib.configuration.source.option.StandardOptions;
import cc.carm.lib.configuration.source.section.ConfigureSection;
import cc.carm.lib.configuration.source.section.MemorySection;
import cc.carm.lib.yamlcommentupdater.CommentedSection;
import cc.carm.lib.yamlcommentupdater.CommentedYAMLWriter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.nodes.*;
import org.yaml.snakeyaml.reader.UnicodeReader;
import org.yaml.snakeyaml.representer.Representer;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.Reader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;

public class YAMLSource extends FileConfigSource<MemorySection, Map<?, ?>, YAMLSource> implements CommentedSection {

    protected final @NotNull YamlConstructor yamlConstructor;
    protected final @NotNull YamlRepresenter yamlRepresenter;
    protected final @NotNull Yaml yaml;

    protected @Nullable MemorySection rootSection;

    protected YAMLSource(@NotNull ConfigurationHolder<? extends YAMLSource> holder,
                         @NotNull File file, @Nullable String resourcePath) {
        super(holder, 0, file, resourcePath);
        this.yamlConstructor = new YamlConstructor(loaderOptions());
        this.yamlRepresenter = new YamlRepresenter(dumperOptions());
        this.yaml = new Yaml(this.yamlConstructor, this.yamlRepresenter, dumperOptions());

        initialize();
    }

    public void initialize() {
        try {
            initializeFile();
            onReload();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected YAMLSource self() {
        return null;
    }

    @Override
    public @NotNull Map<?, ?> original() {
        return section().data();
    }

    @Override
    public @NotNull MemorySection section() {
        return Objects.requireNonNull(this.rootSection, "Root section is not initialized.");
    }

    @Override
    public char separator() {
        return holder().options().get(StandardOptions.PATH_SEPARATOR);
    }

    public @NotNull LoaderOptions loaderOptions() {
        return holder().options().get(YAMLOptions.LOADER);
    }

    public @NotNull DumperOptions dumperOptions() {
        return holder().options().get(YAMLOptions.DUMPER);
    }

    @NotNull
    private MappingNode toNodeTree(@NotNull final ConfigureSection section) {
        List<NodeTuple> nodeTuples = new ArrayList<>();
        for (final Map.Entry<String, Object> entry : section.getValues(false).entrySet()) {
            Node keyNode = this.yaml.represent(entry.getKey());
            Node valueNode;
            if (entry.getValue() instanceof ConfigureSection) {
                valueNode = this.toNodeTree((ConfigureSection) entry.getValue());
            } else {
                valueNode = this.yaml.represent(entry.getValue());
            }
            nodeTuples.add(new NodeTuple(keyNode, valueNode));
        }

        return new MappingNode(Tag.MAP, nodeTuples, DumperOptions.FlowStyle.BLOCK);
    }


    @NotNull
    public String saveToString(ConfigureSection section) {
        MappingNode mappingNode = this.toNodeTree(section);
        StringWriter writer = new StringWriter();
        if ((mappingNode.getBlockComments() == null || mappingNode.getBlockComments().isEmpty())
                && (mappingNode.getEndComments() == null || mappingNode.getEndComments().isEmpty())
                && (mappingNode.getInLineComments() == null || mappingNode.getInLineComments().isEmpty())
                && mappingNode.getValue().isEmpty()) {
            writer.write("");
        } else {
            if (mappingNode.getValue().isEmpty()) {
                mappingNode.setFlowStyle(DumperOptions.FlowStyle.FLOW);
            }
            this.yaml.serialize(mappingNode, writer);
        }
        return writer.toString();
    }

    @Override
    public void save() throws Exception {
        CommentedYAMLWriter writer = new CommentedYAMLWriter(
                String.valueOf(this.separator()),
                dumperOptions().getIndent(),
                holder.options().get(CommentableOptions.COMMENT_EMPTY_VALUE)
        );
        try {
            fileWriter(w -> w.write(writer.saveToString(this)));
        } catch (Exception ex) {
            fileWriter(w -> w.write(saveToString(section())));
        }
    }

    @Override
    protected void onReload() throws Exception {
        this.rootSection = fileReadString(this::loadFromString);
    }

    @Override
    public String toString() {
        return this.saveToString(section());
    }

    public @NotNull MemorySection loadFromString(@NotNull String data) throws Exception {
        MappingNode mappingNode;
        try (Reader reader = new UnicodeReader(new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8)))) {
            Node rawNode = this.yaml.compose(reader);
            mappingNode = (MappingNode) rawNode;
        }
        if (mappingNode == null) return MemorySection.root(this);

        Map<String, Object> map = new LinkedHashMap<>();
        this.constructMap(mappingNode, map);
        return MemorySection.root(this, map);
    }

    private void constructMap(@NotNull MappingNode mappingNode, @NotNull Map<String, Object> section) {
        this.yamlConstructor.flattenMapping(mappingNode);
        for (final NodeTuple nodeTuple : mappingNode.getValue()) {

            final Node keyNode = nodeTuple.getKeyNode();
            final String key = String.valueOf(this.yamlConstructor.construct(keyNode));
            Node valueNode = nodeTuple.getValueNode();

            while (valueNode instanceof AnchorNode) {
                valueNode = ((AnchorNode) valueNode).getRealNode();
            }

            if (valueNode instanceof MappingNode) {
                Map<String, Object> child = new LinkedHashMap<>();
                this.constructMap((MappingNode) valueNode, child);
                section.put(key, child);
            } else {
                section.put(key, this.yamlConstructor.construct(valueNode));
            }
        }
    }

    @Override
    public String serializeValue(@NotNull String key, @NotNull Object value) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put(key, value);
        return saveToString(MemorySection.root(this, map));
    }

    @Override
    public @Nullable Set<String> getKeys(@Nullable String sectionKey, boolean deep) {
        if (sectionKey == null) return section().getKeys(deep);
        ConfigureSection sub = section().getSection(sectionKey);
        if (sub == null) return null;
        return sub.getKeys(deep);
    }

    @Override
    public @Nullable Object getValue(@NotNull String key) {
        return get(key);
    }

    @Override
    public @Nullable String getInlineComment(@NotNull String key) {
        String comment = getInlineComment(key, null);
        if (comment != null) return comment;

        String sep = String.valueOf(separator());
        
        // If the comment is not found, try to get the comment from the parent section
        String[] keys = key.split(sep);
        if (keys.length == 1) return null;

        // Try every possible parent key&child key combination
        for (int i = 1; i < keys.length; i++) {
            String parentKey = String.join(sep, Arrays.copyOfRange(keys, 0, i));
            String childKey = String.join(sep, Arrays.copyOfRange(keys, i, keys.length));
            comment = getInlineComment(childKey, parentKey);
            if (comment != null) return comment;
        }
        return null;
    }

    public @Nullable String getInlineComment(@NotNull String key, @Nullable String sectionKey) {
        Map<String, String> pathComment = holder().metadata(key).get(CommentableMeta.INLINE);
        if (pathComment == null || pathComment.isEmpty()) return null;
        if (sectionKey == null) return pathComment.get(null);

        for (Map.Entry<String, String> entry : pathComment.entrySet()) {
            if (entry.getKey().equals(sectionKey)) return entry.getValue();
            Pattern pattern = Pattern.compile(entry.getKey().replace(".", "\\.").replace("*", ".*"));
            if (pattern.matcher(sectionKey).matches()) return entry.getValue();
        }
        return null;
    }

    @Override
    public @Nullable List<String> getHeaderComments(@Nullable String key) {
        return holder().metadata(key).get(CommentableMeta.HEADER);
    }

    @Override
    public @Nullable List<String> getFooterComments(@Nullable String key) {
        return holder().metadata(key).get(CommentableMeta.FOOTER);
    }

    public static class YamlRepresenter extends Representer {

        public YamlRepresenter(@NotNull final DumperOptions dumperOptions) {
            super(dumperOptions);
            this.multiRepresenters.put(ConfigureSection.class, new RepresentMap() {
                @NotNull
                @Override
                public Node representData(@NotNull final Object object) {
                    return super.representData(((ConfigureSection) object).getValues(false));
                }
            });
            this.multiRepresenters.remove(Enum.class);
        }
    }

    public static class YamlConstructor extends SafeConstructor {

        public YamlConstructor(@NotNull final LoaderOptions loaderOptions) {
            super(loaderOptions);
        }

        @Nullable
        Object construct(@NotNull final Node node) {
            return this.constructObject(node);
        }

        @Override
        public void flattenMapping(@NotNull final MappingNode mappingNode) {
            super.flattenMapping(mappingNode);
        }
    }


}

package cc.carm.lib.configuration.source.yaml;

import cc.carm.lib.configuration.commentable.CommentableMetaTypes;
import cc.carm.lib.configuration.source.ConfigurationHolder;
import cc.carm.lib.configuration.source.file.FileConfigSource;
import cc.carm.lib.configuration.source.meta.ConfigurationMetadata;
import cc.carm.lib.configuration.source.section.ConfigureSection;
import cc.carm.lib.configuration.source.section.MemorySection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.comments.CommentLine;
import org.yaml.snakeyaml.comments.CommentType;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.nodes.*;
import org.yaml.snakeyaml.reader.UnicodeReader;
import org.yaml.snakeyaml.representer.Representer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class YAMLSource extends FileConfigSource<MemorySection, Map<?, ?>, YAMLSource> {

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

            final Node keyNode = this.yaml.represent(entry.getKey());
            final Node valueNode;
            if (entry.getValue() instanceof ConfigureSection) {
                valueNode = this.toNodeTree((ConfigureSection) entry.getValue());
            } else {
                valueNode = this.yaml.represent(entry.getValue());
            }

            keyNode.setBlockComments(buildComments(CommentType.BLOCK, CommentableMetaTypes.HEADER_COMMENTS, entry.getKey()));
            if (valueNode instanceof MappingNode || valueNode instanceof SequenceNode) {
                keyNode.setInLineComments(buildComment(CommentType.IN_LINE, CommentableMetaTypes.INLINE_COMMENT, entry.getKey()));
            } else {
                valueNode.setInLineComments(buildComment(CommentType.IN_LINE, CommentableMetaTypes.INLINE_COMMENT, entry.getKey()));
            }
//            keyNode.setEndComments(buildComments(CommentType.BLOCK, CommentableMetaTypes.FOOTER_COMMENTS, entry.getKey()));

            nodeTuples.add(new NodeTuple(keyNode, valueNode));
        }

        return new MappingNode(Tag.MAP, nodeTuples, DumperOptions.FlowStyle.BLOCK);
    }

    public List<CommentLine> buildComments(@NotNull CommentType type, @NotNull ConfigurationMetadata<List<String>> meta,
                                           @Nullable String path) {
        List<String> comments = holder.metadata(path).get(meta);
        if (comments == null) return Collections.emptyList();
        return comments.stream().map(s -> {
            if (s.isEmpty()) return new CommentLine(null, null, "", CommentType.BLANK_LINE);
            else return new CommentLine(null, null, s, type);
        }).collect(Collectors.toList());
    }

    public List<CommentLine> buildComment(@NotNull CommentType type, @NotNull ConfigurationMetadata<String> meta,
                                          @Nullable String path) {
        String comment = holder.metadata(path).get(meta);
        if (comment == null || comment.isEmpty()) return Collections.emptyList();
        return Collections.singletonList(new CommentLine(null, null, comment, type));
    }


    @NotNull
    public String saveToString() {

        MappingNode mappingNode = this.toNodeTree(this);
//        mappingNode.setBlockComments(this.getCommentLines(this.saveHeader(this.getOptions().getHeader()), CommentType.BLOCK));
//        mappingNode.setEndComments(this.getCommentLines(this.getOptions().getFooter(), CommentType.BLOCK));

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
        fileWriter(w -> w.write(saveToString()));
    }

    @Override
    protected void onReload() throws Exception {
        this.rootSection = fileReadString(this::loadFromString);
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
        protected void flattenMapping(@NotNull final MappingNode mappingNode) {
            super.flattenMapping(mappingNode);
        }
    }


}

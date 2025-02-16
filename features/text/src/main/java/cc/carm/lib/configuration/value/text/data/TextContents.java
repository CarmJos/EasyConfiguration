package cc.carm.lib.configuration.value.text.data;

import cc.carm.lib.configuration.source.section.ConfigureSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class TextContents {

    public static Builder builder() {
        return new Builder();
    }

    public static TextContents of(@NotNull List<String> lines, @NotNull Map<String, List<String>> optional) {
        return new TextContents(lines, optional);
    }

    protected final @NotNull List<String> lines;
    protected final @NotNull Map<String, List<String>> optional;

    public TextContents(@NotNull List<String> lines, @NotNull Map<String, List<String>> optional) {
        this.lines = lines;
        this.optional = optional;
    }

    public boolean isEmpty() {
        return lines.isEmpty() || lines.stream().allMatch(String::isEmpty);
    }

    public @NotNull List<String> lines() {
        return lines;
    }

    public @NotNull Map<String, List<String>> optionalLines() {
        return optional;
    }

    public @Nullable Object serialize() {
        if (optional.isEmpty()) {
            if (lines.isEmpty()) return null;
            else if (lines.size() == 1) return lines.get(0);
            else return lines;
        } else {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("contents", lines);
            map.put("optional", optional);
            return map;
        }
    }

    public static @Nullable TextContents deserialize(@NotNull Object data) {
        Builder builder = builder();
        if (data instanceof String) {
            return builder.set((String) data).build();
        } else if (data instanceof List<?>) {
            ((List<?>) data).stream().map(Object::toString).forEach(builder::add);
            return builder.build();
        } else if (data instanceof ConfigureSection) {
            ConfigureSection section = (ConfigureSection) data;
            builder.set(section.getStringList("contents"));

            ConfigureSection optionalSection = section.getSection("optional");
            if (optionalSection != null) {
                for (String key : optionalSection.getKeys(false)) {
                    builder.optional(key, optionalSection.getStringList(key));
                }
            }
            return builder.build();
        }
        return null;
    }

    public static class Builder {

        protected List<String> lines = new ArrayList<>();
        protected Map<String, List<String>> optional = new HashMap<>();

        /**
         * Add lines to the contents
         *
         * @param lines lines to add
         * @return this builder
         */
        public Builder add(@NotNull String... lines) {
            this.lines.addAll(Arrays.asList(lines));
            return this;
        }

        /**
         * Add lines to the contents
         *
         * @param lines lines to add
         * @return this builder
         */
        public Builder add(@NotNull Iterable<String> lines) {
            lines.forEach(this.lines::add);
            return this;
        }

        /**
         * Set the lines of the contents
         *
         * @param lines lines to set
         * @return this builder
         */
        public Builder set(@NotNull String... lines) {
            this.lines = Arrays.asList(lines);
            return this;
        }

        /**
         * Set the lines of the contents
         *
         * @param lines lines to set
         * @return this builder
         */
        public Builder set(@NotNull Iterable<String> lines) {
            this.lines = new ArrayList<>();
            lines.forEach(this.lines::add);
            return this;
        }

        /**
         * Add optional lines to the contents
         *
         * @param key   key of the optional lines
         * @param lines lines to add
         * @return this builder
         */
        public Builder optional(@NotNull String key, @NotNull String... lines) {
            optional.put(key, Arrays.asList(lines));
            return this;
        }

        /**
         * Add optional lines to the contents
         *
         * @param key   key of the optional lines
         * @param lines lines to add
         * @return this builder
         */
        public Builder optional(@NotNull String key, @NotNull Iterable<String> lines) {
            List<String> list = new ArrayList<>();
            lines.forEach(list::add);
            optional.put(key, list);
            return this;
        }

        /**
         * @return The built TextContents
         */
        public TextContents build() {
            return of(lines, optional);
        }

    }


}

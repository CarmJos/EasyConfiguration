package cc.carm.test.config.offset;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author Chris2018998
 */
public class FieldOffset implements Comparable<FieldOffset> {
    private final Field field;
    private Long offsetValue;
    private List<FieldOffset> subFieldOffsetList;

    public FieldOffset(Field field) {
        this.field = field;
    }

    public Long getOffsetValue() {
        return offsetValue;
    }

    public void setOffsetValue(Long offsetValue) {
        this.offsetValue = offsetValue;
    }

    public String toString() {
        if (subFieldOffsetList == null)
            return field.getName();
        else {

            StringBuilder builder = new StringBuilder();
            builder.append("[");

            for (int i = 0; i < subFieldOffsetList.size(); i++) {
                FieldOffset offset = subFieldOffsetList.get(i);
                if (i > 0) builder.append(",");
                builder.append(field.getName()).append(".").append(offset.toString());
            }
            builder.append("]");
            return builder.toString();
        }
    }

    public List<FieldOffset> getSubFieldOffsetList() {
        return subFieldOffsetList;
    }

    public void setSubFieldOffsetList(List<FieldOffset> subFieldOffsetList) {
        this.subFieldOffsetList = subFieldOffsetList;
    }

    public Field getField() {
        return field;
    }

    @Override
    public int compareTo(@NotNull FieldOffset that) {
        return this.offsetValue.compareTo(that.offsetValue);
    }


}

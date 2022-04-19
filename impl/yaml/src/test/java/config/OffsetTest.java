package config;

import config.offset.FieldOffset;
import config.offset.OffsetUtil;
import config.source.ComplexConfiguration;
import org.junit.Test;

import java.util.List;

public class OffsetTest {

    @Test
    public void test() {


        List<FieldOffset> fieldOffsets = OffsetUtil.getClassMemberOffset(ComplexConfiguration.class);

        output(fieldOffsets);

    }

    protected static void output(List<FieldOffset> fieldOffsets) {
        for (FieldOffset fieldOffset : fieldOffsets) {
            System.out.println(fieldOffset.getOffsetValue() + " -> " + fieldOffset.getField().getName());
            if (!fieldOffset.getSubFieldOffsetList().isEmpty()) {
                output(fieldOffset.getSubFieldOffsetList());
            }
        }
    }

}

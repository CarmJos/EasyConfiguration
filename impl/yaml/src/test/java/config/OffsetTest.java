package config;

import config.offset.FieldOffset;
import config.offset.OffsetUtil;
import config.source.DemoConfiguration;

import java.util.List;

public class OffsetTest {

    //    @Test
    public void test() {
//
        output(OffsetUtil.getClassMemberOffset(DemoConfiguration.class));
        output(OffsetUtil.getClassMemberOffset(DemoConfiguration.Sub.class));

    }

    protected static void output(List<FieldOffset> fieldOffsets) {
        for (FieldOffset fieldOffset : fieldOffsets) {
            System.out.println(fieldOffset.getOffsetValue() + " -> " + fieldOffset);
        }
    }

}

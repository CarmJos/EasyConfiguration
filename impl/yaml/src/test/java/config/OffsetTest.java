package config;

import config.offset.FieldOffset;

import java.util.List;

public class OffsetTest {

//    @Test
//    public void test() {
//
//        output(OffsetUtil.getClassMemberOffset(ComplexConfiguration.class));
//        output(OffsetUtil.getClassMemberOffset(ComplexConfiguration.Sub.class));
//
//    }

    protected static void output(List<FieldOffset> fieldOffsets) {
        for (FieldOffset fieldOffset : fieldOffsets) {
            System.out.println(fieldOffset.getOffsetValue() + " -> " + fieldOffset);
        }
    }

}

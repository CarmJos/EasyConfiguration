package test.section;

import cc.carm.lib.configuration.source.section.ConfigureSection;
import cc.carm.lib.configuration.source.section.MemorySection;
import cc.carm.lib.configuration.source.section.ShadedSection;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Lyzen
 * @date 21/2/2025 下午2:18
 */
public class ShadeTest {

    @Test
    public void test() {
        ConfigureSection template = MemorySection.of(data -> {
            data.put("name", "GentleMan");
            data.put("age", 12);
            data.put("gender", "male");
            Map<String, Object> address = new LinkedHashMap<>();
            address.put("Hotel", "Nanjing Road 101");
            address.put("Store", "Beijing Road 404");
            Map<String, Object> mapInside = new LinkedHashMap<>();
            mapInside.put("InsideKeyExample", "InsideValueExample");
            address.put("Inside", mapInside);
            data.put("addresses", address);
            data.put("cards", Arrays.asList("00000", "11111", "22222"));
        });
        ConfigureSection source = MemorySection.of(data -> {
            data.put("age", 25);
            Map<String, Object> address = new LinkedHashMap<>();
            address.put("NewOne", "Guangdong Road 505");
            data.put("addresses", address);
            Map<String, Object> mapInside = new LinkedHashMap<>();
            mapInside.put("AnotherInsideKey", "AnotherInsideValue");
            address.put("Inside", mapInside);
            data.put("cards", Arrays.asList("33333", "55555")); // 应当直接覆盖原先的List
        });

        ShadedSection root = new ShadedSection(template, source);
        System.out.println("age: "+root.get("age"));
        System.out.println("addresses: ");
        for (Map.Entry<String, Object> entry : root.getSection("addresses").getValues(false).entrySet()) {
            System.out.println("  "+entry.getKey()+": "+entry.getValue());
            if (entry.getValue() instanceof ConfigureSection) {
                for(Map.Entry<String, Object> inner : ((ConfigureSection) entry.getValue()).getValues(false).entrySet()) {
                    System.out.println("    "+inner.getKey()+": "+inner.getValue());
                }
            }
        }
        System.out.println("cards: "+root.getList("cards"));

        System.out.println("\n----------------------\n");
        System.out.println("Deep Search Test");
        System.out.println("addresses: ");
        for (Map.Entry<String, Object> entry : root.getSection("addresses").getValues(true).entrySet()) {
            System.out.println("  "+entry.getKey()+": "+entry.getValue());
        }

        System.out.println("\n----------------------\n");

        root.set("addresses", MemorySection.of(map->{
            map.put("Hotel", "Nanjing Road 101");
            map.put("Store", "Beijing Road banned");

        }));

        for (Map.Entry<String, Object> entry : source.getValues(true).entrySet()) {
            System.out.println("  "+entry.getKey()+": "+entry.getValue());
        }
    }

}

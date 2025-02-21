package test.section;

import cc.carm.lib.configuration.source.section.ConfigureSection;
import cc.carm.lib.configuration.source.section.MemorySection;
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
            address.put("Parent", "BeijingRoad 404");
            data.put("addresses", address);
            data.put("cards", Arrays.asList("00000", "11111", "22222"));
        });
        ConfigureSection source = MemorySection.of(data -> {
            data.put("age", 25);
            Map<String, Object> address = new LinkedHashMap<>();
            address.put("NewOne", "Guangdong Road 505");
            data.put("addresses", address);
            data.put("cards", Arrays.asList("33333", "55555")); // 应当直接覆盖原先的List
        });





    }

}

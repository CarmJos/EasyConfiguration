import cc.carm.lib.configuration.core.ConfigInitializer;
import org.junit.Test;

public class NameTest {


    @Test
    public void onTest() {

        System.out.println(ConfigInitializer.getSectionName("LoveGames")); // -> love-games
        System.out.println(ConfigInitializer.getSectionName("EASY_GAME")); // -> easy-game
        System.out.println(ConfigInitializer.getSectionName("F")); //-? f
        System.out.println(ConfigInitializer.getSectionName("Test123123")); // -? test123123123

    }


}

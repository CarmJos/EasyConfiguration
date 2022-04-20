import cc.carm.lib.configuration.core.ConfigInitializer;
import org.junit.Test;

public class NameTest {


    @Test
    public void onTest() {

        System.out.println(ConfigInitializer.getPathFromName("LoveGames")); // -> love-games
        System.out.println(ConfigInitializer.getPathFromName("EASY_GAME")); // -> easy-game
        System.out.println(ConfigInitializer.getPathFromName("F")); //-? f
        System.out.println(ConfigInitializer.getPathFromName("Test123123")); // -? test123123123

    }


}

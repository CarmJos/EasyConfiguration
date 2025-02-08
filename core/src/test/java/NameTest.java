import org.junit.Test;

public class NameTest {


    @Test
    public void onTest() {

        System.out.println(PathGenerator.covertPathName("LoveGames")); // -> love-games
        System.out.println(PathGenerator.covertPathName("EASY_GAME")); // -> easy-game
        System.out.println(PathGenerator.covertPathName("F")); //-? f
        System.out.println(PathGenerator.covertPathName("Test123123")); // -? test123123123

    }


}

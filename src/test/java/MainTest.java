import lombok.Getter;
import org.junit.jupiter.api.Test;

public class MainTest {
    public static final String TITLE = "Spark Renderer Test";

    @Getter
    private static WindowManager window;
    private static EngineManager engine;

    @Test
    public void test() {
        window = new WindowManager(TITLE, 1600, 800, false, false, false);
        engine = new EngineManager();

        try {
            engine.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
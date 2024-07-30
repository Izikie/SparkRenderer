import org.junit.jupiter.api.Test;

public class BasicRenderWindowTest {

    @Test
    public void test() {
        WindowManager window = new WindowManager("Test", 1600, 800, true, true);
        window.init();

        while (!window.isOpen())
            window.update();

        window.cleanup();
    }
}
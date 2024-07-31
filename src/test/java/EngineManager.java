import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

public class EngineManager {
    public static final long NANOSECONDS = 1000000000L;
    public static final float FRAMERATE = 1000;

    private static int fps;
    private static float frameTime = 1 / FRAMERATE;

    private boolean isRunning;

    private WindowManager window;
    private GLFWErrorCallback errorCallBack;

    private void init() throws Exception {
        GLFW.glfwSetErrorCallback(errorCallBack = GLFWErrorCallback.createPrint(System.err));
        window = MainTest.getWindow();
        window.init();
    }

    public void start() throws Exception {
        init();
        if (isRunning)
            return;
        run();
    }

    private void run() {
        isRunning = true;

        int frames = 0;
        long frameCounter = 0;
        long lastTime = System.nanoTime();
        double unProcessedTime = 0;

        while (isRunning) {
            boolean render = false;
            long startTime = System.nanoTime();
            long passedTime = startTime - lastTime;
            lastTime = startTime;

            unProcessedTime += passedTime / (double) NANOSECONDS;
            frameCounter += passedTime;

            input();

            while (unProcessedTime > frameTime) {
                render = true;
                unProcessedTime -= frameTime;

                if (window.shouldClose())
                    stop();

                if (frameCounter >= NANOSECONDS) {
                    fps = frames;
                    window.setTitle(MainTest.TITLE+ " | " + fps);
                    frames = 0;
                    frameCounter = 0;
                }

            }

            if (render) {
                update();
                render();
                frames++;
            }
        }
        cleanup();
    }

    private void cleanup() {
        window.cleanup();
        errorCallBack.free();
        GLFW.glfwTerminate();
    }

    private void stop() {

    }

    private void input() {

    }

    private void render() {
        window.update();
    }

    private void update() {

    }
}
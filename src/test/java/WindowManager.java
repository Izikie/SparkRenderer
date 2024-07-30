import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryUtil;

// Basic Window Manager From Tutorial For Testing
public class WindowManager {
    public static final float FOV = (float) Math.toRadians(60);
    public static final float Z_NEAR = 0.01F;
    public static final float Z_FAR = 1000F;

    private final String title;
    private int width, height;
    private boolean reSizeable, fullScreen, vSync;

    private long window;
    private final Matrix4f projectionMatrix;

    public WindowManager(String title, int width, int height, boolean fullscreen, boolean vSync) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.fullScreen = fullscreen;
        this.vSync = vSync;
        projectionMatrix = new Matrix4f();
    }

    public void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!GLFW.glfwInit())
            throw new IllegalStateException("Unable to initialization GLFW");

        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GL20.GL_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GL20.GL_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GL20.GL_TRUE);

        if (width == 0 || height == 0) {
            width = 100;
            height = 100;
            GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, GL20.GL_TRUE);
            fullScreen = true;
        }

        window = GLFW.glfwCreateWindow(width, height, "Test", MemoryUtil.NULL, MemoryUtil.NULL);
        if (window == MemoryUtil.NULL)
            throw new RuntimeException("Failed to create GLFW window");

        GLFW.glfwSetFramebufferSizeCallback(window, (window, width, height) -> {
            this.width = width;
            this.height = height;
        });

        GLFW.glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (action == GLFW.GLFW_PRESS) {
                switch (key) {
                    case GLFW.GLFW_KEY_ESCAPE -> GLFW.glfwSetWindowShouldClose(window, true);
                    case GLFW.GLFW_KEY_F11 -> toggleFullScreen(window);
                }
            }
        });

        toggleFullScreen(window);

        GLFW.glfwMakeContextCurrent(window);

        if (vSync)
            GLFW.glfwSwapInterval(1);

        GLFW.glfwShowWindow(window);

        GL.createCapabilities();

        GL20.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
        GL20.glEnable(GL20.GL_DEPTH_TEST);
        GL20.glEnable(GL20.GL_STENCIL_TEST);
        GL20.glEnable(GL20.GL_CULL_FACE);
        GL20.glCullFace(GL20.GL_BACK);
    }

    public void update() {
        GLFW.glfwSwapBuffers(window);
        GLFW.glfwPollEvents();
    }

    public void cleanup() {
        GLFW.glfwDestroyWindow(window);
    }

    public void toggleFullScreen(long window) {
        setFullScreen(window, fullScreen = !fullScreen);
    }

    public void setFullScreen(long window, boolean fullScreen) {
        if (fullScreen) {
            GLFW.glfwDestroyWindow(window);
            window = GLFW.glfwCreateWindow(width, height, "Test", MemoryUtil.NULL, MemoryUtil.NULL);
            if (window == MemoryUtil.NULL)
                throw new RuntimeException("Failed to create fullscreen GLFW window");

            GLFW.glfwMaximizeWindow(window);
        } else {
            GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
            GLFW.glfwSetWindowPos(window, (vidMode.width() - width) / 2, (vidMode.height() - height) / 2);
        }
    }

    public boolean isKeyPressed(int keyCode) {
        return GLFW.glfwGetKey(window, keyCode) == GLFW.GLFW_PRESS;
    }

    public boolean isOpen() {
        return GLFW.glfwWindowShouldClose(window);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public Matrix4f updateProjectionMatrix() {
        float aspectRation = (float) width / height;
        return projectionMatrix.setPerspective(FOV, aspectRation, Z_NEAR, Z_FAR);
    }
}
package dango;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.glfw.GLFW.*;

public class Window {
    private int width, height;
    private String title;
    private long glfwWindow;
    private static Window window = null;

    private Window(){
        this.width = 1920;
        this.height = 1080;
        this.title = "Dango Engine";
    }

    public static Window get(){
        if (Window.window == null){
            Window.window = new Window();
        }
        return Window.window;
    }

    public void run(){
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();
    }

    private void init(){
        //Error callback
        GLFWErrorCallback.createPrint(System.err).set();

        //Init GLFW
        if(!glfwInit()){
            throw new IllegalStateException("GLFW initialisation failed.");
        }

        //GLFW configuration
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, 0);
        glfwWindowHint(GLFW_RESIZABLE, 1);
        glfwWindowHint(GLFW_MAXIMIZED, 1);

        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if (glfwWindow == NULL)
            throw new IllegalStateException("GLFW windows creation failed.");

        //Make OpenGL context current
        glfwMakeContextCurrent(glfwWindow);

        //V-Sync
        glfwSwapInterval(1);

        //Make window visible
        glfwShowWindow(glfwWindow);

        GL.createCapabilities();
    }

    private void loop(){
        while(!glfwWindowShouldClose(glfwWindow)){
            //Poll events
            glfwPollEvents();

            glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);

            glfwSwapBuffers(glfwWindow);

        }
    }
}

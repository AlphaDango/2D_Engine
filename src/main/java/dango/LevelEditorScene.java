package dango;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import renderer.Shader;
import renderer.Texture;
import util.Time;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class LevelEditorScene extends Scene {

    private String vertexShaderSource = "#version 330 core\n" +
            "layout (location=0) in vec3 aPos;\n" +
            "layout (location=1) in vec4 aColor;\n" +
            "\n" +
            "out vec4 fColor;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "    fColor = aColor;\n" +
            "    gl_Position = vec4(aPos, 1.0);\n" +
            "}";
    private String fragmentShaderSource = "#version 330 core\n" +
            "\n" +
            "in vec4 fColor;\n" +
            "\n" +
            "out vec4 color;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "    color = fColor;\n" +
            "}";

    private int vertexID, fragmentID, shaderProgram;

    private float[] vertexArray = {
         // postion                 //color
         100.5f, 0.5f,   0.0f,      1.0f, 0.0f, 0.0f, 1.0f,     1,1,//Bottom right
         0.5f,   100.5f, 0.0f,      0.0f, 1.0f, 0.0f, 1.0f,     0,0,//Top left
         100.5f, 100.5f, 0.0f,      0.0f, 0.0f, 1.0f, 1.0f,     1,0,//Top right
         0.5f,   0.5f,   0.0f,      1.0f, 1.0f, 0.0f, 1.0f,     0,1 //Bottom left
    };

    private int[] elementArray = {
        2, 1, 0, //Top right triangle
        0, 1, 3  //Bottom left triangle
    };

    private int vaoID, vboID, eboID;
    private Shader defaultShader;
    private Texture testTexture;

    public LevelEditorScene(){

    }

    @Override
    public void init(){
         this.camera = new Camera(new Vector2f());
         defaultShader = new Shader("assets/shaders/default.glsl");
         defaultShader.compile();

         this.testTexture = new Texture("assets/img/testImg.png");
        /* Generation of VAO, VBO and EBO buffer objects */
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        //Create float buffer vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        //Create VBO and upload vertex buffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER,vboID);
        glBufferData(GL_ARRAY_BUFFER,vertexBuffer,GL_STATIC_DRAW);

        //Create indecies and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER,elementBuffer,GL_STATIC_DRAW);

        //Add vertex attribute pointers
        int positionsSize = 3;
        int colorSize = 4;
        int uvSize = 2;
        int vertexSizeBytes = (positionsSize + colorSize + uvSize) * Float.BYTES;
        glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeBytes,0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize,GL_FLOAT,false,vertexSizeBytes,positionsSize * Float.BYTES);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2,uvSize,GL_FLOAT, false, vertexSizeBytes,(positionsSize + colorSize) * Float.BYTES);
        glEnableVertexAttribArray(2);
    }

    @Override
    public void update(float dt) {
        //camera.position.x -= dt * 50f;
        defaultShader.use();

        //Upload testImage to shader
        defaultShader.uploadTexture("TEX_SAMPLER",0);
        glActiveTexture(GL_TEXTURE0);
        testTexture.bind();

        defaultShader.uploadMatf4("uProjection",camera.getProjectionMatrix());
        defaultShader.uploadMatf4("uView", camera.getViewMatrix());
        defaultShader.uploadFloat("uTime", Time.getTime());
        //Bind VAO
        glBindVertexArray(vaoID);

        //Enable vertex attrib pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT,0);

        //Unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);

        defaultShader.detach();
    }

}

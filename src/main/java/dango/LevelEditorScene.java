package dango;

import components.Sprite;
import components.SpriteRenderer;
import components.Spritesheet;
import org.joml.Vector2f;
import renderer.Texture;
import util.AssetPool;

import static org.lwjgl.glfw.GLFW.*;

public class LevelEditorScene extends Scene {

    public LevelEditorScene() {

    }

    @Override
    public void init() {
        loadResources();
        this.camera = new Camera(new Vector2f(-250, 0));

        Spritesheet sprites = AssetPool.getSpritesheet("assets/img/spritesheet.png");

        GameObject obj1 = new GameObject("Object 1", new Transform(new Vector2f(100, 100), new Vector2f(256, 256)));
        obj1.addComponent(new SpriteRenderer(sprites.getSprite(0)));
        this.addGameObjectToScene(obj1);

        GameObject obj2 = new GameObject("Object 2", new Transform(new Vector2f(400, 100), new Vector2f(256, 256)));
        obj2.addComponent(new SpriteRenderer(sprites.getSprite(25)));
        this.addGameObjectToScene(obj2);

    }

    private void loadResources() {
        AssetPool.getShader("assets/shaders/default.glsl");

        AssetPool.addSpritesheet("assets/img/spritesheet.png",
                new Spritesheet(new Texture("assets/img/spritesheet.png"),
                        16,16,26,0));
    }

    @Override
    public void update(float dt) {
        if (KeyListener.isKeyPressed(GLFW_KEY_RIGHT)) {
            camera.position.x -= 100f * dt;
        } else if (KeyListener.isKeyPressed(GLFW_KEY_LEFT)) {
            camera.position.x += 100f * dt;
        }
        if (KeyListener.isKeyPressed(GLFW_KEY_UP)) {
            camera.position.y -= 100f * dt;
        } else if (KeyListener.isKeyPressed(GLFW_KEY_DOWN)) {
            camera.position.y += 100f * dt;
        }

        for (GameObject go : this.gameObjects) {
            go.update(dt);
        }

        this.renderer.render();
    }
}
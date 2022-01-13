package util;

import renderer.Shader;
import renderer.Texture;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AssetPool {

    private static Map<String, Shader> shaders = new HashMap<>();
    private static Map<String, Texture> textures = new HashMap<>();

    public static Shader getShader(String resName){
        File file = new File(resName);
        if(shaders.containsKey(file.getAbsolutePath())){
            return shaders.get(file.getAbsolutePath());
        }else{
            Shader shader = new Shader(resName);
            shader.compile();
            AssetPool.shaders.put(file.getAbsolutePath(),shader);
            return shader;
        }
    }

    public static Texture getTexture(String resName){
        File file = new File(resName);
        if(textures.containsKey(file.getAbsolutePath())){
            return textures.get(file.getAbsolutePath());
        }else{
            Texture texture = new Texture(resName);
            AssetPool.textures.put(file.getAbsolutePath(),texture);
            return texture;
        }
    }
}
package sun;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import renderEngine.Loader;
import toolbox.Maths;

public class SunMaster {
	
	private static Sun sun;
	private static SunRenderer renderer;
	private static Matrix4f projection;
	private static Camera camera;
	
	public static void init(Loader loader, Vector3f position, float rotationZ, Vector2f scale, Matrix4f projection, Camera camera){
		renderer = new SunRenderer(loader, projection);
		sun = new Sun(position, rotationZ, scale);
		SunMaster.projection = projection;
		SunMaster.camera = camera;
	}
		
	public static void renderSun(Camera camera){
		renderer.render(sun, camera);
	}
	
	public static Vector2f getSunScreenCoords(){
		return Maths.worldToTextureCoords(sun.getPosition(), Maths.createViewMatrix(camera), projection);
	}
	
	public static void cleanUp(){
		renderer.cleanUp();
	}
}

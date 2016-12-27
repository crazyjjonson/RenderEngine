package sun;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public final class Sun {

	private int texture;
	private Vector3f position;
	private float rotationZ;
	private Vector2f scale;
	
	public Sun(Vector3f position, float rotationZ ,Vector2f scale) {
		this.position = position;
		this.rotationZ = rotationZ;
		this.scale = scale;
	}

	public int getTexture() {
		return texture;
	}

	public Vector3f getPosition() {
		return position;
	}

	public Vector2f getScale() {
		return scale;
	}
	
	public float getRotationZ(){
		return rotationZ;
	}
}

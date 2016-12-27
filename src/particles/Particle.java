package particles;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Player;
import renderEngine.DisplayManager;

public class Particle {
	
	
	private ParticleTexture texture;
	private Vector3f position;
	private Vector3f velocity;
	private float gravityEffect;
	private float lifeLength;
	private float rotation;
	private float scale;
	private float elapsedTime = 0;
	private Vector2f currentTextureOffset = new Vector2f();
	private Vector2f nextTextureOffset = new Vector2f();
	private float blendFactor;
	private float distanceToCamera;
	private Vector3f reUsableChange = new Vector3f();


	public Particle(ParticleTexture texture, Vector3f position, Vector3f velocity, float gravityEffect, float lifeLength, float rotation,
			float scale) {
		this.texture = texture;
		this.position = position;
		this.velocity = velocity;
		this.gravityEffect = gravityEffect;
		this.lifeLength = lifeLength;
		this.rotation = rotation;
		this.scale = scale;
		ParticleMaster.addParticle(this);
	}


	public boolean update(Camera camera){
		velocity.y += Player.GRAVITY * gravityEffect * DisplayManager.getFrameTimeSeconds();
		reUsableChange.set(velocity);
		reUsableChange.scale(DisplayManager.getFrameTimeSeconds());
		Vector3f.add(reUsableChange,  position, position);
		distanceToCamera = Vector3f.sub(camera.getPosition(), position, null).lengthSquared();;
		updateTextureOffsetInfo();
		elapsedTime += DisplayManager.getFrameTimeSeconds();
		return elapsedTime < lifeLength;
	}
	
	
	private void updateTextureOffsetInfo(){
		float lifeFactor = elapsedTime / lifeLength;
		int stageCount = texture.getNumberOfRows() * texture.getNumberOfRows();
		float atlasProgression = lifeFactor * stageCount;
		int index1 = (int) Math.floor(atlasProgression);
		int index2 = index1 < stageCount - 1 ? index1 + 1 : index1; 
		this.blendFactor = atlasProgression % 1;
		setTextureOffset(currentTextureOffset, index1);
		setTextureOffset(nextTextureOffset, index2);
	}
	
	
	private void setTextureOffset(Vector2f offset, int index){
		int collumn = index % texture.getNumberOfRows();
		int row = collumn / texture.getNumberOfRows();
		offset.x = (float) collumn / texture.getNumberOfRows();
		offset.y = (float) row / texture.getNumberOfRows();
	}
	
	
	public ParticleTexture getTexture() {
		return texture;
	}


	public Vector3f getPosition() {
		return position;
	}


	public float getRotation() {
		return rotation;
	}


	public float getScale() {
		return scale;
	}
	
	public Vector2f getCurrentTextureOffset() {
		return currentTextureOffset;
	}


	public Vector2f getNextTextureOffset() {
		return nextTextureOffset;
	}


	public float getBlendFactor() {
		return blendFactor;
	}
	
	
	public float getDistanceToCamera() {
		return distanceToCamera;
	}
}

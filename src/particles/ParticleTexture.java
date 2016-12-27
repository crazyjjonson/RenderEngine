package particles;

public class ParticleTexture {
	
	
	private int textureId;
	private int numberOfRows;
	
	
	public int getTextureId() {
		return textureId;
	}
	
	
	public int getNumberOfRows() {
		return numberOfRows;
	}
	
	
	public ParticleTexture(int textureId, int numberOfRows) {
		super();
		this.textureId = textureId;
		this.numberOfRows = numberOfRows;
	}
}

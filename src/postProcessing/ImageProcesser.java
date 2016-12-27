package postProcessing;

public abstract class ImageProcesser {

	private ImageRenderer imageRenderer;
	
	protected ImageProcesser(){
		this.imageRenderer = new ImageRenderer();
	}
	
	protected ImageProcesser(int width, int height){
		this.imageRenderer = new ImageRenderer(width, height);
	}
	
	public abstract void renderToOutputTexture(float... param);
	
	public abstract void renderToScreen(float... param);
	
	protected ImageRenderer getImageRenderer(){
		return imageRenderer;
	}
	
	public int getOutputTexture(){
		return imageRenderer.getOutputTexture();
	}
	
	public void cleanUp(){
		imageRenderer.cleanUp();
	}
}

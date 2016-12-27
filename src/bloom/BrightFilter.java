package bloom;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import postProcessing.ImageProcesser;

public class BrightFilter extends ImageProcesser{

	private BrightFilterShader shader;
	
	public BrightFilter(int width, int height){
		super(width, height);
		shader = new BrightFilterShader();
	}
	
	@Override
	public void renderToOutputTexture(float... param) {
		shader.start();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, (int)param[0]);
		getImageRenderer().renderQuadToOutputTexture();
		shader.stop();
	}

	@Override
	public void renderToScreen(float... param) {	
		shader.start();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, (int)param[0]);
		getImageRenderer().renderQuadToScreen();
		shader.stop();
	}
	
	public void cleanUp(){
		super.cleanUp();
		shader.cleanUp();
	}
}

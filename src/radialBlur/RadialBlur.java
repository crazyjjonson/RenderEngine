package radialBlur;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.util.vector.Vector2f;

import postProcessing.ImageProcesser;
import renderEngine.DisplayManager;

public class RadialBlur extends ImageProcesser{

	private RadialBlurShader shader;
	private static final float EXPOSURE_DEFAULT = 0.0034f;
	private static final float DECLINE_DEFAULT = 1.0f;
	private static final float DENSITY_DEFAULT = 0.84f;
	private static final float WEIGHT_DEFAULT = 5.56f;
	private static final int  NUM_SAMPLES_DEFAULT = 100;
	public static Vector2f center = new Vector2f();
	
	public RadialBlur(int width, int height){
		super(width, height);
		this.shader = new RadialBlurShader();
		shader.start();
		shader.loadRadialBlurCenter(0.3f, 0.3f);
		shader.loadExposure(EXPOSURE_DEFAULT);
		shader.loadDecline(DECLINE_DEFAULT);
		shader.loadDensity(DENSITY_DEFAULT);
		shader.loadWeight(WEIGHT_DEFAULT);
		shader.loatNumSamples(NUM_SAMPLES_DEFAULT);
		shader.stop();
	}
	
	public RadialBlur(int width, int height, float exposure, float decline, 
			float density, float weight, int numSamples){
		
		super(width, height);
		this.shader = new RadialBlurShader();
		shader.start();
		shader.loadRadialBlurCenter(0.3f, 0.3f);
		shader.loadExposure(exposure);
		shader.loadDecline(decline);
		shader.loadDensity(density);
		shader.loadWeight(weight);
		shader.loatNumSamples(numSamples);
		shader.stop();
	}
	
	@Override
	public void renderToOutputTexture(float... param) {
		shader.start();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, (int)param[0]);
		shader.loadRadialBlurCenter(param[1], param[2]);
		getImageRenderer().renderQuadToOutputTexture();
		shader.stop();
	}

	@Override
	public void renderToScreen(float... param) {
		shader.start();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, (int)param[0]);
		shader.loadRadialBlurCenter(param[1], param[2]);
		getImageRenderer().renderQuadToScreen();
		shader.stop();
	}
	
	@Override
	public void cleanUp(){
		super.cleanUp();
		shader.cleanUp();
	}
}

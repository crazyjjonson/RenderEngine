package postProcessing;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import bloom.BrightFilter;
import bloom.CombineFilter;
import contrastChanger.ContrastChanger;
import gaussianBlur.HorizontalBlur;
import gaussianBlur.VerticalBlur;
import models.RawModel;
import radialBlur.RadialBlur;
import renderEngine.Loader;
import sun.SunMaster;

public class PostProcessing {
	
	private static final float[] POSITIONS = { -1, 1, -1, -1, 1, 1, 1, -1 };	
	private static RawModel quad;
	private static ContrastChanger contrastChanger;
	private static HorizontalBlur hBlur;
	private static VerticalBlur vBlur;
	private static BrightFilter brightFilter;
	private static CombineFilter combineFilter;
	private static RadialBlur radialBlur;
	
	public static void init(Loader loader){
		quad = loader.loadToVAO(POSITIONS, 2);
		contrastChanger = new ContrastChanger();
		hBlur = new HorizontalBlur(Display.getWidth() / 10, Display.getHeight() / 10);
		vBlur = new VerticalBlur(Display.getWidth() / 10, Display.getHeight() / 10);
		brightFilter = new BrightFilter(Display.getWidth() / 4, Display.getHeight() / 4);
		radialBlur = new RadialBlur(Display.getWidth() / 2, Display.getHeight() / 2);
		combineFilter = new CombineFilter();
	}
	
	public static void doPostProcessing(int... textures){
		start();
		hBlur.renderToOutputTexture(textures[1]);
		vBlur.renderToOutputTexture(hBlur.getOutputTexture());
		combineFilter.renderToOutputTexture(textures[0], vBlur.getOutputTexture());
		radialBlur.renderToOutputTexture(textures[2], SunMaster.getSunScreenCoords().x, SunMaster.getSunScreenCoords().y);
		combineFilter.renderToScreen(combineFilter.getOutputTexture(), radialBlur.getOutputTexture());
		end();
	}
	
	public static void cleanUp(){
		contrastChanger.cleanUp();
		hBlur.cleanUp();
		vBlur.cleanUp();
		brightFilter.cleanUp();
		combineFilter.cleanUp();
		radialBlur.cleanUp();
	}
	
	private static void start(){
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
	}
	
	private static void end(){
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}


}

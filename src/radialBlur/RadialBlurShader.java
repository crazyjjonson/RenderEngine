package radialBlur;

import org.lwjgl.util.vector.Vector2f;

import shaders.ShaderProgram;

public class RadialBlurShader extends ShaderProgram{

	private static final String VERTEX_FILE = "src/radialBlur/radialBlurVertex.txt";
	private static final String FRAGMENT_FILE = "src/radialBlur/radialBlurFragment.txt";
	
	private int location_radialBlurCenter;
	private int location_exposure;
	private int location_decline;
	private int location_density;
	private int location_weight;
	private int location_numSamples;
	
	public RadialBlurShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		location_radialBlurCenter = super.getUniformLocation("radialBlurCenter");
		location_exposure = super.getUniformLocation("exposure");
		location_decline = super.getUniformLocation("decline");
		location_density = super.getUniformLocation("density");
		location_weight = super.getUniformLocation("weight");
		location_numSamples = super.getUniformLocation("numSamples");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}
	
	protected void loadRadialBlurCenter(float x, float y){
		super.load2DVector(location_radialBlurCenter, new Vector2f(x, y));
	}
	
	protected void loadExposure(float exposure){
		super.loadFloat(location_exposure, exposure);
	}
	
	protected void loadDecline(float decline){
		super.loadFloat(location_decline, decline);
	}
	
	protected void loadDensity(float density){
		super.loadFloat(location_density, density);
	}
	
	protected void loadWeight(float weight){
		super.loadFloat(location_weight, weight);
	}
	
	protected void loatNumSamples(int numSamples){
		super.loadInt(location_numSamples, numSamples);
	}

}

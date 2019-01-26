package pak;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUseProgram;
import org.lwjgl.input.Keyboard;

import pak.GameCamera.GameCameraMode;
import audio.AudioMaster;
import audio.Source;
import rafgl.RGL;
import rafgl.jglm.Vec3;

/**
 * @author Nevena Dresevic
 *
 */
public class Main {
	
	public static int MAX_TIMEOUT = 150;
	public static int CLICKS_NUMBER = 3;

	public static void main(String[] args) {
		RGL.setParami(RGL.IParam.WIDTH, 1280);
	    RGL.setParami(RGL.IParam.HEIGHT, 720);
	    RGL.setParami(RGL.IParam.FULLSCREEN, 1);
	    
	    RGL.setParami(RGL.IParam.MSAA, 4);
	    RGL.setParami(RGL.IParam.VSYNC, 1);
	    
	    RGL.init();
	    RGL.setTitle("None - Find The MONKEY");
	    
	    // MUSIC
	    AudioMaster.init();
	    AudioMaster.setListenerData();
	    
	    int correctAnswerSound = AudioMaster.loadSound("audio/correct.wav");
	    int wrongAnswerSound = AudioMaster.loadSound("audio/wrong.wav");
	    Source source = new Source();
	    
	    // SHADERS
	    GameShader monkeyShader = new GameShader();
	    monkeyShader.loadShader("monkey");
//	    GameShader disappearShader = new GameShader();
//	    disappearShader.loadShader("disappear");
	    GameShader pyramidShader = new GameShader();
	    pyramidShader.loadShader("pyramid");
	    GameShader cubeShader = new GameShader();
	    cubeShader.loadShader("cube");
	    GameShader ghostShader = new GameShader();
	    ghostShader.loadShader("ghost2");
	    GameShader blendShader = new GameShader();
	    blendShader.loadShader("blend");
	    
	    GameCamera camera = new GameCamera(GameCameraMode.GAMECAM_ORBIT);
	    camera.distance = 5.0f;
	    camera.yaw = RGL.HALF_PIf;
	    camera.update();
	    
	    glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
	    
	    int[] mySkybox = new int[6];
	    RGL.loadSkybox("textures/Yokohama", "jpg", mySkybox);
	    int envMap = RGL.loadCubemap("textures/Yokohama", "jpg");
	    
	    // MODELS
	    GameObject monkey = new GameObject();
	    RGL.Model mdlMonkey = RGL.loadModelOBJ("models/monkey-smooth.obj");
	    monkey.model = mdlMonkey;
	    monkey.textures[0] = envMap;
	    monkey.textureType[0] = GL_TEXTURE_CUBE_MAP;
	    
    	PyramidObject pyramidObject = new PyramidObject();
	    pyramidObject.position = new Vec3(-5.0f, 2.7f, -0.5f);
		    
	    CubeObject cubeLeftObject = new CubeObject();
	    cubeLeftObject.position = new Vec3(-3.0f, 0.0f, -0.5f);
	    cubeLeftObject.yaw -= 0.8;
	    
	    CubeObject cubeRightObject = new CubeObject();
	    cubeRightObject.position = new Vec3(3.0f, 0.0f, -0.5f);
	    cubeRightObject.yaw += 0.8;
	    
	    GameObject blendObject = new GameObject();
	    RGL.Model mdlBlend = RGL.loadModelOBJ("models/blendObject.obj");
	    blendObject.model = mdlBlend;
	    int textureBlend = RGL.loadTexture("textures/shutter.jpg");
	    blendObject.textures[0] = textureBlend;
	    blendObject.textureType[0] = GL_TEXTURE_2D;
	    blendObject.shader = blendShader;
	    
	    glEnable(GL_DEPTH_TEST);
	    
	    boolean cubeLeftSelected = false;
	    boolean cubeRightSelected = false;
	    boolean clickedLeftCube = false;
	    boolean clickedRightCube = false;
	    boolean finishAnimation = false;
	    boolean playingSound = false;
	    
	    // TIMERS
	    int timeout = MAX_TIMEOUT;
	    int clicks = CLICKS_NUMBER;
	    int timeoutONE = 2 * MAX_TIMEOUT / 3;
	    int timeoutTWO = 2 * MAX_TIMEOUT / 3;
	    int timeoutTHREE = MAX_TIMEOUT / 2;
	    int timeoutFinish = 2 * MAX_TIMEOUT / 3;
	    int startTimer = MAX_TIMEOUT;
	    
	    int randPosition = (int) ((Math.random() * 3) % 2);
	    float angSpeed = 0.015f;
	    int score = 0;

	    Vec3 blendColor = RGL.RED;
	    Vec3 highlightColor = RGL.RED;
	    
	    RGL.setRunning(true);
	    while(RGL.isRunning()) {
	        RGL.handleEvents();
	        
	        // on left and right the angle of camera changes
	        if (RGL.isKeyDown(Keyboard.KEY_LEFT)) {
	        	camera.yaw += angSpeed;
	        }
	        if (RGL.isKeyDown(Keyboard.KEY_RIGHT)) {
	        	camera.yaw -= angSpeed;
	        }
	        if (RGL.isKeyDown(Keyboard.KEY_DOWN)) {
	        	camera.pitch += angSpeed;
	        }
	        if (RGL.isKeyDown(Keyboard.KEY_UP)) {
	        	camera.pitch -= angSpeed;
	        }
	        
	        if (RGL.isKeyDown(Keyboard.KEY_W)) {
	        	if (camera.zoom < 10) {
	        		camera.zoom *= 1.01f;
	        	}
	        }
	        if (RGL.isKeyDown(Keyboard.KEY_S)) {
	        	if (camera.zoom > 0.4) {
	        		camera.zoom *= 0.99f;
	        	}
	        }
	        if (RGL.isKeyDown(Keyboard.KEY_ESCAPE)) {
	        	RGL.setRunning(false);
	        	source.delete();
	    	    AudioMaster.cleanUp();
	        }
	        
            
            if (cubeLeftSelected && RGL.wasMouseButtonJustPressed(0)) {
            	clickedLeftCube = true;
            }
            if (cubeRightSelected && RGL.wasMouseButtonJustPressed(0)) {
            	clickedRightCube = true;
            }

            camera.update();
	        monkey.updateMatrix();
	        pyramidObject.updateMatrix();
	        cubeLeftObject.updateMatrix();
	        cubeRightObject.updateMatrix();
	        blendObject.updateMatrix();
	        
	        // check if cubes are selected
	        cubeLeftSelected = RGL.rayHitsSphere(camera.position, camera.getMouseViewVector(), cubeLeftObject.position, 1.0f);
	        cubeRightSelected = RGL.rayHitsSphere(camera.position, camera.getMouseViewVector(), cubeRightObject.position, 1.0f);

	        // project 3D position to 2D
	        Vec3 cubeLeftScreenPos = camera.projectToScreen(cubeLeftObject.position);
	        Vec3 cubeRightScreenPos = camera.projectToScreen(cubeRightObject.position);
	        Vec3 blendObjectScreenPos = camera.projectToScreen(blendObject.position);
	        
	        RGL.beginFrame();

	        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	        RGL.renderSkybox(mySkybox, camera.position, camera.matBuffVP);
	        
	        glUseProgram(cubeShader.shaderID);
	        camera.uploadUniforms(cubeShader);
	        cubeLeftObject.draw(cubeShader, camera);
	        cubeRightObject.draw(cubeShader, camera);
	        
//	        glUseProgram(pyramidShader.shaderID);
//	        pyramidObject.draw(pyramidShader, camera);
//	        camera.uploadUniforms(pyramidShader);
	        
	        if (startTimer > 0) {
  	            
  	            glUseProgram(monkeyShader.shaderID);
	            camera.uploadUniforms(monkeyShader);
	            monkey.draw(monkeyShader, camera);
	            
	            if (startTimer < MAX_TIMEOUT / 2) {
	            	monkey.position = monkey.position.subtract(monkey.forward.multiply(0.15f));
	            	
	            	if (startTimer < MAX_TIMEOUT / 3) {
	            		glUseProgram(blendShader.shaderID);
			        	camera.uploadUniforms(blendShader);
				        glUniform3f(blendShader.uni_objectColor, blendColor.x, blendColor.y, blendColor.z);
				        blendObject.draw(blendShader, camera);
				        
				        blendObject.yaw += 0.07f;
	            	}
	            }
	        	
	        	startTimer--;
	        } else 
	        	if (!finishAnimation) {
	        	glUseProgram(blendShader.shaderID);
	        	camera.uploadUniforms(blendShader);
		        glUniform3f(blendShader.uni_objectColor, blendColor.x, blendColor.y, blendColor.z);
		        blendObject.draw(blendShader, camera);
		        
		        blendObject.yaw += 0.07f;
		        if (!clickedLeftCube && !clickedRightCube) {
		        	blendColor = RGL.RED;
			        RGL.sysPrint((int)blendObjectScreenPos.x - 45, (int)blendObjectScreenPos.y + 30, 1, 0xFFFFFFFF, "Find MONKEY!");
		        }
	        }
	        
	        if (finishAnimation) {
	        	
	        	if (timeoutFinish > 0) {
	        		glUseProgram(blendShader.shaderID);
		        	camera.uploadUniforms(blendShader);
		        	blendColor = RGL.WHITE;
			        glUniform3f(blendShader.uni_objectColor, blendColor.x, blendColor.y, blendColor.z);
			        blendObject.draw(blendShader, camera);
			        
			        blendObject.yaw += 0.07f;
			        RGL.sysPrint((int)blendObjectScreenPos.x - 45, (int)blendObjectScreenPos.y + 30, 1, 0xFF0000FF, "Game over!");
			        blendObject.position = blendObject.position.subtract(blendObject.up.multiply(0.03f));
			        
			        glUseProgram(cubeShader.shaderID);
			        camera.uploadUniforms(cubeShader);
			        cubeLeftObject.draw(cubeShader, camera);
			        cubeRightObject.draw(cubeShader, camera);
			        
	        		timeoutFinish--;
	        	} else {
	        		highlightColor = RGL.RED;
	  	        	glUseProgram(ghostShader.shaderID);
	  	            camera.uploadUniforms(ghostShader);
	  	            glUniform3f(ghostShader.uni_objectColor, highlightColor.x, highlightColor.y, highlightColor.z);
	  	            glBindTexture(GL_TEXTURE_2D, RGL.texWhitePixel);
	  	            monkey.ghostly = true;
	  	            monkey.draw(ghostShader, camera);
	  	            monkey.ghostly = false;
	  	            
	  	            glUseProgram(cubeShader.shaderID);
			        camera.uploadUniforms(cubeShader);
			        cubeLeftObject.draw(cubeShader, camera);
			        cubeRightObject.draw(cubeShader, camera);
	  	            
	  	            if (timeoutONE > 0) {
	  	            	monkey.position = monkey.position.add(monkey.up.multiply(0.01f));
	  	            	cubeLeftObject.yaw += 0.2;
	  	            	cubeRightObject.yaw += 0.2;
	  	            	timeoutONE--;
	  	            } else if (timeoutTWO > 0) {
		            	monkey.position = monkey.position.subtract(monkey.up.multiply(0.01f));
		            	cubeLeftObject.yaw -= 0.2;
	  	            	cubeRightObject.yaw -= 0.2;
		            	timeoutTWO--;
		            } else if (timeoutTHREE > 0) {
		            	camera.distance -= 0.065f;
		            	cubeLeftObject.pitch += 0.2;
		            	cubeRightObject.pitch += 0.2;
		            	timeoutTHREE--;
		            } else {
		            	RGL.setRunning(false);
		            	break;
		            }
	        	}
	        } else if (clicks <= 0) {
	        	monkey.position = new Vec3(0.0f, 0.0f, 0.0f);
  	            monkey.attachChild(cubeLeftObject);
  	            monkey.attachChild(cubeRightObject);
  	            
  	            finishAnimation = true;
	        } else if (clickedLeftCube || clickedRightCube) {
	        	blendColor = RGL.WHITE;
	        	
	        	if (!playingSound) {
	        		if (randPosition == 0 && clickedLeftCube || randPosition != 0 && clickedRightCube) {
	        			source.play(correctAnswerSound);
	        			score++;
	        		} else {
	        			source.play(wrongAnswerSound);
	        		}
	        		playingSound = true;
	        	}
	        	
	        	if (randPosition == 0 && clickedLeftCube || randPosition != 0 && clickedRightCube) {
	        		
	        		RGL.sysPrint((int)blendObjectScreenPos.x - 30, (int)blendObjectScreenPos.y + 30, 1, 0x32CD32FF, "Correct!");
        		} else {
        			RGL.sysPrint((int)blendObjectScreenPos.x - 45, (int)blendObjectScreenPos.y + 30, 1, 0xFF0000FF, "Wrong answer!");
        			
        		}
	        	
	        	if (randPosition == 0) {
	        		highlightColor = RGL.BLUE;
	        		monkey.position = new Vec3(-2.7f, 0.0f, 0.0f);
	        		cubeLeftObject.yaw += 0.1;
	        	} else {
	        		highlightColor = RGL.RED;
	        		monkey.position = new Vec3(2.7f, 0.0f, 0.0f);
	        		cubeRightObject.yaw += 0.1;
	        	}
	        	
	        	if (timeout > 0) {
	        		glUseProgram(ghostShader.shaderID);
		            camera.uploadUniforms(ghostShader);
		            glUniform3f(ghostShader.uni_objectColor, highlightColor.x, highlightColor.y, highlightColor.z);
		            glBindTexture(GL_TEXTURE_2D, RGL.texWhitePixel);
		            monkey.ghostly = true;
		            monkey.draw(ghostShader, camera);
		            monkey.ghostly = false;
		            cubeRightObject.ghostly = false;
		            timeout--;
	        	} else {
	        		clickedLeftCube = false;
		        	clickedRightCube = false;
		        	timeout = MAX_TIMEOUT;
		        	randPosition = (int) ((Math.random() * 3) % 2);
		        	clicks--;
		        	playingSound = false;
	        	}
	        } else if(cubeLeftSelected) {
                RGL.sysPrint((int)cubeLeftScreenPos.x - 25, (int)cubeLeftScreenPos.y, 1, 0xFFFF00FF, "Choose me!");
                cubeLeftObject.pitch += 0.07;
            } else if(cubeRightSelected) {
            	cubeRightObject.pitch += 0.07;
                RGL.sysPrint((int)cubeRightScreenPos.x - 25, (int)cubeRightScreenPos.y, 1, 0xFFFF00FF, "Choose me!");
            }
            
            RGL.sysPrint(10, 10, 3, 0xFFFFFFFF, "Score: " + score);

	        RGL.endFrame();
	    }

	    RGL.deinit();
	    source.delete();
	    AudioMaster.cleanUp();
	}

}

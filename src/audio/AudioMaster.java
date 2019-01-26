package audio;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.util.WaveData;

/**
 * @author Nevena Dresevic
 *
 */
public class AudioMaster {
	
	private static ArrayList<Integer> buffers = new ArrayList<Integer>();

	public static void init() {
		try {
			AL.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}
	
	public static void setListenerData() {
		AL10.alListener3f(AL10.AL_POSITION,	0, 0, 0);
		AL10.alListener3f(AL10.AL_VELOCITY,	0, 0, 0);
	}
	
	public static int loadSound(String file) {
		int buffer = AL10.alGenBuffers();
		buffers.add(buffer);
		WaveData waveFile;
		try {
			waveFile = WaveData.create(new BufferedInputStream(new FileInputStream(file)));
			AL10.alBufferData(buffer, waveFile.format, waveFile.data, waveFile.samplerate);
			waveFile.dispose();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return buffer;
	}
	
	public static void cleanUp() {
		for (int buffer : buffers) {
			AL10.alDeleteBuffers(buffer);
		}
		AL.destroy();
	}
}

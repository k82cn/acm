package coursework5.common;

import java.io.Serializable;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;

/**
 * The proxy object of AudioFormat WITHOUT properties, make it serializable for
 * TCP/UDP
 * 
 * @author dma
 *
 */
public class SimpleAudioFormat implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 986625557085893235L;
	private String encoding;
	private float sampleRate;
	private int sampleSizeInBits;
	private int channels;
	private int frameSize;
	private float frameRate;
	private boolean bigEndian;

	public AudioFormat getAudioFormat() {
		return new AudioFormat(new Encoding(this.encoding), sampleRate,
				sampleSizeInBits, channels, frameSize, frameRate, bigEndian);
	}

	public static SimpleAudioFormat buildSimpleAudioFormat(
			AudioFormat audioFormat) {
		return new SimpleAudioFormat(audioFormat.getEncoding().toString(),
				audioFormat.getSampleRate(), audioFormat.getSampleSizeInBits(),
				audioFormat.getChannels(), audioFormat.getFrameSize(),
				audioFormat.getFrameRate(), audioFormat.isBigEndian());
	}

	private SimpleAudioFormat(String encoding, float sampleRate,
			int sampleSizeInBits, int channels, int frameSize, float frameRate,
			boolean bigEndian) {
		this.setEncoding(encoding);
		this.setSampleRate(sampleRate);
		this.setSampleSizeInBits(sampleSizeInBits);
		this.setChannels(channels);
		this.setFrameSize(frameSize);
		this.setFrameRate(frameRate);
		this.setBigEndian(bigEndian);
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public float getSampleRate() {
		return sampleRate;
	}

	public void setSampleRate(float sampleRate) {
		this.sampleRate = sampleRate;
	}

	public int getSampleSizeInBits() {
		return sampleSizeInBits;
	}

	public void setSampleSizeInBits(int sampleSizeInBits) {
		this.sampleSizeInBits = sampleSizeInBits;
	}

	public int getChannels() {
		return channels;
	}

	public void setChannels(int channels) {
		this.channels = channels;
	}

	public int getFrameSize() {
		return frameSize;
	}

	public void setFrameSize(int frameSize) {
		this.frameSize = frameSize;
	}

	public boolean isBigEndian() {
		return bigEndian;
	}

	public void setBigEndian(boolean bigEndian) {
		this.bigEndian = bigEndian;
	}

	public float getFrameRate() {
		return frameRate;
	}

	public void setFrameRate(float frameRate) {
		this.frameRate = frameRate;
	}
}

package coursework5.client;

import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import coursework5.common.ClientInfo;

/**
 * The player in client side to play the audio
 * 
 * @author REM
 *
 */
public class AudioPlayer implements Runnable {

	/**
	 * The constructor of AudioPlayer
	 * 
	 * @param input
	 *            the input stream of audio stream
	 * @param audioFormat
	 *            the format of audio
	 * @throws UnsupportedAudioFileException
	 *             audio exception
	 * @throws IOException
	 *             I/O exception
	 */
	public AudioPlayer(InputStream input, AudioFormat audioFormat)
			throws UnsupportedAudioFileException, IOException {
		this.input = input;
		this.audioFormat = audioFormat;
	}

	/**
	 * Play the audio in thread
	 */
	@Override
	public void run() {
		int len = 0;
		byte[] buf = new byte[ClientInfo.CHUNCH_SIZE];

		try {

			DataLine.Info info = new DataLine.Info(SourceDataLine.class,
					audioFormat);
			SourceDataLine soundLine = (SourceDataLine) AudioSystem
					.getLine(info);
			soundLine.open(audioFormat);

			soundLine.open();
			soundLine.start();

			while ((len = this.input.read(buf)) > 0) {
				soundLine.write(buf, 0, len);
			}

			soundLine.stop();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private InputStream input;
	private AudioFormat audioFormat;

}

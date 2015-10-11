package coursework5.client;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import coursework5.common.ClientInfo;
import coursework5.common.SimpleAudioFormat;
import coursework5.common.SimpleLogger;
import coursework5.server.ClientManager;

/**
 * The client to send/receive the audio to/from service. Refer to README on how
 * to run client
 * 
 * @author REM
 *
 */
public class Client {

	public static void main(String[] args) throws IOException,
			ClassNotFoundException, UnsupportedAudioFileException {

		byte[] buf = new byte[ClientInfo.CHUNCH_SIZE];
		int len;
		int serverPort = Integer.parseInt(args[1]);
		InetAddress host = InetAddress.getByName(args[0]);
		SimpleLogger.info("Connecting to server on port " + serverPort);

		Socket tcpSkt = new Socket(host, serverPort);

		// get ClientInfo from Servier
		ObjectInputStream in = new ObjectInputStream(tcpSkt.getInputStream());
		ClientInfo info = (ClientInfo) in.readObject();

		SimpleLogger.debug(info.toString());

		if (info.getRole() == ClientManager.ClientRole.SENDER) {
			// get the file size of audio and send it back to server
			ObjectOutputStream out = new ObjectOutputStream(
					tcpSkt.getOutputStream());

			File audio = new File(args[2]);

			AudioInputStream audioIS = AudioSystem.getAudioInputStream(audio);
			info.setAudioFormat(SimpleAudioFormat
					.buildSimpleAudioFormat(audioIS.getFormat()));

			long chunckCnt = audio.length() / ClientInfo.CHUNCH_SIZE;

			if (audio.length() % ClientInfo.CHUNCH_SIZE != 0) {
				chunckCnt++;
			}

			info.setChunckCount(chunckCnt);

			out.writeObject(info);

			SimpleLogger.debug("The length of file is <" + audio.length()
					+ ">, the chunck count is <" + chunckCnt + ">");

			// send the data to the server
			DatagramSocket udpSkt = new DatagramSocket();
			udpSkt.connect(info.getServiceHost(), info.getServiceUDPPort());

			while ((len = audioIS.read(buf)) > 0) {
				DatagramPacket dp = new DatagramPacket(buf, len);
				udpSkt.send(dp);
			}

			audioIS.close();
			udpSkt.close();

		} else if (info.getRole() == ClientManager.ClientRole.RECEIVER) {
			DatagramSocket udpSkt = new DatagramSocket();
			udpSkt.setReceiveBufferSize(ClientInfo.BUFFER_SIZE);
			udpSkt.setSendBufferSize(ClientInfo.BUFFER_SIZE);
			ObjectOutputStream out = new ObjectOutputStream(
					tcpSkt.getOutputStream());

			info.setClientHost(InetAddress.getLocalHost());
			info.setClientUDPPort(udpSkt.getLocalPort());

			out.writeObject(info);

			SimpleLogger.debug(info.toString());

			PipedOutputStream output = new PipedOutputStream();
			PipedInputStream input = new PipedInputStream(output);

			// start thread to play the audio
			new Thread(new AudioPlayer(input, info.getAudioFormat()
					.getAudioFormat())).start();

			for (int i = 0; i < info.getChunckCount(); i++) {
				DatagramPacket dp = new DatagramPacket(buf, buf.length);

				udpSkt.receive(dp);

				SimpleLogger.debug("Receive data of <" + i + "> with length <"
						+ dp.getLength() + ">");

				output.write(buf, 0, dp.getLength());
			}

			output.close();
			udpSkt.close();
		}
		tcpSkt.close();
	}

}

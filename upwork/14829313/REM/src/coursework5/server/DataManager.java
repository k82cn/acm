package coursework5.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import coursework5.common.ClientInfo;
import coursework5.common.SimpleAudioFormat;
import coursework5.common.SimpleLogger;

/**
 * DataManager will manage the audio from the first client; so the coming client
 * will get the data from it.
 * 
 * @author REM
 *
 */
public class DataManager implements Runnable {

	private DatagramSocket skt;
	private ArrayList<byte[]> chunckList = new ArrayList<byte[]>();
	private long chunckCnt;
	private SimpleAudioFormat audioFormat;

	/**
	 * The constructor of DataManager
	 * 
	 * @throws SocketException
	 *             socket exception
	 * @throws UnknownHostException
	 *             unknown host exception
	 */
	public DataManager() throws SocketException, UnknownHostException {
		this.skt = new DatagramSocket();
		this.skt.setReceiveBufferSize(ClientInfo.BUFFER_SIZE);
		this.skt.setSendBufferSize(ClientInfo.BUFFER_SIZE);
		SimpleLogger.info("DataManager listen at : "
				+ this.skt.getLocalSocketAddress());
	}

	/**
	 * The DataManager receives data from the first client and keep it in memory
	 */
	@Override
	public void run() {

		while (true) {
			byte[] buffer = new byte[ClientInfo.CHUNCH_SIZE];

			DatagramPacket chunck = new DatagramPacket(buffer, buffer.length);

			try {
				this.skt.receive(chunck);

				if (chunck.getLength() != ClientInfo.CHUNCH_SIZE) {
					byte[] data = new byte[chunck.getLength()];
					System.arraycopy(buffer, 0, data, 0, chunck.getLength());
					this.addChunck(data);
				} else {
					this.addChunck(chunck.getData());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Get the UDP port of DataManager for the first client to send audio data
	 * 
	 * @return The UDP port of DataManager
	 */
	public int getUDPPort() {
		return this.skt.getLocalPort();
	}

	/**
	 * Get the chunck by index
	 * 
	 * @param i
	 *            The index of chunck
	 * @return The data of chunck
	 */
	public byte[] getChunck(int i) {
		synchronized (this.chunckList) {
			while (i >= this.chunckList.size()) {
				try {
					this.chunckList.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return this.chunckList.get(i);
		}
	}

	private void addChunck(byte[] chunck) {
		synchronized (chunckList) {
			this.chunckList.add(chunck);
			SimpleLogger.debug("Add one chunck at <" + this.chunckList.size()
					+ "> with length <" + chunck.length + ">");
			this.chunckList.notifyAll();
		}
	}

	/**
	 * Get the host of DataManager
	 * 
	 * @return The host of DataManager
	 * @throws UnknownHostException
	 *             unknown host exception
	 */
	public InetAddress getHost() throws UnknownHostException {
		return InetAddress.getLocalHost();
	}

	/**
	 * Get the chunck count of audio
	 * 
	 * @return the chuck count of audio
	 */
	public long getChunckCount() {
		return this.chunckCnt;
	}

	/**
	 * Set the chunck count of audio
	 * 
	 * @param chunckCnt
	 *            the chunck count of audio
	 */
	public void setChunkCount(long chunckCnt) {
		this.chunckCnt = chunckCnt;
	}

	/**
	 * Get the format of audio file
	 * 
	 * @return the format of audio file
	 */
	public SimpleAudioFormat getAudioFormat() {
		return audioFormat;
	}

	/**
	 * Set the format of audio file
	 * 
	 * @param audioFormat
	 *            the format of audio file
	 */
	public void setAudioFormat(SimpleAudioFormat audioFormat) {
		this.audioFormat = audioFormat;
	}

}

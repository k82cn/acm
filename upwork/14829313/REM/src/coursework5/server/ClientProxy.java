package coursework5.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.UUID;

import coursework5.common.ClientInfo;
import coursework5.common.SimpleLogger;
import coursework5.server.ClientManager.ClientRole;

/**
 * The proxy object of client in service side
 * 
 * @author REM
 *
 */
public class ClientProxy implements Runnable {

	/**
	 * The constructor of ClientProxy
	 * 
	 * @param clientManager The reference of ClientManager
	 * @param skt The connection between client and service
	 * @param role The role of client: RECEIVER or SENDER
	 * @throws IOException I/O exception
	 */
	public ClientProxy(ClientManager clientManager, Socket skt, ClientRole role)
			throws IOException {
		this.id = UUID.randomUUID().toString();
		this.clientRole = role;
		this.skt = skt;

		this.clientManager = clientManager;
		this.dataManager = this.clientManager.getDataManager();
	}

	/**
	 * Run ClientProxy to send/receive audio data
	 *     - if its role is SENDER, DataManager will receive the data from client
	 *     - otherwise, ClientProxy will get data from DataManager and send to to client
	 */
	@Override
	public void run() {
		try {
			ObjectOutputStream out = new ObjectOutputStream(
					this.skt.getOutputStream());

			ClientInfo info = new ClientInfo(this.id, this.clientRole,
					this.dataManager.getHost(), this.dataManager.getUDPPort());

			info.setChunckCount(this.dataManager.getChunckCount());
			info.setAudioFormat(this.dataManager.getAudioFormat());

			// send the role & uuid to the client
			out.writeObject(info);

			out.flush();

			SimpleLogger.debug("Send ClientInfo to client:" + info.toString());

			ObjectInputStream in = new ObjectInputStream(
					this.skt.getInputStream());

			info = (ClientInfo) in.readObject();

			SimpleLogger.debug("Receive ClientInfo from client: "
					+ info.toString());

			switch (this.clientRole) {
			case SENDER:
				receiveAudioFromClient(info);
				break;
			case RECEIVER:
				sendAudioToClient(info);
				break;
			default:
				break;
			}

			this.skt.close();
			this.clientManager.removeClient(this);

		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	private void receiveAudioFromClient(ClientInfo info) {
		this.dataManager.setChunkCount(info.getChunckCount());
		this.dataManager.setAudioFormat(info.getAudioFormat());
	}

	private void sendAudioToClient(ClientInfo info) throws SocketException,
			IOException {
		DatagramSocket udpSkt = new DatagramSocket();
		udpSkt.connect(info.getClientHost(), info.getClientUDPPort());

		for (int i = 0; i < this.dataManager.getChunckCount(); i++) {
			byte[] data = this.dataManager.getChunck(i);
			
			SimpleLogger.debug("Send data of <" + i + "> with length <" + data.length + ">");
			
			DatagramPacket dp = new DatagramPacket(data, data.length);
			udpSkt.send(dp);
		}
		udpSkt.close();
	}

	/**
	 * Get the UID of the client
	 * @return The UID of the client
	 */
	public String getId() {
		return this.id;
	}

	private ClientManager clientManager;
	private DataManager dataManager;
	private String id;
	private ClientRole clientRole;
	private Socket skt;
}

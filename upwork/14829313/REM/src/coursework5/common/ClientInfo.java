package coursework5.common;

import java.io.Serializable;
import java.net.InetAddress;

import coursework5.server.ClientManager.ClientRole;

/**
 * The client object to include all information for the communication between
 * client and service
 * 
 * @author REM
 *
 */
public class ClientInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3120124294233860500L;

	/**
	 * The chunck size for communication between client and service
	 */
	public static final int CHUNCH_SIZE = 10240;
	/**
	 * The buffer size for UDP socket
	 */
	public static final int BUFFER_SIZE = 65535 * 1024;

	private String id;
	private ClientRole role;
	private int serviceUDPPort;
	private InetAddress serviceHost;
	private int clientUDPPort;
	private InetAddress clientHost;
	private long chunckCount;

	private SimpleAudioFormat audioFormat;

	/**
	 * The constructor of ClientInfo
	 * 
	 * @param id
	 *            The UID of client
	 * @param role
	 *            The role of client
	 * @param serviceHost
	 *            The host name of service
	 * @param serviceUDPPort
	 *            The UDP port of service to receive audio
	 */
	public ClientInfo(String id, ClientRole role, InetAddress serviceHost,
			int serviceUDPPort) {
		this.setId(id);
		this.setRole(role);
		this.setServiceHost(serviceHost);
		this.setServiceUDPPort(serviceUDPPort);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("ID: ").append(this.id).append(", ");
		sb.append("Client Role: ").append(this.role).append(", ");
		if (this.serviceHost != null) {
			sb.append("Server Host: ").append(this.serviceHost.getHostName())
					.append(", ");
			sb.append("Server Port: ").append(this.serviceUDPPort).append(", ");
		}
		if (this.clientHost != null) {
			sb.append("Client Host: ").append(this.clientHost.getHostName())
					.append(", ");
			sb.append("Client Port: ").append(this.clientUDPPort).append(", ");
		}

		if (this.audioFormat != null) {
			sb.append("Audio Format: ").append(this.audioFormat.toString())
					.append(", ");
		}
		sb.append("Chunck Count: ").append(this.chunckCount);

		return sb.toString();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ClientRole getRole() {
		return role;
	}

	public void setRole(ClientRole role) {
		this.role = role;
	}

	public int getServiceUDPPort() {
		return serviceUDPPort;
	}

	public void setServiceUDPPort(int serviceUDPPort) {
		this.serviceUDPPort = serviceUDPPort;
	}

	public int getClientUDPPort() {
		return clientUDPPort;
	}

	public void setClientUDPPort(int clientUDPPort) {
		this.clientUDPPort = clientUDPPort;
	}

	public long getChunckCount() {
		return this.chunckCount;
	}

	public void setChunckCount(long chunckCount) {
		this.chunckCount = chunckCount;
	}

	public InetAddress getServiceHost() {
		return serviceHost;
	}

	public void setServiceHost(InetAddress serviceHost) {
		this.serviceHost = serviceHost;
	}

	public InetAddress getClientHost() {
		return clientHost;
	}

	public void setClientHost(InetAddress clientHost) {
		this.clientHost = clientHost;
	}

	public SimpleAudioFormat getAudioFormat() {
		return audioFormat;
	}

	public void setAudioFormat(SimpleAudioFormat audioFormat) {
		this.audioFormat = audioFormat;
	}

}

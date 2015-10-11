package coursework5.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import coursework5.common.SimpleLogger;

/**
 * The main class of server, accepting the connection from client:
 * 	1. if the client is the first client, receive data from it
 * 	2. otherwise, send the data to the client
 * 
 * @author REM
 */
public class ClientManager {

	public enum ClientRole {
		RECEIVER, SENDER
	}

	public ClientManager(int port) throws IOException {
		this.servSkt = new ServerSocket(port, 5);
		this.dataManager = new DataManager();
		SimpleLogger.info("ClientManager listen at : " +  this.servSkt.getLocalSocketAddress().toString());
	}

	public static void main(String[] args) {

		ClientManager cliMgr;
		try {
			cliMgr = new ClientManager(28080);
			cliMgr.run();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Run ClientManager to accept client connection
	 */
	private void run() {
		// start data manager
		new Thread(this.dataManager).start();
		
		// accept the connection from client
		while (true) {
			try {
				Socket skt = servSkt.accept();

				// create proxy for each Client; the client proxy will help to
				// receive/send data
				ClientProxy cp = new ClientProxy(this, skt, this.role);

				this.proxies.put(cp.getId(), cp);

				// start thread to receive/send data
				new Thread(cp).start();

				// update the role of client: sender or receiver
				updateClientRole();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void updateClientRole() {
		if (ClientRole.SENDER == this.role) {
			this.role = ClientRole.RECEIVER;
		}
	}

	/**
	 * Un-register client proxy from ClientManager
	 * @param clientProxy client proxy
	 */
	public void removeClient(ClientProxy clientProxy) {
		this.proxies.remove(clientProxy.getId());
	}

	/**
	 * Get the DataManager in ClientManager
	 * @return The instance of DataManager
	 */
	public DataManager getDataManager() {
		return dataManager;
	}

	private ClientRole role = ClientRole.SENDER;

	private ServerSocket servSkt;

	private HashMap<String, ClientProxy> proxies = new HashMap<String, ClientProxy>();
	private DataManager dataManager;

}

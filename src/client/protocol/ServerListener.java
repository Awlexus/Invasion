package client.protocol;

import protocol.ProtocolUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * Created by Alex Wieser on 21.04.2017.
 */
public class ServerListener extends Thread {

	private Socket server;

	public ServerListener(Socket server) {
		this.server = server;
	}

	@Override
	public void run() {
		while (!isInterrupted()) {
			readServerUpdate();
		}
	}

	/**
	 * Reads and applies server updates
	 */
	private void readServerUpdate() {
		try {
			BufferedInputStream in = new BufferedInputStream(server.getInputStream());
			int command = ProtocolUtils.getProtocolHead(in);

			switch (command) {
				case ProtocolUtils.GAME_START:
					/**
					 * Everbody is ready to play
					 */
					break;
				case ProtocolUtils.GAME_UPDATE:
					/**
					 * Server updates the Battlefield
					 */
					break;
				case ProtocolUtils.GAME_END:
					/**
					 * Somebody is a winner
					 */
					break;
				case ProtocolUtils.GAME_MAP:
					/**
					 * Load map details
					 */
					break;
			}

		} catch (IOException e) {
			e.printStackTrace();
			interrupt();
		}
	}
}

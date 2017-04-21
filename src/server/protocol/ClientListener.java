package server.protocol;

import protocol.ProtocolUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by Alex Wieser on 21.04.2017.
 */
public class ClientListener extends Thread {

	private Socket client;

	public ClientListener(Socket client) {
		this.client = client;
	}

	@Override
	public void run() {
		while (!isInterrupted()) {
			readClientUpdate();
		}
	}

	private void readClientUpdate() {
		try {
			BufferedInputStream in = new BufferedInputStream(client.getInputStream());
			int size = ProtocolUtils.getProtocolHead(in);

			short[] actions = getUpdateContent(in, size);

			// TODO: Use the Content

		} catch (IOException e) {
			e.printStackTrace();
			interrupt();
		}
	}

	private short[] getUpdateContent(BufferedInputStream in, int size) throws IOException {
		short[] actions = new short[size];

		for (int i = 0; i < size; i++) {
			actions[i] += in.read() << 8;
			actions[i] += in.read();
		}
		return actions;
	}
}

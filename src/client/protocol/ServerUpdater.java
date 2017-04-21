package client.protocol;

import protocol.ProtocolUtils;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex Wieser on 21.04.2017.
 */
public class ServerUpdater extends Thread {

	private Socket server;

	private final List<Short> actions;

	public ServerUpdater(Socket server) {
		this.server = server;
		actions = new ArrayList<>();
	}

	@Override
	public void run() {
		while (!isInterrupted()) {
			waitForActions();

			sendUpdates();
		}
	}

	/**
	 * Sends All updates to the server
	 */
	private void sendUpdates() {
		try {
			BufferedOutputStream out = new BufferedOutputStream(server.getOutputStream());

			// write changes:
			synchronized (actions) {
				for (int i = 0; i < ProtocolUtils.INITIAL_CODES; i++) {
					out.write(actions.size());
				}

				for (short s : actions) {
					out.write(s >> 8);
					out.write(s & 0xFF);
				}
				out.write(0);
				out.write(0);
				out.flush();
				actions.clear();
			}

		} catch (IOException e) {
			e.printStackTrace();
			interrupt();
		}
	}

	/**
	 * Adds an Action and wakes the thread, if it was waiting
	 *
	 * @param id index of the blocked/unblocked Section
	 */
	public void addAction(Short id) {
		synchronized (actions) {

			if (actions.contains(id))
				actions.remove(id);
			else
				actions.add(id);

			if (getState() == State.WAITING)
				interrupt();
		}
	}

	/**
	 * Waits until a action needs to be submitted
	 */
	private void waitForActions() {
		try {
			if (actions.isEmpty()) wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

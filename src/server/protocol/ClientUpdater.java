package server.protocol;

import protocol.ProtocolUtils;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

/**
 * Created by Alex Wieser on 21.04.2017.
 */
public class ClientUpdater extends Thread {

	private Socket client;

	private List<ClientUpdateEvent> events;

	public ClientUpdater(Socket client) {
		this.client = client;
	}

	@Override
	public void run() {
		while (!isInterrupted()) {
			waitForEvents();

			sendEvents();
		}
	}

	/**
	 * Adds an Event and wakes the thread, if it was waiting
	 *
	 * @param type the Eventtype (Constant from {@link ProtocolUtils})
	 * @param data the event data
	 */
	public void addAction(int type, short[] data) {
		synchronized (events) {
			ClientUpdateEvent event = new ClientUpdateEvent(type, data);

			if (!events.contains(event)) {
				events.add(event);
				if (getState() == State.WAITING) interrupt();
			}

		}
	}

	/**
	 * Sends the stored events
	 */
	private void sendEvents() {
		try {
			BufferedOutputStream out = new BufferedOutputStream(client.getOutputStream());

			synchronized (events) {
				for (ClientUpdateEvent event : events) {
					sendEvent(out, event);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sends a single Event
	 * @param out
	 * @param event
	 * @throws IOException
	 */
	private void sendEvent(BufferedOutputStream out, ClientUpdateEvent event) throws IOException {
		for (int i = 0; i < ProtocolUtils.INITIAL_CODES; i++) {
			out.write(event.eventType);
		}

		for (short s : event.data) {
			out.write(s >> 8);
			out.write(s & 0xFF);
		}
		out.write(0);
		out.write(0);
		out.flush();
		events.clear();
	}

	private void waitForEvents() {
		try {
			if (events.isEmpty()) wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private class ClientUpdateEvent {

		private int eventType;
		private short[] data;

		public ClientUpdateEvent(int eventType, short[] data) {
			this.eventType = eventType;
			this.data = data;
		}

		@Override
		public boolean equals(Object obj) {
			boolean ret = true;
			ClientUpdateEvent event = null;

			if (obj instanceof ClientUpdateEvent) event = (ClientUpdateEvent) obj;
			else ret = false;

			if (ret && event.eventType == this.eventType && event.data.length == this.data.length) {
				for (int i = 0; ret && i < eventType; i++) {
					if (event.data[i] != this.data[i]) ret = false;
				}
			} else ret = false;

			return ret;
		}
	}
}

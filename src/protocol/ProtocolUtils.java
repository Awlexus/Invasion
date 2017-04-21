package protocol;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Alex Wieser on 21.04.2017.
 */
public class ProtocolUtils {

	/**
	 * Every Client is ready
	 */
	public final static int GAME_START = 1;

	/**
	 * Server update
	 */
	public final static int GAME_UPDATE = 2;

	/**
	 * Somebody is a winner
	 */
	public final static int GAME_END = 3;

	/**
	 * Server sends mapdata
	 */
	public final static int GAME_MAP = 4;

	/**
	 * How many times the server repeats the command
	 */
	public final static int INITIAL_CODES = 4;

	/**
	 * How many milliseconds the server should wait between updates
	 */
	public final static long UPDATE_FREQUENZY = 200;

	/**
	 * Reads the start from the protocol
	 *
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static int getProtocolHead(InputStream in) throws IOException {

		// Receive Command
		double info = 0;
		int i = 0;
		while (i < ProtocolUtils.INITIAL_CODES) {
			int read = in.read();
			if (read == -1) throw new IOException("Connection to server lost.");

			if (read == 0) {
				i = 0;
				info = 0;
			}
			info += read;
			i++;
		}
		info /= ProtocolUtils.INITIAL_CODES;
		return (int) Math.round(info);
	}
}

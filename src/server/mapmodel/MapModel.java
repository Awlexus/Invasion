package server.mapmodel;

import com.google.gson.Gson;
import com.sun.istack.internal.NotNull;
import server.map.BattlefieldThread;
import server.map.sections.Section;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex Wieser on 06.04.2017.
 */
public class MapModel {
	int mapSize;
	int playerCount;
	String mapName;

	ArrayList<FieldModel> fields = new ArrayList<>();

	public MapModel() {}

	public MapModel(BattlefieldThread battlefield) {

		Section[][] sections = battlefield.getSections();
		for (int i = 0; i < sections.length; i++) {
			for (int j = 0; j < sections[i].length; j++) {

			}
		}
	}

	/**
	 * Converts this map into a {@link BattlefieldThread}
	 *
	 * @return
	 */
	public BattlefieldThread toBattleField() {
		BattlefieldThread ret = new BattlefieldThread(mapSize, playerCount, mapName);
		for (FieldModel field : fields) {
			field.apply(ret);
		}
		return ret;
	}

	public static void parseMapToJSONFile(@NotNull MapModel map, @NotNull File file) throws IOException {
		if (!file.exists()) file.createNewFile();

		String mapContent = new Gson().toJson(map);
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));

		writer.write(mapContent);
		writer.close();
	}

	public static MapModel readMapFromJSONFile(@NotNull File f) throws FileNotFoundException {
		return new Gson().fromJson(new FileReader(f), MapModel.class);
	}

	/**
	 * Handles Drag and Drop for Map-files, for the given {@link JFrame}
	 *
	 * @param frame
	 */
	public void handleDragAndDrop(JFrame frame) {
		DropTarget target = new DropTarget() {
			@Override
			public synchronized void drop(DropTargetDropEvent dtde) {
				try {
					dtde.acceptDrop(DnDConstants.ACTION_COPY);
					List<File> files = (List<File>) dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
					for (File file : files) {
						// TODO: Magie mit Dateien ausführen
					}
				} catch (UnsupportedFlavorException | IOException e) {
					e.printStackTrace();
				}
			}
		};
		frame.setDropTarget(target);
	}

	/**
	 * Writes this object to to the given Stream. The Map should be send using [@link {@link MapModel#receiveMap(InputStream)}
	 *
	 * @param out
	 */
	public void writeMap(OutputStream out) throws IOException {
		writeMap(new OutputStreamWriter(out));
	}

	/**
	 * Writes this object to to the given Writer. The Map should be send using {@link MapModel#receiveMap(InputStream)}
	 *
	 * @param out
	 */
	public void writeMap(Writer out) throws IOException {
		Gson gson = new Gson();
		gson.toJson(this, MapModel.class, gson.newJsonWriter(out));
	}

	/**
	 * Read a {@link MapModel} from an {@link InputStream}. The map should be send using {@link MapModel#writeMap(OutputStream)}
	 *
	 * @param in
	 * @return A map created from the contents of this Stream
	 */
	public static MapModel receiveMap(InputStream in) {
		return receiveMap(new InputStreamReader(in));
	}

	/**
	 * Read a {@link MapModel} from an {@link Reader}. The map should be send using {@link MapModel#writeMap(OutputStream)}
	 *
	 * @param in
	 * @return A map created from the contents of this reader
	 */
	public static MapModel receiveMap(Reader in) {
		return new Gson().fromJson(in, MapModel.class);
	}

	public int getMapSize() {
		return mapSize;
	}

	public void setMapSize(int mapSize) {
		if (mapSize > 2) this.mapSize = mapSize;
	}

	public int getPlayerCount() {
		return playerCount;
	}

	public void setPlayerCount(int playerCount) {
		if (playerCount > 2) this.playerCount = playerCount;
	}

	public String getMapName() {
		return mapName;
	}

	public void setMapName(String mapName) {
		if (mapName != null) this.mapName = mapName;
	}

	public ArrayList<FieldModel> getFields() {
		return fields;
	}

}

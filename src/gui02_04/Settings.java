package gui02_04;

import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Settings {

	final String fontName;
	final boolean bold;
	final boolean italic;
	final int fontSize;
	final Color fontColor;
	final Color bgColor;

	public Settings(String fontName, boolean bold, boolean italic, int fontSize, Color fontColor, Color bgColor) {
		super();
		this.fontName = fontName;
		this.bold = bold;
		this.italic = italic;
		this.fontSize = fontSize;
		this.fontColor = fontColor;
		this.bgColor = bgColor;
	}

	private static final List<String> FONT_LIST = Arrays
			.asList(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
	private static final List<Integer> FONT_SIZE_LIST = Arrays.asList(10, 20, 30, 40, 50, 75, 100, 150, 200, 250, 300);

	private static final Map<String, Color> COLOR_MAP = new HashMap<>();

	static {
		COLOR_MAP.put("Black", Color.BLACK);
		COLOR_MAP.put("Red", Color.RED);
		COLOR_MAP.put("Blue", Color.BLUE);
		COLOR_MAP.put("Green", Color.GREEN);
		COLOR_MAP.put("Yellow", Color.YELLOW);
		COLOR_MAP.put("Orange", Color.ORANGE);
		COLOR_MAP.put("Pink", Color.PINK);
		COLOR_MAP.put("Glay", Color.GRAY);
		COLOR_MAP.put("White", Color.WHITE);
	}

	private static final Settings DEFAULT_SETTINGS = new Settings(FONT_LIST.get(0), false, false, 100, Color.BLACK,
			Color.WHITE);

	public static Settings getDefaultSettings() {
		return DEFAULT_SETTINGS;
	}

	public static List<String> getFontList() {
		return FONT_LIST;
	}

	public static List<Integer> getFontSizeList() {
		return FONT_SIZE_LIST;
	}

	public static Map<String, Color> getColorMap() {
		return COLOR_MAP;
	}

	public static String getColorName(Color color) {
		for (Entry<String, Color> e : COLOR_MAP.entrySet()) {
			if (e.getValue().equals(color)) {
				return e.getKey();
			}
		}
		return null;
	}
}

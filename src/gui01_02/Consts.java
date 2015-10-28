package gui01_02;

import java.awt.Font;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Consts {

	public static class Properties {
		public static final Font MENU_DEFAULT_FONT = new Font("Century Gothic", Font.PLAIN, 12);
		/** 時刻フォーマット */
		public static final DateFormat CLOCK_FORMAT = new SimpleDateFormat("HH:mm:ss");

	}

	public static class Strings {
		public static final String APP_NAME = "Digital Clock";
		public static final String MENU = "Menu";
		public static final String SETTINGS = "Settings";
		public static final String FONT = "Font";
		public static final String FONT_SIZE = "Font Size";
		public static final String FONT_COLOR = "Font Color";
		public static final String BG_COLOR = "Background Color";
	}
}

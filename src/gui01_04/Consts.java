package gui01_04;

import java.awt.Color;
import java.awt.Font;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Consts {

	public static class Properties {
		public static final Font MENU_DEFAULT_FONT = new Font("Century Gothic", Font.PLAIN, 12);
		/** 時刻フォーマット */
		public static final DateFormat CLOCK_FORMAT = new SimpleDateFormat("HH:mm:ss");

	}
	
	public static class PrefName {
		public static final String  FONT_NAME = "font_name";
		public static final String  BOLD = "bold";
		public static final String  ITALIC = "italic";
		public static final String  FONT_SIZE = "font_size";
		public static final String  FONT_COLOR = "font_color";
		public static final String  BG_COLOR = "bg_name";
		public static final String  WINDOW_LEFT = "window_left";
		public static final String  WINDOW_TOP = "window_top";
	}

	public static class Strings {
		public static final String APP_NAME = "Digital Clock";
		public static final String MENU = "Menu";
		public static final String SETTINGS = "Settings";
		public static final String FONT = "Font";
		public static final String FONT_SIZE = "Font Size";
		public static final String FONT_COLOR = "Font Color";
		public static final String BG_COLOR = "Background Color";
		public static final String BOLD = "Bold";
		public static final String ITALIC = "Italic";
		public static final String EXIT = "Exit";
	}
}

package gui01_04;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * awt.Frameによるデジタル時計
 * 
 * @author hwatanabe
 *
 */
public class ClockWindow extends Window implements SettingsListener {

	/** 現在時刻 */
	private volatile Calendar now = Calendar.getInstance();

	private Settings settings = Settings.getDefaultSettings();
	/** 描画フォント */
	private Font font = new Font(settings.fontName, Font.PLAIN, settings.fontSize);

	private SettingPopupMenu settingMenu;

	private int strWidth;
	private int strHeight;

	private Image imageBuffer;
	private Graphics graphicBuffer;

	public ClockWindow(Frame owner) {
		super(owner);
		// owner.setResizable(false);
		setAlwaysOnTop(true);
		setFont(Consts.Properties.MENU_DEFAULT_FONT);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				savePrefs();
				System.exit(0);
			}
		});
		addMouseListener(mouseAdapter);
		addMouseMotionListener(mouseAdapter);
		loadPrefs();
		// MenuBar menuBar = new MenuBar();
		// Menu menu = new Menu(Consts.Strings.MENU);
		// menu.add(item);
		// menuBar.add(menu);
		// setMenuBar(menuBar);

	}

	private void init() {
		imageBuffer = createImage(getWidth() + 1, getHeight() + 1);
		graphicBuffer = imageBuffer.getGraphics();
		autoResize();
	}

	private void autoResize() {
		FontMetrics metrics = getGraphics().getFontMetrics(font);
		strWidth = metrics.stringWidth("00:00:00");
		strHeight = metrics.getAscent();
		int frameWidth = strWidth + 50;
		int frameHeight = strHeight + metrics.getDescent() + 50;
		setSize(frameWidth, frameHeight);
		imageBuffer = createImage(getWidth(), getHeight());
		graphicBuffer = imageBuffer.getGraphics();
	}

	@Override
	public void paint(Graphics g) {
		Dimension frameSize = getSize();
		graphicBuffer.setColor(settings.bgColor);
		graphicBuffer.fillRect(0, 0, frameSize.width, frameSize.width);
		graphicBuffer.setColor(settings.fontColor);
		graphicBuffer.setFont(font);
		graphicBuffer.drawString(Consts.Properties.CLOCK_FORMAT.format(now.getTime()), (frameSize.width - strWidth) / 2,
				(frameSize.height + strHeight) / 2);
		g.drawImage(imageBuffer, 0, 0, this);
		super.paint(g);
	}

	@Override
	public void update(Graphics g) {
		paint(g);
	}

	public void start() {
		setVisible(true);
		init();
		new Timer().schedule(new TimerTask() {
			public void run() {
				now = Calendar.getInstance();
				repaint();
			}
		}, 0, 1000);
	}

	@Override
	public void onSettingsChanged(Settings settings) {
		this.settings = settings;
		int fontType = settings.bold ? Font.BOLD : Font.PLAIN;
		fontType |= settings.italic ? Font.ITALIC : Font.PLAIN;
		font = new Font(settings.fontName, fontType, settings.fontSize);
		autoResize();
	}

	public static void main(String[] args) {
		ClockWindow clockFrame = new ClockWindow(new Frame());
		clockFrame.start();
	}

	private MouseAdapter mouseAdapter = new MouseAdapter() {

		private Point start;
		private int currentButton;

		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON3) {
				if (settingMenu == null) {
					settingMenu = new SettingPopupMenu();
					settingMenu.setSettingsListener(ClockWindow.this);
					add(settingMenu);
				}
				settingMenu.setSelection(settings);
				settingMenu.show(e.getComponent(), e.getX(), e.getY());
			}
			super.mouseClicked(e);
		}

		@Override
		public void mousePressed(MouseEvent e) {
			currentButton = e.getButton();
			if (e.getButton() == MouseEvent.BUTTON1) {
				start = e.getPoint();
			}
			super.mousePressed(e);
		}

		public void mouseReleased(MouseEvent e) {
			currentButton = MouseEvent.NOBUTTON;
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			if (currentButton == MouseEvent.BUTTON1) {
				Point current = e.getPoint();
				Point location = getLocation();
				location.translate(current.x - start.x, current.y - start.y);
				setLocation(location);
			}
			super.mouseDragged(e);
		}
	};
	
	private final Preferences prefs = Preferences.userRoot().node("hodaka.hw.watanabe.clock");
	
	private void loadPrefs() {
		String fontName = prefs.get(Consts.PrefName.FONT_NAME, settings.fontName);
		int fontSize = prefs.getInt(Consts.PrefName.FONT_SIZE, settings.fontSize);
		boolean bold = prefs.getBoolean(Consts.PrefName.BOLD, settings.bold);
		boolean italic = prefs.getBoolean(Consts.PrefName.ITALIC, settings.italic);
		String fontColorName = prefs.get(Consts.PrefName.FONT_COLOR, Settings.getColorName(settings.fontColor));
		Color fontColor = Settings.getColorMap().get(fontColorName);
		String bgColorName = prefs.get(Consts.PrefName.FONT_COLOR, Settings.getColorName(settings.bgColor));
		Color bgColor = Settings.getColorMap().get(bgColorName);
		int windowLeft = prefs.getInt(Consts.PrefName.WINDOW_LEFT, 0);
		int windowTop = prefs.getInt(Consts.PrefName.WINDOW_TOP, 0);
		settings = new Settings(fontName, bold, italic, fontSize, fontColor, bgColor);
//		onSettingsChanged(settings);
		setLocation(windowLeft, windowTop);
	}
	
	private void savePrefs() {
		prefs.put(Consts.PrefName.FONT_NAME, settings.fontName);
		prefs.putInt(Consts.PrefName.FONT_SIZE, settings.fontSize);
		prefs.putBoolean(Consts.PrefName.BOLD, settings.bold);
		prefs.putBoolean(Consts.PrefName.ITALIC, settings.italic);
		prefs.put(Consts.PrefName.FONT_COLOR, Settings.getColorName(settings.fontColor));
		prefs.put(Consts.PrefName.BG_COLOR, Settings.getColorName(settings.bgColor));
		Point p = getLocation();
		prefs.putInt(Consts.PrefName.WINDOW_LEFT, p.x);
		prefs.putInt(Consts.PrefName.WINDOW_TOP, p.y);
		try {
			prefs.flush();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
	}
}

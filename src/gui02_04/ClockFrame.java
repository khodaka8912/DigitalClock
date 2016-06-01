package gui02_04;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

/**
 * swing.JFrameによるデジタル時計
 * 
 * @author hwatanabe
 *
 */
public class ClockFrame extends JFrame implements SettingsListener {

	private static final long serialVersionUID = 1L;
	/** 描画フォント */
	private Font font = new Font("Segoe UI", Font.PLAIN, 80);
	/** 現在時刻 */
	private volatile Calendar now = Calendar.getInstance();

	private Settings settings = Settings.getDefaultSettings();

	private SettingDialog settingDialog;

	private int strWidth;	

	private final Preferences prefs = Preferences.userRoot().node("hodaka.hw.watanabe.clock");

	public ClockFrame() {
		super("Digital Clock");
		setSize(370, 140);
		JPanel panel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Dimension frameSize = getSize();
				g.setColor(settings.bgColor);
				g.fillRect(0, 0, ClockFrame.this.getWidth(), ClockFrame.this.getHeight());
				g.setColor(settings.fontColor);
				g.setFont(font);
				g.drawString(Consts.Properties.CLOCK_FORMAT.format(now.getTime()),
						(frameSize.width - strWidth) / 2, frameSize.height - 25);
			}
		};
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu(Consts.Strings.MENU);
		JMenuItem item = new JMenuItem(Consts.Strings.SETTINGS);
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (settingDialog == null) {
					settingDialog = new SettingDialog(ClockFrame.this);
					settingDialog.setSettingsListener(ClockFrame.this);
				}
				settingDialog.setSelection(settings);
				settingDialog.setVisible(true);
			}
		});
		menu.add(item);
		menuBar.add(menu);
		setJMenuBar(menuBar);
		loadPrefs();
		int fontType = settings.bold ? Font.BOLD : Font.PLAIN;
		fontType |= settings.italic ? Font.ITALIC : Font.PLAIN;
		font = new Font(settings.fontName, fontType, settings.fontSize);
		getContentPane().add(panel);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				savePrefs();
				System.exit(0);
			}
		});
	}

	public void start() {
		new Timer().schedule(new TimerTask() {
			public void run() {
				now = Calendar.getInstance();
				repaint();
			}
		}, 0, 1000);
		setVisible(true);
		autoResize();
	}

	public static void main(String[] args) {
		new ClockFrame().start();
	}

	@Override
	public void onSettingsChanged(Settings settings) {
		this.settings = settings;
		int fontType = settings.bold ? Font.BOLD : Font.PLAIN;
		fontType |= settings.italic ? Font.ITALIC : Font.PLAIN;
		font = new Font(settings.fontName, fontType, settings.fontSize);
		autoResize();
	}

	private void autoResize() {
		strWidth = getGraphics().getFontMetrics(font).stringWidth("00:00:00");
		int frameWidth = strWidth + 50;
		int frameHeight = font.getSize() + 75;
		setSize(frameWidth, frameHeight);
	}
	private void loadPrefs() {
		String fontName = prefs.get(Consts.PrefName.FONT_NAME, settings.fontName);
		int fontSize = prefs.getInt(Consts.PrefName.FONT_SIZE, settings.fontSize);
		boolean bold = prefs.getBoolean(Consts.PrefName.BOLD, settings.bold);
		boolean italic = prefs.getBoolean(Consts.PrefName.ITALIC, settings.italic);
		String fontColorName = prefs.get(Consts.PrefName.FONT_COLOR, Settings.getColorName(settings.fontColor));
		Color fontColor = Settings.getColorMap().get(fontColorName);
		String bgColorName = prefs.get(Consts.PrefName.BG_COLOR, Settings.getColorName(settings.bgColor));
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

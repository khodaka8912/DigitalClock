package gui01_02;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * awt.Frameによるデジタル時計
 * 
 * @author hwatanabe
 *
 */
public class ClockFrame extends Frame implements SettingsListener {

	/** 現在時刻 */
	private volatile Calendar now = Calendar.getInstance();

	private Settings settings = Settings.getDefaultSettings();
	/** 描画フォント */
	private Font font = new Font(settings.fontName, Font.PLAIN, settings.fontSize);

	private SettingDialog settingDialog;

	private int strWidth;

	private Image imageBuffer;
	private Graphics graphicBuffer;

	public ClockFrame() {
		super("Digital Clock");
		setResizable(false);
		setFont(Consts.Properties.MENU_DEFAULT_FONT);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		MenuBar menuBar = new MenuBar();
		Menu menu = new Menu("File");
		MenuItem item = new MenuItem("Settings");
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
		setMenuBar(menuBar);

	}

	private void init() {
		imageBuffer = createImage(getWidth(), getHeight());
		graphicBuffer = imageBuffer.getGraphics();
		autoResize();
	}

	private void autoResize() {
		strWidth = graphicBuffer.getFontMetrics(font).stringWidth("00:00:00");
		int frameWidth = strWidth + 50;
		int frameHeight = font.getSize() + 80;
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
				frameSize.height - 25);
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
		fontType |= settings.itaric ? Font.ITALIC : Font.PLAIN;
		font = new Font(settings.fontName, fontType, settings.fontSize);
		autoResize();
	}

	public static void main(String[] args) {
		new ClockFrame().start();
	}

}

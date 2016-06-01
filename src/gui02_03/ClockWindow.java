package gui02_03;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JWindow;

/**
 * swingによるデジタル時計
 * 
 * @author hwatanabe
 *
 */
@SuppressWarnings("serial")
public class ClockWindow extends JWindow implements SettingsListener {

	/** 現在時刻 */
	private volatile Calendar now = Calendar.getInstance();

	private Settings settings = Settings.getDefaultSettings();
	/** 描画フォント */
	private Font font = new Font(settings.fontName, Font.PLAIN, settings.fontSize);

	private SettingPopupMenu settingMenu;
	private SettingDialog settingDialog;
	
	private int strWidth;
	private int strHeight;
	private JPanel clockPanel;

	public ClockWindow(JFrame owner) {
		super(owner);
		// owner.setResizable(false);
		setAlwaysOnTop(true);
		setFont(Consts.Properties.MENU_DEFAULT_FONT);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		clockPanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Dimension frameSize = getSize();
				g.setColor(settings.bgColor);
				g.fillRect(0, 0, ClockWindow.this.getWidth(), ClockWindow.this.getHeight());
				g.setColor(settings.fontColor);
				g.setFont(font);
				g.drawString(Consts.Properties.CLOCK_FORMAT.format(now.getTime()),
						(frameSize.width - strWidth) / 2, frameSize.height - 25);
			}
		};
		settingDialog = new SettingDialog(owner);
		getContentPane().add(clockPanel);
		addMouseListener(mouseAdapter);
		addMouseMotionListener(mouseAdapter);
	}


	private void autoResize() {
		FontMetrics metrics = getGraphics().getFontMetrics(font);
		strWidth = metrics.stringWidth("00:00:00");
		strHeight = metrics.getAscent();
		int frameWidth = strWidth + 50;
		int frameHeight = strHeight + 50;
		clockPanel.setSize(frameWidth, frameHeight);
		setSize(frameWidth, frameHeight);
	}

	@Override
	public void update(Graphics g) {
		paint(g);
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

	@Override
	public void onSettingsChanged(Settings settings) {
		this.settings = settings;
		settingDialog.setSelection(settings);
		int fontType = settings.bold ? Font.BOLD : Font.PLAIN;
		fontType |= settings.itaric ? Font.ITALIC : Font.PLAIN;
		font = new Font(settings.fontName, fontType, settings.fontSize);
		autoResize();
	}

	public static void main(String[] args) {
		ClockWindow window = new ClockWindow(new JFrame());
		window.start();
	}

	private MouseAdapter mouseAdapter = new MouseAdapter() {

		private Point start;
		private int currentButton;

		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON3) {
				if (settingMenu == null) {
					settingMenu = new SettingPopupMenu(settingDialog);
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
}

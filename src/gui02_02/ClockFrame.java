package gui02_02;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import gui01_02.Consts;

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
		getContentPane().add(panel);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
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
		fontType |= settings.itaric ? Font.ITALIC : Font.PLAIN;
		font = new Font(settings.fontName, fontType, settings.fontSize);
		autoResize();
	}

	private void autoResize() {
		strWidth = getGraphics().getFontMetrics(font).stringWidth("00:00:00");
		int frameWidth = strWidth + 50;
		int frameHeight = font.getSize() + 75;
		setSize(frameWidth, frameHeight);
	}

}

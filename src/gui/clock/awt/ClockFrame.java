package gui.clock.awt;

import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * awt.Frameによるデジタル時計
 * 
 * @author hwatanabe
 *
 */
public class ClockFrame extends Frame {

	private static final long serialVersionUID = 1L;
	/** 時刻フォーマット */
	private static final DateFormat CLOCK_FORMAT = new SimpleDateFormat("HH:mm:ss");
	/** 描画フォント */
	private Font font = new Font("Century Gothic", Font.PLAIN, 60);
	/** 現在時刻 */
	private volatile Calendar now = Calendar.getInstance();

	public ClockFrame() {
		super("Digital Clock");
		setSize(300, 130);
		setFont(font);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}

	@Override
	public void paint(Graphics g) {
		g.drawString(CLOCK_FORMAT.format(now.getTime()), 30, 95);
		super.paint(g);
	}

	public void start() {
		new Timer().schedule(new TimerTask() {
			public void run() {
				now = Calendar.getInstance();
				repaint();
			}
		}, 0, 1000);
		setVisible(true);
	}

	public static void main(String[] args) {
		new ClockFrame().start();
	}
}

package gui02_01;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * swing.JFrameによるデジタル時計
 * 
 * @author hwatanabe
 *
 */
public class ClockFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	/** 時刻フォーマット */
	private static final DateFormat CLOCK_FORMAT = new SimpleDateFormat("HH:mm:ss");
	/** 描画フォント */
	private Font font = new Font("Segoe UI", Font.PLAIN, 80);
	/** 現在時刻 */
	private volatile Calendar now = Calendar.getInstance();

	public ClockFrame() {
		super("Digital Clock");
		setSize(370, 140);
		JPanel panel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.setColor(Color.WHITE);
				g.fillRect(0, 0, ClockFrame.this.getWidth(), ClockFrame.this.getHeight());
				g.setColor(Color.BLACK);
				g.setFont(font);
				g.drawString(CLOCK_FORMAT.format(now.getTime()), 30, 80);
			}
		};
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
	}

	public static void main(String[] args) {
		new ClockFrame().start();
	}
}

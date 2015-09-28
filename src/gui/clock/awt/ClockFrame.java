package gui.clock.awt;

import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Calendar;

/**
 * awt.Frameによるデジタル時計
 * 
 * @author hwatanabe
 *
 */
public class ClockFrame extends Frame {

	private static final String CLOCK_FORMAT = "%1$02d:%2$02d:%3$02d";

	private Font font = new Font("Century Gothic", Font.PLAIN, 60);

	private volatile Calendar now = Calendar.getInstance();

	@Override
	public void paint(Graphics g) {
		g.setFont(font);
		String time = String.format(CLOCK_FORMAT, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE),
				now.get(Calendar.SECOND));
		g.drawString(time, 30, 95);
		super.paint(g);
	}

	void start() {
		setSize(300, 130);
		setTitle("Digital Clock");
		setVisible(true);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					now = Calendar.getInstance();
					repaint();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException ignore) {
					}
				}
			}
		}).start();
	}

	public static void main(String[] args) {
		new ClockFrame().start();
	}

}

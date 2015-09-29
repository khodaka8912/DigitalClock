package gui.clock.awt;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class ClockFrameTest {	

	@Test
	public void testClockFrame() {
		ClockFrame target = new ClockFrame();
		assertThat(target, is(notNullValue()));
		assertThat(target.isValid(), is(false));
	}
	
	@Test
	public void testStart() {
		ClockFrame target = new ClockFrame();
		target.start();
		assertThat(target.isVisible(), is(true));
	}
}

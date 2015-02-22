package no.tagstory.utils;

import junit.framework.TestCase;

public class StringUtilsTest extends TestCase {

	public void testFormatDistance() throws Exception {
		assertEquals("999 meters", StringUtils.formatDistance(999));
		assertEquals("1.00 km", StringUtils.formatDistance(1000));
		assertEquals("4.32 km", StringUtils.formatDistance(4321));
	}

	public void testFormatDuration() throws Exception {
		assertEquals("58 seconds", StringUtils.formatDuration(58000));
		assertEquals("34 minutes", StringUtils.formatDuration(2040000));
		assertEquals("2h and 34m", StringUtils.formatDuration(9240000));
		assertEquals("2h and 34m", StringUtils.formatDuration(9298000));
	}
}
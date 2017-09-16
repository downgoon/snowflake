package xyz.downgoon.snowflake.util;

public class BinHexUtil {

	public static String bin(long d) {
		return leftZeroPadding64(Long.toBinaryString(d));
	}

	public static String hex(long d) {
		return leftZeroPadding16(Long.toHexString(d).toUpperCase());
	}

	/**
	 * a diode is a long value whose left and right margin are ZERO, while
	 * middle bits are ONE in binary string layout. it looks like a diode in
	 * shape.
	 * 
	 * @param offset
	 *            left margin position
	 * @param length
	 *            offset+length is right margin position
	 * @return a long value
	 */
	public static long diode(int offset, int length) {
		if (offset < 0 || length < 0 || (offset + length) > 64) {
			throw new IllegalArgumentException("bits ranges: [0, 64)");
		}
		if (length == 0) {
			return 0L;
		}
		if (length == 64) {
			return -1L;
		}
		int lb = 64 - offset;
		int rb = 64 - (offset + length);
		return (-1L << lb) ^ (-1L << rb);
	}

	private static final String ZERO_PADDING_64 = "0000000000000000000000000000000000000000000000000000000000000000";

	private static String leftZeroPadding64(String text) {
		return ZERO_PADDING_64.substring(text.length()) + text;
	}

	private static final String ZERO_PADDING_16 = "0000000000000000";

	private static String leftZeroPadding16(String text) {
		return ZERO_PADDING_16.substring(text.length()) + text;
	}

}

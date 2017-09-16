package xyz.downgoon.snowflake;

import xyz.downgoon.snowflake.util.BinHexUtil;

public class SnowflakeDemo1 {

	/*
	 * 由于``System.out.println``IO操作比较耗时，导致前后两次生成ID的时间间隔偶尔会超过1毫秒。
	 * 超过1毫秒的，序号字段会是``#0``；没超过1毫秒的，序号字段会累加。
	 */

	public static void main(String[] args) {
		Snowflake snowflake = new Snowflake(2, 5);
		for (int i = 0; i < 20; i++) {
			long id = snowflake.nextId();
			System.out.println(String.format("%s => id: %d, hex: %s, bin: %s", snowflake.formatId(id), id,
					BinHexUtil.hex(id), BinHexUtil.bin(id)));
		}
	}

}

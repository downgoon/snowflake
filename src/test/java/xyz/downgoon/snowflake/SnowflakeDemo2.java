package xyz.downgoon.snowflake;

import java.util.ArrayList;
import java.util.List;

import xyz.downgoon.snowflake.util.BinHexUtil;

public class SnowflakeDemo2 {

	/*
	 * 快速生成1千个ID，基本在1毫秒内就能
	 * */
	public static void main(String[] args) {
		Snowflake snowflake = new Snowflake(2, 5);
		final int idAmout = 1000;
		List<Long> idPool = new ArrayList<Long>(idAmout);
		for (int i = 0; i < idAmout; i++) {
			long id = snowflake.nextId();
			idPool.add(id);
		}

		for (Long id : idPool) {
			System.out.println(String.format("%s => id: %d, hex: %s, bin: %s", snowflake.formatId(id), id,
					BinHexUtil.hex(id), BinHexUtil.bin(id)));
		}

	}

}

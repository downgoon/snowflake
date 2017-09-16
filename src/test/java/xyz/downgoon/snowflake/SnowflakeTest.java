package xyz.downgoon.snowflake;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class SnowflakeTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testNextIdAndParse() throws Exception {
		long beginTimeStamp = System.currentTimeMillis();
		Snowflake snowflake = new Snowflake(3, 16);
	
		// gen id and parse it
		long id = snowflake.nextId();
		long[] arr = snowflake.parseId(id);
		System.out.println(snowflake.formatId(id));
		
		Assert.assertTrue(arr[0] >= beginTimeStamp);
		Assert.assertEquals(3, arr[1]); // datacenterId
		Assert.assertEquals(16, arr[2]); // workerId
		Assert.assertEquals(0, arr[3]); // sequenceId

		// gen two ids in different MS
		long id2 = snowflake.nextId();
		Assert.assertFalse(id == id2);
		System.out.println(snowflake.formatId(id2));
		 
		Thread.sleep(1); // wait one ms 
		long id3 = snowflake.nextId();
		long[] arr3 = snowflake.parseId(id3);
		System.out.println(snowflake.formatId(id3));
		Assert.assertTrue(arr3[0] > arr[0]);
		
		// gen two ids in the same MS
		long[] ids = new long[2];
		for (int i = 0; i < ids.length; i ++) {
			ids[i] = snowflake.nextId();
		}
		System.out.println(snowflake.formatId(ids[0]));
		System.out.println(snowflake.formatId(ids[1]));
		Assert.assertFalse(ids[0] == ids[1]);
		long[] arr_ids0 = snowflake.parseId(ids[0]);
		long[] arr_ids1 = snowflake.parseId(ids[1]);
		Assert.assertEquals(arr_ids0[0], arr_ids1[0]);
		Assert.assertEquals(arr_ids0[3], arr_ids1[3] - 1);
	}
	
}

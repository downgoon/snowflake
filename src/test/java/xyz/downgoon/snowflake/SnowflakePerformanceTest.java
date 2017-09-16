package xyz.downgoon.snowflake;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import xyz.downgoon.snowflake.ConcurrentTestFramework.SummaryReport;

public class SnowflakePerformanceTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testSingleThread() {
		int n1 = 1000000; // 1百万次
		long[] r1 = runC1N(n1);
		showReport(1, n1, r1);
		
		int n2 = 10000000; // 1千万次
		long[] r2 = runC1N(n2);
		showReport(1, n2, r2);
	}
	
	@Test
	public void testC10N10w() throws Exception {
		ConcurrentTestFramework ctf = new ConcurrentTestFramework("C10N10w");
		final Snowflake snowflake = new Snowflake(2, 5);
		SummaryReport report = ctf.test(10, 100000, new Runnable() {
			
			@Override
			public void run() {
				snowflake.nextId();
			}
		});
		report.setAttachment(String.format("wait: %d", snowflake.getWaitCount()));
		System.out.println("C10N10w Report: " + report);
	}

	/**
	 * @return time cost in MS, wait count
	 */
	private long[] runC1N(int n) {
		Snowflake snowflake = new Snowflake(2, 5);
		long btm = System.currentTimeMillis();
		for (int i = 0; i < n; i++) {
			snowflake.nextId();
		}
		long etm = System.currentTimeMillis();
		long[] r = new long[2];
		r[0] = etm - btm;
		r[1] = snowflake.getWaitCount();
		return r;
	}

	private void showReport(int c, int n, long[] r) {
		long costMS = r[0];
		long qps = (long) (n / (costMS / 1000.0));
		long qpms = n / costMS;
		System.out
				.println(String.format("C%dN%d: costMS=%d, QPS=%d, QPMS:=%d, wait=%d", c, n, costMS, qps, qpms, r[1]));
	}

}

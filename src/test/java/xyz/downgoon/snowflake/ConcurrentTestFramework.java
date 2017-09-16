package xyz.downgoon.snowflake;

import java.util.concurrent.CountDownLatch;

public class ConcurrentTestFramework {

	private String name;

	public ConcurrentTestFramework(String name) {
		this.name = name;
	}

	static class SummaryReport {

		/** concurrent count */
		private int c;

		/** action number per thread */
		private int n;

		/** start time stamp */
		private volatile long stm = -1L;

		/** end time stamp */
		private volatile long etm = -1L;

		String attachment;

		public SummaryReport(int c, int n) {
			this.c = c;
			this.n = n;
		}

		public boolean startIfNot() {
			if (stm < 0) {
				stm = System.currentTimeMillis();
				return true;
			}
			return false;
		}

		public boolean endIfNot() {
			if (etm < 0) {
				etm = System.currentTimeMillis();
				return true;
			}
			return false;
		}

		public boolean isStarted() {
			return stm > 0;
		}

		public boolean isEnded() {
			return etm > 0;
		}

		public void setAttachment(String attachment) {
			this.attachment = attachment;
		}

		@Override
		public String toString() {
			long costMS = etm - stm;
			long qps = (long) ((c * n) / (costMS / 1000.0));
			long qpms = (c * n) / costMS;
			return String.format("C%dN%d: costMS=%d, QPS=%d, QPMS=%d, %s", c, n, costMS, qps, qpms, attachment);
		}

	}

	public SummaryReport test(final int c, final int n, final Runnable action) throws Exception {
		final SummaryReport report = new SummaryReport(c, n);
		Thread[] threads = new Thread[c];
		final CountDownLatch startLatch = new CountDownLatch(c);
		final CountDownLatch finishLatch = new CountDownLatch(c);

		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(new Runnable() {
				/* Runnable proxy */
				@Override
				public void run() {

					try {
						startLatch.await(); // wait for start cmd
						System.out.println(Thread.currentThread().getName() + " starting ...");
						report.startIfNot();
						for (int j = 0; j < n; j++) {
							action.run();
						}
						finishLatch.countDown(); // report finish
						System.out.println(Thread.currentThread().getName() + " finished !!!");

					} catch (Exception e) {
						e.printStackTrace();
					}

				}

			}, name + "#" + i);
		}

		for (int i = 0; i < threads.length; i++) {
			threads[i].start();
			startLatch.countDown();
		}

		finishLatch.await(); // wait all finish
		report.endIfNot();
		return report;

	}

}

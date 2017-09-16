package xyz.downgoon.snowflake;

import java.util.concurrent.CountDownLatch;

public class ConcurrentTestFramework {

	private String name;

	private boolean debugMode;

	public ConcurrentTestFramework(String name, boolean debugMode) {
		this.name = name;
		this.debugMode = debugMode;
	}

	static class SummaryReport {

		/** concurrent count */
		private int c;

		/** action number per thread */
		private int n;

		/** start time stamp */
		private volatile long startNanoTime = -1L;

		/** end time stamp */
		private volatile long endedNanoTime = -1L;

		private String attachment;

		public SummaryReport(int c, int n) {
			this.c = c;
			this.n = n;
		}

		public boolean startIfNot() {
			if (startNanoTime < 0) {
				startNanoTime = System.nanoTime();
				return true;
			}
			return false;
		}

		public boolean endIfNot() {
			if (endedNanoTime < 0) {
				endedNanoTime = System.nanoTime();
				return true;
			}
			return false;
		}

		public boolean isStarted() {
			return startNanoTime > 0;
		}

		public boolean isEnded() {
			return endedNanoTime > 0;
		}

		public void setAttachment(String attachment) {
			this.attachment = attachment;
		}

		@Override
		public String toString() {
			long costNano = (endedNanoTime - startNanoTime);
			long costMS = costNano / (1000000L);

			/* 1ms = 1000us = 1000*1000 nano */
			double qpms = ((c * n + 0.0) / (costNano)) * 1000000;
			double qps = (c * n + 0.0) / (costNano / 1000000000.0);

			if (qpms > 4096) { // max sequence
				/* anti time precision lost by plus 1 ms */
				// costNano += 1000000L;
				qpms = 4096;
				qps = qpms * 1000L;
			}

			return String.format("C%dN%d: costMS=%d, QPMS=%.2f, costSec=%d, QPS=%.2f, %s", c, n, costMS, qpms,
					(costMS / 1000L), qps, attachment);
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
						if (debugMode) {
							System.out.println(Thread.currentThread().getName() + " starting ...");
						}
						report.startIfNot();
						for (int j = 0; j < n; j++) {
							action.run();
						}
						finishLatch.countDown(); // report finish
						if (debugMode) {
							System.out.println(Thread.currentThread().getName() + " finished !!!");
						}

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

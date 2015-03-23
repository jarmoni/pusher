package org.jarmoni.pusher.file;

import java.nio.file.Paths;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class FileChangeScannerTest {

	// CHECKSTYLE:OFF
	@Rule
	public TemporaryFolder tf = new TemporaryFolder();

	// CHECKSTYLE:ON

	@Test
	public void testScan() throws Exception {
		final CountDownLatch cdl = new CountDownLatch(1);
		final IFileChangeListener fcl = event -> cdl.countDown();

		final FileChangeScanner fcs = new FileChangeScanner(fcl, Paths.get(tf.getRoot().getAbsolutePath()));
		fcs.start();
		this.tf.newFile("test");
		cdl.await(2, TimeUnit.SECONDS);
	}
}

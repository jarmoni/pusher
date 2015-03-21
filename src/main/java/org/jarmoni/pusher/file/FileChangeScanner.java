package org.jarmoni.pusher.file;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.WatchService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;

public class FileChangeScanner {

	private static final Logger LOG = LoggerFactory.getLogger(FileChangeScanner.class);

	private final Path rootDir;

	public FileChangeScanner(final Path rootDir) {
		this.rootDir = Preconditions.checkNotNull(rootDir);
		LOG.info("Scanning path={}", rootDir);
		this.scan();
	}

	private void scan() {
		try (final WatchService watchService = FileSystems.getDefault().newWatchService()) {
			this.rootDir.register(watchService, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
		}
		catch (final Throwable e) {
			Throwables.propagate(e);
		}
	}

}

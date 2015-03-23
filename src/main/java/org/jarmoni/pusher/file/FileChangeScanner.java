package org.jarmoni.pusher.file;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;

public class FileChangeScanner {

	private static final Logger LOG = LoggerFactory.getLogger(FileChangeScanner.class);

	private final IFileChangeListener changeListener;
	private final Path path;

	private boolean stopped = true;

	private Runnable scannerThread;

	public FileChangeScanner(final IFileChangeListener changeListener, final Path path) {
		this.changeListener = Preconditions.checkNotNull(changeListener);
		this.path = Preconditions.checkNotNull(path);
	}

	public void start() {
		LOG.info("Trying to start FileChangeScanner for path={}...", this.path);
		this.scannerThread = () -> scan(this.path);
		Executors.newSingleThreadExecutor().execute(this.scannerThread);
		this.stopped = false;
		LOG.info("FileChangeScanner has started. Scanning path={}", this.path);
	}

	public void stop() {
		LOG.info("Trying to stop FileChangeScanner for path={}...", this.path);
		this.stopped = true;

	}

	@SuppressWarnings("unchecked")
	private void scan(final Path path) {
		try (final WatchService watchService = path.getFileSystem().newWatchService()) {
			path.register(watchService, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
			WatchKey key = watchService.take();
			while (key != null && !stopped) {
				for (final WatchEvent<?> event : key.pollEvents()) {
					LOG.debug("Received event={}, Path={}", event, path);
					this.changeListener.fileChanged((WatchEvent<Path>) event);
				}
				key.reset();
				key = watchService.take();
			}
		}
		catch (final Throwable e) {
			Throwables.propagate(e);
		}
		finally {
			this.stopped = true;
			LOG.info("FileChangeScanner has stopped. Path={}", this.path);
		}
	}
}

package org.jarmoni.pusher.file;

import java.nio.file.Path;
import java.nio.file.WatchEvent;

public interface IFileChangeListener {

	void fileChanged(WatchEvent<Path> event);

}

package org.jarmoni.pusher.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;

public class PusherService {

	private Path appHome;

	public PusherService(final String appHome) {
		final Path path = Paths.get(Preconditions.checkNotNull(appHome));
		if(!Files.isDirectory(this.appHome)) {
			this.appHome = path;
		}
		else {
			try {
				this.appHome = Files.createDirectory(path);
			}
			catch(final Throwable t) {
				Throwables.propagate(t);
			}
		}
	}

	public String getAppHome() {
		return this.appHome.toString();
	}

}

package org.jarmoni.pusher.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.jarmoni.resource.Repository;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;

public class PusherService {

	private Path appHome;

	private List<Repository> repositories;

	public PusherService(final String appHome) {
		final Path path = Paths.get(Preconditions.checkNotNull(appHome));
		if (Files.isDirectory(path)) {
			this.appHome = path;
		}
		else {
			try {
				this.appHome = Files.createDirectory(path);
			}
			catch (final Throwable t) {
				Throwables.propagate(t);
			}
		}
	}

	public String getAppHome() {
		return this.appHome.toString();
	}

	public List<Repository> getRepositories() {
		return this.repositories;
	}

	public Repository getRepository(final String name) {
		return this.repositories.stream().filter(rep -> name.equals(rep.name)).findFirst().get();
	}
}

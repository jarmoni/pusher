package org.jarmoni.pusher.repository;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;

import org.eclipse.jgit.lib.Repository;
import org.jarmoni.pusher.file.FileChangeScanner;
import org.jarmoni.pusher.file.IFileChangeListener;
import org.jarmoni.pusher.git.GitExecutor;
import org.jarmoni.pusher.resource.RepositoryResource;

import com.google.common.base.Preconditions;

public class RepositoryInstance implements IFileChangeListener {

	private final Repository repository;
	private final RepositoryResource repositoryResource;
	private final GitExecutor gitExecutor;
	private FileChangeScanner fileChangeScanner;

	public RepositoryInstance(final Repository repository, final RepositoryResource repositoryResource,
			final GitExecutor gitExecutor) {
		super();
		this.repository = Preconditions.checkNotNull(repository);
		this.repositoryResource = Preconditions.checkNotNull(repositoryResource);
		this.gitExecutor = Preconditions.checkNotNull(gitExecutor);
	}

	public void start() {
		this.fileChangeScanner = new FileChangeScanner(this, Paths.get(repositoryResource.getPath()));
		this.fileChangeScanner.start();
	}

	public void stop() {
		if (this.fileChangeScanner != null) {
			this.fileChangeScanner.stop();
			this.fileChangeScanner = null;
		}
	}

	@Override
	public void fileChanged(final WatchEvent<Path> event) {
		this.gitExecutor.commitChanges(this.repository, this.repositoryResource);
	}

	public Repository getRepository() {
		return repository;
	}

	public RepositoryResource getRepositoryResource() {
		return repositoryResource;
	}

}

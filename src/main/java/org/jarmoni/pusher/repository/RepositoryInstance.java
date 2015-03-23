package org.jarmoni.pusher.repository;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;

import org.eclipse.jgit.lib.Repository;
import org.jarmoni.pusher.file.FileChangeScanner;
import org.jarmoni.pusher.file.IFileChangeListener;
import org.jarmoni.pusher.git.GitExecutor;
import org.jarmoni.pusher.resource.RepositoryResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

public class RepositoryInstance implements IFileChangeListener {

	private static final Logger LOG = LoggerFactory.getLogger(RepositoryInstance.class);

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
		LOG.info("Trying to start RepositoryInstance={}", repositoryResource.getName());
		this.fileChangeScanner = new FileChangeScanner(this, Paths.get(repositoryResource.getPath()));
		this.fileChangeScanner.start();
		LOG.info("RepositoryInstance started={}", repositoryResource.getName());
	}

	public void stop() {
		if (this.fileChangeScanner != null) {
			LOG.info("Trying to stop RepositoryInstance={}", repositoryResource.getName());
			this.fileChangeScanner.stop();
			this.fileChangeScanner = null;
			LOG.info("RepositoryInstance stopped={}", repositoryResource.getName());
		}
		else {
			LOG.info("RepositoryInstance not running={}", repositoryResource.getName());
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

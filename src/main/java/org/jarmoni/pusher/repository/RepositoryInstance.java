package org.jarmoni.pusher.repository;

import java.nio.file.Path;
import java.nio.file.WatchEvent;

import org.eclipse.jgit.lib.Repository;
import org.jarmoni.pusher.file.IFileChangeListener;
import org.jarmoni.pusher.git.GitExecutor;
import org.jarmoni.pusher.resource.RepositoryResource;

public class RepositoryInstance implements IFileChangeListener {

	private Repository repository;
	private RepositoryResource repositoryResource;
	private GitExecutor gitExecutor;

	@Override
	public void fileChanged(final WatchEvent<Path> event) {
		this.gitExecutor.commitChanges(this.repository, this.repositoryResource);
	}

}

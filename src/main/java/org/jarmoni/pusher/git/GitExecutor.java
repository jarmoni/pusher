package org.jarmoni.pusher.git;

import java.nio.file.Files;
import java.nio.file.Path;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.NoWorkTreeException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Throwables;

public class GitExecutor {

	public static final String GIT_DIR_NAME = ".git";

	private static final Logger LOG = LoggerFactory.getLogger(GitExecutor.class);

	public Repository createRepository(final Path directory) {
		// does directory exist at all?
		if (!Files.isDirectory(directory)) {
			if (!directory.toFile().mkdirs()) {
				throw new RuntimeException("Could not create directory=" + directory);
			}
			LOG.info("Will create GIT-repository in newly created directory");
			return this.createRepositoryInternal(directory);
		}
		else {
			final Path gitDir = directory.resolve(GIT_DIR_NAME);
			// does the (existing) directory contain a '.git'-dir?
			if (!Files.isDirectory(gitDir)) {
				LOG.info("Will create GIT-repository in existing directory");
				return this.createRepositoryInternal(directory);
			}
			else {
				throw new RuntimeException("Directory already contains a '.git'-folder");
			}
		}
	}

	private Repository createRepositoryInternal(final Path directory) {
		try {
			final Repository repos = FileRepositoryBuilder.create(directory.resolve(".git").toFile());
			repos.create();
			repos.close();
			return repos;
		}
		catch (final Throwable t) {
			throw Throwables.propagate(t);
		}
	}
	
	public void commitChanges(Repository repos) {
		Git git = new Git(repos);
		try {
			Status status = git.status().call();
		} catch (Throwable t) {
			throw Throwables.propagate(t);
		}
	}
}

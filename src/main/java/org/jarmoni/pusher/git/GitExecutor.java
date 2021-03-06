package org.jarmoni.pusher.git;

import java.nio.file.Files;
import java.nio.file.Path;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.jarmoni.pusher.resource.RepositoryResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Throwables;

public class GitExecutor {

	public static final String GIT_DIR_NAME = ".git";

	private static final Logger LOG = LoggerFactory.getLogger(GitExecutor.class);

	public Repository openAndCreateRepository(final Path directory) {
		// does directory exist at all?
		if (!Files.isDirectory(directory)) {
			if (!directory.toFile().mkdirs()) {
				throw new RuntimeException("Could not create directory=" + directory);
			}
			LOG.info("Will create GIT-repository in newly created directory");
			return this.createRepository(directory);
		}
		else {
			final Path gitDir = directory.resolve(GIT_DIR_NAME);
			// does the (existing) directory contain a '.git'-dir?
			if (!Files.isDirectory(gitDir)) {
				LOG.info("Will create GIT-repository in existing directory");
				return this.createRepository(directory);
			}
			else {
				throw new RuntimeException("Directory already contains a '" + GIT_DIR_NAME + "'-folder");
			}
		}
	}

	public Repository openRepository(final Path directory) {
		try {
			return FileRepositoryBuilder.create(directory.resolve(GIT_DIR_NAME).toFile());
		}
		catch (final Throwable t) {
			throw Throwables.propagate(t);
		}
	}

	public Repository createRepository(final Path directory) {
		try {
			final Repository repos = this.openRepository(directory);
			repos.create();
			repos.close();
			return repos;
		}
		catch (final Throwable t) {
			throw Throwables.propagate(t);
		}
	}

	public void commitChanges(final Repository repos, final RepositoryResource reposResource) {
		final Git git = new Git(repos);
		try {
			final Status status = git.status().call();
			if (status.isClean()) {
				return;
			}
			if (!status.getConflicting().isEmpty()) {
				throw new IllegalStateException("Repository contains conflicted files. Repos=" + repos);
			}
			git.add().addFilepattern(".").call();
			git.add().setUpdate(true).addFilepattern(".").call();
			git.commit().setCommitter(reposResource.getUserName(), reposResource.getUserEmail())
					.setMessage(reposResource.getCommitMsg()).call();
			if (reposResource.isAutoSync()) {
				git.pull().setRemote("origin").setRemoteBranchName("master").call();
				git.push().setRemote("origin").setPushAll().call();
			}
		}
		catch (final Throwable t) {
			throw Throwables.propagate(t);
		}
	}
}

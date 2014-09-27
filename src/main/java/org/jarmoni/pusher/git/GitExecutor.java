package org.jarmoni.pusher.git;

import java.nio.file.Files;
import java.nio.file.Path;

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
				// does the (existing) '.git'-dir hold a valid repository?
				final FileRepositoryBuilder builder = new FileRepositoryBuilder();
				try {
					final Repository repos = builder.setGitDir(gitDir.toFile()).build();
					LOG.info("Valid GIT-repository already exists. Nothing to do");
					return repos;

				}
				catch (final Throwable t) {
					throw Throwables.propagate(t);
				}
			}
		}
	}

	private Repository createRepositoryInternal(final Path directory) {
		return null;

	}
}

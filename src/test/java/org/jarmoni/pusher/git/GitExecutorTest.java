package org.jarmoni.pusher.git;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

public class GitExecutorTest {

	// CHECKSTYLE:OFF
	@Rule
	public TemporaryFolder tf = new TemporaryFolder();

	@Rule
	public ExpectedException ee = ExpectedException.none();
	// CHECKSTYLE:ON

	private Path reposRoot;
	private final GitExecutor gitExecutor = new GitExecutor();

	@Before
	public void setUp() throws Exception {
		this.reposRoot = Paths.get(this.tf.newFolder("reposRoot").toURI());
	}

	@Test
	public void testCreateRepositoryGitFolderAlreadyPresent() throws Exception {
		this.ee.expect(RuntimeException.class);
		this.ee.expectMessage("Directory already contains a '.git'-folder");
		final Path gitDir = this.reposRoot.resolve(".git");
		Files.createDirectory(gitDir);
		this.gitExecutor.createRepository(this.reposRoot);
	}

}

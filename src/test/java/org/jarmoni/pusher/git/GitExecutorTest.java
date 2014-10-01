package org.jarmoni.pusher.git;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.lib.Repository;
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

	@Test
	public void testCreateRepositoryReposFolderAlreadyPresent() throws Exception {
		assertFalse(Files.isDirectory(this.reposRoot.resolve(".git")));
		this.gitExecutor.createRepository(this.reposRoot);
		assertTrue(Files.isDirectory(this.reposRoot.resolve(".git")));
	}

	@Test
	public void testCreateRepositoryReposFolderNotPresent() throws Exception {
		Files.deleteIfExists(this.reposRoot);
		assertFalse(Files.isDirectory(this.reposRoot));
		this.gitExecutor.createRepository(this.reposRoot);
		assertTrue(Files.isDirectory(this.reposRoot));
		assertTrue(Files.isDirectory(this.reposRoot.resolve(".git")));
	}

	@Test
	public void testCreateRepositoryNewlyCreatedReposIsValid() throws Exception {
		Repository repository = this.gitExecutor.createRepository(this.reposRoot);
		Git git = new Git(repository);
		Status status = git.status().call();
		assertEquals(0, status.getAdded().size());
		assertEquals(0, status.getUntracked().size());
		assertEquals(0, status.getChanged().size());
		assertEquals(0, status.getConflicting().size());
		assertEquals(0, status.getUncommittedChanges().size());
		assertEquals(0, status.getMissing().size());
		assertEquals(0, status.getModified().size());
		assertEquals(0, status.getRemoved().size());

		Path testFile = this.reposRoot.resolve("testfile");
		Files.createFile(testFile);

		Status status2 = git.status().call();
		assertEquals(1, status2.getUntracked().size());
		assertEquals(0, status2.getAdded().size());
		assertEquals(0, status2.getChanged().size());
		assertEquals(0, status2.getConflicting().size());
		assertEquals(0, status2.getUncommittedChanges().size());
		assertEquals(0, status2.getMissing().size());
		assertEquals(0, status2.getModified().size());
		assertEquals(0, status2.getRemoved().size());

		git.add().addFilepattern(".").call();
		Status status3 = git.status().call();
		assertEquals(0, status3.getUntracked().size());
		assertEquals(1, status3.getAdded().size());
		assertEquals(0, status3.getChanged().size());
		assertEquals(0, status3.getConflicting().size());
		assertEquals(1, status3.getUncommittedChanges().size());
		assertEquals(0, status3.getMissing().size());
		assertEquals(0, status3.getModified().size());
		assertEquals(0, status3.getRemoved().size());

		git.commit().setMessage("commit no.1").setCommitter("john doe", "john@doe.com").call();
		Status status4 = git.status().call();
		assertEquals(0, status4.getUntracked().size());
		assertEquals(0, status4.getAdded().size());
		assertEquals(0, status4.getChanged().size());
		assertEquals(0, status4.getConflicting().size());
		assertEquals(0, status4.getUncommittedChanges().size());
		assertEquals(0, status4.getMissing().size());
		assertEquals(0, status4.getModified().size());
		assertEquals(0, status4.getRemoved().size());

		Files.write(testFile, "xyz".getBytes());
		Status status5 = git.status().call();
		System.out.println(testFile);
		assertEquals(0, status5.getUntracked().size());
		assertEquals(0, status5.getAdded().size());
		assertEquals(0, status5.getChanged().size());
		assertEquals(0, status5.getConflicting().size());
		assertEquals(1, status5.getUncommittedChanges().size());
		assertEquals(0, status5.getMissing().size());
		assertEquals(1, status5.getModified().size());
		assertEquals(0, status5.getRemoved().size());

		Files.delete(testFile);
		Status status6 = git.status().call();
		System.out.println(testFile);
		assertEquals(0, status6.getUntracked().size());
		assertEquals(0, status6.getAdded().size());
		assertEquals(0, status6.getChanged().size());
		assertEquals(0, status6.getConflicting().size());
		assertEquals(1, status6.getUncommittedChanges().size());
		assertEquals(1, status6.getMissing().size());
		assertEquals(0, status6.getModified().size());
		assertEquals(0, status6.getRemoved().size());

		git.add().setUpdate(true).addFilepattern(".").call();
		Status status7 = git.status().call();
		System.out.println(testFile);
		assertEquals(0, status7.getUntracked().size());
		assertEquals(0, status7.getAdded().size());
		assertEquals(0, status7.getChanged().size());
		assertEquals(0, status7.getConflicting().size());
		assertEquals(1, status7.getUncommittedChanges().size());
		assertEquals(0, status7.getMissing().size());
		assertEquals(0, status7.getModified().size());
		assertEquals(1, status7.getRemoved().size());
	}
}

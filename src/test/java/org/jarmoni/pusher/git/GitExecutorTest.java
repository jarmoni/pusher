package org.jarmoni.pusher.git;

import static org.jarmoni.pusher.git.GitExecutor.GIT_DIR_NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.jarmoni.resource.RepositoryResource;
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
		this.ee.expectMessage("Directory already contains a '" + GIT_DIR_NAME + "'-folder");
		final Path gitDir = this.reposRoot.resolve(GIT_DIR_NAME);
		Files.createDirectory(gitDir);
		this.gitExecutor.createRepository(this.reposRoot);
	}

	@Test
	public void testCreateRepositoryReposFolderAlreadyPresent() throws Exception {
		assertFalse(Files.isDirectory(this.reposRoot.resolve(GIT_DIR_NAME)));
		this.gitExecutor.createRepository(this.reposRoot);
		assertTrue(Files.isDirectory(this.reposRoot.resolve(GIT_DIR_NAME)));
	}

	@Test
	public void testCreateRepositoryReposFolderNotPresent() throws Exception {
		Files.deleteIfExists(this.reposRoot);
		assertFalse(Files.isDirectory(this.reposRoot));
		this.gitExecutor.createRepository(this.reposRoot);
		assertTrue(Files.isDirectory(this.reposRoot));
		assertTrue(Files.isDirectory(this.reposRoot.resolve(GIT_DIR_NAME)));
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
		// System.out.println(testFile);
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
		// System.out.println(testFile);
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
		// System.out.println(testFile);
		assertEquals(0, status7.getUntracked().size());
		assertEquals(0, status7.getAdded().size());
		assertEquals(0, status7.getChanged().size());
		assertEquals(0, status7.getConflicting().size());
		assertEquals(1, status7.getUncommittedChanges().size());
		assertEquals(0, status7.getMissing().size());
		assertEquals(0, status7.getModified().size());
		assertEquals(1, status7.getRemoved().size());
	}

	@Test
	public void testCommitChanges() throws Exception {
		// Create bare repos
		final Repository bareRepos = FileRepositoryBuilder.create(this.reposRoot.resolve(GIT_DIR_NAME).toFile());
		bareRepos.create(true);
		Path clonePath = Paths.get(this.tf.newFolder("root").toURI());
		Git git = Git.cloneRepository().setURI(this.reposRoot.toString()).setDirectory(clonePath.toFile()).call();
		Repository clone = git.getRepository();
		git.commit().setCommitter("666", "666@777.com").setMessage("initial").call();
		git.push().setRemote("origin").setPushAll().call();
		bareRepos.close();

		// System.out.println(clonePath.toString());

		Path path1 = clonePath.resolve("file1");
		Files.createFile(path1);
		git.add().addFilepattern(".").call();
		git.commit().setCommitter("john doe", "john@doe.com").setMessage("some message").call();
		Files.write(path1, "abc".getBytes());

		Path path2 = clonePath.resolve("file2");
		Files.createFile(path2);
		git.add().addFilepattern("file2").call();
		git.commit().setCommitter("john doe", "john@doe.com").setMessage("another message").call();
		Files.delete(path2);

		Files.createFile(clonePath.resolve("file3"));
		git.add().addFilepattern("file3").call();

		Files.createFile(clonePath.resolve("file4"));

		Status status = git.status().call();
		assertEquals(1, status.getUntracked().size());
		assertEquals(1, status.getAdded().size());
		assertEquals(0, status.getChanged().size());
		assertEquals(0, status.getConflicting().size());
		assertEquals(3, status.getUncommittedChanges().size());
		assertEquals(1, status.getMissing().size());
		assertEquals(1, status.getModified().size());
		assertEquals(0, status.getRemoved().size());

		RepositoryResource reposResource = RepositoryResource.builder().name("myrepos").autoPush(true).userName("xyz")
				.userEmail("xyz@abc.at").commitMsg("my msg").build();
		this.gitExecutor.commitChanges(clone, reposResource);

		Status status2 = git.status().call();
		assertEquals(0, status2.getUntracked().size());
		assertEquals(0, status2.getAdded().size());
		assertEquals(0, status2.getChanged().size());
		assertEquals(0, status2.getConflicting().size());
		assertEquals(0, status2.getUncommittedChanges().size());
		assertEquals(0, status2.getMissing().size());
		assertEquals(0, status2.getModified().size());
		assertEquals(0, status2.getRemoved().size());

		Iterable<RevCommit> logs = git.log().setMaxCount(1).call();
		RevCommit commit = logs.iterator().next();
		assertEquals("xyz", commit.getAuthorIdent().getName());
		assertEquals("xyz@abc.at", commit.getAuthorIdent().getEmailAddress());
		assertEquals("my msg", commit.getFullMessage());
		String commitChecksum = commit.getName();
		// System.out.println(commit.getName());
		Map<String, Ref> refs = clone.getAllRefs();
		boolean originMasterPresent = false;
		for (Entry<String, Ref> entry : refs.entrySet()) {
			if (entry.getKey().contains("origin/master")) {
				originMasterPresent = true;
			}
			assertTrue(entry.getValue().toString().contains(commitChecksum));
		}
		assertTrue(originMasterPresent);
	}
}

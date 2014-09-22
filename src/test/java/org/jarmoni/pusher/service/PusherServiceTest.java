package org.jarmoni.pusher.service;

import static org.jarmoni.pusher.service.PusherService.REPOS_FILE_NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.jarmoni.resource.Repository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.google.common.collect.Lists;

public class PusherServiceTest {

	//CHECKSTYLE:OFF
	@Rule
	public TemporaryFolder tf = new TemporaryFolder();
	//CHECKSTYLE:ON

	private Path appHome;
	private Path reposFile;
	private PusherService pusherService;

	@Before
	public void setUp() throws Exception {
		this.appHome = this.tf.newFolder().toPath();
		assertTrue(Files.isDirectory(this.appHome));
		this.reposFile = appHome.resolve(REPOS_FILE_NAME);
		assertFalse(Files.exists(this.reposFile));
		this.pusherService = new PusherService(this.appHome.toString());
	}

	@Test
	public void testConstructorCreateMissingFolder() throws Exception {
		Files.deleteIfExists(this.appHome);
		assertFalse(Files.exists(this.appHome));
		this.pusherService = new PusherService(this.appHome.toString());
	}

	@Test
	public void testReloadRepositoriesReposFileNotExisting() throws Exception {
		this.pusherService.reloadRepositories();
		assertEquals(0, this.pusherService.getRepositories().size());
	}

	@Test
	public void testReloadRepositoriesReposFileExists() throws Exception {
		assertFalse(Files.exists(reposFile));
		final long len = Files.copy(this.getClass().getResourceAsStream("test.json"), this.reposFile);
		assertTrue(len > 0);
		assertTrue(Files.exists(reposFile));
		this.pusherService = new PusherService(appHome.toString());
		this.pusherService.reloadRepositories();
		assertEquals(2, this.pusherService.getRepositories().size());
	}

	@Test
	public void testCreateRepository() throws Exception {
		final Repository r1 = new Repository();
		r1.autoCommit = true;
		r1.autoPush = false;
		r1.name = "/home/johndoe/myrepos";

		assertEquals(0, this.pusherService.getRepositories().size());
		this.pusherService.createRepository(r1);
		assertEquals(1, this.pusherService.getRepositories().size());
	}

	@Test
	public void testSaveRepositories() throws Exception {
		final Repository r1 = new Repository();
		r1.autoCommit = true;
		r1.autoPush = false;
		r1.name = "/home/johndoe/myrepos";

		final Repository r2 = new Repository();
		r2.autoCommit = true;
		r2.autoPush = true;
		r2.name = "/usr/local/another_repos";

		final List<Repository> reposList = Lists.newArrayList(r1, r2);
		assertFalse(Files.exists(reposFile));
		this.pusherService.saveRepositories();
		assertTrue(Files.exists(reposFile));
	}

	@Test
	public void testSaveRepositoriesReposFileExists() throws Exception {
		Files.createFile(this.reposFile);
		assertTrue(Files.exists(reposFile));
		assertTrue(Files.size(this.reposFile) == 0L);
		this.pusherService.saveRepositories();
		assertTrue(Files.exists(reposFile));
		assertFalse(Files.size(this.reposFile) == 0L);
	}
}

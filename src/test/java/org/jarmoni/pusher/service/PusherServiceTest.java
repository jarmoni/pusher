package org.jarmoni.pusher.service;

import static org.jarmoni.pusher.service.PusherService.REPOS_FILE_NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;

import org.jarmoni.resource.Repository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

public class PusherServiceTest {

	// CHECKSTYLE:OFF
	@Rule
	public TemporaryFolder tf = new TemporaryFolder();
	@Rule
	public ExpectedException ee = ExpectedException.none();
	// CHECKSTYLE:ON

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
		assertEquals(0, this.pusherService.getRepositories().size());
		this.pusherService.createRepository(createRepository());
		assertEquals(1, this.pusherService.getRepositories().size());
	}

	@Test
	public void testUpdateRepository() throws Exception {
		this.pusherService.createRepository(createRepository());
		Repository repos = this.pusherService.getRepository("myrepos");
		assertNotSame("/one/two/three", repos.getPath());
		repos = Repository.builder().name(repos.getName()).path("/one/two/three").build();
		System.out.println(repos);
		this.pusherService.updateRepository(repos);
		assertEquals("/one/two/three", this.pusherService.getRepository("myrepos").getPath());
	}

	@Test
	public void testDeleteRepository() throws Exception {
		this.pusherService.createRepository(createRepository());
		assertNotNull(this.pusherService.getRepository("myrepos"));
		this.pusherService.deleteRepository("myrepos");
		this.ee.expect(RuntimeException.class);
		this.ee.expectMessage("Repository does not exist");
		assertNull(this.pusherService.getRepository("myrepos"));
	}

	@Test
	public void testSaveRepositories() throws Exception {
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

	public static Repository createRepository() {
		return Repository.builder().autoCommit(true).autoPush(false).name("myrepos").path("/home/johndoe/myrepos").build();
	}
}

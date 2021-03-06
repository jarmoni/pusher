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
import java.nio.file.Paths;

import org.easymock.EasyMock;
import org.eclipse.jgit.lib.Repository;
import org.jarmoni.pusher.git.GitExecutor;
import org.jarmoni.pusher.resource.RepositoryResource;
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
	private final GitExecutor gitExecutor = EasyMock.createMock(GitExecutor.class);
	private final Repository repository = EasyMock.createMock(Repository.class);

	@Before
	public void setUp() throws Exception {
		this.appHome = this.tf.newFolder().toPath();
		assertTrue(Files.isDirectory(this.appHome));
		this.reposFile = appHome.resolve(REPOS_FILE_NAME);
		assertFalse(Files.exists(this.reposFile));
		this.pusherService = new PusherService(this.appHome.toString(), this.gitExecutor);
	}

	@Test
	public void testConstructorCreateMissingFolder() throws Exception {
		Files.deleteIfExists(this.appHome);
		assertFalse(Files.exists(this.appHome));
		this.pusherService = new PusherService(this.appHome.toString(), this.gitExecutor);
		assertTrue(Files.exists(this.appHome));
	}

	@Test
	public void testReloadRepositoriesReposFileNotExisting() throws Exception {
		this.pusherService.reloadRepositories();
		assertEquals(0, this.pusherService.getRepositories().size());
	}

	@Test
	public void testReloadRepositoriesReposFileExists() throws Exception {
		assertFalse(Files.exists(reposFile));
		EasyMock.expect(this.gitExecutor.openRepository(EasyMock.anyObject(Path.class))).andReturn(this.repository).times(3);
		EasyMock.replay(this.gitExecutor, this.repository);
		final long len = Files.copy(this.getClass().getResourceAsStream("test.json"), this.reposFile);
		assertTrue(len > 0);
		assertTrue(Files.exists(reposFile));
		this.pusherService = new PusherService(appHome.toString(), this.gitExecutor);
		this.pusherService.reloadRepositories();
		assertEquals(3, this.pusherService.getRepositories().size());
		EasyMock.verify(this.gitExecutor, this.repository);
	}

	@Test
	public void testCreateRepository() throws Exception {
		assertEquals(0, this.pusherService.getRepositories().size());
		final RepositoryResource repositoryResource = createRepositoryResource();
		EasyMock.expect(this.gitExecutor.openAndCreateRepository(Paths.get(repositoryResource.getPath()))).andReturn(
				this.repository);
		EasyMock.replay(this.gitExecutor, this.repository);
		this.pusherService.createRepository(repositoryResource);
		assertEquals(1, this.pusherService.getRepositories().size());
		EasyMock.verify(this.gitExecutor, this.repository);
	}

	@Test
	public void testUpdateRepository() throws Exception {
		final RepositoryResource repositoryResourceOrig = createRepositoryResource();
		final RepositoryResource repositoryResourceNew = RepositoryResource.builder().name(repositoryResourceOrig.getName())
				.path("/one/two/three").build();

		EasyMock.expect(this.gitExecutor.openAndCreateRepository(Paths.get(repositoryResourceOrig.getPath()))).andReturn(
				this.repository);
		EasyMock.expect(this.gitExecutor.openAndCreateRepository(Paths.get(repositoryResourceNew.getPath()))).andReturn(
				this.repository);
		EasyMock.replay(this.gitExecutor, this.repository);

		this.pusherService.createRepository(repositoryResourceOrig);
		final RepositoryResource repositoryResourceTmp = this.pusherService.getRepository("myrepos");
		assertEquals(repositoryResourceOrig.getPath(), repositoryResourceTmp.getPath());
		assertNotSame(repositoryResourceNew.getPath(), repositoryResourceTmp.getPath());

		this.pusherService.updateRepository(repositoryResourceNew);
		assertEquals("/one/two/three", this.pusherService.getRepository("myrepos").getPath());
		EasyMock.verify(this.gitExecutor, this.repository);
	}

	@Test
	public void testDeleteRepository() throws Exception {
		final RepositoryResource repositoryResource = createRepositoryResource();
		EasyMock.expect(this.gitExecutor.openAndCreateRepository(Paths.get(repositoryResource.getPath()))).andReturn(
				this.repository);
		EasyMock.replay(this.gitExecutor, this.repository);
		this.pusherService.createRepository(repositoryResource);
		assertNotNull(this.pusherService.getRepository("myrepos"));
		this.pusherService.deleteRepository("myrepos");
		assertNull(this.pusherService.getRepository("myrepos"));
		EasyMock.verify(this.gitExecutor, this.repository);
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

	public static RepositoryResource createRepositoryResource() {
		return RepositoryResource.builder().autoCommit(true).autoSync(false).name("myrepos").path("/home/johndoe/myrepos")
				.build();
	}
}

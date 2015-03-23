package org.jarmoni.pusher.service;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import org.jarmoni.pusher.git.GitExecutor;
import org.jarmoni.pusher.repository.RepositoryInstance;
import org.jarmoni.pusher.resource.RepositoryResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.Maps;

@ManagedResource(objectName = "org.jarmoni.pusher.service:name=PusherService")
public class PusherService implements IPusherService {

	public static final String REPOS_FILE_NAME = "repos.json";

	private static final Logger LOG = LoggerFactory.getLogger(PusherService.class);

	private Path appHome;
	private final Path reposFile;

	private final Map<String, RepositoryInstance> repositoryInstances = Maps.newHashMap();

	private final GitExecutor gitExecutor;

	private Timer timer;

	public PusherService(final String appHome, final GitExecutor gitExecutor) {
		this.gitExecutor = Preconditions.checkNotNull(gitExecutor);
		final Path path = Paths.get(Preconditions.checkNotNull(appHome));
		if (Files.isDirectory(path)) {
			this.appHome = path;
		}
		else {
			try {
				this.appHome = Files.createDirectory(path);
			}
			catch (final Throwable t) {
				Throwables.propagate(t);
			}
		}
		this.reposFile = this.appHome.resolve(REPOS_FILE_NAME);
		LOG.info("Initialized with appHome={}, reposFile={}", this.appHome, this.reposFile);
	}

	@Override
	public void start() {
		LOG.info("Trying to start PusherService....");
		this.reloadRepositories();
		this.startInstances();
		this.timer = new Timer("GitTimer");
		this.timer.scheduleAtFixedRate(new GitTimerTask(), 0L, 60 * 1000L);
		LOG.info("PusherService started");
	}

	@Override
	public void stop() {
		LOG.info("Trying to stop PusherService....");
		this.unregisterRepositories();
		this.timer.cancel();
		this.timer = null;
		LOG.info("PusherService stopped");
	}

	void reloadRepositories() {
		this.unregisterRepositories();
		if (Files.isRegularFile(this.reposFile)) {
			try (final InputStream is = Files.newInputStream(this.reposFile)) {
				final ObjectMapper mapper = new ObjectMapper();
				final List<RepositoryResource> reposList = mapper.readValue(is, new TypeReference<List<RepositoryResource>>(){});
				reposList.forEach(res ->
				this.repositoryInstances.put(res.getName(), new RepositoryInstance(this.gitExecutor.openRepository(Paths.get(res.getPath(), GitExecutor.GIT_DIR_NAME)), res, gitExecutor)));
			}
			catch (final Throwable t) {
				Throwables.propagate(t);
			}
		}
	}

	private void startInstances() {
		this.repositoryInstances.entrySet().forEach(entry -> entry.getValue().start());
	}

	void unregisterRepositories() {
		LOG.info("Trying to unregister Repositories...");
		this.repositoryInstances.entrySet().forEach(entry -> entry.getValue().stop());
		this.repositoryInstances.clear();
		LOG.info("Unregistered Repositories");
	}

	void saveRepositories() {
		try (OutputStream os = Files.newOutputStream(this.reposFile)) {
			final ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(os, this.getRepositories());
		}
		catch (final Throwable t) {
			Throwables.propagate(t);
		}
	}

	@ManagedAttribute
	public String getAppHome() {
		return this.appHome.toString();
	}

	@ManagedAttribute
	public String getReposFile() {
		return this.reposFile.toString();
	}

	@Override
	public List<RepositoryResource> getRepositories() {
		return this.repositoryInstances.entrySet().stream().map(entry -> entry.getValue().getRepositoryResource()).collect(Collectors.toList());
	}

	@Override
	public RepositoryResource getRepository(final String name) {
		final RepositoryInstance repositoryInstance = this.repositoryInstances.get(name);
		return repositoryInstance != null ? repositoryInstance.getRepositoryResource() : null;
	}

	@Override
	public RepositoryResource createRepository(final RepositoryResource repositoryResource) {
		LOG.info("Trying to create RepositoryInstance for RepositoryResource={}...", repositoryResource);
		this.repositoryInstances.put(repositoryResource.getName(),
				new RepositoryInstance(this.gitExecutor.openAndCreateRepository(Paths.get(repositoryResource.getPath())),
						repositoryResource, this.gitExecutor));
		LOG.info("Created RepositoryInstance for RepositoryResource={}", repositoryResource);
		return repositoryResource;
	}

	@Override
	public void deleteRepository(final String name) {
		final RepositoryInstance repositoryInstance = this.repositoryInstances.get(name);
		if (repositoryInstance != null) {
			LOG.info("Trying to delete RepositoryInstance={}...", name);
			repositoryInstance.stop();
			this.repositoryInstances.remove(name);
			this.saveRepositories();
			LOG.info("Deleted RepositoryInstance={}", name);
		}
		else {
			LOG.info("RepositoryInstance does not exist={}", name);
		}
	}

	@Override
	public RepositoryResource updateRepository(final RepositoryResource repository) {
		// What should we allow in this method in future??? Just renaming? Or
		// moving working copy?
		this.deleteRepository(repository.getName());
		return this.createRepository(repository);
	}

	@Override
	public void triggerRepository(final String name) {
		final RepositoryInstance repositoryInstance = this.repositoryInstances.get(name);
		this.gitExecutor.commitChanges(repositoryInstance.getRepository(), repositoryInstance.getRepositoryResource());
	}

	public class GitTimerTask extends TimerTask {

		@Override
		public void run() {
			repositoryInstances.entrySet().forEach(entry -> gitExecutor.commitChanges(entry.getValue().getRepository(), entry.getValue().getRepositoryResource()));
		}
	}

}

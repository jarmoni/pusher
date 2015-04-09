package org.jarmoni.pusher.controller;

import java.util.List;

import org.jarmoni.pusher.resource.RepositoryResource;
import org.jarmoni.pusher.service.IPusherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RepositoriesController {

	public static final String PATH_REPOSITORIES = Paths.PATH_API_ROOT + "/repositories";
	public static final String PATH_REPOSITORIES_LIST = PATH_REPOSITORIES + "/list";
	public static final String PATH_REPOSITORIES_CREATE = PATH_REPOSITORIES + "/create";

	@Autowired
	private IPusherService pusherService;

	@RequestMapping(value = PATH_REPOSITORIES_LIST, method = RequestMethod.GET)
	@ResponseBody
	public List<RepositoryResource> listRepositories() {
		return this.pusherService.getRepositories();
	}

	@RequestMapping(value = PATH_REPOSITORIES_CREATE, method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.CREATED)
	public RepositoryResource createRepository(@RequestBody final RepositoryResource repository) {
		return this.pusherService.createRepository(repository);
	}
}

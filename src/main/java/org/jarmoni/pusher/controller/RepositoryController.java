package org.jarmoni.pusher.controller;

import org.jarmoni.pusher.resource.RepositoryResource;
import org.jarmoni.pusher.service.IPusherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RepositoryController {

	public static final String PATH_REPOSITORY = Paths.PATH_API_ROOT + "/repository";
	public static final String PATH_REPOSITORY_GET = PATH_REPOSITORY + "/get/{name}";
	public static final String PATH_REPOSITORY_DELETE = PATH_REPOSITORY + "/delete/{name}";
	public static final String PATH_REPOSITORY_UPDATE = PATH_REPOSITORY + "/update";
	public static final String PATH_REPOSITORY_TRIGGER = PATH_REPOSITORY + "/trigger";

	@Autowired
	private IPusherService pusherService;

	@RequestMapping(value = PATH_REPOSITORY_GET, method = RequestMethod.GET)
	@ResponseBody
	public RepositoryResource getRepository(@PathVariable final String name) {

		return this.pusherService.getRepository(name);
	}

	@RequestMapping(value = PATH_REPOSITORY_DELETE, method = RequestMethod.DELETE)
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void deleteRepository(@PathVariable final String name) {
		this.pusherService.deleteRepository(name);
	}

	@RequestMapping(value = PATH_REPOSITORY_UPDATE, method = RequestMethod.PUT)
	@ResponseBody
	public RepositoryResource updateRepository(@RequestBody final RepositoryResource repository) {
		return this.pusherService.updateRepository(repository);
	}

	@RequestMapping(value = PATH_REPOSITORY_TRIGGER, method = RequestMethod.POST, params = "name")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void triggerRepository(@RequestParam final String name) {
		this.pusherService.triggerRepository(name);
	}
}

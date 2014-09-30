package org.jarmoni.pusher.controller;

import org.jarmoni.pusher.controller.util.RepositoryLinkCreator;
import org.jarmoni.pusher.service.IPusherService;
import org.jarmoni.resource.Repository;
import org.jarmoni.restxe.common.HttpVerb;
import org.jarmoni.restxe.common.Item;
import org.jarmoni.restxe.common.LinkFactory;
import org.jarmoni.restxe.common.LinkType;
import org.jarmoni.restxe.common.Representation;
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
	@Autowired
	private LinkFactory linkFactory;
	@Autowired
	private RepositoryLinkCreator repositoryLinkCreator;

	@RequestMapping(value = PATH_REPOSITORY_GET, method = RequestMethod.GET)
	@ResponseBody
	public Representation<Repository> getRepository(@PathVariable final String name) {

		return Representation
				.<Repository> builder()
				.item(Item.<Repository> builder().data(this.pusherService.getRepository(name))
						.links(this.repositoryLinkCreator.createLinks(name)).build())
				.link(this.linkFactory.createLink(LinkType.SELF_REF,
						this.repositoryLinkCreator.replaceNameVariable(PATH_REPOSITORY_GET, name), HttpVerb.GET)).build();
	}

	@RequestMapping(value = PATH_REPOSITORY_DELETE, method = RequestMethod.DELETE)
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void deleteRepository(@PathVariable final String name) {
		this.pusherService.deleteRepository(name);
	}

	@RequestMapping(value = PATH_REPOSITORY_UPDATE, method = RequestMethod.PUT)
	@ResponseBody
	public Representation<Repository> updateRepository(@RequestBody final Repository repository) {
		final Repository updatedRepos = this.pusherService.updateRepository(repository);
		return Representation
				.<Repository> builder()
				.item(Item.<Repository> builder().data(updatedRepos)
						.links(this.repositoryLinkCreator.createLinks(updatedRepos.getName())).build())
				.link(this.linkFactory.createLink(LinkType.SELF_REF,
						this.repositoryLinkCreator.replaceNameVariable(PATH_REPOSITORY_GET, updatedRepos.getName()), HttpVerb.GET))
				.build();
	}

	@RequestMapping(value = PATH_REPOSITORY_TRIGGER, method = RequestMethod.POST, params = "name")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void triggerRepository(@RequestParam final String name) {
		this.pusherService.triggerRepository(name);
	}
}

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RepositoryController {

	public static final String PATH_REPOSITORY = Paths.PATH_API_ROOT + "/repository";
	public static final String PATH_REPOSITORY_GET = PATH_REPOSITORY + "/get/{name}";
	public static final String PATH_REPOSITORY_DELETE = PATH_REPOSITORY + "/delete/{name}";
	public static final String PATH_REPOSITORY_UPDATE = PATH_REPOSITORY + "/update";
	public static final String PATH_REPOSITORY_TRIGGER = PATH_REPOSITORY + "/trigger/{name}";

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
				.link(this.linkFactory.createLink(LinkType.SELF_REF, PATH_REPOSITORY_GET, HttpVerb.GET)).build();
	}

	@RequestMapping(value = PATH_REPOSITORY_DELETE, method = RequestMethod.DELETE)
	public void deleteRepository(@PathVariable final String name) {
		this.pusherService.deleteRepository(name);
	}

	@RequestMapping(value = PATH_REPOSITORY_UPDATE, method = RequestMethod.PUT)
	@ResponseBody
	public Representation<Repository> updateRepository(final Repository repository) {
		return Representation.<Repository> builder()
				.item(Item.<Repository> builder().data(this.pusherService.updateRepository(repository)).build())
				.links(this.repositoryLinkCreator.createLinks(repository.name)).build();
	}

	@RequestMapping(value = PATH_REPOSITORY_TRIGGER, method = RequestMethod.GET)
	public void triggerRepository(@PathVariable final String name) {
		this.pusherService.deleteRepository(name);
	}
}

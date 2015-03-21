package org.jarmoni.pusher.controller;

import java.util.stream.Collectors;

import org.jarmoni.pusher.controller.util.RepositoryLinkCreator;
import org.jarmoni.pusher.resource.RepositoryResource;
import org.jarmoni.pusher.service.IPusherService;
import org.jarmoni.restxe.common.HttpVerb;
import org.jarmoni.restxe.common.Item;
import org.jarmoni.restxe.common.LinkFactory;
import org.jarmoni.restxe.common.LinkType;
import org.jarmoni.restxe.common.Representation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;

@RestController
public class RepositoriesController {

	public static final String PATH_REPOSITORIES = Paths.PATH_API_ROOT + "/repositories";
	public static final String PATH_REPOSITORIES_LIST = PATH_REPOSITORIES + "/list";
	public static final String PATH_REPOSITORIES_CREATE = PATH_REPOSITORIES + "/create";

	@Autowired
	private IPusherService pusherService;
	@Autowired
	private LinkFactory linkFactory;
	@Autowired
	private RepositoryLinkCreator repositoryLinkCreator;

	@RequestMapping(value = PATH_REPOSITORIES_LIST, method = RequestMethod.GET)
	@ResponseBody
	public Representation<RepositoryResource> listRepositories() {
		return Representation
				.<RepositoryResource> builder()
				.items(this.pusherService
						.getRepositories()
						.stream()
						.map(rep -> Item
								.<RepositoryResource> builder()
								.data(rep)
								.links(this.repositoryLinkCreator
										.createLinks(rep.getName())).build())
										.collect(Collectors.toList()))
										.links(Lists.newArrayList(this.linkFactory
												.createLink(LinkType.SELF_REF, PATH_REPOSITORIES_LIST,
														HttpVerb.GET), this.linkFactory.createLink(
																LinkType.CREATE, PATH_REPOSITORIES_CREATE,
																HttpVerb.POST))).build();
	}

	@RequestMapping(value = PATH_REPOSITORIES_CREATE, method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.CREATED)
	public Representation<RepositoryResource> createRepository(@RequestBody final RepositoryResource repository) {
		this.pusherService.createRepository(repository);
		return Representation
				.<RepositoryResource> builder()
				.item(Item.<RepositoryResource> builder().data(repository)
						.links(this.repositoryLinkCreator.createLinks(repository.getName())).build())
				.link(this.linkFactory.createLink(LinkType.SELF_REF, RepositoryController.PATH_REPOSITORY_GET, HttpVerb.GET))
				.build();
	}
}

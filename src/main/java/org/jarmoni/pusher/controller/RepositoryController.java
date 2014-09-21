package org.jarmoni.pusher.controller;

import java.util.List;

import org.jarmoni.pusher.service.IPusherService;
import org.jarmoni.resource.Repository;
import org.jarmoni.restxe.common.Item;
import org.jarmoni.restxe.common.Link;
import org.jarmoni.restxe.common.LinkFactory;
import org.jarmoni.restxe.common.Representation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;

@RestController
public class RepositoryController {

	public static final String PATH_REPOSITORY = Paths.PATH_API_ROOT + "/repository";
	public static final String PATH_REPOSITORIES_GET = PATH_REPOSITORY + "/get";
	public static final String PATH_REPOSITORIES_CREATE = PATH_REPOSITORY + "/create";
	public static final String PATH_REPOSITORIES_DELETE = PATH_REPOSITORY + "/delete";
	public static final String PATH_REPOSITORIES_UPDATE = PATH_REPOSITORY + "/update";

	@Autowired
	private IPusherService pusherService;
	@Autowired
	private LinkFactory linkFactory;

	@RequestMapping(value = PATH_REPOSITORIES_GET + "/{name}", method = RequestMethod.GET)
	@ResponseBody
	public Representation<Repository> getRepository(@PathVariable final String name) {
		return Representation.<Repository> builder()
				.item(Item.<Repository> builder().data(this.pusherService.getRepository(name)).build())
				.links(this.createRepositoryLinks(name)).build();
	}

	@RequestMapping(value = PATH_REPOSITORIES_CREATE, method = RequestMethod.POST)
	@ResponseBody
	public Representation<Repository> createRepository(final Repository repository) {
		this.pusherService.createRepository(repository);
		return Representation.<Repository> builder().item(Item.<Repository> builder().data(repository).build())
				.links(this.createRepositoryLinks(repository.name)).build();
	}

	@RequestMapping(value = PATH_REPOSITORIES_DELETE + "/{name}", method = RequestMethod.DELETE)
	public void deleteRepository(@PathVariable final String name) {
		this.pusherService.deleteRepository(name);
	}

	@RequestMapping(value = PATH_REPOSITORIES_UPDATE, method = RequestMethod.PUT)
	@ResponseBody
	public Representation<Repository> updateRepository(final Repository repository) {
		return Representation.<Repository> builder()
				.item(Item.<Repository> builder().data(this.pusherService.updateRepository(repository)).build())
				.links(this.createRepositoryLinks(repository.name)).build();
	}

	List<Link> createRepositoryLinks(final String name) {
		return Lists.newArrayList(this.linkFactory.createLink(LinkFactory.SELF_REF, PATH_REPOSITORIES_GET + "/" + name),
				this.linkFactory.createLink("delete", PATH_REPOSITORIES_DELETE + "/" + name),
				this.linkFactory.createLink("update", PATH_REPOSITORIES_UPDATE));
	}

}

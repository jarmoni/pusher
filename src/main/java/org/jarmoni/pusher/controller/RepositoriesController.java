package org.jarmoni.pusher.controller;

import java.util.stream.Collectors;

import org.jarmoni.pusher.service.IPusherService;
import org.jarmoni.resource.Repository;
import org.jarmoni.restxe.common.Item;
import org.jarmoni.restxe.common.LinkFactory;
import org.jarmoni.restxe.common.Representation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RepositoriesController {

	public static final String PATH_REPOSITORIES = Paths.PATH_API_ROOT + "/repositories";
	public static final String PATH_REPOSITORIES_LIST = PATH_REPOSITORIES + "/list";

	@Autowired
	private IPusherService pusherService;
	@Autowired
	private LinkFactory linkFactory;

	@RequestMapping(value = PATH_REPOSITORIES_LIST, method = RequestMethod.GET)
	@ResponseBody
	public Representation<Repository> listRepositories() {
		return Representation
				.<Repository> builder()
				.items(this.pusherService
						.getRepositories()
						.stream()
						.map(rep -> Item.<Repository> builder().data(rep)
								.build()).collect(Collectors.toList()))
								.link(this.linkFactory.createLink(LinkFactory.SELF_REF,
										PATH_REPOSITORIES_LIST)).build();
	}
}

package org.jarmoni.pusher.controller;

import java.util.stream.Collectors;

import org.jarmoni.pusher.service.IPusherService;
import org.jarmoni.resource.Repository;
import org.jarmoni.restxe.common.Item;
import org.jarmoni.restxe.common.Representation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Paths.API_ROOT_PATH + "/repositories")
public class RepositoriesController {

	@Autowired
	private IPusherService pusherService;

	@RequestMapping("")
	@ResponseBody
	public Representation<Repository> getRepositories() {
		return Representation
				.<Repository> builder()
				.items(this.pusherService
						.getRepositories()
						.stream()
						.map(rep -> Item.<Repository> builder().data(rep)
								.build()).collect(Collectors.toList())).build();
	}
}

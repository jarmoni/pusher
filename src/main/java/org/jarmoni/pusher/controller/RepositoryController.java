package org.jarmoni.pusher.controller;

import org.jarmoni.pusher.service.IPusherService;
import org.jarmoni.resource.Repository;
import org.jarmoni.restxe.common.Item;
import org.jarmoni.restxe.common.Representation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Paths.API_ROOT_PATH + "/repository")
public class RepositoryController {

	@Autowired
	private IPusherService pusherService;

	@RequestMapping("{name}")
	@ResponseBody
	public Representation<Repository> getRepository(@PathVariable final String name) {
		return Representation.<Repository> builder()
				.item(Item.<Repository> builder().data(this.pusherService.getRepository(name)).build()).build();
	}

}

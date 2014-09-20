package org.jarmoni.pusher.controller;

import org.jarmoni.pusher.service.PusherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RepositoriesController {

	@Autowired
	private PusherService pusherService;

	@RequestMapping("/")
	public String getRepositories() {
		return "hallo" + this.pusherService.getAppHome();
	}

}

package org.jarmoni.pusher.controller.util;

import static org.jarmoni.pusher.controller.RepositoryController.PATH_REPOSITORY_DELETE;
import static org.jarmoni.pusher.controller.RepositoryController.PATH_REPOSITORY_GET;
import static org.jarmoni.pusher.controller.RepositoryController.PATH_REPOSITORY_UPDATE;

import java.util.List;
import java.util.Objects;

import org.jarmoni.restxe.common.Link;
import org.jarmoni.restxe.common.LinkFactory;
import org.jarmoni.restxe.common.LinkType;

import com.google.common.collect.Lists;

public class RepositoryLinkCreator {

	private final LinkFactory linkFactory;

	public RepositoryLinkCreator(final LinkFactory linkFactory) {
		this.linkFactory = Objects.requireNonNull(linkFactory);
	}

	public List<Link> createLinks(final String name) {
		return Lists.newArrayList(this.linkFactory.createLink(LinkType.SELF_REF, PATH_REPOSITORY_GET.replace("{name}", name)),
				this.linkFactory.createLink(LinkType.DELETE, PATH_REPOSITORY_DELETE.replace("{name}", name)),
				this.linkFactory.createLink(LinkType.UPDATE, PATH_REPOSITORY_UPDATE));
	}

}

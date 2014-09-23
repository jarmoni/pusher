package org.jarmoni.resource;

import com.google.common.base.MoreObjects;

public class Repository {

	public String name;
	public String path;
	public boolean autoCommit;
	public boolean autoPush;
	public boolean autoPull;

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return MoreObjects.toStringHelper(Repository.class)
				.add("name", this.name).add("path", this.path)
				.add("autoCommit", this.autoCommit)
				.add("autoPush", this.autoPush).add("autoPull", this.autoPull)
				.toString();
	}
}

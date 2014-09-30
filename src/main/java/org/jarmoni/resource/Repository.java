package org.jarmoni.resource;

public class Repository {

	/**
	 * Required. Unique name
	 */
	private String name;
	/**
	 * Required. Path to local (non-bare) repository
	 */
	private String path;
	/**
	 * Optional. If 'true' pusher will check the repository for changes
	 * frequently and and commits (pushes) changes automatically. Alternatively
	 * commits can be triggered manually.
	 */
	private boolean autoCommit = false;

	/**
	 * Optional. Indicates if pusher should pull from origin/master frequently
	 */
	private boolean autoPull = false;
	/**
	 * Optional. Indicates if pusher should push all changes to origin/master
	 */
	private boolean autoPush = false;

	public String getName() {
		return name;
	}

	public String getPath() {
		return path;
	}

	public boolean isAutoCommit() {
		return autoCommit;
	}

	public boolean isAutoPull() {
		return autoPull;
	}

	public boolean isAutoPush() {
		return autoPush;
	}

	@Override
	public String toString() {
		return "Repository [name=" + name + ", path=" + path + ", autoCommit=" + autoCommit + ", autoPush=" + autoPush
				+ ", autoPull=" + autoPull + "]";
	}

	public static RepositoryBuilder builder() {
		return new RepositoryBuilder();
	}

	public static final class RepositoryBuilder {

		private final Repository repository;

		private RepositoryBuilder() {
			this.repository = new Repository();
		}

		public Repository build() {
			return this.repository;
		}

		public RepositoryBuilder name(final String name) {
			this.repository.name = name;
			return this;
		}

		public RepositoryBuilder path(String path) {
			this.repository.path = path;
			return this;
		}

		public RepositoryBuilder autoCommit(final boolean autoCommit) {
			this.repository.autoCommit = autoCommit;
			return this;
		}

		public RepositoryBuilder autoPull(final boolean autoPull) {
			this.repository.autoPull = autoPull;
			return this;
		}

		public RepositoryBuilder autoPush(final boolean autoPush) {
			this.repository.autoPush = autoPush;
			return this;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (autoCommit ? 1231 : 1237);
		result = prime * result + (autoPull ? 1231 : 1237);
		result = prime * result + (autoPush ? 1231 : 1237);
		result = prime * result + (name == null ? 0 : name.hashCode());
		result = prime * result + (path == null ? 0 : path.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Repository other = (Repository) obj;
		if (autoCommit != other.autoCommit) {
			return false;
		}
		if (autoPull != other.autoPull) {
			return false;
		}
		if (autoPush != other.autoPush) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		}
		else if (!name.equals(other.name)) {
			return false;
		}
		if (path == null) {
			if (other.path != null) {
				return false;
			}
		}
		else if (!path.equals(other.path)) {
			return false;
		}
		return true;
	}
}

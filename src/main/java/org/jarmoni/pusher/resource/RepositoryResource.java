package org.jarmoni.pusher.resource;

public class RepositoryResource {

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
	 * Optional. Indicates if pusher should pull from/push to origin/master
	 * frequently
	 */
	private boolean autoSync = false;

	private String userName;
	private String userEmail;
	private String commitMsg;

	public String getName() {
		return name;
	}

	public String getPath() {
		return path;
	}

	public boolean isAutoCommit() {
		return autoCommit;
	}

	public boolean isAutoSync() {
		return autoSync;
	}

	public String getUserName() {
		return userName;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public String getCommitMsg() {
		return commitMsg;
	}

	@Override
	public String toString() {
		return "RepositoryResource [name=" + name + ", path=" + path + ", autoCommit=" + autoCommit + ", autoSync=" + autoSync
				+ ", userName=" + userName + ", userEmail=" + userEmail + ", commitMsg=" + commitMsg + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (autoCommit ? 1231 : 1237);
		result = prime * result + (autoSync ? 1231 : 1237);
		result = prime * result + ((commitMsg == null) ? 0 : commitMsg.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((path == null) ? 0 : path.hashCode());
		result = prime * result + ((userEmail == null) ? 0 : userEmail.hashCode());
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
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
		final RepositoryResource other = (RepositoryResource) obj;
		if (autoCommit != other.autoCommit) {
			return false;
		}
		if (autoSync != other.autoSync) {
			return false;
		}
		if (commitMsg == null) {
			if (other.commitMsg != null) {
				return false;
			}
		}
		else if (!commitMsg.equals(other.commitMsg)) {
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
		if (userEmail == null) {
			if (other.userEmail != null) {
				return false;
			}
		}
		else if (!userEmail.equals(other.userEmail)) {
			return false;
		}
		if (userName == null) {
			if (other.userName != null) {
				return false;
			}
		}
		else if (!userName.equals(other.userName)) {
			return false;
		}
		return true;
	}

	public static RepositoryResourceBuilder builder() {
		return new RepositoryResourceBuilder();
	}

	public static final class RepositoryResourceBuilder {

		private final RepositoryResource repository;

		private RepositoryResourceBuilder() {
			this.repository = new RepositoryResource();
		}

		public RepositoryResource build() {
			return this.repository;
		}

		public RepositoryResourceBuilder name(final String name) {
			this.repository.name = name;
			return this;
		}

		public RepositoryResourceBuilder path(final String path) {
			this.repository.path = path;
			return this;
		}

		public RepositoryResourceBuilder autoCommit(final boolean autoCommit) {
			this.repository.autoCommit = autoCommit;
			return this;
		}

		public RepositoryResourceBuilder autoSync(final boolean autoSync) {
			this.repository.autoSync = autoSync;
			return this;
		}

		public RepositoryResourceBuilder userName(final String userName) {
			this.repository.userName = userName;
			return this;
		}

		public RepositoryResourceBuilder userEmail(final String userEmail) {
			this.repository.userEmail = userEmail;
			return this;
		}

		public RepositoryResourceBuilder commitMsg(final String commitMsg) {
			this.repository.commitMsg = commitMsg;
			return this;
		}
	}
}

package org.jarmoni.resource;

public class Repository {

	public String name;
	public String path;
	public boolean autoCommit;
	public boolean autoPush;
	public boolean autoPull;

	@Override
	public String toString() {
		return "Repository [name=" + name + ", path=" + path + ", autoCommit=" + autoCommit + ", autoPush=" + autoPush
				+ ", autoPull=" + autoPull + "]";
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

package g8.library.api.model;

import g8.library.api.domain.SystemUser;

public class UserDataBuilder {
	private SystemUser systemUser;
	
	public UserDataBuilder(SystemUser systemUser) {
		this.systemUser = systemUser;
	}
	
	public UserDataBuilder withSystemUser(SystemUser systemUser) {
		this.systemUser = systemUser;
		return this;
	}
	public SystemUser systemUser() {
		return systemUser;
	}
}

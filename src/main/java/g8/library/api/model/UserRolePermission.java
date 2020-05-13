package g8.library.api.model;

import g8.library.api.domain.PermissionType;
import g8.library.api.domain.SystemUser;

public class UserRolePermission {
	private final SystemUser systemUser;
	private final boolean isLibMemberUpdatePermited;
	private final boolean isLibMemberAddPermited;
	private final boolean isLibMemberDeletePermited;
	private final boolean isBookCheckoutPermited;
	private final boolean isBookAddPermited;

	public UserRolePermission(SystemUser user) {
		this.systemUser = user;
		this.isLibMemberUpdatePermited = user.hasPermissionGranted(PermissionType.UPDATE_MEMBER);
		this.isLibMemberAddPermited = user.hasPermissionGranted(PermissionType.ADD_MEMBER);
		this.isLibMemberDeletePermited = user.hasPermissionGranted(PermissionType.DELETE_MEMBER);
		this.isBookCheckoutPermited = user.hasPermissionGranted(PermissionType.CHECKOUT_BOOK);
		this.isBookAddPermited = user.hasPermissionGranted(PermissionType.ADD_BOOK);
	}

	public SystemUser getSystemUser() {
		return systemUser;
	}

	public boolean isLibMemberUpdatePermited() {
		return isLibMemberUpdatePermited;
	}

	public boolean isLibMemberAddPermited() {
		return isLibMemberAddPermited;
	}

	public boolean isLibMemberDeletePermited() {
		return isLibMemberDeletePermited;
	}

	public boolean isBookCheckoutPermited() {
		return isBookCheckoutPermited;
	}

	public boolean isBookAddPermited() {
		return isBookAddPermited;
	}

	@Override
	public String toString() {
		return "UserRolePermission [systemUser=" + systemUser + ", isLibMemberUpdatePermited="
				+ isLibMemberUpdatePermited + ", isLibMemberAddPermited=" + isLibMemberAddPermited
				+ ", isLibMemberDeletePermited=" + isLibMemberDeletePermited + ", isBookCheckoutPermited="
				+ isBookCheckoutPermited + ", isBookAddPermited=" + isBookAddPermited + "]";
	}
}

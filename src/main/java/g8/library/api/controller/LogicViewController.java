package g8.library.api.controller;

import g8.library.api.domain.PermissionType;
import g8.library.api.domain.SystemUser;
import g8.library.api.service.LogicViewInterface;

public class LogicViewController {
	private SystemUser systemUser;
	public LogicViewController(SystemUser systemUser) {
		this.systemUser = systemUser;
	}
	
	private LogicViewInterface logicView = (user, access) -> {
		return user.hasPermissionGranted(access);
	};

	public boolean isPermissionGranted(PermissionType accessPermission) {
		return logicView.isPermissionGranted(systemUser, accessPermission);
	}
	
	public boolean isLibMemberUpdatePermited() {
		return isPermissionGranted(PermissionType.UPDATE_MEMBER);
	}
	
	public boolean isLibMemberAddPermited() {
		return isPermissionGranted(PermissionType.ADD_MEMBER);
	}
	
	public boolean isLibMemberDeletePermited() {
		return isPermissionGranted(PermissionType.DELETE_MEMBER);
	}
	
	public boolean isBookCheckoutPermited() {
		return isPermissionGranted(PermissionType.CHECKOUT_BOOK);
	}
	
	public boolean isBookAddPermited() {
		return isPermissionGranted(PermissionType.ADD_BOOK);
	}
}

package g8.library.api.service;

import g8.library.api.domain.PermissionType;
import g8.library.api.domain.SystemUser;

public interface LogicViewInterface {
	public boolean isPermissionGranted(SystemUser user, PermissionType access);
}

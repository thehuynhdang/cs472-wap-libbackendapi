package g8.library.api.domain;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(value = { "loginCredentials" })
public final class SystemUser extends User implements SystemUserInterface {
	private static final long serialVersionUID = 1L;
	private final LoginCredentials loginCredentials;
	private final Set<UserRole> userRoles;

	public SystemUser(LoginCredentials loginCredentials, String firstName, String lastName, String phoneNumber, Address address) {
		super(firstName, lastName, phoneNumber, address);
		this.loginCredentials = loginCredentials;
		this.userRoles = new HashSet<UserRole>();
	}

	public void addNewRole(UserRole role) {
		userRoles.add(role);
	}
	
	public LoginCredentials getLoginCredentials() {
		return loginCredentials;
	}

	public Set<UserRole> getUserRoles() {
		return userRoles;
	}
	
	public Set<PermissionType> getPermissionsGranted() {
		Set<PermissionType> permissions = new HashSet<PermissionType>();
		for(UserRole role : userRoles) {
			permissions.addAll(role.getAccessPermissions());
		}
		return permissions;
	}
	
	public boolean hasPermissionGranted(PermissionType access) {
		return getPermissionsGranted().contains(access);
	}
	
	public String getUserName() {
		return loginCredentials.getUserName();
	}

	@Override
	public SystemUser login(LoginCredentials loginCredentials) {
		if(this.loginCredentials.equals(loginCredentials)) return this;
		return null;
	}

	@Override
	public String toString() {
		return "SystemUser [userRoles=" + userRoles + ", firstName=" + getFirstName() + ", lastName="
				+ getLastName() + ", phoneNumber=" + getPhoneNumber() + ", address=" + getAddress()
				+ ", fullName=" + getFullName() + "]";
	}
	
	
}

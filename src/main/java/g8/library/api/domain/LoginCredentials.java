package g8.library.api.domain;

import java.io.Serializable;
import java.util.Objects;

public class LoginCredentials implements Serializable {
	private static final long serialVersionUID = 1L;
	private final String userName;
	private final String password;
	
	public LoginCredentials(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}
	public String getUserName() {
		return userName;
	}
	public String getPassword() {
		return password;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		LoginCredentials other = (LoginCredentials) obj;
		return Objects.equals(userName, other.userName)
				&& Objects.equals(password, other.password);
		
	}
}

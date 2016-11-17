package org.judking.carkeeper.src.model;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

public class MyUserDetails extends org.springframework.security.core.userdetails.User{
	private UserModel userModel = null;
	
	public MyUserDetails(String username, String password, boolean enabled,
			boolean accountNonExpired, boolean credentialsNonExpired,
			boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities,
			UserModel userModel
			) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired,
				accountNonLocked, authorities);
		this.userModel = userModel;
	}

	public UserModel getUserModel() {
		return userModel;
	}

	public void setUserModel(UserModel userModel) {
		this.userModel = userModel;
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}

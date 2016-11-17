package org.judking.carkeeper.src.service;

import org.springframework.security.authentication.encoding.PasswordEncoder;

public class MyPasswordEncoder implements PasswordEncoder {

	@Override
	public String encodePassword(String rawPass, Object salt) {
		// TODO Auto-generated method stub
		return rawPass;
	}

	@Override
	public boolean isPasswordValid(String encPass, String rawPass, Object salt) {
		// TODO Auto-generated method stub
		return (MysqlPasswordEncoder.encode(rawPass).equals(encPass));
	}

}

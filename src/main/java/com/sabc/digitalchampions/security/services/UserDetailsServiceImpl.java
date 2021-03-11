package com.sabc.digitalchampions.security.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Value("${auth_host}")
	private String host;

	private Logger logger = LogManager.getLogger(UserDetailsServiceImpl.class);

	public UserDetailsServiceImpl() {

	}

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		OkHttpClient client = new OkHttpClient();
		MediaType mediaType = MediaType.parse("application/json");
//		RequestBody body = RequestBody.create(mediaType, )
		Request request = new Request.Builder()
				.url(host.concat("/prelogin/user/email/"+ email))
				.build();

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			Response response = client.newCall(request).execute();
			Result result = objectMapper.readValue(response.body().string(),Result.class);
			return UserDetailsImpl.build(result.data);
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
			return null;
		}

	}

	static class Result {
		private String message;
		private int status;
		private User data;

		public Result(){}

		public String getMessage() {
			return message;
		}

		public Result setMessage(String message) {
			this.message = message;
			return this;
		}

		public int getStatus() {
			return status;
		}

		public Result setStatus(int status) {
			this.status = status;
			return this;
		}

		public User getData() {
			return data;
		}

		public Result setData(User data) {
			this.data = data;
			return this;
		}
	}

	static class User {

		private long id;
		private String code;
		private String firstname;
		private String lastname;
		private String phone;
		private String email;
		private Date lastConnected;
		private Date deletedAt;
		private Date enabledAt;
		private String nickname;
		private boolean enabled;
		private boolean emailChecked;
		private boolean phoneChecked;
		private String password;
		private Date createdAt;
		private Date lastUpdatedAt;
		private String role;

		public User() {
		}

		public long getId() {
			return id;
		}

		public User setId(long id) {
			this.id = id;
			return this;
		}

		public String getCode() {
			return code;
		}

		public User setCode(String code) {
			this.code = code;
			return this;
		}

		public String getFirstname() {
			return firstname;
		}

		public User setFirstname(String firstname) {
			this.firstname = firstname;
			return this;
		}

		public String getLastname() {
			return lastname;
		}

		public User setLastname(String lastname) {
			this.lastname = lastname;
			return this;
		}

		public String getPhone() {
			return phone;
		}

		public User setPhone(String phone) {
			this.phone = phone;
			return this;
		}

		public String getEmail() {
			return email;
		}

		public User setEmail(String email) {
			this.email = email;
			return this;
		}

		public Date getLastConnected() {
			return lastConnected;
		}

		public User setLastConnected(Date lastConnected) {
			this.lastConnected = lastConnected;
			return this;
		}

		public Date getDeletedAt() {
			return deletedAt;
		}

		public User setDeletedAt(Date deletedAt) {
			this.deletedAt = deletedAt;
			return this;
		}

		public Date getEnabledAt() {
			return enabledAt;
		}

		public User setEnabledAt(Date enabledAt) {
			this.enabledAt = enabledAt;
			return this;
		}

		public String getNickname() {
			return nickname;
		}

		public User setNickname(String nickname) {
			this.nickname = nickname;
			return this;
		}

		public boolean isEnabled() {
			return enabled;
		}

		public User setEnabled(boolean enabled) {
			this.enabled = enabled;
			return this;
		}

		public boolean isEmailChecked() {
			return emailChecked;
		}

		public User setEmailChecked(boolean emailChecked) {
			this.emailChecked = emailChecked;
			return this;
		}

		public boolean isPhoneChecked() {
			return phoneChecked;
		}

		public User setPhoneChecked(boolean phoneChecked) {
			this.phoneChecked = phoneChecked;
			return this;
		}

		public String getPassword() {
			return password;
		}

		public User setPassword(String password) {
			this.password = password;
			return this;
		}

		public Date getCreatedAt() {
			return createdAt;
		}

		public User setCreatedAt(Date createdAt) {
			this.createdAt = createdAt;
			return this;
		}

		public Date getLastUpdatedAt() {
			return lastUpdatedAt;
		}

		public User setLastUpdatedAt(Date lastUpdatedAt) {
			this.lastUpdatedAt = lastUpdatedAt;
			return this;
		}

		public String getRole() {
			return role;
		}

		public User setRole(String role) {
			this.role = role;
			return this;
		}
	}
}

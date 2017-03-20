package com.account;

import java.util.Date;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

//TODO Spring data rest 프로젝트 확인.
public class AccountDTO {
	public static class Create{
		@NotBlank
		@Size(min = 10)
		private String username;
		
		@NotBlank
		@Size(min = 8)
		private String password;
		
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
	}
	public static class Respose{
		private Long id;
		private String username;
		private String fullName;
		private Date joined;
		private Date updated;
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getFullName() {
			return fullName;
		}
		public void setFullName(String fullName) {
			this.fullName = fullName;
		}
		public Date getJoined() {
			return joined;
		}
		public void setJoined(Date joined) {
			this.joined = joined;
		}
		public Date getUpdated() {
			return updated;
		}
		public void setUpdated(Date updated) {
			this.updated = updated;
		}
	}
	public static class Update{
		private String password;
		private String fullname;
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public String getFullname() {
			return fullname;
		}
		public void setFullname(String fullname) {
			this.fullname = fullname;
		}
	}
}

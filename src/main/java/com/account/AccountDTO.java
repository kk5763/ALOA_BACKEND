package com.account;

import java.util.Date;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

//TODO Spring data rest 프로젝트 확인.
public class AccountDTO {
	public static class Create{
		@NotBlank(message="email을 입력하십시오")
		@Size(min = 10, max=35)
		@Email(message="올바른 email 형식이 아닙니다")
		private String username;
		
		@NotBlank(message="비밀번호를 입력하십시오")
		@Size(min = 8, max=20, message="8~20자 이내의 패스워드를 사용해 주세요.")
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

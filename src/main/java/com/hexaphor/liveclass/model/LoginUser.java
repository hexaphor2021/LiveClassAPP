package com.hexaphor.liveclass.model;

import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name="login_user")
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class LoginUser {
	@Id
	@GeneratedValue(generator = "LU")
	@GenericGenerator(name="LU",strategy = "com.hexaphor.liveclass.generator.LoginIdGenerator")
	@Column(name="login_id")
	private String loginId;
	@Column(name="username",unique = true)
	private String username;
	@Column(name="password")
	private String password;
	@CollectionTable(name="user_role_tab",joinColumns = @JoinColumn(name="login_id"))
	@ElementCollection(fetch = FetchType.EAGER)
	@Column(name="login_role")
	private List<String> roles;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sc_id_col")
	private School school;
	@Override
	public String toString() {
		return "LoginUser [loginId=" + loginId + ", username=" + username + ", password=" + password + ", roles="
				+ roles + ", school=" + school + "]";
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LoginUser other = (LoginUser) obj;
		if (loginId == null) {
			if (other.loginId != null)
				return false;
		} else if (!loginId.equals(other.loginId))
			return false;
		return true;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((loginId == null) ? 0 : loginId.hashCode());
		return result;
	}
	
	
}

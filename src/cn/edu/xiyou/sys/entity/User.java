package cn.edu.xiyou.sys.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import cn.edu.xiyou.entity.College;
import cn.edu.xiyou.entity.Major;
import cn.edu.xiyou.sys.base.BaseEntity;

/**
 * 用户表
 * 
 * @author lzp
 * 
 */
@Entity
@Table(name = "system_users")
public class User extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*
	 * 用户名
	 */
	private String username;
	/*
	 * 密码
	 */
	private String password;
	/*
	 * 姓名
	 */
	private String name;
	/*
	 * email地址
	 */
	private String email;
	/*
	 * 联系电话
	 */
	private String phone;
	/*
	 * 状态 1为开启，0为禁用
	 */
	private Integer zt = 1;

	@Temporal(TemporalType.TIMESTAMP)
	private Date registerdate;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Roles roles;
	
	/**
	 * 学院
	 */
	private String college;
	
	public String getCollege() {
		return college;
	}

	public void setCollege(String college) {
		this.college = college;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getZt() {
		return zt;
	}

	public void setZt(Integer zt) {
		this.zt = zt;
	}

	public String getRoles() {
		if (roles != null) {
			return roles.getName();
		} else {
			return "";
		}
	}

	public Roles getRolesObject() {
		return roles;
	}

	public void setRoles(Roles roles) {
		this.roles = roles;
	}

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		if (email == null) {
			return "";
		} else {
			return email;
		}
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRegisterdate() {
		if (registerdate == null) {
			return "";
		} else {
			return registerdate.toString();
		}
	}

	public Date getRegisterdateObject() {
		return registerdate;
	}

	public void setRegisterdate(Date registerdate) {
		this.registerdate = registerdate;
	}
}

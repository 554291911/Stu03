package cn.edu.xiyou.sys.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import cn.edu.xiyou.sys.base.BaseEntity;

/**
 * 角色表
 * 
 * @author lzp
 *
 */
@Entity
@Table(name = "system_roles")
public class Roles extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*
	 * 角色名
	 */
	private String name;
	/*
	 * 角色描述
	 */
	private String description;
	/*
	 * 权限
	 */
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "system_roles_function")
	private Set<Function> functions;

	@Temporal(TemporalType.TIMESTAMP)
	private Date registerdate;

	public Set<Function> getFunctions() {
		return functions;
	}

	public void setFunctions(Set<Function> functions) {
		this.functions = functions;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getRegisterdate() {
		return registerdate;
	}

	public void setRegisterdate(Date registerdate) {
		this.registerdate = registerdate;
	}
}

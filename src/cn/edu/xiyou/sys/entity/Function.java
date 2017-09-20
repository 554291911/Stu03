package cn.edu.xiyou.sys.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import cn.edu.xiyou.sys.base.BaseEntity;

/**
 * 功能表
 * @author lzp
 *
 */
@Entity
@Table(name="system_function")
public class Function extends BaseEntity implements Comparable<Function>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public int compareTo(Function o) {
		return this.getSorder().compareTo(o.getSorder());
	}
	/*
	 * 功能名称
	 */
	private String name;
	/*
	 * 功能描述
	 */
	private String description;
	/*
	 * 功能权限对应的URi地址
	 */
	@Column(unique=true,nullable=false)
	private String uri;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date registerdate;
	
	@Column(unique=true,nullable=false)
	private String homepage;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="navbar_id")
	private Navbar navbar;
	
	private Integer sorder=1;
	/**
	 * 图标
	 */
	private String iconCls;
	
	public String getIconCls() {
		return iconCls;
	}
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
	public Integer getSorder() {
		return sorder;
	}
	public void setSorder(Integer sorder) {
		this.sorder = sorder;
	}
	public Navbar getNavbar() {
		return navbar;
	}
	public void setNavbar(Navbar navbar) {
		this.navbar = navbar;
	}
	
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
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
	public String getHomepage() {
		return homepage;
	}
	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}
}

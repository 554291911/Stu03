package cn.edu.xiyou.sys.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import cn.edu.xiyou.sys.base.BaseEntity;

@Entity
@Table(name = "system_navbar")
// 导航栏
public class Navbar extends BaseEntity implements Comparable<Navbar>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public int compareTo(Navbar o) {
		return this.getSorder().compareTo(o.getSorder());
	}
	private String name;

	private String description;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
package cn.edu.xiyou.sys.core;

import java.io.Serializable;
import java.util.List;

import cn.edu.xiyou.sys.entity.Function;

/**
 * 
 * @author lzp
 *
 */
public class NavbarView implements Serializable,Comparable<NavbarView>{
	private String id;
	private String nvaname;
	private String iconCls;
	private Integer s_order;
	private List<Function> functions;
	private String cz;
	
	public Integer getS_order() {
		return s_order;
	}
	public void setS_order(Integer s_order) {
		this.s_order = s_order;
	}
	public String getCz() {
		return cz;
	}
	public void setCz(String cz) {
		this.cz = cz;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Override
	public int compareTo(NavbarView shiroView) {
		return this.getOrder().compareTo(shiroView.getOrder());
	}
	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public String getNvaname() {
		return nvaname;
	}

	public void setNvaname(String nvaname) {
		this.nvaname = nvaname;
	}

	public Integer getOrder() {
		return s_order;
	}

	public void setOrder(Integer order) {
		this.s_order = order;
	}
	public List<Function> getFunctions() {
		return functions;
	}
	public void setFunctions(List<Function> functions) {
		this.functions = functions;
	}
}

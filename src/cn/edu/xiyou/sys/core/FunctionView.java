package cn.edu.xiyou.sys.core;

/**
 * 功能视图
 * @author lzp
 *
 */
public class FunctionView{
	private String id;
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
	private String uri;
	
	private String homepage;
	
	private Integer sorder=1;
	/**
	 * 图标
	 */
	private String iconCls;
	
	private String cz;
	
	private String navbar;
	
	
	public String getNavbar() {
		return navbar;
	}
	public void setNavbar(String navbar) {
		this.navbar = navbar;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCz() {
		return cz;
	}
	public void setCz(String cz) {
		this.cz = cz;
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
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getHomepage() {
		return homepage;
	}
	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}
	public Integer getSorder() {
		return sorder;
	}
	public void setSorder(Integer sorder) {
		this.sorder = sorder;
	}
	public String getIconCls() {
		return iconCls;
	}
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
}

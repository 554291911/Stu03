package cn.edu.xiyou.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import cn.edu.xiyou.sys.base.BaseEntity;

/**
 * 专业
 * @author lzp
 *
 */
@Entity
public class Major extends BaseEntity {
	
	/**
	 * 专业名称
	 */
	private String name;
	
	@ManyToOne
	@JoinColumn(name = "college_id")
	private College college;

	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public College getCollege() {
		return college;
	}

	public void setCollege(College college) {
		this.college = college;
	}
}

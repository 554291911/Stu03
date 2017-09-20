package cn.edu.xiyou.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import cn.edu.xiyou.sys.base.BaseEntity;

/**
 * 学院信息
 * @author lzp
 *
 */
@Entity
public class College extends BaseEntity {
	
	/**
	 * 学院名称
	 */
	private String name;
	
	/**
	 * 专业
	 */
	@OneToMany(mappedBy = "college",cascade = CascadeType.ALL)
	private Set<Major> majors = new HashSet<Major>();

	public College(String name) {
		this.name = name;
	}

	public College(){
		
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Major> getMajors() {
		return majors;
	}

	public void setMajors(Set<Major> majors) {
		this.majors = majors;
	}
}

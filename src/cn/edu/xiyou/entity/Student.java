package cn.edu.xiyou.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.hibernate.annotations.NamedQuery;

import cn.edu.xiyou.sys.base.BaseEntity;

@Entity
@NamedQuery(name = "Student.findSnumbers", query = "select s.snumber from Student s order by snumber")
public class Student extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String university;// 学校
	private String college;// 学院
	private String major;// 专业
	private String classes;// 班级
	@Column(nullable = false, unique = true)
	private String snumber;// 学号
	private String name;// 名字
	private String sex;// 性别
	private String address;// 生源地
	private String ethnic;// 民族
	private String year;// 年级
	private String email;// 邮箱
	private String phone;// 电话
	private String password ;//密码
	
	
	public String getUniversity() {
		return university;
	}

	public void setUniversity(String university) {
		this.university = university;
	}

	public String getCollege() {
		return college;
	}

	public void setCollege(String college) {
		this.college = college;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public String getClasses() {
		return classes;
	}

	public void setClasses(String classes) {
		this.classes = classes;
	}

	public String getSnumber() {
		return snumber;
	}

	public void setSnumber(String snumber) {
		this.snumber = snumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEthnic() {
		return ethnic;
	}

	public void setEthnic(String ethnic) {
		this.ethnic = ethnic;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}

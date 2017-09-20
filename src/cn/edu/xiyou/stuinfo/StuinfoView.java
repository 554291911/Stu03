package cn.edu.xiyou.stuinfo;

import java.util.ArrayList;
import java.util.List;

import cn.edu.xiyou.entity.Credit;

public class StuinfoView {

	private String id;
	private String college;//学院
	private String major;//专业
	private String classes;//班级
	private String snumber;//学号
	private String name;//名字
	private String sex;//性别
	private String address;//生源地
	private String ethnic;//民族
	
	private List<Credit> credits = new ArrayList<Credit>();
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public List<Credit> getCredits() {
		return credits;
	}

	public void setCredits(List<Credit> credits) {
		this.credits = credits;
	}

	
}

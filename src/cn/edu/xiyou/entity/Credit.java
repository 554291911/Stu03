package cn.edu.xiyou.entity;


import javax.persistence.Entity;
import cn.edu.xiyou.sys.base.BaseEntity;

/**
 * 诚信信息
 * @author lzp
 *
 */

@Entity
public class Credit extends BaseEntity {
	/**
	 * 学院
	 */
	private String college;
	
	private String major;// 专业
	
	private String classes;// 班级
	/**
	 * 学生学号
	 */
	private String snumber="";
	/**
	 * 学生姓名
	 */
	private String sname="";
	
	/**
	 * 录入人员姓名
	 */
	private String lrryName="";
	/**
	 * 录入人员用户名
	 */
	private String lrryUsername="";
	/**
	 * 审核人员姓名
	 */
	private String shryName="";
	/**
	 * 审核人员用户名
	 */
	private String shryUsername="";
	/**
	 * 录入时间
	 */
	private String lrsj="";
	/**
	 * 审核时间
	 */
	private String shsj="";
	/**
	 * 审核人员姓名
	 */
	private String shryName1="";
	/**
	 * 审核人员用户名
	 */
	private String shryUsername1="";
	/**
	 * 审核时间
	 */
	private String shsj1="";
	/**
	 * 记录内容
	 */
	private String text="";
	/**
	 * 审核状态：未审核、一级审核、通过
	 */
	private String status="未审核";
	/**
	 * 备注
	 */
	private String bz="无";
	/**
	 * 分数
	 */
	private Integer fs;
	/**
	 * 申诉状态
	 */
	private String sszt="未申诉";
	/**
	 * 申诉时间
	 */
	private String sssj;
	/**
	 * 申诉理由
	 */
	private String ssly;
	/**
	 * 申诉审核人员
	 */
	private String ssshry;
	/**
	 * 申诉审核时间
	 */
	private String ssshsj;
	
	public String getSsshry() {
		return ssshry;
	}
	public void setSsshry(String ssshry) {
		this.ssshry = ssshry;
	}
	public String getSsshsj() {
		return ssshsj;
	}
	public void setSsshsj(String ssshsj) {
		this.ssshsj = ssshsj;
	}
	public String getSszt() {
		return sszt;
	}
	public void setSszt(String sszt) {
		this.sszt = sszt;
	}
	public String getSssj() {
		return sssj;
	}
	public void setSssj(String sssj) {
		this.sssj = sssj;
	}
	public String getSsly() {
		return ssly;
	}
	public void setSsly(String ssly) {
		this.ssly = ssly;
	}
	public Integer getFs() {
		return fs;
	}
	public void setFs(Integer fs) {
		this.fs = fs;
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
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	public String getSnumber() {
		return snumber;
	}
	public void setSnumber(String snumber) {
		this.snumber = snumber;
	}
	public String getSname() {
		return sname;
	}
	public void setSname(String sname) {
		this.sname = sname;
	}
	
	public String getLrryName() {
		return lrryName;
	}
	public void setLrryName(String lrryName) {
		this.lrryName = lrryName;
	}
	public String getLrryUsername() {
		return lrryUsername;
	}
	public void setLrryUsername(String lrryUsername) {
		this.lrryUsername = lrryUsername;
	}
	public String getShryName() {
		return shryName;
	}
	public void setShryName(String shryName) {
		this.shryName = shryName;
	}
	public String getShryUsername() {
		return shryUsername;
	}
	public void setShryUsername(String shryUsername) {
		this.shryUsername = shryUsername;
	}
	public String getLrsj() {
		return lrsj;
	}
	public void setLrsj(String lrsj) {
		this.lrsj = lrsj;
	}
	public String getShsj() {
		return shsj;
	}
	public void setShsj(String shsj) {
		this.shsj = shsj;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getShryName1() {
		return shryName1;
	}
	public void setShryName1(String shryName1) {
		this.shryName1 = shryName1;
	}
	public String getShryUsername1() {
		return shryUsername1;
	}
	public void setShryUsername1(String shryUsername1) {
		this.shryUsername1 = shryUsername1;
	}
	public String getShsj1() {
		return shsj1;
	}
	public void setShsj1(String shsj1) {
		this.shsj1 = shsj1;
	}
}

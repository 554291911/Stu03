package cn.edu.xiyou.cxjlcx;

import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import cn.edu.xiyou.dao.CreditDao;
import cn.edu.xiyou.dao.StudentDao;
import cn.edu.xiyou.entity.Credit;
import cn.edu.xiyou.entity.Student;
import cn.edu.xiyou.sys.dao.RolesDao;
import cn.edu.xiyou.sys.dao.UserDao;
import cn.edu.xiyou.sys.entity.User;

import com.alibaba.fastjson.JSON;

/**
 * 诚信记录查询
 * @author lzp
 *
 */
@Controller
@RequestMapping("/cxjlcx")
public class CxjlcxController {

	@Resource
	CreditDao creditDao;

	@Resource
	StudentDao studentDao;

	@Resource
	UserDao userDao;

	@Resource
	RolesDao rolesDao;

	@RequestMapping("/index.html")
	public String index() {
		return "classes/cn/edu/xiyou/cxjlcx/index";
	}

	/** 显示诚信信息表 */
	@RequestMapping("/load.do")
	public void load(HttpServletRequest request, HttpServletResponse response,HttpSession session) throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		response.setCharacterEncoding("utf8");
		Writer writer = response.getWriter();
		List<Credit> credits = new ArrayList<Credit>();
		User user=null;
		Object su = request.getSession().getAttribute("user");
		if(su instanceof User){
			user=(User)su;
		}
		if(user.getUsername().equals("root")){
			credits = creditDao.findAll();
		}else{
			credits = creditDao.findByCollege(user.getCollege());
		}
		for(Credit c:credits){
			if(c.getStatus().equals("未审核")){
				c.setStatus("<span class=\"label label-sm label-info\">未审核 </span>");
			}else if(c.getStatus().equals("一级审核")){
				c.setStatus("<span class=\"label label-sm label-warning\">一级审核 </span>");
			}else if(c.getStatus().equals("通过")){
				c.setStatus("<span class=\"label label-sm label-success\">通过 </span>");
			}else{
				c.setStatus("<span class=\"label label-sm label-danger\">未通过 </span>");
			}
			if(c.getSszt().equals("未申诉")){
				c.setSszt("<span class=\"label label-sm label-info\">未申诉 </span>");
			}else if(c.getSszt().equals("通过")){
				c.setSszt("<span class=\"label label-sm label-success\">通过 </span>");
			}else if(c.getSszt().equals("未通过")){
				c.setSszt("<span class=\"label label-sm label-danger\">未通过 </span>");
			}else if(c.getSszt().equals("申诉中")){
				c.setSszt("<span class=\"label label-sm label-warning\">申诉中</span>");
			}
			
			c.setShryUsername("<a href=\"javascript:;\" class=\"btn btn-circle btn-sm blue\" onclick=\"jl('"
					+ c.getText()
					+ "','"+c.getBz()+"')\"><i class=\"fa fa-edit\"></i>详细内容</a>");
		}
		map.put("data", credits);
		writer.append(JSON.toJSONStringWithDateFormat(map, "yyyy-MM-dd"));
		writer.flush();
		writer.close();
	}
	
	@RequestMapping("/del.do")
	public void del(String id, HttpServletResponse response) throws IOException {
		response.setCharacterEncoding("utf-8");
		Writer writer = response.getWriter();
		String s = "ok";
		if (id != null && !id.equals("")) {
			Credit credit = creditDao.findOne(id);
			if (credit != null) {
				if(!credit.getStatus().equals("通过")){
					// 删除该诚信记录信息
					creditDao.delete(id);
				}else{
					s = "审核通过的记录不能删除!";
				}
				
			} else {
				s = "您删除的记录不存在,请刷新页面后再试!";
			}
		} else {
			s = "请选择要删除的记录!";
		}
		writer.append(s);
		writer.flush();
		writer.close();
	}
	
	@RequestMapping("/addCXJL.do")
	public void addCXJL(String xh,String text, Writer writer, HttpServletRequest request) throws IOException {
		User user = (User) request.getSession().getAttribute("user");
		String s = "ok";
		if (xh==null || xh.equals("") || text==null || text.equals("")) {
			s = "请输入学号及诚信记录内容!";
		}else{
			Student student = studentDao.findBySnumber(xh);
			if(student!=null){
				Credit credit = new Credit();
				credit.setClasses(student.getClasses());
				credit.setCollege(student.getCollege());
				credit.setLrryName(user.getName());
				credit.setLrryUsername(user.getUsername());
				credit.setLrsj(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
				credit.setMajor(student.getMajor());
				credit.setSname(student.getName());
				credit.setSnumber(student.getSnumber());
				credit.setText(text);
				credit.setStatus("未审核");
				creditDao.saveAndFlush(credit);
				s="ok";
			}else{
				s="您输入的学号不正确,系统未找到该学生信息!";
			}
		}
		writer.append(s);
		writer.flush();
		writer.close();
	}
	@RequestMapping("/getXS.do")
	public void getstuinfo(String xh, Writer writer) throws IOException {
		String s = JSON.toJSONString("error");
		if (xh != null && !xh.equals("")) {
			Student student = studentDao.findBySnumber(xh);
			if (student != null) {
				s = JSON.toJSONString(student);
			}
		}
		writer.append(s);
		writer.flush();
		writer.close();
	}
	/*

	// 找到诚信记录信息
	@RequestMapping("/getCredit.do")
	public void getCredit(String id, Writer writer) throws IOException {
		String s = "error";
		if (id != null && !id.equals("")) {
			Credit credit = creditDao.findOne(id);
			Student student = studentDao.findBySnumber(credit.getSnumber());
			CreditView view = new CreditView();
			view.setSnumber(student.getSnumber());
			view.setStudentname(student.getName());
			view.setMajor(student.getMajor());
			view.setClasses(student.getClasses());
			view.setText(credit.getText());
			if (credit != null) {
				s = JSON.toJSONString(view);
			}
		}
		System.out.println(s);
		writer.append(s);
		writer.flush();
		writer.close();
	}

	

	@RequestMapping("/ckcredit.do")
	public void ckcredit(String id, String remark, String status, Writer writer) throws IOException {
		String s = "ok";
		if (status.equals("")) {
			s = "没有选择审核结果";
		} else if (id != null && !id.equals("")) {
			Credit credit = creditDao.findOne(id);
			if (credit != null) {
				credit.setRemarks(remark);
				credit.setStatus(status);
				creditDao.saveAndFlush(credit);
			} else {
				s = "信息不存在！";
			}
		} else {
			s = "无信息!";
		}
		writer.append(s);
		writer.flush();
		writer.close();
	}*/
}
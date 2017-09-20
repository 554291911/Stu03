package cn.edu.xiyou.sscl;

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
 * 申诉处理
 * @author lzp
 *
 */
@Controller
@RequestMapping("/sscl")
public class SsclController {

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
		return "classes/cn/edu/xiyou/sscl/index";
	}

	/** 显示诚信信息表 */
	@RequestMapping("/load.do")
	public void load(HttpServletRequest request, HttpServletResponse response) throws IOException {
		User user = (User) request.getSession().getAttribute("user");
		Map<String, Object> map = new HashMap<String, Object>();
		response.setCharacterEncoding("utf8");
		Writer writer = response.getWriter();
		List<Credit> credits = new ArrayList<Credit>();
		if(user.getCollege()!=null && !user.getCollege().equals("")){
			credits = creditDao.findBySsztAndCollege("申诉中", user.getCollege());
		}else{
			credits = creditDao.findBySszt("申诉中");
		}
		
		for(Credit c:credits){
			c.setSszt("<span class=\"label label-sm label-success\">申诉中 </span>");
			c.setShryUsername("<a href=\"javascript:;\" class=\"btn btn-circle btn-sm blue\" onclick=\"jl('"
					+ c.getText()+ "','"+c.getSsly()+"')\"><i class=\"fa fa-edit\"></i>申诉理由</a>");
		}
		map.put("data", credits);
		writer.append(JSON.toJSONStringWithDateFormat(map, "yyyy-MM-dd"));
		writer.flush();
		writer.close();
	}
	
	@RequestMapping("/shtg.do")
	public void del(String id, HttpServletResponse response,HttpServletRequest request) throws IOException {
		response.setCharacterEncoding("utf-8");
		Writer writer = response.getWriter();
		User user = (User) request.getSession().getAttribute("user");
		String s = "ok";
		if (id != null && !id.equals("")) {
			Credit credit = creditDao.findOne(id);
			if (credit != null) {
				credit.setSszt("通过");
				credit.setSsshry(user.getName());
				credit.setSsshsj(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
				creditDao.saveAndFlush(credit);
			} else {
				s = "您审核的记录不存在,请刷新页面后再试!";
			}
		} else {
			s = "请选择要审核的记录!";
		}
		writer.append(s);
		writer.flush();
		writer.close();
	}
	@RequestMapping("/shbtg.do")
	public void shbtg(String id, HttpServletResponse response,HttpServletRequest request) throws IOException {
		response.setCharacterEncoding("utf-8");
		Writer writer = response.getWriter();
		User user = (User) request.getSession().getAttribute("user");
		String s = "ok";
		if (id != null && !id.equals("")) {
			Credit credit = creditDao.findOne(id);
			if (credit != null) {
				credit.setSszt("未通过");
				credit.setSsshry(user.getName());
				credit.setSsshsj(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
				creditDao.saveAndFlush(credit);
			} else {
				s = "您审核的记录不存在,请刷新页面后再试!";
			}
		} else {
			s = "请选择要审核的记录!";
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
}

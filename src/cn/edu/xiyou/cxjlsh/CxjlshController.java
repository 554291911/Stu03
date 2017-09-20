package cn.edu.xiyou.cxjlsh;

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
 * 诚信记录一级审核
 * @author lzp
 *
 */
@Controller
@RequestMapping("/cxjlsh")
public class CxjlshController {

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
		return "classes/cn/edu/xiyou/cxjlsh/index";
	}

	/** 显示诚信信息表 */
	@RequestMapping("/load.do")
	public void load(HttpServletRequest request, HttpServletResponse response,HttpSession session) throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		User user=null;
		Object su = request.getSession().getAttribute("user");
		if(su instanceof User){
			user=(User)su;
		}
		response.setCharacterEncoding("utf8");
		Writer writer = response.getWriter();
		List<Credit> credits = new ArrayList<Credit>();
		if(user.getUsername().equals("root")){
			credits = creditDao.findByStatus("未审核");
		}else{
			credits = creditDao.findByStatusAndCollege("未审核",user.getCollege());
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
	
	@RequestMapping("/shtg.do")
	public void del(String id, HttpServletResponse response,HttpServletRequest request) throws IOException {
		response.setCharacterEncoding("utf-8");
		Writer writer = response.getWriter();
		User user = (User) request.getSession().getAttribute("user");
		String s = "ok";
		if (id != null && !id.equals("")) {
			Credit credit = creditDao.findOne(id);
			if (credit != null) {
				credit.setStatus("一级审核");
				credit.setShryName(user.getName());
				credit.setShryUsername(user.getUsername());
				credit.setShsj(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
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
	
	@RequestMapping("/addBZ.do")
	public void addCXJL(String bz,String id, Writer writer, HttpServletRequest request) throws IOException {
		User user = (User) request.getSession().getAttribute("user");
		String s = "ok";
		if (bz==null || bz.equals("")) {
			s = "请输入不通过理由!";
		}else if(id==null || id.equals("")){
			s = "请选择审核的学生记录!";
		}else{
			Credit credit = creditDao.findOne(id);
			if(credit!=null){
				credit.setBz(bz);
				credit.setStatus("未通过");
				credit.setShryName(user.getName());
				credit.setShryUsername(user.getUsername());
				credit.setShsj(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
				creditDao.saveAndFlush(credit);
				s="ok";
			}else{
				s = "您审核的记录不存在,请刷新页面后再试!";
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
}

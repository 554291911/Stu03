package cn.edu.xiyou.sys.core;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.edu.xiyou.dao.StudentDao;
import cn.edu.xiyou.entity.Student;
import cn.edu.xiyou.sys.dao.UserDao;
import cn.edu.xiyou.sys.entity.User;

@Controller
public class PersonalController {
	@Resource
	private UserDao userDao;
	@Resource
	private StudentDao studentDao;
	
	
	@RequestMapping("personal.html")
	public String personal(HttpSession session,Map<String,Object> map){
		Object object = session.getAttribute("user");
		if(object instanceof User){
			return "viewfiles/admin";
		}else if(object instanceof Student){
			map.put("stu", object);
			return "viewfiles/personal";
		}else{
			return "viewfiles/personal";
		}
		
	}
	@RequestMapping("/setUserMM.do")
	public void SetMM(String ymm,String xmm,Writer writer,HttpSession session) throws IOException{
		User user = (User) session.getAttribute("user");
		String s="ok";
		if(user.getPassword().equals(ymm)){
			User user2 = userDao.findByUsername(user.getUsername()).get(0);
			user2.setPassword(xmm);
			userDao.saveAndFlush(user2);
		}else{
			s="error";
		}
		writer.append(s);
		writer.flush();
		writer.close();
	}
	@RequestMapping("/setStuMM.do")
	public void SetStuMM(String ymm,String xmm,Writer writer,HttpSession session) throws IOException{
		Student student = (Student) session.getAttribute("user");
		String s="ok";
		if(student.getPassword().equals(ymm)){
			Student student2 = studentDao.findBySnumber(student.getSnumber());
			student2.setPassword(xmm);
			studentDao.saveAndFlush(student2);
		}else{
			s="error";
		}
		writer.append(s);
		writer.flush();
		writer.close();
	}
	@RequestMapping("/setGrxx.do")
	public void SetGrxx(String email,String phone,Writer writer,HttpSession session) throws IOException{
		Student student = (Student) session.getAttribute("user");
		String s="ok";
		Student student2 = studentDao.findBySnumber(student.getSnumber());
		student2.setPhone(phone);
		student2.setEmail(email);
		studentDao.saveAndFlush(student2);
		writer.append(s);
		writer.flush();
		writer.close();
	}
}

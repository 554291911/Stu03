package cn.edu.xiyou.sys.core;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.edu.xiyou.dao.CollegeDao;
import cn.edu.xiyou.entity.College;
import cn.edu.xiyou.sys.dao.RolesDao;
import cn.edu.xiyou.sys.dao.UserDao;
import cn.edu.xiyou.sys.entity.Roles;
import cn.edu.xiyou.sys.entity.User;

import com.alibaba.fastjson.JSON;

/**
 * 用户管理
 * 
 * @author lzp
 *
 */
@Controller
@RequestMapping("/user")
public class UserController {

	@Resource
	private UserDao userDao;
	@Resource
	private RolesDao rolesDao;
	@Resource
	private CollegeDao collegeDao;

	@RequestMapping("/index.html")
	public String index(Map<String, Object> map) {
		map.put("roles", rolesDao.findAll());
		map.put("college", collegeDao.findAll());
		return "viewfiles/user";
	}

	@RequestMapping("/load.do")
	public void load(HttpServletResponse response) throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		response.setCharacterEncoding("utf8");
		Writer writer = response.getWriter();
		List<UserView> views = new ArrayList<UserView>();
		for (User u : userDao.findAll()) {
			UserView view = new UserView();
			view.setId(u.getId());
			view.setName(u.getName());
			view.setUsername(u.getUsername());
			view.setPhone(u.getPhone());
			view.setEmail(u.getEmail());
			view.setRoles(u.getRoles());
			if (u.getCollege() != null) {
				view.setCollege(u.getCollege());
			} else {
				view.setCollege("");
			}
			if (u.getZt() == 1) {
				view.setZt("<span class=\"label label-sm label-success\">正常 </span>");
				view.setCz("<a href=\"javascript:;\" class=\"btn btn-circle btn-sm purple\" onclick=\"roles('"
						+ u.getId() + "')\"><i class=\"fa fa-user\"></i>角色</a>"
						+ "<a href=\"javascript:;\" class=\"btn btn-circle btn-sm blue\" onclick=\"upd('" + u.getId()
						+ "')\"><i class=\"fa fa-edit\"></i>修改</a>"
						+ "<a href=\"javascript:;\" class=\"btn btn-circle btn-sm red\" onclick=\"del('" + u.getId()
						+ "')\"><i class=\"fa fa-remove\"></i>删除</a>"
						+ "<a href=\"javascript:;\" class=\"btn btn-circle btn-sm yellow\" onclick=\"paw('" + u.getId()
						+ "')\"><i class=\"fa fa-edit\"></i>密码重置</a>"
						+ "<a href=\"javascript:;\" class=\"btn btn-circle btn-sm red\" onclick=\"zt('" + u.getId()
						+ "','0')\">禁用</a>");
			} else {
				view.setZt("<span class=\"label label-sm label-danger\">禁用 </span>");
				view.setCz("<a href=\"javascript:;\" class=\"btn btn-circle btn-sm blue\" onclick=\"roles('" + u.getId()
						+ "')\"><i class=\"fa fa-user\"></i>角色</a>"
						+ "<a href=\"javascript:;\" class=\"btn btn-circle btn-sm blue\" onclick=\"upd('" + u.getId()
						+ "')\"><i class=\"fa fa-edit\"></i>修改</a>"
						+ "<a href=\"javascript:;\" class=\"btn btn-circle btn-sm red\" onclick=\"del('" + u.getId()
						+ "')\"><i class=\"fa fa-remove\"></i>删除</a>"
						+ "<a href=\"javascript:;\" class=\"btn btn-circle btn-sm yellow\" onclick=\"paw('" + u.getId()
						+ "')\"><i class=\"fa fa-edit\"></i>密码重置</a>"
						+ "<a href=\"javascript:;\" class=\"btn btn-circle btn-sm green\" onclick=\"zt('" + u.getId()
						+ "','1')\">启用</a>");
			}
			view.setPassword("重置密码");

			views.add(view);

		}

		map.put("data", views);
		writer.append(JSON.toJSONString(map));
		writer.flush();
		writer.close();

	}

	@RequestMapping("/zt.do")
	@ResponseBody
	public String zt(String id, Integer zt) {
		String s = "ok";
		if (id != null && !id.equals("")) {
			User user = userDao.findById(id);
			if (user != null) {
				user.setZt(zt);
				userDao.saveAndFlush(user);
			} else {
				s = "error";
			}
		} else {
			s = "error";
		}
		return s;
	}

	@RequestMapping("/paw.do")
	@ResponseBody
	public String paw(String id) {
		String s = "ok";
		if (id != null && !id.equals("")) {
			User user = userDao.findById(id);
			if (user != null) {
				user.setPassword("123456");
				userDao.saveAndFlush(user);
			} else {
				s = "error";
			}
		} else {
			s = "error";
		}
		return s;
	}

	@RequestMapping("/adduser.do")
	public void adduser(User user, String collegeid, Writer writer) throws IOException {
		String s = "ok";
		if (user.getUsername() != null && !user.getUsername().equals("")) {
			if (userDao.findByUsername(user.getUsername()).isEmpty()) {
				user.setId();
				user.setPassword("123456");
				if(collegeid==null){
					user.setCollege("");
				}else{
					College college = collegeDao.findOne(collegeid);
					if(college!=null){
						user.setCollege(college.getName());
					}else{
						user.setCollege("");
					}
				}
				userDao.saveAndFlush(user);
			} else {
				s = "用户名已经存在！";
			}
		} else {
			s = "用户名不能为空!";
		}
		writer.append(s);
		writer.flush();
		writer.close();
	}

	@RequestMapping("/addroles.do")
	public void addroles(String userid, String rolesid, Writer writer) throws IOException {
		String s = "ok";
		if (userid != null && !userid.equals("") && rolesid != null && !rolesid.equals("")) {
			User user = userDao.findOne(userid);
			if (user != null) {
				if (rolesid.equals("0")) {
					user.setRoles(null);
					userDao.saveAndFlush(user);
				} else {
					Roles roles = rolesDao.findOne(rolesid);
					if (roles != null) {
						user.setRoles(roles);
						userDao.saveAndFlush(user);
					} else {
						s = "角色不存在!";
					}
				}
			} else {
				s = "用户不存在!";
			}
		} else {
			s = "请选择角色!";
		}
		writer.append(s);
		writer.flush();
		writer.close();
	}

	@RequestMapping("/getUser.do")
	public void getUser(String id, Writer writer) throws IOException {
		String s = "error";
		if (id != null && !id.equals("")) {
			User user = userDao.findOne(id);
			if (user != null) {
				if (user.getCollege() == null) {
					user.setCollege("");
				}
				s = JSON.toJSONString(user);
			}
		}
		System.out.println(s);
		writer.append(s);
		writer.flush();
		writer.close();
	}

	@RequestMapping("/editeuser.do")
	public void editeuser(String id, String name, String email, String phone, String collegeid, Writer writer)
			throws IOException {
		String s = "ok";
		if (id != null && !id.equals("")) {
			User user = userDao.findOne(id);
			if (user != null) {
				user.setName(name);
				user.setEmail(email);
				user.setPhone(phone);
				if (collegeid != null && !collegeid.equals("")){
					College college = collegeDao.findOne(collegeid);
					if(college!=null){
						user.setCollege(college.getName());
					}else{
						user.setCollege("");
					}
				}else{
					user.setCollege("");
				}
				userDao.saveAndFlush(user);
			} else {
				s = "用户不存在！";
			}
		} else {
			s = "请选择用户!";
		}
		writer.append(s);
		writer.flush();
		writer.close();
	}

	@RequestMapping("/del.do")
	@ResponseBody
	public String del(String id) {
		String s = "ok";
		if (id != null && !id.trim().equals("")) {
			User user = userDao.findById(id);
			if (user != null) {
				user.setRoles(null);
				userDao.delete(user);
			}
		} else {
			s = "error";
		}
		return s;
	}
}

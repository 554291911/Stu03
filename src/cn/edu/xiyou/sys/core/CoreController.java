package cn.edu.xiyou.sys.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.poi.ss.formula.functions.Na;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.edu.xiyou.dao.StudentDao;
import cn.edu.xiyou.entity.Student;
import cn.edu.xiyou.sys.dao.FunctionDao;
import cn.edu.xiyou.sys.dao.NavbarDao;
import cn.edu.xiyou.sys.dao.RolesDao;
import cn.edu.xiyou.sys.dao.UserDao;
import cn.edu.xiyou.sys.entity.Function;
import cn.edu.xiyou.sys.entity.Navbar;
import cn.edu.xiyou.sys.entity.Roles;
import cn.edu.xiyou.sys.entity.User;

/**
 * 
 * @author lzp
 *
 */
@Controller
public class CoreController {

	private static Logger logger = Logger.getLogger(CoreController.class);

	
	@Resource
	private UserDao userDao;
	@Resource
	private FunctionDao functionDao;
	@Resource
	private NavbarDao navbarDao;

	@Resource
	private StudentDao studentDao;

	@Resource
	private RolesDao rolesDao;
	
	
	/**
	 * 登录
	 * 
	 * @param username
	 * @param password
	 * @param session
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/login.do")
	public void login(String username, String password, HttpSession session, HttpServletResponse response)
			throws IOException {
		session.getServletContext().getContextPath();
		if (username != null && !username.trim().equals("") && password != null && !password.trim().equals("")) {
			List<User> users = userDao.findByUsernameAndPassword(username, password);
			if (users.size() == 1) {
				User user = users.get(0);
				session.setAttribute("user", user);
				Roles roles = user.getRolesObject();
				Map<String, String> user_perms = new HashMap<String, String>();
				user_perms.put("/index.html", "主页");
				user_perms.put("/personal.html", "个人设置");
				if (roles != null) {
					for (Function p : roles.getFunctions()) {
						user_perms.put(p.getUri(), p.getName());
					}
				}
				session.setAttribute("user_perms", user_perms);
				response.sendRedirect("index.html");
			} else {
				// 超级管理员
				if (username.equals("admin") && password.equals("liuzhipeng")) {
					User user = new User();
					user.setUsername("admin");
					user.setPassword("password");
					session.setAttribute("user", user);
					Map<String, String> user_perms = new HashMap<String, String>();
					user_perms.put("/index.html", "主页");
					user_perms.put("/personal.html", "个人设置");
					user_perms.put("/roles/**", "角色管理");
					user_perms.put("/user/**", "用户管理");
					user_perms.put("/function/**", "应用管理");
					session.setAttribute("user_perms", user_perms);
					response.sendRedirect("index.html");
				} else if (users.size() == 0) {
					Student student = studentDao.findBySnumberAndPassword(username, password);
					if (student == null)
						response.sendRedirect("login.html");
					else {
						session.setAttribute("user", student);
						Map<String, String> user_perms = new HashMap<String, String>();
						user_perms.put("/index.html", "主页");
						user_perms.put("/personal.html", "个人设置");
						user_perms.put("/stuinfo/**", "个人诚信记录");
						session.setAttribute("user_perms", user_perms);
						response.sendRedirect("index.html");
					}
				} else {
					response.sendRedirect("login.html");
				}

			}
		} else {
			response.sendRedirect("login.html");
		}
	}

	/**
	 * 首页
	 * 
	 * @param session
	 * @param map
	 * @return
	 */
	@RequestMapping("/index.html")
	public String index(HttpSession session, HttpSession map) {
		logger.info("请求地址：/index.html");
		User user = null;
		Student student = null;
		try {
			user = (User) session.getAttribute("user");
		} catch (Exception e) {
			student = (Student) session.getAttribute("user");
		}
		if (user != null) {
			logger.debug("当前登录用户：" + user.getUsername());
			List<NavbarView> shiroViews = new ArrayList<NavbarView>();
			if (user.getUsername().equals("admin")) {
				map.setAttribute("roles", "超级管理员");
				NavbarView shiroView = new NavbarView();
				Navbar navbars = navbarDao.findByName("系统设置");
				if (navbars!=null) {
					shiroView.setId(navbars.getId());
					shiroView.setNvaname(navbars.getName());
					shiroView.setIconCls(navbars.getIconCls());
					List<Function> permissions = functionDao.findByNavbar(navbars);
					Collections.sort(permissions);
					shiroView.setFunctions(permissions);
					shiroView.setOrder(1);
					shiroViews.add(shiroView);
				}
				Collections.sort(shiroViews);
				map.setAttribute("bar", shiroViews);
				return "viewfiles/index";
			} else {
				user = userDao.findById(user.getId());
				Set<Function> per1 = new HashSet<Function>();
				/** 增加角色权限 **/
				Roles roles = user.getRolesObject();
				String sr = "";
				if (roles != null) {
					if (sr.equals("")) {
						sr = roles.getName();
					} else {
						sr = sr + "," + roles.getName();
					}
					per1.addAll(roles.getFunctions());
				}
				map.setAttribute("roles", sr);
				Map<String, List<Function>> map2 = new HashMap<String, List<Function>>();
				Map<String, String> naicons = new HashMap<String, String>();
				for (Function p : per1) {
					String navname = p.getNavbar().getName();
					String icons = p.getNavbar().getIconCls();
					naicons.put(navname, icons);
					if (map2.containsKey(navname)) {
						List<Function> permissions = map2.get(navname);
						permissions.add(p);
						Collections.sort(permissions);
						map2.put(navname, permissions);
					} else {
						List<Function> permissions = new ArrayList<Function>();
						permissions.add(p);
						Collections.sort(permissions);
						map2.put(navname, permissions);
					}
				}
				Set<String> set = map2.keySet();
				Iterator<String> iterator = set.iterator();
				while (iterator.hasNext()) {
					String navname = iterator.next();
					Navbar navbars = navbarDao.findByName(navname);
					System.out.println(navbars);
					if(navbars!=null){
						List<Function> permissions = map2.get(navname);
						NavbarView shiroView = new NavbarView();
						shiroView.setId(navbars.getId());
						shiroView.setOrder(navbars.getSorder());
						shiroView.setNvaname(navname);
						shiroView.setIconCls(naicons.get(navname));
						shiroView.setFunctions(permissions);
						shiroViews.add(shiroView);
					}else{
						System.out.println(navname+"的分组不存在。");
					}
				}
				Collections.sort(shiroViews);
				map.setAttribute("bar", shiroViews);
				return "forward:cxjlcx/index.html";
			}
			
			//return "viewfiles/index";
			
		} else if (user == null && student != null) {
			map.setAttribute("roles", "学生");
			NavbarView shiroView = new NavbarView();
			List<NavbarView> shiroViews = new ArrayList<NavbarView>();
			Navbar navbars = navbarDao.findByName("诚信信息管理");
			if (navbars!=null) {
				shiroView.setId(navbars.getId());
				shiroView.setNvaname(navbars.getName());
				shiroView.setIconCls(navbars.getIconCls());
				List<Function> permissions = new ArrayList<Function>();
				Function function = functionDao.findByName("个人诚信记录").get(0);
				permissions.add(function);
				shiroView.setFunctions(permissions);
				shiroView.setOrder(1);
				shiroViews.add(shiroView);
			}
			Collections.sort(shiroViews);
			map.setAttribute("bar", shiroViews);
			return "forward:stuinfo/index.html";
			//return "viewfiles/index";
		} else {
			return "viewfiles/login";
		}
	}

	@RequestMapping("/login.html")
	public String loginhtml(HttpSession session) {
		session.removeAttribute("user");
		session.removeAttribute("user_perms");
		return "viewfiles/login";
	}

	@RequestMapping("/404.html")
	public String s404() {
		return "viewfiles/404";
	}

	@RequestMapping("/500.html")
	public String s500() {
		return "viewfiles/500";
	}

	@RequestMapping("/unauthorized.html")
	public String unauthorized() {
		return "viewfiles/unauthorized";
	}
}

package cn.edu.xiyou.sys.core;

import java.io.IOException;
import java.io.Writer;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.edu.xiyou.sys.dao.FunctionDao;
import cn.edu.xiyou.sys.dao.NavbarDao;
import cn.edu.xiyou.sys.dao.RolesDao;
import cn.edu.xiyou.sys.dao.UserDao;
import cn.edu.xiyou.sys.entity.Function;
import cn.edu.xiyou.sys.entity.Navbar;
import cn.edu.xiyou.sys.entity.User;

/**
 * 应用管理
 * @author lzp
 *
 */
@Controller
@RequestMapping("/function")
public class FunctionController {
	@Resource
	private NavbarDao navbarDao;
	@Resource
	private FunctionDao functionDao;
	@Resource
	private UserDao userDao;
	@Resource
	private RolesDao rolesDao;
	@Resource
	private DataSource dataSource;
	
	@RequestMapping("/index.html")
	public String index(){
		return "viewfiles/function";
	}
	
	@RequestMapping("/loadNavbar.do")
	public void load(HttpServletResponse response) throws IOException{
		Map<String, Object> map = new HashMap<String, Object>();
		response.setCharacterEncoding("utf8");
		Writer writer = response.getWriter();
		List<NavbarView> views = new ArrayList<NavbarView>();
		for(Navbar n:navbarDao.findAll()){
			if(n.getName().equals("系统设置")){
				continue;
			}
			NavbarView navbarView = new NavbarView();
			navbarView.setId(n.getId());
			navbarView.setNvaname(n.getName());
			navbarView.setOrder(n.getSorder());
			navbarView.setCz("<a href=\"javascript:;\" class=\"btn btn-circle btn-sm blue\" onclick=\"updn('"+n.getId()+"')\"><i class=\"fa fa-edit\"></i>修改</a>"
					+ "<a href=\"javascript:;\" class=\"btn btn-circle btn-sm red\" onclick=\"deln('"+n.getId()+"')\"><i class=\"fa fa-remove\"></i>删除</a>");
			views.add(navbarView);
			
		}
		map.put("data", views);
		writer.append(JSON.toJSONString(map));
		writer.flush();
		writer.close();
		
	}
	@RequestMapping("/loadFunction.do")
	public void loadFunction(HttpServletResponse response,String nid) throws IOException{
		System.out.println(nid);
		Map<String, Object> map = new HashMap<String, Object>();
		response.setCharacterEncoding("utf8");
		Writer writer = response.getWriter();
		List<FunctionView> views = new ArrayList<FunctionView>();
		List<Function> functions = new ArrayList<Function>();
		if(nid==null || nid.equals("")){
			functions = functionDao.findAll();
		}else{
			Navbar navbar = navbarDao.findOne(nid);
			if(navbar!=null){
				functions = functionDao.findByNavbar(navbar);
			}
		}
		for(Function f:functions){
			if(f.getNavbar().getName().equals("系统设置")){
				continue;
			}
			FunctionView functionView = new FunctionView();
			functionView.setId(f.getId());
			functionView.setName(f.getName());
			functionView.setDescription(f.getDescription());
			functionView.setUri(f.getUri());
			functionView.setNavbar(f.getNavbar().getName());
			functionView.setHomepage(f.getHomepage());
			functionView.setSorder(f.getSorder());
			functionView.setCz("<a href=\"javascript:;\" class=\"btn btn-circle btn-sm blue\" onclick=\"updf('"+f.getId()+"')\"><i class=\"fa fa-edit\"></i>修改</a>"
					+ "<a href=\"javascript:;\" class=\"btn btn-circle btn-sm red\" onclick=\"delf('"+f.getId()+"')\"><i class=\"fa fa-remove\"></i>删除</a>");
			views.add(functionView);
			
		}
		map.put("data", views);
		writer.append(JSON.toJSONString(map));
		writer.flush();
		writer.close();
		
	}
	@RequestMapping("/zt.do")
	@ResponseBody
	public String zt(String id,Integer zt){
		String s="ok";
		if(id!=null && !id.equals("")){
			User user = userDao.findById(id);
			if(user!=null){
				user.setZt(zt);
				userDao.saveAndFlush(user);
			}else{
				s="error";
			}
		}else{
			s="error";
		}
		return s;
	}
	@RequestMapping("/delf.do")
	@ResponseBody
	public String delf(String id){
		String s="ok";
		if(id!=null && !id.equals("")){
			try {
				Connection connection = dataSource.getConnection();
				Statement statement = connection.createStatement();
				statement.executeUpdate("delete from system_roles_function where functions_id='"+id+"'");
				statement.close();
				connection.close();
			} catch (SQLException e) {
				System.out.println("删除异常!");
			}
			functionDao.delete(id);
		}else{
			s="error";
		}
		return s;
	}
	@RequestMapping("/deln.do")
	@ResponseBody
	public String deln(String id){
		String s="ok";
		if(id!=null && !id.equals("")){
			Navbar navbar = navbarDao.findOne(id);
			if(navbar!=null){
				functionDao.delete(functionDao.findByNavbar(navbar));
			}
			navbarDao.delete(id);
		}else{
			s="error";
		}
		return s;
	}
	@RequestMapping("/paw.do")
	@ResponseBody
	public String paw(String id){
		String s="ok";
		if(id!=null && !id.equals("")){
			User user = userDao.findById(id);
			if(user!=null){
				user.setPassword("123456");
				userDao.saveAndFlush(user);
			}else{
				s="error";
			}
		}else{
			s="error";
		}
		return s;
	}
	@RequestMapping("/addNavbar.do")
	public void adduser(String name,Integer sorder,Writer writer) throws IOException{
		String s="ok";
		if(name!=null && !name.equals("")){
			if(navbarDao.findByName(name)==null){
				Navbar navbar = new Navbar();
				navbar.setName(name);
				navbar.setSorder(sorder);
				navbarDao.saveAndFlush(navbar);
			}else{
				s="导航栏名称<"+name+">已经存在！";
			}
		}else{
			s="导航栏名称不能为空!";
		}
		writer.append(s);
		writer.flush();
		writer.close();
	}
	@RequestMapping("/addFun.do")
	public void addFun(Function function,String nid,Writer writer) throws IOException{
		String s="ok";
		if(function.getName()!=null && !function.getName().equals("") && nid!=null && !nid.equals("")){
			Navbar navbar = navbarDao.findOne(nid);
			if(navbar!=null){
				function.setId();
				function.setNavbar(navbar);
				functionDao.saveAndFlush(function);
			}else{
				s="导航栏不存在！";
			}
		}else{
			s="功能名称不能为空!";
		}
		writer.append(s);
		writer.flush();
		writer.close();
	}
	@RequestMapping("/getNavbar.do")
	public void getUser(String id,Writer writer) throws IOException{
		String s="error";
		if(id!=null&& !id.equals("")){
			Navbar navbar = navbarDao.findOne(id);
			if(navbar!=null){
				s= JSON.toJSONString(navbar);
			}
		}
		writer.append(s);
		writer.flush();
		writer.close();
	}
	@RequestMapping("/upNavbar.do")
	public void upNavbar(String id,String name,Integer sorder,Writer writer) throws IOException{
		String s="error";
		if(id!=null&& !id.equals("")){
			Navbar navbar = navbarDao.findOne(id);
			if(navbar!=null){
				navbar.setName(name);
				navbar.setSorder(sorder);
				navbarDao.saveAndFlush(navbar);
				s="ok";
			}
		}
		writer.append(s);
		writer.flush();
		writer.close();
	}
	@RequestMapping("/getFunction.do")
	public void getFunction(String id,Writer writer) throws IOException{
		String s="error";
		if(id!=null&& !id.equals("")){
			Function function = functionDao.findOne(id);
			if(function!=null){
				s= JSON.toJSONString(function);
			}
		}
		writer.append(s);
		writer.flush();
		writer.close();
	}
	@RequestMapping("/upFunction.do")
	public void upFunction(String id,String name,Integer sorder,String homepage,String uri,String description,Writer writer) throws IOException{
		String s="error";
		if(id!=null&& !id.equals("")){
			Function function = functionDao.findOne(id);
			if(function!=null){
				function.setName(name);
				function.setSorder(sorder);
				function.setDescription(description);
				function.setHomepage(homepage);
				function.setUri(uri);
				functionDao.saveAndFlush(function);
				s="ok";
			}
		}
		writer.append(s);
		writer.flush();
		writer.close();
	}
}

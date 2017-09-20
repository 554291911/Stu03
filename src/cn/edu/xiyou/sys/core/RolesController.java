package cn.edu.xiyou.sys.core;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import cn.edu.xiyou.sys.entity.Roles;
import cn.edu.xiyou.sys.entity.User;

/**
 * 角色管理
 * @author lzp
 *
 */
@Controller
@RequestMapping("/roles")
public class RolesController {
	@Resource
	private FunctionDao functionDao;
	@Resource
	private UserDao userDao;
	@Resource
	private RolesDao rolesDao;
	
	@Resource
	private NavbarDao navbarDao;
	
	@RequestMapping("/index.html")
	public String index(){
		return "viewfiles/roles";
	}
	
	@RequestMapping("/load.do")
	public void load(HttpServletResponse response) throws IOException{
		Map<String, Object> map = new HashMap<String, Object>();
		response.setCharacterEncoding("utf8");
		Writer writer = response.getWriter();
		List<RolesView> views = new ArrayList<RolesView>();
		for(Roles u:rolesDao.findAll()){
			RolesView view = new RolesView();
			view.setId(u.getId());
			view.setName(u.getName());
			view.setDescription(u.getDescription());
			view.setCz("<a href=\"javascript:;\" class=\"btn btn-circle btn-sm blue\" onclick=\"upd('"+u.getId()+"')\"><i class=\"fa fa-edit\"></i>修改</a>"
					+ "<a href=\"javascript:;\" class=\"btn btn-circle btn-sm red\" onclick=\"del('"+u.getId()+"')\"><i class=\"fa fa-remove\"></i>删除</a>");
			views.add(view);
		}
		map.put("data", views);
		writer.append(JSON.toJSONString(map));
		writer.flush();
		writer.close();
		
	}
	
	@RequestMapping("/del.do")
	@ResponseBody
	public String del(String id){
		String s="ok";
		if(id!=null && !id.equals("")){
			Roles roles = rolesDao.findOne(id);
			if(roles!=null){
				//删除拥有该角色的所有用户的角色信息
				List<User> users = userDao.findByRoles(roles);
				for(User u:users){
					u.setRoles(null);
					userDao.saveAndFlush(u);
				}
				rolesDao.delete(id);
			}else{
				s="角色不存在!";
			}
		}else{
			s="error";
		}
		return s;
	}
	@RequestMapping("/addroles.do")
	public void adduser(Roles roles,Writer writer) throws IOException{
		String s="ok";
		if(roles.getName()!=null && !roles.getName().equals("")){
			if(rolesDao.findByName(roles.getName()).isEmpty()){
				roles.setId();
				rolesDao.saveAndFlush(roles);
			}else{
				s="角色名称已存在,请重新填写!";
			}
		}else{
			s="角色名称不能为空!";
		}
		writer.append(s);
		writer.flush();
		writer.close();
	}
	@RequestMapping("/getRoles.do")
	public void getUser(String id,Writer writer) throws IOException{
		String s="error";
		if(id!=null&& !id.equals("")){
			Roles roles = rolesDao.findOne(id);
			if(roles!=null){
				s= JSON.toJSONString(roles);
			}
		}
		writer.append(s);
		writer.flush();
		writer.close();
	}
	@RequestMapping("/editeroles.do")
	public void editeuser(String id,String name,String description,Writer writer) throws IOException{
		String s="ok";
		if(id!=null && !id.equals("")){
			Roles roles= rolesDao.findOne(id);
			if(roles!=null){
				roles.setName(name);
				roles.setDescription(description);
				rolesDao.saveAndFlush(roles);
			}else{
				s="角色不存在！";
			}
		}else{
			s="请选择角色!";
		}
		writer.append(s);
		writer.flush();
		writer.close();
	}
	@RequestMapping("/tree.do")
	public void tree(String uid,Writer writer,HttpSession session) throws IOException{
		User user = (User) session.getAttribute("user");
		List<NavbarTreeView> navbarTreeViews = new ArrayList<NavbarTreeView>();
		List<Navbar> navbars = navbarDao.findAll();
		for(Navbar n:navbars){
			NavbarTreeView treeView = new NavbarTreeView();
			treeView.setId(n.getId());
			treeView.setText(n.getName());
			treeView.setState(new StateView());
			treeView.setIcon("fa fa-folder icon-state-warning");
			List<Function> functions = functionDao.findByNavbar(n);
			List<FunctionTreeView> functionTreeViews = new ArrayList<FunctionTreeView>();
			for(Function f:functions){
				if(!f.getName().equals("应用管理")){
					FunctionTreeView functionTreeView = new FunctionTreeView();
					functionTreeView.setId(f.getId());
					functionTreeView.setText(f.getName());
					StateView state = new StateView();
					state.setSelected(false);
					functionTreeView.setState(state);
					functionTreeView.setIcon("fa fa-file icon-state-success");
					functionTreeViews.add(functionTreeView);
				}
			}
			treeView.setChildren(functionTreeViews);
			navbarTreeViews.add(treeView);
		}
		writer.append(JSON.toJSONString(navbarTreeViews));
		writer.flush();
		writer.close();
	}
	@RequestMapping("/getFunction.do")
	public void getFunction(String id,Writer writer) throws IOException{
		String s="error";
		if(id!=null && !id.equals("")){
			Roles roles= rolesDao.findOne(id);
			if(roles!=null){
				s = JSON.toJSONString(roles.getFunctions());
			}else{
				s="角色不存在！";
			}
		}else{
			s="请选择角色!";
		}
		writer.append(s);
		writer.flush();
		writer.close();
	}
	@RequestMapping("/setFunction.do")
	public void setFunction(String ids,Writer writer,String rid) throws IOException{
		String s="ok";
		if(ids!=null&&rid!=null&!rid.equals("")){
			Roles roles = rolesDao.findOne(rid);
			if(roles!=null){
				Set<Function> functions = new HashSet<Function>();
				for(String id:ids.split(",")){
					Function function = functionDao.findOne(id);
					if(function!=null){
						functions.add(function);
					}
				}
				roles.setFunctions(functions);
				rolesDao.saveAndFlush(roles);
			}else{
				s="角色不存在!";
			}
		}else{
			s="请选择角色!";
		}
		writer.append(s);
		writer.flush();
		writer.close();
	}
}

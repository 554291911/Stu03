package cn.edu.xiyou.sys.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.edu.xiyou.sys.dao.FunctionDao;
import cn.edu.xiyou.sys.entity.Function;
/**
 * 
 * @author lzp
 *
 */

public class CoreFilter implements Filter{
	
	private static Logger logger = Logger.getLogger(CoreFilter.class);
	
	private String login="/login.html";
	private String unauthorized="/unauthorized.html";
	private Map<String,String> perms = new HashMap<String, String>();
	
	@Override
	public void destroy() {
		logger.info("CoreFilter Destroy!");
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,FilterChain chain) throws IOException, ServletException {
		logger.info("CoreFilter Do!");
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		ServletContext context = request.getSession().getServletContext();
		String contextpath =  context.getContextPath();
		String uri = request.getRequestURI();
		HttpSession session = request.getSession();
		String ur = uri.substring(contextpath.length(), uri.length());
		String perm=null;
		for(String key:this.perms.keySet()){
			if(key.lastIndexOf("**")==(key.length()-2)){
				String u1 = key.substring(0, key.length()-2);
				if(ur.indexOf(u1)==0){
					perm=key;
					break;
				}
			}else{
				if(ur.equals(key)){
					perm=key;
					break;
				}
			}
		}
		if(perm!=null){
			if(session.getAttribute("user")==null){
				response.sendRedirect(contextpath+login);
			}else{
				//判断该登录用户是否有权限，如果有权限则跳转，如果没有权限，则跳转到无权限提示页面
				session.setAttribute("barid","home");
				session.setAttribute("funname", "欢迎访问");
		    	session.setAttribute("fundesc", "");
				@SuppressWarnings("unchecked")
				Map<String,String> user_perms = (Map<String, String>) session.getAttribute("user_perms");
				if(user_perms!=null&&user_perms.containsKey(perm)){
					if(!perm.equals("/index.html")){
						 ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(context);  
					     FunctionDao functionDao = (FunctionDao) ctx.getBean("functionDao");
					     Function function =functionDao.findByHomepage(ur);
					     if(function!=null){
					    	 session.setAttribute("barid", function.getNavbar().getId());
					    	 session.setAttribute("funid", function.getId());
					    	 session.setAttribute("barname", function.getNavbar().getName());
					    	 session.setAttribute("funname", function.getName());
					    	 session.setAttribute("fundesc", function.getDescription());
					     }
					}
					chain.doFilter(request, response);
				}else{
					response.sendRedirect(contextpath+unauthorized);
				}
			}
		}else{
			chain.doFilter(request, response);
		}
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		logger.info("CoreFilter Init!");
		if(config.getInitParameter("loginUrl")!=null){
			this.login = config.getInitParameter("loginUrl");
		}
		if(config.getInitParameter("unauthorizedUrl")!=null){
			this.unauthorized = config.getInitParameter("unauthorizedUrl");
		}
		ServletContext context = config.getServletContext();  
        ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(context);  
        FunctionDao permissionDao = (FunctionDao) ctx.getBean("functionDao");
        List<Function> permissions = permissionDao.findAll();
        for(Function p:permissions){
        	perms.put(p.getUri(), p.getName());
        }
        perms.put("/index.html", "主页");
        perms.put("/personal.html", "个人设置");
	}
}	

package cn.edu.xiyou.office;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ppt")
public class PptController {
	
	@RequestMapping("/index.ppt")
	public String index(HttpServletResponse response,Map<String, Object> map){
		response.setContentType("application/vnd.ms-powerpoint");
		map.put("xm", "张三");
		map.put("xy", "我是标题");
		//application/msword
		return "classes/cn/edu/xiyou/office/ppt";
	}
}

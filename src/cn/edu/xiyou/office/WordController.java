package cn.edu.xiyou.office;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/word")
public class WordController {
	
	@RequestMapping("/index.doc")
	public String index(HttpServletResponse response,Map<String, Object> map){
		response.setContentType("application/msword");
		map.put("xy", "经济与管理学院");
		map.put("nj", "2015");
		map.put("xh", "201500101");
		map.put("xm", "张三");
		//application/msword
		return "classes/cn/edu/xiyou/office/word";
	}
}

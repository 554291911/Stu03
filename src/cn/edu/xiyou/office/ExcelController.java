package cn.edu.xiyou.office;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/excel")
public class ExcelController {
	
	@RequestMapping("/index.xls")
	public String index(HttpServletResponse response,Map<String, Object> map){
		response.setContentType("application/vnd.ms-excel");
		map.put("xm", "张三");
		//application/msword
		return "classes/cn/edu/xiyou/office/excel";
	}
}

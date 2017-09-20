package cn.edu.xiyou.stuinfo;

import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.edu.xiyou.dao.CreditDao;
import cn.edu.xiyou.entity.Credit;
import cn.edu.xiyou.entity.Student;

@Controller
@RequestMapping("/stuinfo")
public class StuinfoController {

	@Resource
	CreditDao creditDao;

	@RequestMapping("/index.html")
	public String index(Map<String, Object> map, HttpServletRequest request)throws IOException {
		Student student;
		try {
			student = (Student) request.getSession().getAttribute("user");
			Integer fs = creditDao.findFS(student.getSnumber());
			if(fs==null){
				fs=0;
			}
			fs=100+fs;
			Integer cf = creditDao.findFScf(student.getSnumber());
			Integer jl = creditDao.findFSjl(student.getSnumber());
			if(cf==null){
				cf=0;
			}
			if(jl==null){
				jl=0;
			}
			List<Credit> credits = creditDao.findBySnumberAndStatus(new Sort(Direction.DESC,"lrsj"),student.getSnumber(),"通过");
			StuinfoView view = new StuinfoView();
			view.setName(student.getName());
			view.setSex(student.getSex());
			view.setEthnic(student.getEthnic());
			view.setCollege(student.getCollege());
			view.setMajor(student.getMajor());
			view.setClasses(student.getClasses());
			view.setSnumber(student.getSnumber());
			view.setAddress(student.getAddress());
			view.setCredits(credits);
			map.put("student", view);
			map.put("fs", fs);
			map.put("cf", cf);
			map.put("jl", jl);
		} catch (ClassCastException e) {
			StuinfoView view = new StuinfoView();
			map.put("student", view);
			map.put("fs", 0);
			map.put("cf", 0);
			map.put("jl", 0);
		}
		return "classes/cn/edu/xiyou/stuinfo/index";
	}
	@RequestMapping("/ss.do")
	public void ss(String ssid,String ssly,Writer writer) throws IOException{
		Credit credit = creditDao.findOne(ssid);
		credit.setSsly(ssly);
		credit.setSszt("申诉中");
		credit.setSssj(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		creditDao.saveAndFlush(credit);
		writer.append("ok");
		writer.flush();
		writer.close();
	}
}
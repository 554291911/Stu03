package cn.edu.xiyou.stucx;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.edu.xiyou.dao.CollegeDao;
import cn.edu.xiyou.dao.MajorDao;
import cn.edu.xiyou.dao.StudentDao;
import cn.edu.xiyou.entity.College;
import cn.edu.xiyou.entity.Major;
import cn.edu.xiyou.entity.Student;
import cn.edu.xiyou.student.StudentView;
import cn.edu.xiyou.student.ViewMajor;
import cn.edu.xiyou.sys.dao.FileDao;

import com.alibaba.fastjson.JSON;

/**
 * 学生信息维护
 * @author lzp
 *
 */
@Controller
@RequestMapping("/stucx")
public class StudentCxController {

	@Resource
	private StudentDao studentDao;

	@Resource
	private CollegeDao collegeDao;

	@Resource
	private MajorDao majorDao;
	
	@Resource
	private FileDao fileDao;

	@RequestMapping("/index.html")
	public String index(Map<String, Object> map, HttpServletRequest request) {
		map.put("year", fileDao.findAll());
		map.put("college", collegeDao.findAll());
		return "classes/cn/edu/xiyou/stucx/index";
	}

	/** 显示学生信息表 */
	@RequestMapping("/load.do")
	public void load(HttpServletRequest request, HttpServletResponse response,String nj,String xy) throws IOException {
		System.out.println("年级:"+nj+"/"+"学院"+xy); 
		Map<String, Object> map = new HashMap<String, Object>();
		response.setCharacterEncoding("utf8");
		Writer writer = response.getWriter();
		List<StudentView> views = new ArrayList<StudentView>();
		if(xy==null || nj==null){
			map.put("data", views);
			writer.append(JSON.toJSONString(map));
			writer.flush();
			writer.close();
		}else{
			for (Student student : studentDao.findByYearAndCollege(nj,collegeDao.findOne(xy).getName())) {
				StudentView view = new StudentView();
				view.setId(student.getId());
				view.setUniversity(student.getUniversity());
				view.setCollege(student.getCollege());
				view.setName(student.getName());
				view.setMajor(student.getMajor());
				view.setYear(student.getYear());
				view.setClasses(student.getClasses());
				view.setAddress(student.getAddress());
				view.setSex(student.getSex());
				view.setEthnic(student.getEthnic());
				view.setSnumber(student.getSnumber());
				views.add(view);
			}
			map.put("data", views);
			writer.append(JSON.toJSONString(map));
			writer.flush();
			writer.close();
		}
	}
	@RequestMapping("/getMajors.do")
	public void getMajor(String collegeid, Writer writer) throws IOException {
		String s = "error";
		if (collegeid != null && !collegeid.equals("")) {
			College college = collegeDao.findOne(collegeid);
			if (college != null) {
				List<ViewMajor> views = new ArrayList<ViewMajor>();
				for (Major major : majorDao.findByCollege(college)) {
					ViewMajor view = new ViewMajor();
					view.setId(major.getId());
					view.setName(major.getName());
					views.add(view);
				}
				s = JSON.toJSONString(views);
			}
		}
		System.out.println(s);
		writer.append(s);
		writer.flush();
		writer.close();
	}
}

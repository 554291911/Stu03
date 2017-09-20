package cn.edu.xiyou.college;

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
import cn.edu.xiyou.entity.College;
import cn.edu.xiyou.entity.Major;

import com.alibaba.fastjson.JSON;

/**
 * 学院信息维护
 * 
 * @author lzp
 *
 */
@Controller
@RequestMapping("/college")
public class CollegeController {

	@Resource
	private CollegeDao collegeDao;

	@Resource
	private MajorDao majorDao;

	@RequestMapping("index.html")
	public String index() {
		return "classes/cn/edu/xiyou/college/index";
	}

	@RequestMapping("/load.do")
	public void load(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		response.setCharacterEncoding("utf8");
		Writer writer = response.getWriter();
		List<CollegeView> views = new ArrayList<CollegeView>();
		for (College college : collegeDao.findAll()) {
			CollegeView view = new CollegeView();
			view.setId(college.getId());
			view.setName(college.getName());
			view.setCz("<a href=\"javascript:;\" class=\"btn btn-circle btn-sm blue\" onclick=\"updCollage('"
					+ college.getId()
					+ "')\"><i class=\"fa fa-edit\"></i>修改</a>"
					+ "<a href=\"javascript:;\" class=\"btn btn-circle btn-sm red\" onclick=\"delCollage('"
					+ college.getId()
					+ "')\"><i class=\"fa fa-remove\"></i>删除</a>");
			views.add(view);
		}
		map.put("data", views);
		writer.append(JSON.toJSONString(map));
		writer.flush();
		writer.close();
	}

	@RequestMapping("/loadMajor.do")
	public void loadMajor(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		response.setCharacterEncoding("utf8");
		Writer writer = response.getWriter();
		List<MajorView> views = new ArrayList<MajorView>();
		for (Major major : majorDao.findAll()) {
			MajorView view = new MajorView();
			view.setId(major.getId());
			view.setMajor(major.getName());
			view.setCollege(major.getCollege().getName());
			view.setCz("<a href=\"javascript:;\" class=\"btn btn-circle btn-sm blue\" onclick=\"updmajor('"
					+ major.getId()
					+ "')\"><i class=\"fa fa-edit\"></i>修改</a>"
					+ "<a href=\"javascript:;\" class=\"btn btn-circle btn-sm red\" onclick=\"delmajor('"
					+ major.getId()
					+ "')\"><i class=\"fa fa-remove\"></i>删除</a>");
			views.add(view);
		}
		map.put("data", views);
		writer.append(JSON.toJSONString(map));
		writer.flush();
		writer.close();
	}

	@RequestMapping("/addcollege.do")
	public void addcollege(College college, Writer writer) throws IOException {
		String s = "ok";
		if (college.getName() != null && !college.getName().equals("")) {
			if (collegeDao.findByName(college.getName()) == null) {
				college.setId();
				collegeDao.saveAndFlush(college);
			} else
				s = "该学院已经存在！";
		} else {
			s = "学院名称不能为空!";
		}
		writer.append(s);
		writer.flush();
		writer.close();
	}

	@RequestMapping("/getCollege.do")
	public void getCollege(String id, Writer writer) throws IOException {
		String s = "error";
		if (id != null && !id.equals("")) {
			College college = collegeDao.findOne(id);
			if (college != null) {
				s = JSON.toJSONString(college);
			}
		}
		System.out.println(s);
		writer.append(s);
		writer.flush();
		writer.close();
	}
	@RequestMapping("/getMajorOne.do")
	public void getMajorOne(String id, Writer writer) throws IOException {
		String s = "error";
		if (id != null && !id.equals("")) {
			Major major = majorDao.findOne(id);
			if (major != null) {
				s = JSON.toJSONString(major);
			}
		}
		System.out.println(s);
		writer.append(s);
		writer.flush();
		writer.close();
	}
	@RequestMapping("/editcollege.do")
	public void editcollege(String id, String name, Writer writer)
			throws IOException {
		String s = "ok";
		if (id != null && !id.equals("")) {
			College college = collegeDao.findOne(id);
			if (college != null) {
				college.setName(name);
				collegeDao.saveAndFlush(college);
			} else {
				s = "学院不存在！";
			}
		} else {
			s = "请选择学院!";
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
			College college = collegeDao.findOne(id);
			if (college != null) {
				collegeDao.delete(id);
			}
		} else {
			s = "error";
		}
		return s;
	}

	@RequestMapping("/getMajor.do")
	public void getMajor(String id, Writer writer) throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		List<MajorView> views = new ArrayList<MajorView>();
		if (id != null && !id.equals("")) {
			College college = collegeDao.findOne(id);
			if (college != null) {
				for (Major major : college.getMajors()) {
					MajorView view = new MajorView();
					view.setMajor(major.getName());
					view.setId(major.getId());
					view.setCollege(college.getName());
					view.setCz("<a href=\"javascript:;\" class=\"btn btn-circle btn-sm blue\" onclick=\"updmajor('"
							+ major.getId()
							+ "')\"><i class=\"fa fa-edit\"></i>修改</a>"
							+ "<a href=\"javascript:;\" class=\"btn btn-circle btn-sm red\" onclick=\"delmajor('"
							+ major.getId()
							+ "')\"><i class=\"fa fa-remove\"></i>删除</a>");
					views.add(view);
				}
			}
		}
		map.put("data", views);
		writer.append(JSON.toJSONString(map));
		writer.flush();
		writer.close();
	}

	@RequestMapping("/addmajor.do")
	public void addMajor(College college, Major major, Writer writer)
			throws IOException {
		String s = "ok";
		if (college != null && !college.getId().equals("")
				&& major.getName() != null && !major.getName().equals("")) {
			if (majorDao.findByName(major.getName()).isEmpty()) {
				major.setId();
				major.setCollege(college);
				majorDao.saveAndFlush(major);
			} else
				s = "该专业已经存在！";
		} else {
			s = "专业名称不能为空!";
		}
		writer.append(s);
		writer.flush();
		writer.close();
	}

	@RequestMapping("/delMajor.do")
	@ResponseBody
	public String delMajor(String id) {
		String s = "ok";
		if (id != null && !id.trim().equals("")) {
			Major major = majorDao.findOne(id);
			if (major != null) {
				majorDao.delete(major);
			}
		} else {
			s = "error";
		}
		return s;
	}
	@RequestMapping("/editMajor.do")
	public void editMajor(String id, String name, Writer writer)throws IOException {
		String s = "ok";
		if (id != null && !id.equals("")) {
			Major major = majorDao.findOne(id);
			if (major != null) {
				major.setName(name);
				majorDao.saveAndFlush(major);
			} else {
				s = "专业不存在！";
			}
		} else {
			s = "请选择专业!";
		}
		writer.append(s);
		writer.flush();
		writer.close();
	}
}

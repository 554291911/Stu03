package cn.edu.xiyou.fileupload;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import cn.edu.xiyou.dao.StudentDao;
import cn.edu.xiyou.entity.Student;
import cn.edu.xiyou.sys.dao.FileDao;
import cn.edu.xiyou.sys.entity.User;

import com.alibaba.fastjson.JSON;

@Controller
@RequestMapping("/fileupload")
public class FileUploadController {

	private String uploader;// 上传人
	private Date uploadDate;// 上传日期
	private Long downloadcount = 0L;// 下载次数
	private Long fileSize;// 文件大小

	@Resource
	FileDao fileDao;

	@Resource
	StudentDao studentDao;

	@RequestMapping("index.html")
	public String index() {
		return "classes/cn/edu/xiyou/fileupload/index";
	}

	@RequestMapping("/upload.do")
	@ResponseBody
	public void upload(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String s = "ok";//前端页面显示的信息
		Map<String, Object> map = new HashMap<String, Object>();
		response.setCharacterEncoding("utf8");//设置编码
		Writer writer = response.getWriter();
		//上传地址
		String logoRealPathDir = request.getSession().getServletContext().getRealPath("/upload");
		System.out.println("上传地址为 :" + logoRealPathDir);
		// 创建文件存放目录
		File folder = new File(logoRealPathDir);
		if (!folder.exists())
			folder.mkdirs();
		// 创建一个通用的多部分解析器
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if (multipartResolver.isMultipart(request) == false) {
			s = "文件不存在请重新上传";
			map.put("data", s);
			writer.append(JSON.toJSONString(map));
		}
		// 判断 request 是否有文件上传,即多部分请求
		if (multipartResolver.isMultipart(request)) {
			// 转换成多部分request
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			// 取得request中的所有文件名
			Iterator<String> iter = multiRequest.getFileNames();
			while (iter.hasNext()) {
				// 取得上传文件
				MultipartFile file = multiRequest.getFile(iter.next());
				if (file != null) {
					// 取得当前上传文件的文件名称
					String fileName = file.getOriginalFilename();
					System.out.println(fileName);
					StringBuffer displayName = new StringBuffer(fileName);
					// 如果名称不为“”,说明该文件存在，否则说明该文件不存在
					if (!(fileName.trim().equals(""))) {
						String type = fileName.substring(fileName.lastIndexOf(".") + 1);
						String name = fileName.substring(0, fileName.lastIndexOf("."));
						Integer nj=null;
						try {
							nj=Integer.parseInt(name);
						} catch (Exception e1) {
							
						}
						if (!type.equals("xls")) {
							s = "请使用模板或按照模板的方式上传";
							map.put("data", s);
							writer.append(JSON.toJSONString(map));
						} else if(name.length()!=4|| nj==null){
							s = "请使用年级做为文件名,例如:2015.xls";
							map.put("data", s);
							writer.append(JSON.toJSONString(map));
						}else if(!fileDao.findByFilenameContaining(fileName).isEmpty()){
							s = name+"级的学生信息已经上传,不能重复上传!";
							map.put("data", s);
							writer.append(JSON.toJSONString(map));
						}else {
							Integer sum = fileDao.findHaved(fileName);
							File localFile;
							if (sum != 0) {
								displayName.delete(0, displayName.length());
								String str = fileName.substring(0, fileName.lastIndexOf("."));
								displayName.append(str);
								displayName.append('(');
								displayName.append(sum);
								displayName.append(").");
								displayName.append(type);
								localFile = new File(logoRealPathDir + "/" + displayName);
							}
							else
							// 定义上传路径
							localFile = new File(logoRealPathDir + "/" + fileName);
							// 上传
							file.transferTo(localFile);
							// 在system中保存文件信息
							User user = (User) request.getSession().getAttribute("user");
							uploader = user.getName();
							Sheet sheet;
							Workbook book;
							int rows = 0;
							try {
								book = Workbook.getWorkbook(localFile);
								Sheet[] sheets = book.getSheets();
								for (int j = 0; j < sheets.length; j++) {
									sheet = book.getSheet(j);
									rows += sheet.getRows() - 1;
									if(rows < 0)
										rows = 2;
									for (int i = 1; i < rows; i++) {
										Cell[] cells = sheet.getRow(i);
										for (int k = 0; k < cells.length; k++) {
											if (cells[k].getContents() == "")
												throw new Exception();
										}
									}
								}
							} catch (Exception e) {
								e.printStackTrace();
								s = "文件内容不符合导入要求，请使用模板或按照模板的方式上传";
								map.put("data", s);
								writer.append(JSON.toJSONString(map));
								return;
							}
							fileSize = file.getSize();
							uploadDate = new Date(System.currentTimeMillis());
							cn.edu.xiyou.sys.entity.File excel = new cn.edu.xiyou.sys.entity.File(fileName,
									displayName.toString(), logoRealPathDir, type, uploader, uploadDate, downloadcount,
									fileSize, "未导入", rows, 0,name);
							try {
								fileDao.save(excel);
							} catch (Exception e) {
								e.printStackTrace();
								s = "请不要同时上传多个同名文件";
								map.put("data", s);
								writer.append(JSON.toJSONString(map));
								return;
							}
							map.put("data", s);
							writer.append(JSON.toJSONString(map));
						}
					}
				}
			}
		}
		writer.flush();
		writer.close();
	}

	/** 显示文件信息表格 */
	@RequestMapping("/load.do")
	public void load(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		response.setCharacterEncoding("utf8");
		Writer writer = response.getWriter();
		List<FileView> views = new ArrayList<FileView>();
		for (cn.edu.xiyou.sys.entity.File file : fileDao.findAll()) {
			FileView view = new FileView();
			view.setId(file.getId());
			view.setDownloadcount(file.getDownloadcount());
			view.setFilename(file.getDisplayName());
			view.setFilepath(file.getFilepath());
			view.setCount(file.getCount() + "条");
			view.setSum(file.getSum() + "条");
			String fileSize;
			if (file.getFilesize() >= 1048576) {
				fileSize = file.getFilesize() / 1048576 + "MB";
			} else if (file.getFilesize() > 1024 && file.getFilesize() < 1048576) {
				fileSize = file.getFilesize() / 1024 + "KB";
			} else {
				fileSize = file.getFilesize() + "字节";
			}
			view.setFilesize(fileSize);
			view.setFiletype(file.getFiletype());
			view.setUploader(file.getUploader());
			view.setYear(file.getYear()+"级");
			Date date = file.getUploaddate();
			String uploaddate = date.toLocaleString();
			view.setUploaddate(uploaddate);
			if (file.getFlag().equals("未导入")) {
				view.setFlag("<span class=\"label label-sm label-danger\">未导入 </span>");
				view.setCz("<a href=\"javascript:;\" class=\"btn btn-circle btn-sm purple\" onclick=\"push('"
						+ file.getId() + "')\"><i class=\"fa fa-user\"></i>导入</a>"
						+ "<a href=\"javascript:;\" class=\"btn btn-circle btn-sm red\" onclick=\"del('" + file.getId()
						+ "')\"><i class=\"fa fa-remove\"></i>删除</a>");
			} else {
				view.setFlag("<span class=\"label label-sm label-success\">已导入 </span>");
				view.setCz("<a href=\"javascript:;\" class=\"btn btn-circle btn-sm red\" onclick=\"del('" + file.getId()
						+ "')\"><i class=\"fa fa-remove\"></i>删除</a>");
			}
			views.add(view);
		}
		map.put("data", views);
		writer.append(JSON.toJSONString(map));
		writer.flush();
		writer.close();

	}

	@RequestMapping("/del.do")
	@ResponseBody
	public String del(String id) {
		String s = "ok";
		if (id != null && !id.trim().equals("")) {
			cn.edu.xiyou.sys.entity.File file = fileDao.findById(id);
			StringBuffer sb = new StringBuffer();
			sb.append(file.getFilepath());
			sb.append("\\");
			sb.append(file.getFilename());
			new File(sb.toString()).delete();
			String year = file.getYear();
			studentDao.delete(studentDao.findByYear(year));
			fileDao.delete(id);
			// 删除服务器文件
		} else {
			s = "error";
		}
		return s;
	}

	@RequestMapping("/push.do")
	@ResponseBody
	public String push(String id, HttpServletResponse response) throws UnsupportedEncodingException {
		if (id != null && !id.equals("")) {
			// Excel导入数据库
			cn.edu.xiyou.sys.entity.File system_file = fileDao.findById(id);// system_file
			StringBuffer sb = new StringBuffer();
			sb.append(system_file.getFilepath());
			sb.append("\\");
			sb.append(system_file.getFilename());
			java.io.File file = new File(sb.toString());// java.io.File
			String year = system_file.getYear();
			Sheet sheet;
			Workbook book;
			try {
				book = Workbook.getWorkbook(file);
				Sheet[] sheets = book.getSheets();
				int rows = 0;
				for (int j = 0; j < sheets.length; j++) {
					// 获得工作表对象(ecxel中sheet的编号从0开始,0,1,2,3,....)
					sheet = book.getSheet(j);
					// 得到行数
					rows += sheet.getRows() - 1;
					// Student集合
					Set<Student> set = new HashSet<Student>();
					// 查出所有的学号
					ArrayList<String> snumbers = studentDao.findSnumbers();
					// 判断是否存在这个学生的变量
					boolean result = false;
					// 生成对象
					for (int i = 1; i <= rows; i++) {
						Cell[] cells = sheet.getRow(i);
						result = binarySearch(snumbers, cells[6].getContents());
						if (result)
							continue;
						Student student = new Student();
						student.setUniversity(cells[0].getContents());
						student.setCollege(cells[1].getContents());
						student.setMajor(cells[2].getContents());
						student.setYear(year);
						student.setClasses(cells[3].getContents());
						student.setSnumber(cells[4].getContents());
						student.setName(cells[5].getContents());
						student.setSex(cells[6].getContents());
						student.setAddress(cells[7].getContents());
						student.setEthnic(cells[8].getContents());
						student.setPassword("123456");
						set.add(student);
					}
					if (!set.isEmpty())
						studentDao.save(set);
					system_file.setFlag("已导入");
					system_file.setCount(set.size());
					system_file.setSum(rows);
					fileDao.save(system_file);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return "error";
			}
		} else {
			return "error";
		}
		return "ok";
	}

	private boolean binarySearch(ArrayList<String> snumbers, String string) {
		int low = 0;
		int length = snumbers.size();
		int high = length - 1;
		while (low <= high) {
			int middle = (low + high) / 2;
			if (string.equals(snumbers.get(middle))) {
				return true;
			} else if (string.compareTo(snumbers.get(middle)) < 0) {
				high = middle - 1;
			} else {
				low = middle + 1;
			}
		}
		return false;
	}
}

package cn.edu.xiyou.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.edu.xiyou.entity.Student;

public interface StudentDao extends JpaRepository<Student, String>{
	public List<Student> findByCollege(String college);
	
	public ArrayList<String> findSnumbers();
	
	public Student findBySnumber(String snumber);
	
	public Student findBySnumberAndPassword(String snumber, String password);
	
	public List<Student> findByYear(String year);
	public List<Student> findByYearAndCollege(String year,String college);
}

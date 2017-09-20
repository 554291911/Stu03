package cn.edu.xiyou.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.edu.xiyou.entity.College;
import cn.edu.xiyou.entity.Major;

public interface MajorDao extends JpaRepository<Major, String>{
	public List<Major> findByName(String name);
	public Major findById(String id);
	public List<Major> findByCollege(College college);

}

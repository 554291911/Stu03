package cn.edu.xiyou.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.edu.xiyou.entity.College;

public interface CollegeDao extends JpaRepository<College, String>{

	public College findByName(String name);
	
}

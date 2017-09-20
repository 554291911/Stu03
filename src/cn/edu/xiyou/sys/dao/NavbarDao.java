package cn.edu.xiyou.sys.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import cn.edu.xiyou.sys.entity.Navbar;

@Repository
public interface NavbarDao extends JpaRepository<Navbar, String>{
	public Navbar findByName(String name);
	public Navbar findById(String id);
	public Page<Navbar> findAll(Pageable pageable);
	@Query("select count(*) from Navbar")
	public Long getTotal();
}

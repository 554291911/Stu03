package cn.edu.xiyou.sys.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import cn.edu.xiyou.sys.entity.Navbar;
import cn.edu.xiyou.sys.entity.Function;

public interface FunctionDao extends JpaRepository<Function, String> {
	public List<Function> findAll();

	public Page<Function> findAll(Pageable pageable);

	public List<Function> findByName(String name);

	public Function findById(String id);

	public List<Function> findByNavbar(Navbar navbar);

	public List<Function> findByNavbarOrderBySorderAsc(Navbar navbar);
	
	public Function findByHomepage(String homepage);
	
	public Function findByUri(String uri);
}

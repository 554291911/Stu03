package cn.edu.xiyou.sys.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import cn.edu.xiyou.sys.entity.Roles;

@Repository
public interface RolesDao extends JpaRepository<Roles, String> {
	public List<Roles> findAll();

	public Page<Roles> findAll(Pageable pageable);

	// public Page<Roles> findByName(String name,Pageable pageable);
	public List<Roles> findByName(String name);

	public Page<Roles> findByNameContaining(Pageable pageable, String name);

	public Roles findById(String id);

	@Query("select count(*) from Roles")
	public Long getTotal();
}

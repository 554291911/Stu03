package cn.edu.xiyou.sys.dao;

import java.util.List;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cn.edu.xiyou.entity.College;
import cn.edu.xiyou.sys.entity.Roles;
import cn.edu.xiyou.sys.entity.User;
@Repository
public interface UserDao extends JpaRepository<User, String> {
	public List<User> findByUsernameAndPassword(String username, String password);
	public List<User> findByUsername(String username);
	public User findById(String id);
	public Page<User> findAll(Pageable pageable);
	public Page<User> findByUsernameContaining(Pageable pageable,String username);
	public Page<User> findByNameContaining(Pageable pageable,String name);
	public Page<User> findByNameContainingOrUsernameContaining(Pageable pageable,String name,String username);
	public List<User> findByUsernameContainingOrNameContaining(String username, String name);
	public List<User> findByName(String name);
	public List<User> findByRoles(Roles roles);
	public List<User> findByRolesIsNull();
	public List<User> findByCollegeAndRoles(College college, Roles roles);
	
//	@NamedQuery(name = "User.findCollegeLeader", query = "select username from system_users where college = ? and roles_id = '2'")

//	public String findCollegeLeader(String college);
}

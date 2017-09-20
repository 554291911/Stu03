package cn.edu.xiyou.dao;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cn.edu.xiyou.entity.Credit;

public interface CreditDao extends JpaRepository<Credit, String>{
	
	public List<Credit> findBySnumber(Sort sort,String snumber);
	public List<Credit> findBySnumberAndStatus(Sort sort,String snumber,String status);
	public List<Credit> findByLrryUsername(String username);
	public List<Credit> findByCollege(String college);
	
	public List<Credit> findByStatus(String status);
	public List<Credit> findByStatusAndCollege(String status,String collge);
	public List<Credit> findBySszt(String sszt);
	public List<Credit> findBySsztAndCollege(String sszt,String collge);
	@Query("select sum(fs) from Credit where snumber=?1 and sszt<>'通过' and status='通过'")
	public Integer findFS(String snumber);
	
	@Query("select sum(fs) from Credit where snumber=?1 and fs<0 and sszt<>'通过' and status='通过'")
	public Integer findFScf(String snumber);
	
	@Query("select sum(fs) from Credit where snumber=?1 and fs>0 and sszt<>'通过' and status='通过'")
	public Integer findFSjl(String snumber);
}

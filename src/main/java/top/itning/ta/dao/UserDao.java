package top.itning.ta.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import top.itning.ta.entity.User;

/**
 * USER Table Dao
 *
 * @author wangn
 */
public interface UserDao extends JpaRepository<User, String> {
}

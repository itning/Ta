package top.itning.ta.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import top.itning.ta.entity.Clazz;

/**
 * CLASS Table Dao
 *
 * @author wangn
 */
public interface ClazzDao extends JpaRepository<Clazz, String> {
}

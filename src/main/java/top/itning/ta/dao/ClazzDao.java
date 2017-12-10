package top.itning.ta.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import top.itning.ta.entity.Clazz;

import java.util.List;

/**
 * CLASS Table Dao
 *
 * @author wangn
 */
public interface ClazzDao extends JpaRepository<Clazz, String> {
    /**
     * 根据班级名查找班级
     *
     * @param clzz 班级名
     * @return 班级集合
     */
    List<Clazz> findByClazz(String clzz);

    /**
     * 根据教师名查找班级
     *
     * @param teacher 教师名
     * @return 班级集合
     */
    List<Clazz> findByTeacher(String teacher);
}

package top.itning.ta.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import top.itning.ta.entity.Clazz;
import top.itning.ta.entity.StudentInfo;

import java.util.List;

/**
 * STU_INFO Table Dao
 *
 * @author wangn
 */
public interface StudentInfoDao extends JpaRepository<StudentInfo, String> {
    /**
     * 根据班级查找所有学生信息
     *
     * @param clazz 要查找的班级
     * @return 学生信息集合
     */
    List<StudentInfo> findAllByClazz(Clazz clazz);

    /**
     * 根据ID查找img字段
     *
     * @param id 学生ID
     * @return 学生头像信息
     */
    @Query("select s.img from StudentInfo s where s.id=?1")
    String findimgByID(String id);

    /**
     * 根据班级计算学生数量
     *
     * @param clazz 班级
     * @return 该班级学生数量
     */
    int countAllByClazz(Clazz clazz);

    /**
     * 根据班级删除学生信息
     *
     * @param clazz 班级
     */
    void deleteAllByClazz(Clazz clazz);

    /**
     * 根据学生姓名查询学生信息(like %name%)
     *
     * @param name 学生姓名
     * @return 学生信息集合
     */
    List<StudentInfo> findByNameContaining(String name);
}

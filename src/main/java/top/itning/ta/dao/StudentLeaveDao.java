package top.itning.ta.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import top.itning.ta.entity.StudentInfo;
import top.itning.ta.entity.StudentLeave;

import java.util.List;

/**
 * STU_LEAVE Table Dao
 *
 * @author wangn
 */
public interface StudentLeaveDao extends JpaRepository<StudentLeave, String>, JpaSpecificationExecutor<StudentLeave> {
    /**
     * 根据学生ID删除所有请假信息
     *
     * @param studentInfo 要删除的学生
     */
    void deleteAllBySid(StudentInfo studentInfo);

    /**
     * 查找用户查询请假信息
     *
     * @param uname 用户名
     * @return 请假信息集合
     */
    List<StudentLeave> findByUname(String uname);

    /**
     * 根据用户名记算请假数量
     *
     * @param uname 用户名
     * @return 数量
     */
    long countByUname(String uname);
}

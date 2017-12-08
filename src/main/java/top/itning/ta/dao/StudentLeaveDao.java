package top.itning.ta.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import top.itning.ta.entity.StudentInfo;
import top.itning.ta.entity.StudentLeave;

/**
 * STU_LEAVE Table Dao
 *
 * @author wangn
 */
public interface StudentLeaveDao extends JpaRepository<StudentLeave, String> {
    /**
     * 根据学生ID删除所有请假信息
     *
     * @param studentInfo 要删除的学生
     */
    void deleteAllBySid(StudentInfo studentInfo);
}

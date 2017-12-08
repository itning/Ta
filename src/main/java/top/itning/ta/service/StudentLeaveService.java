package top.itning.ta.service;

import top.itning.ta.entity.StudentLeave;
import top.itning.ta.exception.DataNotFindException;
import top.itning.ta.exception.NullParameterException;

import java.util.List;

/**
 * 请假信息管理服务
 *
 * @author Ning
 */
public interface StudentLeaveService {
    /**
     * 获得所有学生的请假信息
     *
     * @return 所有学生请假信息
     */
    List<StudentLeave> getAllStudentLeave();

    /**
     * 根据ID删除请假信息
     *
     * @param id 要删除的请假ID
     * @throws DataNotFindException 如果该ID的请假信息没有找到则抛出该异常
     */
    void delStudentLeaveByID(String id) throws DataNotFindException;

    /**
     * 添加学生请假信息方法
     *
     * @param studentLeave 学生请假信息
     * @throws NullParameterException 如果学生请假信息有字段为空则抛出该异常
     */
    void addStudentLeaveInfo(StudentLeave studentLeave) throws NullParameterException;
}

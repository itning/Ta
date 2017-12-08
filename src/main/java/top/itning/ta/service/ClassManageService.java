package top.itning.ta.service;

import top.itning.ta.entity.Clazz;
import top.itning.ta.exception.DataNotFindException;
import top.itning.ta.exception.NullParameterException;

import java.util.List;

/**
 * 班级信息管理服务
 *
 * @author wangn
 */
public interface ClassManageService {
    /**
     * 获取所有班级信息
     *
     * @return 班级信息集合
     */
    List<Clazz> getAllClassInfo();

    /**
     * 根据班级ID获取该班级学生数量
     *
     * @param id 班级ID
     * @return 该班级学生数量
     */
    int getStudentNumByClassID(String id);

    /**
     * 添加班级信息
     *
     * @param clazz 班级信息
     * @throws NullParameterException 如果班级信息有字段为空则抛出该异常
     */
    void addClassInfo(Clazz clazz) throws NullParameterException;

    /**
     * 根基班级ID删除班级信息
     *
     * @param id 班级ID
     * @throws DataNotFindException 如果该班级没有找到则抛出该异常
     */
    void delClassInfo(String id) throws DataNotFindException;
}

package top.itning.ta.service;

import top.itning.ta.entity.Clazz;

import java.util.List;

/**
 * 框架初始化服务
 *
 * @author wangn
 */
public interface FrameWorkService {
    /**
     * 获取所有Clazz试题
     *
     * @return 班级集合
     */
    List<Clazz> getAllClazzInfo();
}

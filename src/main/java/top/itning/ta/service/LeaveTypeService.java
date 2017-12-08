package top.itning.ta.service;

import top.itning.ta.entity.LeaveType;

import java.util.List;

/**
 * 学生请假类型服务
 *
 * @author Ning
 */
public interface LeaveTypeService {
    /**
     * 获取所有请假类型信息
     *
     * @return 请假类型集合
     */
    List<LeaveType> getAllLeaveType();
}

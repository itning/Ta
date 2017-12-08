package top.itning.ta.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.itning.ta.dao.LeaveTypeDao;
import top.itning.ta.entity.LeaveType;
import top.itning.ta.service.LeaveTypeService;

import javax.transaction.Transactional;
import java.util.List;

/**
 * 学生请假类型服务实现类
 *
 * @author Ning
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class LeaveTypeServiceImpl implements LeaveTypeService {
    private final LeaveTypeDao leaveTypeDao;

    @Autowired
    public LeaveTypeServiceImpl(LeaveTypeDao leaveTypeDao) {
        this.leaveTypeDao = leaveTypeDao;
    }

    @Override
    public List<LeaveType> getAllLeaveType() {
        return leaveTypeDao.findAll();
    }
}

package top.itning.ta.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(LeaveTypeServiceImpl.class);

    private final LeaveTypeDao leaveTypeDao;

    @Autowired
    public LeaveTypeServiceImpl(LeaveTypeDao leaveTypeDao) {
        this.leaveTypeDao = leaveTypeDao;
    }

    @Override
    public List<LeaveType> getAllLeaveType() {
        List<LeaveType> leaveTypeList = leaveTypeDao.findAll();
        logger.debug("getAllLeaveType::获取到请假类型数->" + leaveTypeList.size());
        return leaveTypeList;
    }
}

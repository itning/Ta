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

    /**
     * 默认请假类型数量,该字段用于判断自动生成是否成功
     */
    private static final int DEFAUT_LEAVE_TYPE_NUM = 3;

    private final LeaveTypeDao leaveTypeDao;

    @Autowired
    public LeaveTypeServiceImpl(LeaveTypeDao leaveTypeDao) {
        this.leaveTypeDao = leaveTypeDao;
        if (leaveTypeDao.count() == 0) {
            logger.info("LeaveTypeServiceImpl::请假类型信息不存在,将自动生成3种请假类型");
            LeaveType leaveType1 = new LeaveType();
            leaveType1.setId("1");
            leaveType1.setName("事假");
            LeaveType leaveType2 = new LeaveType();
            leaveType2.setId("2");
            leaveType2.setName("病假");
            LeaveType leaveType3 = new LeaveType();
            leaveType3.setId("3");
            leaveType3.setName("旷课");
            leaveTypeDao.saveAndFlush(leaveType1);
            leaveTypeDao.saveAndFlush(leaveType2);
            leaveTypeDao.saveAndFlush(leaveType3);
            if (leaveTypeDao.count() == DEFAUT_LEAVE_TYPE_NUM) {
                logger.info("LeaveTypeServiceImpl::已自动生成3种请假类型,并保存");
            } else {
                logger.warn("LeaveTypeServiceImpl::保存失败!!!");
            }
        }
    }

    @Override
    public List<LeaveType> getAllLeaveType() {
        List<LeaveType> leaveTypeList = leaveTypeDao.findAll();
        logger.debug("getAllLeaveType::获取到请假类型数->" + leaveTypeList.size());
        return leaveTypeList;
    }
}

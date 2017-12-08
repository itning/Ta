package top.itning.ta.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.itning.ta.dao.StudentLeaveDao;
import top.itning.ta.entity.StudentLeave;
import top.itning.ta.exception.DataNotFindException;
import top.itning.ta.exception.NullParameterException;
import top.itning.ta.service.StudentLeaveService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

/**
 * 请假信息服务实现类
 *
 * @author Ning
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class StudentLeaveServiceImpl implements StudentLeaveService {
    private final StudentLeaveDao studentLeaveDao;

    @Autowired
    public StudentLeaveServiceImpl(StudentLeaveDao studentLeaveDao) {
        this.studentLeaveDao = studentLeaveDao;
    }

    @Override
    public List<StudentLeave> getAllStudentLeave() {
        return studentLeaveDao.findAll();
    }

    @Override
    public void delStudentLeaveByID(String id) throws DataNotFindException {
        if (!studentLeaveDao.exists(id)) {
            throw new DataNotFindException("请假ID:" + id + "没有找到");
        }
        studentLeaveDao.delete(id);
    }

    @Override
    public void addStudentLeaveInfo(StudentLeave studentLeave) throws NullParameterException {
        if (studentLeave.getId() == null) {
            studentLeave.setId(UUID.randomUUID().toString().replace("-", ""));
        }
        if (studentLeave.getOften() == null) {
            throw new NullParameterException("参数often为空");
        }
        if (studentLeave.getMatter() == null) {
            throw new NullParameterException("参数Matter为空");
        }
        if (studentLeave.getSid() == null) {
            throw new NullParameterException("参数sid为空");
        }
        if (studentLeave.getStarttime() == null) {
            throw new NullParameterException("参数starttime为空");
        }
        if (studentLeave.getReason() == null) {
            throw new NullParameterException("参数reason为空");
        }
        studentLeaveDao.saveAndFlush(studentLeave);
    }
}

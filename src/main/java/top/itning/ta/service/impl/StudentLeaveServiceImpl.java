package top.itning.ta.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.itning.ta.dao.ClazzDao;
import top.itning.ta.dao.LeaveTypeDao;
import top.itning.ta.dao.StudentInfoDao;
import top.itning.ta.dao.StudentLeaveDao;
import top.itning.ta.entity.SearchLeave;
import top.itning.ta.entity.StudentInfo;
import top.itning.ta.entity.StudentLeave;
import top.itning.ta.exception.DataNotFindException;
import top.itning.ta.exception.NullParameterException;
import top.itning.ta.service.StudentLeaveService;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

    private final StudentInfoDao studentInfoDao;

    private final LeaveTypeDao leaveTypeDao;

    private final ClazzDao clazzDao;

    @Autowired
    public StudentLeaveServiceImpl(StudentLeaveDao studentLeaveDao, StudentInfoDao studentInfoDao, LeaveTypeDao leaveTypeDao, ClazzDao clazzDao) {
        this.studentLeaveDao = studentLeaveDao;
        this.studentInfoDao = studentInfoDao;
        this.leaveTypeDao = leaveTypeDao;
        this.clazzDao = clazzDao;
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

    @Override
    public List<StudentLeave> searchStudentLeaveInfo(SearchLeave searchLeave) {
        return studentLeaveDao.findAll((root, query, cb) -> {
            List<Predicate> list = new ArrayList<>();
            if (StringUtils.isNoneBlank(searchLeave.getKey())) {
                if (NumberUtils.isParsable(searchLeave.getKey())) {
                    list.add(cb.equal(root.<StudentInfo>get("sid"), studentInfoDao.findOne(searchLeave.getKey())));
                } else {
                    List<Predicate> predicateList = new ArrayList<>();
                    List<StudentInfo> byNameContaining = studentInfoDao.findByNameContaining(searchLeave.getKey());
                    byNameContaining.forEach(studentInfo -> predicateList.add(cb.equal(root.get("sid"), studentInfo)));
                    Predicate[] p = new Predicate[predicateList.size()];
                    list.add(cb.or(predicateList.toArray(p)));
                }
            }

            //班级
            if (searchLeave.getClazz() != null) {
                List<Predicate> predicateList = new ArrayList<>();
                for (int i = 0; i < searchLeave.getClazz().length; i++) {
                    List<StudentInfo> allByClazz = studentInfoDao.findAllByClazz(clazzDao.findOne(searchLeave.getClazz()[i]));
                    allByClazz.forEach(studentInfo -> predicateList.add(cb.equal(root.get("sid"), studentInfo)));
                }
                Predicate[] p = new Predicate[predicateList.size()];
                list.add(cb.or(predicateList.toArray(p)));
            }

            //请假类型
            if (searchLeave.getMatter() != null) {
                Predicate[] p = new Predicate[searchLeave.getMatter().length];
                for (int i = 0; i < searchLeave.getMatter().length; i++) {
                    p[i] = cb.equal(root.get("matter"), leaveTypeDao.getOne(searchLeave.getMatter()[i]));
                }
                list.add(cb.or(p));
            }

            //请假时常often
            if (searchLeave.getOften() != null) {
                boolean hadMore3 = false;
                for (String s : searchLeave.getOften()) {
                    if ("-1".equals(s)) {
                        hadMore3 = true;
                        break;
                    }
                }
                Predicate[] p;
                if (hadMore3) {
                    p = new Predicate[searchLeave.getOften().length + 1];
                    p[searchLeave.getOften().length] = cb.greaterThan(root.get("often"),3);
                } else {
                    p = new Predicate[searchLeave.getOften().length];
                }
                for (int i = 0; i < searchLeave.getOften().length; i++) {
                    p[i] = cb.equal(root.get("often"), searchLeave.getOften()[i]);
                }
                list.add(cb.or(p));
            }

            //时间区间
            if (searchLeave.getStartdate() != null || searchLeave.getEnddate() != null) {
                //有开始有结束
                if (searchLeave.getStartdate() != null && searchLeave.getEnddate() != null) {
                    list.add(cb.between(root.get("starttime"), searchLeave.getStartdate(), searchLeave.getEnddate()));
                } else {
                    Date minDate = null;
                    Date maxDate = null;
                    try {
                        minDate = new SimpleDateFormat("yyyy-MM-dd").parse("2005-01-01");
                        maxDate = new SimpleDateFormat("yyyy-MM-dd").parse("9999-12-31");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    //只有开始时间
                    if (searchLeave.getStartdate() != null) {
                        list.add(cb.between(root.get("starttime"), searchLeave.getStartdate(), maxDate));
                    } else {//只有结束时间
                        list.add(cb.between(root.get("starttime"), minDate, searchLeave.getEnddate()));
                    }
                }
            }
            Predicate[] p = new Predicate[list.size()];
            return cb.and(list.toArray(p));
        });
    }
}

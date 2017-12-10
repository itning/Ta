package top.itning.ta.service.impl;

import org.apache.commons.lang3.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import top.itning.ta.dao.ClazzDao;
import top.itning.ta.dao.StudentInfoDao;
import top.itning.ta.dao.StudentLeaveDao;
import top.itning.ta.entity.Clazz;
import top.itning.ta.entity.User;
import top.itning.ta.exception.DataNotFindException;
import top.itning.ta.exception.NullParameterException;
import top.itning.ta.service.ClassManageService;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 班级信息管理服务实现类
 *
 * @author wangn
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class ClassManageServiceImpl implements ClassManageService {
    private static final Logger logger = LoggerFactory.getLogger(ClassManageServiceImpl.class);

    private final ClazzDao clazzDao;

    private final StudentInfoDao studentInfoDao;

    private final StudentLeaveDao studentLeaveDao;

    @Autowired
    public ClassManageServiceImpl(ClazzDao clazzDao, StudentInfoDao studentInfoDao, StudentLeaveDao studentLeaveDao) {
        this.clazzDao = clazzDao;
        this.studentInfoDao = studentInfoDao;
        this.studentLeaveDao = studentLeaveDao;
    }

    @Override
    public List<Clazz> getAllClassInfo() {
        if (ClassUtils.isAssignable(SecurityContextHolder.getContext().getAuthentication().getPrincipal().getClass(), User.class)) {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (user.getName() != null) {
                logger.info("getAllClassInfo::获取到的登陆教师->" + user.getName());
                List<Clazz> clazzList = clazzDao.findByTeacher(user.getName());
                logger.debug("getAllClassInfo::获取到的班级数量" + clazzList.size());
                return clazzList;
            }
        }
        return new ArrayList<>();
    }

    @Override
    public int getStudentNumByClassID(String id) {
        logger.debug("getStudentNumByClassID::开始统计班级人数");
        return studentInfoDao.countAllByClazz(clazzDao.findOne(id));
    }

    @Override
    public void addClassInfo(Clazz clazz) throws NullParameterException {
        clazz.setId(UUID.randomUUID().toString().replace("-", ""));
        if (ClassUtils.isAssignable(SecurityContextHolder.getContext().getAuthentication().getPrincipal().getClass(), User.class)) {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (user.getName() != null) {
                logger.info("addClassInfo::获取到的登陆教师->" + user.getName());
                clazz.setTeacher(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getName());
            } else {
                logger.warn("addClassInfo::获取到的登陆教师为空");
                throw new NullParameterException("登陆用户名为空");
            }
        } else {
            logger.warn("addClassInfo::类转换出现异常");
            throw new ClassCastException("类转换出现异常");
        }
        if (clazz.getClazz() == null) {
            throw new NullParameterException("参数clazz为空");
        }
        if (clazz.getCollege() == null) {
            throw new NullParameterException("参数college为空");
        }
        if (clazz.getProfession() == null) {
            throw new NullParameterException("参数profession为空");
        }
        clazzDao.saveAndFlush(clazz);
    }

    @Override
    public void delClassInfo(String id) throws DataNotFindException {
        if (!clazzDao.exists(id)) {
            logger.warn("delClassInfo::班级ID" + id + "没有找到");
            throw new DataNotFindException("班级ID" + id + "没有找到");
        }
        logger.debug("delClassInfo::开始删除请假信息");
        studentInfoDao.findAllByClazz(clazzDao.findOne(id)).forEach(studentLeaveDao::deleteAllBySid);
        logger.debug("delClassInfo::开始删除学生信息");
        studentInfoDao.deleteAllByClazz(clazzDao.findOne(id));
        logger.debug("delClassInfo::开始班级信息");
        clazzDao.delete(id);
    }
}

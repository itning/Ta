package top.itning.ta.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;
import top.itning.ta.dao.ClazzDao;
import top.itning.ta.dao.StudentInfoDao;
import top.itning.ta.dao.StudentLeaveDao;
import top.itning.ta.entity.Clazz;
import top.itning.ta.entity.StudentInfo;
import top.itning.ta.exception.DataNotFindException;
import top.itning.ta.exception.NullParameterException;
import top.itning.ta.service.StudentInfoService;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 学生信息服务实现类
 *
 * @author wangn
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class StudentInfoServiceImpl implements StudentInfoService {
    @Value("${spring.http.multipart.location}")
    private String uploadPath;

    private final StudentInfoDao studentInfoDao;

    private final ClazzDao clazzDao;

    private final StudentLeaveDao studentLeaveDao;

    @Autowired
    public StudentInfoServiceImpl(StudentInfoDao studentInfoDao, ClazzDao clazzDao, StudentLeaveDao studentLeaveDao) {
        this.studentInfoDao = studentInfoDao;
        this.clazzDao = clazzDao;
        this.studentLeaveDao = studentLeaveDao;
    }

    @Override
    public List<StudentInfo> getAllStudentInfoByClass(String clazz) throws DataNotFindException {
        Clazz clazzInfo = clazzDao.findOne(clazz);
        if (clazzInfo == null) {
            throw new DataNotFindException("班级ID:" + clazz + "没有找到");
        }
        return studentInfoDao.findAllByClazz(clazzInfo);
    }

    @Override
    public StudentInfo getOneStudentInfoByID(String id) throws DataNotFindException {
        if (!studentInfoDao.exists(id)) {
            throw new DataNotFindException("ID:" + id + "的学生没有找到");
        }
        return studentInfoDao.getOne(id);
    }

    @Override
    public Clazz getClassInfoByID(String id) throws DataNotFindException {
        Clazz clazz = clazzDao.findOne(id);
        if (clazz == null) {
            throw new DataNotFindException("班级ID:" + id + "没有找到");
        }
        return clazz;
    }

    @Override
    public void addStudentInfo(StudentInfo studentInfo, MultipartFile file) throws NullParameterException {
        if (studentInfo.getAddress() == null) {
            throw new NullParameterException("参数address为空");
        }
        if (studentInfo.getBirthday() == null) {
            throw new NullParameterException("参数birthday为空");
        }
        if (studentInfo.getCollege() == null) {
            throw new NullParameterException("参数college为空");
        }
        if (studentInfo.getHtel() == null) {
            throw new NullParameterException("参数htel为空");
        }
        if (studentInfo.getTel() == null) {
            throw new NullParameterException("参数tel为空");
        }
        if (studentInfo.getId() == null) {
            throw new NullParameterException("参数id为空");
        }
        if (studentInfo.getName() == null) {
            throw new NullParameterException("参数name为空");
        }
        if (studentInfo.getProfession() == null) {
            throw new NullParameterException("参数profession为空");
        }
        if (studentInfo.getTeacher() == null) {
            throw new NullParameterException("参数teacher为空");
        }
        if (studentInfo.getIntime() == null) {
            throw new NullParameterException("参数intime为空");
        }
        //有上传文件
        if (!file.isEmpty()) {
            //获取文件扩展名
            String extensionName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            //用学号作为文件名
            String newFileName = studentInfo.getId() + extensionName;
            studentInfo.setImg(newFileName);
            File newFile = new File(uploadPath + newFileName);
            try {
                file.transferTo(newFile);
            } catch (IOException e) {
                //回滚
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
        } else {
            if (studentInfoDao.exists(studentInfo.getId())) {
                String img = studentInfoDao.findimgByID(studentInfo.getId());
                if (img != null) {
                    studentInfo.setImg(img);
                }
            }
        }
        studentInfoDao.saveAndFlush(studentInfo);
    }

    @Override
    public void delStudentInfo(String id) throws DataNotFindException {
        if (!studentInfoDao.exists(id)) {
            throw new DataNotFindException("ID:" + id + "不存在");
        }
        //先删除请假信息
        studentLeaveDao.deleteAllBySid(studentInfoDao.getOne(id));
        studentInfoDao.delete(id);
    }
}

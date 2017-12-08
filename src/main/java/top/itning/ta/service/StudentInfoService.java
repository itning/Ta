package top.itning.ta.service;

import org.springframework.web.multipart.MultipartFile;
import top.itning.ta.entity.Clazz;
import top.itning.ta.entity.StudentInfo;
import top.itning.ta.exception.DataNotFindException;
import top.itning.ta.exception.NullParameterException;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * 学生信息管理服务
 *
 * @author wangn
 */
public interface StudentInfoService {
    /**
     * 根据班级ID获取所有学生信息
     *
     * @param clazz 班级id
     * @return 该班级所有学生信息
     * @throws DataNotFindException 如果所查找的数据没有找到抛出该异常
     */
    List<StudentInfo> getAllStudentInfoByClass(String clazz) throws DataNotFindException;

    /**
     * 根据学生ID获取学生信息
     *
     * @param id 学生ID
     * @return 学生实体
     * @throws DataNotFindException 该ID学生没有找到则抛出该异常
     */
    StudentInfo getOneStudentInfoByID(String id) throws DataNotFindException;

    /**
     * 根据班级ID获取班级实体
     *
     * @param id 班级ID
     * @return 班级信息
     * @throws DataNotFindException 没有找到班级时抛出此异常
     */
    Clazz getClassInfoByID(String id) throws DataNotFindException;

    /**
     * 添加学生信息
     *
     * @param studentInfo 学生信息
     * @param file        学生头像文件
     * @throws NullParameterException 实体类有空参数时抛出该异常
     */
    void addStudentInfo(StudentInfo studentInfo, MultipartFile file) throws NullParameterException;


    /**
     * 根据ID删除学生信息
     *
     * @param id 学生ID
     * @throws DataNotFindException 学生ID不存在则抛出该异常
     */
    void delStudentInfo(String id) throws DataNotFindException;

    /**
     * 根据学生ID批量生成EXCEL文件生成
     *
     * @param servletOutputStream servlet输出流
     * @param id                  学生ID
     * @throws DataNotFindException 如果学生ID未找到则抛出该异常
     * @throws IOException          文件操作出错抛出该异常
     */
    void downStudentInfo(ServletOutputStream servletOutputStream, String... id) throws DataNotFindException, IOException;

    /**
     * 使用Excel文件添加学生信息
     *
     * @param file Excel文件
     * @throws NullParameterException 文件为空则抛出该异常
     * @throws DataNotFindException   班级没有找到则抛出该异常
     * @throws IOException            文件读写失败抛出该异常
     */
    void addStudentInfoByExcel(MultipartFile file) throws NullParameterException, DataNotFindException, IOException;
}

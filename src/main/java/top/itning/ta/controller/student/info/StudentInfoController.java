package top.itning.ta.controller.student.info;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.itning.ta.entity.ServerMessage;
import top.itning.ta.entity.StudentInfo;
import top.itning.ta.exception.DataNotFindException;
import top.itning.ta.exception.JsonDataNotFindException;
import top.itning.ta.exception.NullParameterException;
import top.itning.ta.service.ClassManageService;
import top.itning.ta.service.SettingService;
import top.itning.ta.service.StudentInfoService;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 学生信息控制器
 *
 * @author wangn
 */
@Controller
@RequestMapping("/studentInfo")
public class StudentInfoController {
    private static final Logger logger = LoggerFactory.getLogger(StudentInfoController.class);

    private final StudentInfoService studentInfoService;

    private final ClassManageService classManageService;

    private final SettingService settingService;

    @Autowired
    public StudentInfoController(StudentInfoService studentInfoService, ClassManageService classManageService, SettingService settingService) {
        this.studentInfoService = studentInfoService;
        this.classManageService = classManageService;
        this.settingService = settingService;
    }

    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        binder.registerCustomEditor(
                Date.class,
                new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
    }

    /**
     * 根据班级ID显示所有学生信息
     *
     * @param classID 班级ID
     * @return 学生信息集合
     * @throws JsonDataNotFindException 如果班级没有找到则抛出该异常
     */
    @GetMapping("/show/class/{classID}")
    @ResponseBody
    public List<StudentInfo> showStudentInfo(@PathVariable("classID") String classID) throws JsonDataNotFindException {
        logger.debug("showStudentInfo::获取到的班级ID->" + classID);
        try {
            return studentInfoService.getAllStudentInfoByClass(classID);
        } catch (DataNotFindException e) {
            throw new JsonDataNotFindException(e.getExceptionMessage());
        }
    }

    /**
     * 显示学生详情方法
     *
     * @param model 模型
     * @param id    学生ID
     * @return student.html
     * @throws DataNotFindException 该学生ID没有找到则抛出该异常
     */
    @GetMapping("/show/student/{id}")
    public String showOneStudentInfo(Model model, @PathVariable("id") String id) throws DataNotFindException {
        logger.debug("showOneStudentInfo::获取到的学生ID->" + id);
        model.addAttribute("studentInfo", studentInfoService.getOneStudentInfoByID(id));
        model.addAttribute("classList", classManageService.getAllClassInfo());
        logger.debug("showOneStudentInfo::studentInfo/classList添加完成");
        return "student";
    }

    /**
     * 使用WEB页面添加学生
     *
     * @param model 模型
     * @param id    班级ID
     * @return addstudent.html
     * @throws DataNotFindException 该班级没有找到则抛出该异常
     */
    @PreAuthorize("hasAnyAuthority('A','B')")
    @GetMapping("/add/web/{id}")
    public String addStudentByWeb(Model model, @PathVariable("id") String id) throws DataNotFindException {
        logger.debug("addStudentByWeb::获取到要添加学生的班级ID->" + id);
        model.addAttribute("classInfo", studentInfoService.getClassInfoByID(id));
        model.addAttribute("classList", classManageService.getAllClassInfo());
        model.addAttribute("themeColor", settingService.getThemeColor());
        model.addAttribute("themeColorAccent", settingService.getThemeColorAccent());
        model.addAttribute("hasStudent", classManageService.hasStudent());
        logger.debug("addStudentByWeb::classInfo/classList添加完成");
        return "addstudent";
    }

    /**
     * 添加学生信息
     *
     * @param studentInfo 学生信息实体类
     * @param file        文件
     * @return 重定向到主页(添加哪个班就显示哪个班学生)
     * @throws DataNotFindException   如果数据没有找到抛出该异常
     * @throws NullParameterException 如果有参数为空抛出该异常
     */
    @PreAuthorize("hasAnyAuthority('A','B')")
    @PostMapping("/add")
    public String addStudentInfo(StudentInfo studentInfo, @RequestParam("file") MultipartFile file) throws DataNotFindException, NullParameterException {
        logger.debug("addStudentInfo::file为空->" + file.isEmpty());
        studentInfoService.addStudentInfo(studentInfo, file);
        return "redirect:/#/" + studentInfo.getClazz().getId();
    }

    /**
     * 删除学生信息
     *
     * @param id 学生ID
     * @return json 消息
     */
    @PreAuthorize("hasAuthority('A')")
    @GetMapping("/del/student/{id}")
    @ResponseBody
    public ServerMessage delStudentInfo(@PathVariable("id") String id) {
        logger.debug("delStudentInfo::删除学生的ID->" + id);
        ServerMessage serverMessage = new ServerMessage();
        serverMessage.setMsg("学生ID:" + id + "删除成功");
        serverMessage.setCode(ServerMessage.SUCCESS_CODE);
        try {
            studentInfoService.delStudentInfo(id);
        } catch (DataNotFindException e) {
            logger.warn("delStudentInfo::删除ID为" + id + "失败,消息->" + e.getExceptionMessage());
            serverMessage.setCode(ServerMessage.NOT_FIND);
            serverMessage.setMsg(e.getExceptionMessage());
        }
        serverMessage.setUrl("/studentInfo/del/student/" + id);
        return serverMessage;
    }

    /**
     * 修改学生信息
     *
     * @param studentInfo 学生信息
     * @param file        学生头像
     * @return 重定向到该生详情页
     * @throws NullParameterException StudentInfo实体类有空参数时抛出该异常
     */
    @PreAuthorize("hasAnyAuthority('A','B')")
    @PostMapping("/modify/student")
    public String modifyStudentInfo(StudentInfo studentInfo, @RequestParam("file") MultipartFile file) throws NullParameterException {
        logger.debug("modifyStudentInfo::修改学生ID->" + studentInfo.getId() + "file为空->" + file.isEmpty());
        studentInfoService.addStudentInfo(studentInfo, file);
        return "redirect:/studentInfo/show/student/" + studentInfo.getId();
    }

    /**
     * 下载学生信息
     *
     * @param id       学生ID用 - 符号分割ID
     * @param response HttpServletResponse
     * @throws IOException          文件操作失败抛出该异常
     * @throws DataNotFindException 该学生ID没有找到则抛出该异常
     */
    @PreAuthorize("hasAnyAuthority('A','B')")
    @GetMapping("/down")
    public void downStudentInfoListByClass(String id, HttpServletResponse response) throws IOException, DataNotFindException {
        logger.debug("downStudentInfoListByClass::要下载的学生ID->" + id);
        String[] idArray = StringUtils.split(id, "-");
        for (String i : idArray) {
            if (!NumberUtils.isParsable(i)) {
                logger.warn("downStudentInfoListByClass::错误参数->" + i);
                throw new IllegalArgumentException("参数不正确->" + i);
            }
        }
        String nowTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(("学生信息" + nowTime + ".xlsx").getBytes(), "ISO-8859-1"));
        ServletOutputStream outputStream = response.getOutputStream();
        logger.debug("downStudentInfoListByClass::outputStream.isReady->" + outputStream.isReady());
        studentInfoService.downStudentInfo(outputStream, idArray);
        outputStream.flush();
        outputStream.close();
        logger.debug("downStudentInfoListByClass::outputStream close");
    }

    /**
     * 上传学生信息通过Excel文件
     *
     * @param file 文件
     * @return 重定向到主页
     * @throws NullParameterException 文件不存在则抛出该异常
     * @throws DataNotFindException   班级没有找到则抛出该异常
     * @throws IOException            文件操作异常抛出
     */
    @PreAuthorize("hasAnyAuthority('A','B')")
    @PostMapping("/upExcelFile")
    public String upExcelFile(@RequestParam("file") MultipartFile file) throws NullParameterException, DataNotFindException, IOException {
        if (file.isEmpty()) {
            logger.warn("upExcelFile::file为空");
            throw new NullParameterException("file为空");
        }
        studentInfoService.addStudentInfoByExcel(file);
        return "redirect:/";
    }
}

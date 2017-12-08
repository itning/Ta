package top.itning.ta.controller.student.info;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.itning.ta.entity.ServerMessage;
import top.itning.ta.entity.StudentInfo;
import top.itning.ta.exception.DataNotFindException;
import top.itning.ta.exception.NullParameterException;
import top.itning.ta.service.ClassManageService;
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
    private final StudentInfoService studentInfoService;

    private final ClassManageService classManageService;

    @Autowired
    public StudentInfoController(StudentInfoService studentInfoService, ClassManageService classManageService) {
        this.studentInfoService = studentInfoService;
        this.classManageService = classManageService;
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
     * @throws NullParameterException 如果参数为空则抛出该异常
     * @throws DataNotFindException   如果班级没有找到则抛出该异常
     */
    @RequestMapping(value = "/show/class/{classID}", method = RequestMethod.GET)
    @ResponseBody
    public List<StudentInfo> showStudentInfo(@PathVariable("classID") String classID) throws NullParameterException, DataNotFindException {
        if (classID == null) {
            throw new NullParameterException("参数:classID为空");
        }
        return studentInfoService.getAllStudentInfoByClass(classID);
    }

    /**
     * 显示学生详情方法
     *
     * @param model 模型
     * @param id    学生ID
     * @return student.html
     * @throws DataNotFindException 该学生ID没有找到则抛出该异常
     */
    @RequestMapping(value = "/show/student/{id}", method = RequestMethod.GET)
    public String showOneStudentInfo(Model model, @PathVariable("id") String id) throws DataNotFindException {
        model.addAttribute("studentInfo", studentInfoService.getOneStudentInfoByID(id));
        model.addAttribute("classList", classManageService.getAllClassInfo());
        return "student";
    }

    /**
     * 使用WEB页面添加学生
     *
     * @param model 模型
     * @param id    班级ID
     * @return addstudent.html
     * @throws NullParameterException 如果班级ID为空则抛出该异常
     * @throws DataNotFindException   该班级没有找到则抛出该异常
     */
    @RequestMapping(value = "/add/web/{id}", method = RequestMethod.GET)
    public String addStudentByWeb(Model model, @PathVariable("id") String id) throws NullParameterException, DataNotFindException {
        if (id == null) {
            throw new NullParameterException("参数ID为空");
        }
        model.addAttribute("classInfo", studentInfoService.getClassInfoByID(id));
        model.addAttribute("classList", classManageService.getAllClassInfo());
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
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addStudentInfo(StudentInfo studentInfo, @RequestParam("file") MultipartFile file) throws DataNotFindException, NullParameterException {
        studentInfoService.addStudentInfo(studentInfo, file);
        return "redirect:/#/" + studentInfo.getClazz().getId();
    }

    /**
     * 删除学生信息
     *
     * @param id 学生ID
     * @return json 消息
     */
    @RequestMapping(value = "/del/student/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ServerMessage delStudentInfo(@PathVariable("id") String id) {
        ServerMessage serverMessage = new ServerMessage();
        serverMessage.setMsg("学生ID:" + id + "删除成功");
        serverMessage.setCode(ServerMessage.SUCCESS_CODE);
        try {
            studentInfoService.delStudentInfo(id);
        } catch (DataNotFindException e) {
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
    @RequestMapping(value = "/modify/student", method = RequestMethod.POST)
    public String modifyStudentInfo(StudentInfo studentInfo, @RequestParam("file") MultipartFile file) throws NullParameterException {
        studentInfoService.addStudentInfo(studentInfo, file);
        System.out.println(studentInfo);
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
    @RequestMapping(value = "/down", method = RequestMethod.GET)
    public void downStudentInfoListByClass(String id, HttpServletResponse response) throws IOException, DataNotFindException {
        String[] idArray = StringUtils.split(id, "-");
        for (String i : idArray) {
            if (!NumberUtils.isParsable(i)) {
                throw new IllegalArgumentException("参数不正确->" + i);
            }
        }
        String nowTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(("学生信息" + nowTime + ".xlsx").getBytes(), "ISO-8859-1"));
        ServletOutputStream outputStream = response.getOutputStream();
        studentInfoService.downStudentInfo(outputStream, idArray);
        outputStream.flush();
        outputStream.close();
    }
}

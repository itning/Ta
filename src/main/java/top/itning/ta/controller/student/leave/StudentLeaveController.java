package top.itning.ta.controller.student.leave;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;
import top.itning.ta.entity.SearchLeave;
import top.itning.ta.entity.ServerMessage;
import top.itning.ta.entity.StudentLeave;
import top.itning.ta.exception.DataNotFindException;
import top.itning.ta.exception.NullParameterException;
import top.itning.ta.service.ClassManageService;
import top.itning.ta.service.LeaveTypeService;
import top.itning.ta.service.StudentInfoService;
import top.itning.ta.service.StudentLeaveService;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 学生请假控制器
 *
 * @author Ning
 */
@Controller
@RequestMapping("/studentLeave")
public class StudentLeaveController {
    private static final Logger logger = LoggerFactory.getLogger(StudentLeaveController.class);

    private final StudentLeaveService studentLeaveService;

    private final ClassManageService classManageService;

    private final LeaveTypeService leaveTypeService;

    private final StudentInfoService studentInfoService;

    @Autowired
    public StudentLeaveController(StudentLeaveService studentLeaveService, ClassManageService classManageService, LeaveTypeService leaveTypeService, StudentInfoService studentInfoService) {
        this.studentLeaveService = studentLeaveService;
        this.classManageService = classManageService;
        this.leaveTypeService = leaveTypeService;
        this.studentInfoService = studentInfoService;
    }

    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        binder.registerCustomEditor(
                Date.class,
                new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
    }

    /**
     * 历史请假信息
     *
     * @param model 模型
     * @return historyleave.html
     */
    @PreAuthorize("hasAnyAuthority('A','B')")
    @RequestMapping(value = "/show", method = RequestMethod.GET)
    public String getAllStudentLeaveInfo(Model model) {
        List<StudentLeave> studentLeaveList = studentLeaveService.getAllStudentLeave();
        if (studentLeaveList.size() == 0) {
            logger.info("getAllStudentLeaveInfo::学生请假信息为空,重定向到添加页面");
            return "redirect:/studentLeave/add";
        }
        model.addAttribute("studentLeaveList", studentLeaveList);
        model.addAttribute("classList", classManageService.getAllClassInfo());
        logger.debug("getAllStudentLeaveInfo::studentLeaveList/classList添加完成");
        return "historyleave";
    }

    /**
     * 根据请假ID删除请假信息
     *
     * @param id 请假ID
     * @return 服务器消息Json
     */
    @PreAuthorize("hasAuthority('A')")
    @RequestMapping(value = "/del/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ServerMessage delStudentLeaveInfo(@PathVariable("id") String id) {
        logger.debug("delStudentLeaveInfo::要删除的ID->" + id);
        ServerMessage serverMessage = new ServerMessage();
        serverMessage.setCode(ServerMessage.SUCCESS_CODE);
        serverMessage.setMsg("ID:" + id + "删除成功");
        try {
            studentLeaveService.delStudentLeaveByID(id);
        } catch (DataNotFindException e) {
            logger.warn("delStudentLeaveInfo::ID->" + id + "没找到,删除失败");
            serverMessage.setCode(ServerMessage.SERVICE_ERROR);
            serverMessage.setMsg(e.getExceptionMessage());
        }
        serverMessage.setUrl("/studentLeave/del/" + id);
        return serverMessage;
    }

    /**
     * 添加学生信息页面
     *
     * @param model 模型
     * @return addleave.html
     */
    @PreAuthorize("hasAnyAuthority('A','B')")
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String addStudentLeaveInfoRoute(Model model) {
        model.addAttribute("leaveTypeList", leaveTypeService.getAllLeaveType());
        model.addAttribute("classList", classManageService.getAllClassInfo());
        model.addAttribute("studentLeaveNum", studentLeaveService.getStudentLeaveNum());
        logger.debug("addStudentLeaveInfoRoute::leaveTypeList/classList添加完成");
        return "addleave";
    }

    /**
     * 添加请假信息
     *
     * @param studentLeave 学生请假信息实体
     * @param sid          学生ID
     * @return 重定向到历史请假信息
     * @throws NullParameterException 如果studentLeave有参数为空则抛出该异常
     * @throws DataNotFindException   如果学生ID没有找到则抛出该异常
     */
    @PreAuthorize("hasAnyAuthority('A','B')")
    @RequestMapping(value = "/addLeave", method = RequestMethod.POST)
    public String addStudentLeaveInfo(StudentLeave studentLeave, String sid) throws NullParameterException, DataNotFindException {
        if (sid == null) {
            logger.warn("addStudentLeaveInfo:;未获取到sid");
            throw new NullParameterException("参数sid为空");
        }
        studentLeave.setSid(studentInfoService.getOneStudentInfoByID(sid));
        studentLeaveService.addStudentLeaveInfo(studentLeave);
        return "redirect:/studentLeave/show";
    }

    /**
     * 搜索请假信息页面
     *
     * @param model 模型
     * @return searchleave.html
     */
    @PreAuthorize("hasAnyAuthority('A','B')")
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String searchStudentLeave(Model model) {
        if (studentLeaveService.getStudentLeaveNum() == 0) {
            logger.info("searchStudentLeave::学生请假信息为空,重定向到添加页面");
            return "redirect:/studentLeave/add";
        }
        model.addAttribute("classList", classManageService.getAllClassInfo());
        model.addAttribute("leaveTypeList", leaveTypeService.getAllLeaveType());
        logger.debug("searchStudentLeave::leaveTypeList/classList添加完成");
        return "searchleave";
    }

    /**
     * 搜索请假信息
     *
     * @param searchLeave 搜索请假信息
     * @return 搜索的学生请假信息集合
     */
    @PreAuthorize("hasAnyAuthority('A','B')")
    @RequestMapping(value = "/searchLeave", method = RequestMethod.GET)
    @ResponseBody
    public List<StudentLeave> searchStudentLeaveInfo(SearchLeave searchLeave) {
        logger.debug("searchStudentLeaveInfo::开始搜索");
        return studentLeaveService.searchStudentLeaveInfo(searchLeave);
    }

}

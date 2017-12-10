package top.itning.ta.controller.clazz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import top.itning.ta.entity.Clazz;
import top.itning.ta.entity.ServerMessage;
import top.itning.ta.exception.DataNotFindException;
import top.itning.ta.exception.NullParameterException;
import top.itning.ta.service.ClassManageService;
import top.itning.ta.service.SettingService;
import top.itning.ta.service.StudentLeaveService;

import java.util.List;

/**
 * 班级管理控制器
 *
 * @author wangn
 */
@Controller
@PreAuthorize("hasAuthority('A')")
@RequestMapping("/class")
public class ClassManageController {
    private static final Logger logger = LoggerFactory.getLogger(ClassManageController.class);

    private final ClassManageService classManageService;

    private final StudentLeaveService studentLeaveService;

    private final SettingService settingService;

    @Autowired
    public ClassManageController(ClassManageService classManageService, StudentLeaveService studentLeaveService, SettingService settingService) {
        this.classManageService = classManageService;
        this.studentLeaveService = studentLeaveService;
        this.settingService = settingService;
    }

    /**
     * 显示所有班级信息
     *
     * @param model 模型
     * @return classset.html
     */
    @GetMapping("/show")
    public String showClassInfo(Model model) {
        List<Clazz> allClassInfo = classManageService.getAllClassInfo();
        logger.debug("showClassInfo::班级集合信息获取完成");
        if (allClassInfo != null) {
            allClassInfo.forEach(clazz -> clazz.setNum(classManageService.getStudentNumByClassID(clazz.getId())));
            model.addAttribute("classList", allClassInfo);
            logger.debug("showClassInfo::添加classList完成,集合大小->" + allClassInfo.size());
        }
        model.addAttribute("studentLeaveNum", studentLeaveService.getStudentLeaveNum());
        model.addAttribute("themeColor", settingService.getThemeColor());
        model.addAttribute("themeColorAccent", settingService.getThemeColorAccent());
        return "classset";
    }

    /**
     * 添加班级信息
     *
     * @param clazz 班级信息
     * @return 服务器消息Json
     */
    @GetMapping("/add")
    @ResponseBody
    public ServerMessage addClassInfo(Clazz clazz) {
        ServerMessage serverMessage = new ServerMessage();
        serverMessage.setCode(ServerMessage.SUCCESS_CODE);
        serverMessage.setMsg("添加成功");
        try {
            classManageService.addClassInfo(clazz);
        } catch (NullParameterException e) {
            logger.warn("addClassInfo::添加失败->" + e.getExceptionMessage());
            serverMessage.setCode(ServerMessage.SERVICE_ERROR);
            serverMessage.setMsg(e.getExceptionMessage());
        }
        serverMessage.setUrl("/class/add");
        return serverMessage;
    }

    /**
     * 删除班级信息
     *
     * @param id 班级ID
     * @return 服务器消息json
     */
    @GetMapping("/del/{id}")
    @ResponseBody
    public ServerMessage delClassInfo(@PathVariable("id") String id) {
        logger.debug("delClassInfo::要删除的班级ID->" + id);
        ServerMessage serverMessage = new ServerMessage();
        serverMessage.setCode(ServerMessage.SUCCESS_CODE);
        serverMessage.setMsg("删除成功");
        try {
            classManageService.delClassInfo(id);
        } catch (DataNotFindException e) {
            logger.warn("addClassInfo::删除失败->" + e.getExceptionMessage());
            serverMessage.setCode(ServerMessage.SERVICE_ERROR);
            serverMessage.setMsg(e.getExceptionMessage());
        }
        serverMessage.setUrl("/class/del/" + id);
        return serverMessage;
    }
}

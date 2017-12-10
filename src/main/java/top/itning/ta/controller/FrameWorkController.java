package top.itning.ta.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import top.itning.ta.entity.Clazz;
import top.itning.ta.service.ClassManageService;
import top.itning.ta.service.StudentLeaveService;

import java.util.List;

/**
 * 框架初始化控制器
 *
 * @author wangn
 */
@Controller
public class FrameWorkController {
    private static final Logger logger = LoggerFactory.getLogger(FrameWorkController.class);

    private final ClassManageService classManageService;

    private final StudentLeaveService studentLeaveService;

    @Autowired
    public FrameWorkController(ClassManageService classManageService, StudentLeaveService studentLeaveService) {
        this.classManageService = classManageService;
        this.studentLeaveService = studentLeaveService;
    }

    /**
     * 根路径
     *
     * @return 重定向到主页
     */
    @GetMapping("/")
    public String root() {
        logger.debug("root::重定向到主页");
        return "redirect:/index";
    }

    /**
     * 登陆页面
     *
     * @return login.html
     */
    @GetMapping("/login")
    public String login() {
        logger.debug("login::跳转到登录页面");
        return "login";
    }

    /**
     * 页面框架初始化
     *
     * @param model 模型
     * @return index.html
     */
    @GetMapping("/index")
    public String frameWorkInit(Model model) {
        List<Clazz> allClazzInfo = classManageService.getAllClassInfo();
        if (allClazzInfo.size() == 0) {
            logger.info("frameWorkInit::没有班级信息,将重定向到班级显示页面");
            return "redirect:/class/show";
        }
        model.addAttribute("classList", allClazzInfo);
        model.addAttribute("studentLeaveNum", studentLeaveService.getStudentLeaveNum());
        logger.debug("frameWorkInit::添加classList完成,集合大小->" + allClazzInfo.size());
        return "index";
    }
}

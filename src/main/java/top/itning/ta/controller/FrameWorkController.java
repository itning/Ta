package top.itning.ta.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.itning.ta.entity.Clazz;
import top.itning.ta.entity.ServerMessage;
import top.itning.ta.exception.NullParameterException;
import top.itning.ta.service.ClassManageService;
import top.itning.ta.service.SettingService;
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

    private final SettingService settingService;

    @Autowired
    public FrameWorkController(ClassManageService classManageService, StudentLeaveService studentLeaveService, SettingService settingService) {
        this.classManageService = classManageService;
        this.studentLeaveService = studentLeaveService;
        this.settingService = settingService;
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
        model.addAttribute("themeColor", settingService.getThemeColor());
        model.addAttribute("themeColorAccent", settingService.getThemeColorAccent());
        model.addAttribute("hasStudent", classManageService.hasStudent());
        logger.debug("frameWorkInit::添加classList完成,集合大小->" + allClazzInfo.size());
        return "index";
    }

    /**
     * 主题设置页面
     *
     * @param model 模型
     * @return settheme.html
     */
    @GetMapping("/theme")
    public String theme(Model model) {
        model.addAttribute("classList", classManageService.getAllClassInfo());
        model.addAttribute("studentLeaveNum", studentLeaveService.getStudentLeaveNum());
        model.addAttribute("themeColor", settingService.getThemeColor());
        model.addAttribute("themeColorAccent", settingService.getThemeColorAccent());
        return "settheme";
    }

    /**
     * 设置主题颜色
     *
     * @param primaryColor 主色
     * @param accentColor  强调色
     * @return Json格式服务器消息
     */
    @GetMapping("/theme/set")
    @ResponseBody
    public ServerMessage setTheme(String primaryColor, String accentColor) {
        ServerMessage serverMessage = new ServerMessage();
        serverMessage.setCode(ServerMessage.SUCCESS_CODE);
        serverMessage.setMsg("设置成功");
        try {
            settingService.setThemeColor(primaryColor);
            settingService.setThemeColorAccent(accentColor);
        } catch (NullParameterException e) {
            serverMessage.setCode(ServerMessage.SERVICE_ERROR);
            serverMessage.setMsg("设置失败:" + e.getExceptionMessage());
        }
        serverMessage.setUrl("/theme/set?primaryColor=" + primaryColor + "&accentColor=" + accentColor);
        return serverMessage;
    }
}

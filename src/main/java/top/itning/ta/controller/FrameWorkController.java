package top.itning.ta.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import top.itning.ta.entity.Clazz;
import top.itning.ta.service.FrameWorkService;

import java.util.List;

/**
 * 框架初始化控制器
 *
 * @author wangn
 */
@Controller
public class FrameWorkController {
    private final FrameWorkService frameWorkService;

    @Autowired
    public FrameWorkController(FrameWorkService frameWorkService) {
        this.frameWorkService = frameWorkService;
    }

    /**
     * 根路径
     *
     * @return 重定向到主页
     */
    @GetMapping("/")
    public String root() {
        return "redirect:/index";
    }

    /**
     * 登陆页面
     *
     * @return login.html
     */
    @GetMapping("/login")
    public String login() {
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
        List<Clazz> allClazzInfo = frameWorkService.getAllClazzInfo();
        if (allClazzInfo.size() == 0) {
            return "redirect:/class/show";
        }
        model.addAttribute("classList", allClazzInfo);
        return "index";
    }
}

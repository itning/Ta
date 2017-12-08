package top.itning.ta.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
     * 页面框架初始化
     *
     * @param model 模型
     * @return index.html
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String frameWorkInit(Model model) {
        List<Clazz> allClazzInfo = frameWorkService.getAllClazzInfo();
        if (allClazzInfo.size() == 0) {
            return "redirect:/class/show";
        }
        model.addAttribute("classList", allClazzInfo);
        return "index";
    }
}

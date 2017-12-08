package top.itning.ta.controller.clazz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import top.itning.ta.entity.Clazz;
import top.itning.ta.entity.ServerMessage;
import top.itning.ta.exception.DataNotFindException;
import top.itning.ta.exception.NullParameterException;
import top.itning.ta.service.ClassManageService;

import java.util.List;
import java.util.UUID;

/**
 * 班级管理控制器
 *
 * @author wangn
 */
@Controller
@RequestMapping("/class")
public class ClassManageController {
    private final ClassManageService classManageService;

    @Autowired
    public ClassManageController(ClassManageService classManageService) {
        this.classManageService = classManageService;
    }

    /**
     * 显示所有班级信息
     *
     * @param model 模型
     * @return classset.html
     */
    @RequestMapping(value = "/show", method = RequestMethod.GET)
    public String showClassInfo(Model model) {
        List<Clazz> allClassInfo = classManageService.getAllClassInfo();
        if (allClassInfo != null) {
            allClassInfo.forEach(clazz -> clazz.setNum(classManageService.getStudentNumByClassID(clazz.getId())));
            model.addAttribute("classList", allClassInfo);
        }
        return "classset";
    }

    /**
     * 添加班级信息
     *
     * @param clazz 班级信息
     * @return 服务器消息Json
     */
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    @ResponseBody
    public ServerMessage addClassInfo(Clazz clazz) {
        clazz.setId(UUID.randomUUID().toString().replace("-", ""));
        ServerMessage serverMessage = new ServerMessage();
        serverMessage.setCode(ServerMessage.SUCCESS_CODE);
        serverMessage.setMsg("添加成功");
        try {
            classManageService.addClassInfo(clazz);
        } catch (NullParameterException e) {
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
    @RequestMapping(value = "/del/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ServerMessage delClassInfo(@PathVariable("id") String id) {
        ServerMessage serverMessage = new ServerMessage();
        serverMessage.setCode(ServerMessage.SUCCESS_CODE);
        serverMessage.setMsg("删除成功");
        try {
            classManageService.delClassInfo(id);
        } catch (DataNotFindException e) {
            serverMessage.setCode(ServerMessage.SERVICE_ERROR);
            serverMessage.setMsg(e.getExceptionMessage());
        }
        serverMessage.setUrl("/class/del/" + id);
        return serverMessage;
    }
}

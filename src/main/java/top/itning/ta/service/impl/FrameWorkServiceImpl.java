package top.itning.ta.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.itning.ta.dao.ClazzDao;
import top.itning.ta.entity.Clazz;
import top.itning.ta.service.FrameWorkService;

import javax.transaction.Transactional;
import java.util.List;

/**
 * 框架初始化服务实现类
 *
 * @author wangn
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class FrameWorkServiceImpl implements FrameWorkService {

    private final ClazzDao clazzDao;

    @Autowired
    public FrameWorkServiceImpl(ClazzDao clazzDao) {
        this.clazzDao = clazzDao;
    }

    @Override
    public List<Clazz> getAllClazzInfo() {
        return clazzDao.findAll();
    }

}

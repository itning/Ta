package top.itning.ta.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import top.itning.ta.dao.UserDao;
import top.itning.ta.entity.User;
import top.itning.ta.exception.NullParameterException;
import top.itning.ta.service.SettingService;

import javax.transaction.Transactional;


/**
 * 设置服务实线类
 *
 * @author Ning
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class SettingServiceImpl implements SettingService {
    private static final Logger logger = LoggerFactory.getLogger(SettingServiceImpl.class);

    private final UserDao userDao;

    @Autowired
    public SettingServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public String getThemeColor() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (StringUtils.isNotBlank(user.getThemeColor())) {
            logger.info("getThemeColor::设置主题颜色->" + user.getThemeColor());
            return user.getThemeColor();
        } else {
            logger.info("getThemeColor::设置默认主题颜色");
            return "mdui-theme-primary-indigo";
        }
    }

    @Override
    public String getThemeColorAccent() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (StringUtils.isNotBlank(user.getThemeColorAccent())) {
            logger.info("getThemeColor::设置强调色->" + user.getThemeColorAccent());
            return user.getThemeColorAccent();
        } else {
            logger.info("getThemeColorAccent::设置默认强调色");
            return "mdui-theme-accent-pink";
        }
    }

    @Override
    public void setThemeColor(String color) throws NullParameterException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (color == null) {
            logger.warn("setThemeColor::参数color为空");
            throw new NullParameterException("参数color为空");
        }
        user.setThemeColor("mdui-theme-primary-" + color);
        userDao.saveAndFlush(user);
    }

    @Override
    public void setThemeColorAccent(String color) throws NullParameterException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (color == null) {
            logger.warn("setThemeColorAccent::参数color为空");
            throw new NullParameterException("参数color为空");
        }
        user.setThemeColorAccent("mdui-theme-accent-" + color);
        userDao.saveAndFlush(user);
    }
}

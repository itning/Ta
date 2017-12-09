package top.itning.ta.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import top.itning.ta.dao.UserDao;
import top.itning.ta.entity.User;

/**
 * @author Ning
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("loadUserByUsername::开始登陆,用户名->" + username);
        User user = userDao.findByUsername(username);
        if (user == null) {
            logger.warn("loadUserByUsername::用户名:" + username + "不存在");
            throw new UsernameNotFoundException("用户名:" + username + "不存在");
        }
        return user;
    }
}

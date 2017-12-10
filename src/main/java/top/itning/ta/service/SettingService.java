package top.itning.ta.service;

import top.itning.ta.exception.NullParameterException;

/**
 * 设置服务
 *
 * @author Ning
 */
public interface SettingService {
    /**
     * 获取主色
     *
     * @return 主色
     */
    String getThemeColor();

    /**
     * 获取强调色
     *
     * @return 强调色
     */
    String getThemeColorAccent();

    /**
     * 设置主色
     *
     * @param color 颜色
     * @throws NullParameterException 如果参数为空则抛出该异常
     */
    void setThemeColor(String color) throws NullParameterException;

    /**
     * 设置强调色
     *
     * @param color 颜色
     * @throws NullParameterException 如果参数为空则抛出该异常
     */
    void setThemeColorAccent(String color) throws NullParameterException;
}

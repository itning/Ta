package top.itning.ta.entity;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

/**
 * 搜索请假信息实体类
 *
 * @author Ning
 */
public class SearchLeave implements Serializable {
    /**
     * 要查询的关键字(姓名/ID)
     */
    private String key;
    /**
     * 请假时常
     */
    private String[] often;
    /**
     * 开始日期
     */
    private Date startdate;
    /**
     * 结束日期
     */
    private Date enddate;
    /**
     * 请假类型
     */
    private String[] matter;
    /**
     * 班级
     */
    private String[] clazz;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String[] getOften() {
        return often;
    }

    public void setOften(String[] often) {
        this.often = often;
    }

    public Date getStartdate() {
        return startdate;
    }

    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

    public Date getEnddate() {
        return enddate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    public String[] getMatter() {
        return matter;
    }

    public void setMatter(String[] matter) {
        this.matter = matter;
    }

    public String[] getClazz() {
        return clazz;
    }

    public void setClazz(String[] clazz) {
        this.clazz = clazz;
    }

    @Override
    public String toString() {
        return "SearchLeave{" +
                "key='" + key + '\'' +
                ", often=" + Arrays.toString(often) +
                ", startdate=" + startdate +
                ", enddate=" + enddate +
                ", matter=" + Arrays.toString(matter) +
                ", clazz=" + Arrays.toString(clazz) +
                '}';
    }
}

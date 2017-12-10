package top.itning.ta.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 实体类:StudentLeave
 * 学生请假信息
 *
 * @author wangn
 */
@Entity
@Table(name = "STU_LEAVE")
public class StudentLeave implements Serializable {
    /**
     * UUID
     */
    @Id
    @Column(name = "id")
    private String id;
    /**
     * 学生ID
     */
    @NotNull
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "sid")
    private StudentInfo sid;
    /**
     * 开始日期
     */
    @NotNull
    @Column(name = "starttime")
    private Date starttime;
    /**
     * 请假时长
     */
    @NotNull
    @Column(name = "often")
    private String often;
    /**
     * 请假类型
     */
    @NotNull
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "matter")
    private LeaveType matter;
    /**
     * 原因
     */
    @NotNull
    @Column(name = "reason")
    private String reason;
    /**
     * 备注
     */
    @Column(name = "remarks")
    private String remarks;

    /**
     * 所属用户名
     */
    @NotNull
    @Column(name = "uname")
    private String uname;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public StudentInfo getSid() {
        return sid;
    }

    public void setSid(StudentInfo sid) {
        this.sid = sid;
    }

    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public String getOften() {
        return often;
    }

    public void setOften(String often) {
        this.often = often;
    }

    public LeaveType getMatter() {
        return matter;
    }

    public void setMatter(LeaveType matter) {
        this.matter = matter;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    @Override
    public String toString() {
        return "StudentLeave{" +
                "id='" + id + '\'' +
                ", sid=" + sid +
                ", starttime=" + starttime +
                ", often='" + often + '\'' +
                ", matter=" + matter +
                ", reason='" + reason + '\'' +
                ", remarks='" + remarks + '\'' +
                ", uname='" + uname + '\'' +
                '}';
    }
}

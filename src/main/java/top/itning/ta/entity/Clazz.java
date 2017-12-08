package top.itning.ta.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 实体类:Class
 * 班级信息
 *
 * @author wangn
 */
@Entity
@Table(name = "CLASS")
public class Clazz implements Serializable {
    /**
     * UUID
     */
    @Id
    @Column(name = "id")
    private String id;
    /**
     * 班级名
     */
    @NotNull
    @Column(name = "class")
    private String clazz;
    /**
     * 教师
     */
    @NotNull
    @Column(name = "teacher")
    private String teacher;
    /**
     * 所在学院
     */
    @NotNull
    @Column(name = "college")
    private String college;
    /**
     * 所在专业
     */
    @NotNull
    @Column(name = "profession")
    private String profession;

    private Integer num;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "Clazz{" +
                "id='" + id + '\'' +
                ", clazz='" + clazz + '\'' +
                ", teacher='" + teacher + '\'' +
                ", college='" + college + '\'' +
                ", profession='" + profession + '\'' +
                '}';
    }
}

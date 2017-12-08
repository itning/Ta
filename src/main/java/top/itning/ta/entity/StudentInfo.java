package top.itning.ta.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 实体类:StudentInfo
 * 学生信息
 *
 * @author wangn
 */
@Entity
@Table(name = "STU_INFO")
public class StudentInfo implements Serializable {
    /**
     * 学号
     */
    @Id
    @Column(name = "id")
    private String id;
    /**
     * 姓名
     */
    @NotNull
    @Column(name = "name")
    private String name;
    /**
     * 性别
     */
    @NotNull
    @Column(name = "sex")
    private boolean sex;
    /**
     * 出生日期
     */
    @NotNull
    @Column(name = "birthday")
    private Date birthday;
    /**
     * 家庭地址
     */
    @NotNull
    @Column(name = "address")
    private String address;
    /**
     * 联系电话
     */
    @NotNull
    @Column(name = "tel")
    private String tel;
    /**
     * 家长电话
     */
    @NotNull
    @Column(name = "htel")
    private String htel;
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
    /**
     * 班级
     */
    @NotNull
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "class")
    private Clazz clazz;
    /**
     * 班主任
     */
    @NotNull
    @Column(name = "teacher")
    private String teacher;
    /**
     * 职务
     */
    @Column(name = "position")
    private String position;
    /**
     * 入学时间
     */
    @NotNull
    @Column(name = "intime")
    private Date intime;
    /**
     * 是否在籍
     */
    @NotNull
    @Column(name = "isin")
    private boolean isin;
    /**
     * 头像
     */
    @Column(name = "img")
    private String img;
    /**
     * 备注
     */
    @Column(name = "remarks")
    private String remarks;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getHtel() {
        return htel;
    }

    public void setHtel(String htel) {
        this.htel = htel;
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

    public Clazz getClazz() {
        return clazz;
    }

    public void setClazz(Clazz clazz) {
        this.clazz = clazz;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Date getIntime() {
        return intime;
    }

    public void setIntime(Date intime) {
        this.intime = intime;
    }

    public boolean isIsin() {
        return isin;
    }

    public void setIsin(boolean isin) {
        this.isin = isin;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return "StudentInfo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", sex=" + sex +
                ", birthday=" + birthday +
                ", address='" + address + '\'' +
                ", tel='" + tel + '\'' +
                ", htel='" + htel + '\'' +
                ", college='" + college + '\'' +
                ", profession='" + profession + '\'' +
                ", clazz=" + clazz +
                ", teacher='" + teacher + '\'' +
                ", position='" + position + '\'' +
                ", intime=" + intime +
                ", isin=" + isin +
                ", img='" + img + '\'' +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}

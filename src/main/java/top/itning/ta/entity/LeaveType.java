package top.itning.ta.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 实体类:LeaveType
 * 请假类型
 *
 * @author wangn
 */
@Entity
@Table(name = "LEAVE_TYPE")
public class LeaveType implements Serializable {
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
    @Column(name = "name")
    private String name;

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

    @Override
    public String toString() {
        return "LeaveType{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}

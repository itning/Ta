package top.itning.ta.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 实体类:User
 * 用于登录信息
 *
 * @author wangn
 */
@Entity
@Table(name = "USER")
public class User implements Serializable {
    /**
     * UUID
     */
    @Id
    @Column(name = "id")
    private String id;
    /**
     * 用户名
     */
    @NotNull
    @Column(name = "username")
    private String username;
    /**
     * 密码
     */
    @NotNull
    @Column(name = "password")
    private String password;
    /**
     * 角色
     */
    @NotNull
    @Column(name = "role")
    private String role;
    /**
     * 姓名
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

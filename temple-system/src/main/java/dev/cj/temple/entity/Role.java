package dev.cj.temple.entity;

import lombok.Data;
import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "sys_role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "name", unique = true, nullable = false, length = 50)
    private String name;
    
    @Column(name = "description", length = 200)
    private String description;
    
    @Column(name = "status")
    private Integer status;
    
    @Column(name = "create_time")
    private Date createTime;
    
    @Column(name = "update_time")
    private Date updateTime;
    
    @ManyToMany(mappedBy = "roles")
    private List<User> users;
    
    @ManyToMany
    @JoinTable(name = "sys_role_menu",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "menu_id"))
    private List<Menu> menus;
    
    @PrePersist
    public void prePersist() {
        createTime = new Date();
        updateTime = new Date();
    }
    
    @PreUpdate
    public void preUpdate() {
        updateTime = new Date();
    }
}
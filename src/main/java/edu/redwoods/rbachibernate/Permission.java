package edu.redwoods.rbachibernate;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.FetchType;

import org.hibernate.annotations.GenericGenerator;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "permission")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    // The below annotation, and native generator above, became necessary in Hibernate 6 to avoid table_seq error.
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "perm_id")
    private Long id;
    @ManyToMany(mappedBy = "permissions", fetch = FetchType.LAZY)
    // @ManyToMany(mappedBy = "permissions", fetch = FetchType.EAGER) // Use if you have no Transaction Manager
    private Set<Role> roles = new HashSet<>();

    protected Permission() {}

    public Permission(String name) {
        this.name = name;
    }

    @Column(name = "perm_name")
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}


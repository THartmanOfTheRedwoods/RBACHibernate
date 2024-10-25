package edu.redwoods.rbachibernate;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    // The below annotation, and native generator above, became necessary in Hibernate 6 to avoid table_seq error.
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "role_id")
    private Long id;
    @Column(name = "role_name")
    private String name;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    // @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER) // Use if you have no Transaction Manager
    private Set<User> users = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    // @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST) // Use if you have no Transaction Manager
    @JoinTable(name = "rolepermlink",
            joinColumns = {
                    @JoinColumn(name = "role_id", referencedColumnName = "role_id",
                            nullable = false, updatable = false)},
            inverseJoinColumns = {
                    @JoinColumn(name = "perm_id", referencedColumnName = "perm_id",
                            nullable = false, updatable = false)})
    private Set<Permission> permissions = new HashSet<>();

    protected Role() {
    }

    public Role(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format(
                "Role[id=%d, name='%s']", id, name);
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }
}

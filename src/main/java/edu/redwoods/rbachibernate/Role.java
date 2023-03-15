package edu.redwoods.rbachibernate;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.FetchType;
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

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}

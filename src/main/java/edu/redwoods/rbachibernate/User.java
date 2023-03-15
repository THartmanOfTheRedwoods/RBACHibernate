package edu.redwoods.rbachibernate;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.FetchType;
import jakarta.persistence.CascadeType;
import org.hibernate.annotations.GenericGenerator;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="user")
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator="native" )
    @GenericGenerator( name = "native", strategy = "native" )
    @Column(name="user_id")
    private Long id;
    @Column(name="user_username")
    private String username;
    @Column(name="user_password_hash")
    private String passwordHash;
    @Column(name="user_salt")
    private String salt;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    // @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST) // Use if you have no Transaction Manager
    @JoinTable(name = "userrolelink",
            joinColumns = {
                    @JoinColumn(name = "user_id", referencedColumnName = "user_id",
                            nullable = false, updatable = false)},
            inverseJoinColumns = {
                    @JoinColumn(name = "role_id", referencedColumnName = "role_id",
                            nullable = false, updatable = false)})
    private Set<Role> roles = new HashSet<>();
    protected User() {}

    public User(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
    }

    @Override
    public String toString() {
        return String.format(
                "User[id=%d, username='%s', passwordHash='%s']", id, username, passwordHash);
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void setPasswordHash(String password) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        try {
            this.salt = Utilities.convertByteToHexadecimal(salt);
            this.passwordHash = Utilities.genHexPassword(password, salt);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            System.err.println(e.getMessage());
        }
    }

    public String getSalt() {
        return salt;
    }
}



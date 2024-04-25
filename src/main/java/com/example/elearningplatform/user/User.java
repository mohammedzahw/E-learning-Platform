package com.example.elearningplatform.user;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.elearningplatform.course.Course;
import com.example.elearningplatform.user.address.Address;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    private String email;
    private String firstName;

    private String lastName;

    private String password;

    private String phoneNumber;

    private byte[] profilePicture;

    private Boolean enabled;

    private LocalDateTime registrationDate;

    private String bio;
    private LocalDateTime lastLogin;
    private Integer age;


    @OneToOne(mappedBy = "user")
    @ToString.Exclude
    private Address address;

    @ToString.Exclude
    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_role")
    @Column(name = "role")
    private List<Role> roles;

    @ManyToMany(fetch = FetchType.LAZY)
    @Builder.Default
    private List<Course> enrolledCourses = new ArrayList<>();

    // @ManyToMany(mappedBy = "instructors", fetch = FetchType.LAZY)
    // @ToString.Exclude
    // @Builder.Default
    // private List<Course> instructedCourses = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_wishlist", joinColumns = {
            @JoinColumn(name = "user_id") }, inverseJoinColumns = {
                    @JoinColumn(name = "course_id") })
    private List<Course> wishList;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_archived", joinColumns = {
            @JoinColumn(name = "user_id") }, inverseJoinColumns = {
                    @JoinColumn(name = "course_id") })
    private List<Course> archivedCourses;

    public void enrollCourse(Course course) {
        if(course==null){
            return;
        }

        enrolledCourses.add(course);
    }
public void addToWishList(Course course) {
    if(course==null){
        return;
    }

    wishList.add(course);
}
public void addToArchived(Course course) {
    if(course==null){
        return;
    }

    archivedCourses.add(course);
}
    // public void addInstructedCourse(Course course) {
    //     if(course==null){
    //         return;
    //     }
    //     instructedCourses = new ArrayList<>();
    //     instructedCourses.add(course);
    // }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (roles == null) {
            return Collections.emptyList();
        }

        Collection<? extends GrantedAuthority> mapRoles = roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.toString()))
                .collect(Collectors.toList());
        return mapRoles;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public Object orElseThrow(String msg) {

        throw new RuntimeException(msg);
    }

    @Override
    public String getUsername() {
        return email;
    }

}

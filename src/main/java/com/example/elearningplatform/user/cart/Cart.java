package com.example.elearningplatform.user.cart;

import java.util.ArrayList;
import java.util.List;

import com.example.elearningplatform.course.course.Course;
import com.example.elearningplatform.user.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name = "cart")
@Data
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)

    private Integer id;

    private Double totalPrice = 0.0;

    private Integer numberOfCourses = 0;

    @OneToOne
    @ToString.Exclude
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(fetch = FetchType.EAGER)
    @ToString.Exclude
    @JoinTable(name = "courses_in_cart", joinColumns = {
            @JoinColumn(name = "COURSE_ID", referencedColumnName = "ID") }, inverseJoinColumns = {
                    @JoinColumn(name = "CART_ID", referencedColumnName = "ID") })
    private List<Course> courses = new ArrayList<>();

    public void addCourse(Course course) {
        this.courses.add(course);
        this.numberOfCourses++;
        this.totalPrice += course.getPrice();
    }
}

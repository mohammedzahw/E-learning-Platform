package com.example.elearningplatform.course.tag;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "tag")
@Data
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(name = "title")
    private String name;
    
    private Integer numberOfCourses;

    public void incrementNumberOfCourses() {
        this.numberOfCourses++;
    }

    public void decrementNumberOfCourses() {
        this.numberOfCourses--;
    }

}

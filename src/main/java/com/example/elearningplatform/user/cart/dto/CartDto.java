package com.example.elearningplatform.user.cart.dto;

import java.util.ArrayList;
import java.util.List;

import com.example.elearningplatform.course.course.dto.SearchCourseDto;

import lombok.Data;

@Data
public class CartDto {
    private Integer id;

    private Double totalPrice = 0.0;

    private Integer numberOfCourses = 0;
    private List<SearchCourseDto> courses = new ArrayList<>();

    // public CartDto(Cart cart) {
    // if (cart == null)
    // return;
    // this.id = cart.getId();
    // this.courses=cart.getCourses().stream().map(course->)
    // this.totalPrice = cart.getTotalPrice();
    // this.numberOfCourses = cart.getNumberOfCourses();
    // }
}

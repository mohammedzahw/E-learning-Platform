package com.example.elearningplatform.user.cart.dto;

import org.springframework.stereotype.Service;

import com.example.elearningplatform.course.course.dto.CourseDtoService;
import com.example.elearningplatform.user.cart.Cart;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartDtoService {
    private final CourseDtoService courseDtoService;

    public CartDto mapCartToDto(Cart cart) {
        CartDto cartDto = new CartDto();
        cartDto.setId(cart.getId());
        cartDto.setNumberOfCourses(cart.getNumberOfCourses());
        cartDto.setTotalPrice(cart.getTotalPrice());
        cartDto.setCourses(
                cart.getCourses().stream().map(course -> courseDtoService.mapCourseToSearchDto(course)).toList());
        return cartDto;
    }

}

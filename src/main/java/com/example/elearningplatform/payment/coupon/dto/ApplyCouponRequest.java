package com.example.elearningplatform.payment.coupon.dto;

import java.util.List;

import lombok.Data;

@Data
public class ApplyCouponRequest {
      private String couponCode;
      private Integer courseId;
}


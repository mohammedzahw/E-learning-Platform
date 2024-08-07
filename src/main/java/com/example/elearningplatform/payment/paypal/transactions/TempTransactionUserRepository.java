package com.example.elearningplatform.payment.paypal.transactions;

import java.util.Optional;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TempTransactionUserRepository extends JpaRepository<TempTransactionUser, Integer> {

      List<TempTransactionUser> findByPaymentIdAndUserId(String paymentId, Integer userId);

      Optional<TempTransactionUser> findByUserIdAndCourseId(Integer userId, Integer courseId);

      void deleteByPaymentIdAndUserId(String paymentId, Integer userId);
}
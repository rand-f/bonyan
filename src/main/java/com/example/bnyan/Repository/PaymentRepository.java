package com.example.bnyan.Repository;

import com.example.bnyan.Model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    Payment getPaymentById(Integer id);

}

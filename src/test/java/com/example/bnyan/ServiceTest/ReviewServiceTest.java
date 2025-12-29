package com.example.bnyan.ServiceTest;

import com.example.bnyan.Api.ApiException;
import com.example.bnyan.Model.Customer;
import com.example.bnyan.Model.Review;
import com.example.bnyan.Model.Specialist;
import com.example.bnyan.Repository.CustomerRepository;
import com.example.bnyan.Repository.ReviewRepository;
import com.example.bnyan.Repository.SpecialistRepository;
import com.example.bnyan.Service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    @InjectMocks
    private ReviewService reviewService;

    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private SpecialistRepository specialistRepository;

    private Review review1;
    private Customer customer;
    private Specialist specialist;
    private List<Review> reviewList;

    @BeforeEach
    void setup() {
        customer = new Customer();
        customer.setId(1);

        specialist = new Specialist();
        specialist.setId(1);

        review1 = new Review();
        review1.setId(1);
        review1.setRate("5");
        review1.setComment("Great job");
        review1.setCustomer(customer);
        review1.setSpecialist(specialist);

        reviewList = new ArrayList<>();
        reviewList.add(review1);
    }

    @Test
    public void getAllReviewsSuccessTest() {
        when(reviewRepository.findAll()).thenReturn(reviewList);

        List<Review> result = reviewService.getAllReviews();

        assertEquals(1, result.size());
        verify(reviewRepository, times(1)).findAll();
    }

    @Test
    public void addReviewSuccessTest() {
        when(customerRepository.getCustomerById(1)).thenReturn(customer);
        when(specialistRepository.getSpecialistById(1)).thenReturn(specialist);
        when(reviewRepository.getReviewByCustomerIdAndSpecialistId(1, 1)).thenReturn(null);

        reviewService.add(1, 1, review1);

        verify(reviewRepository, times(1)).save(review1);
    }

    @Test
    public void addReviewDuplicateTest() {
        when(customerRepository.getCustomerById(1)).thenReturn(customer);
        when(specialistRepository.getSpecialistById(1)).thenReturn(specialist);
        when(reviewRepository.getReviewByCustomerIdAndSpecialistId(1, 1)).thenReturn(review1);

        assertThrows(ApiException.class, () -> {
            reviewService.add(1, 1, review1);
        });
    }

    @Test
    public void updateReviewSuccessTest() {
        when(reviewRepository.getReviewById(1)).thenReturn(review1);

        Review newReviewData = new Review();
        newReviewData.setRate("4");
        newReviewData.setComment("Updated comment");

        reviewService.update(1, newReviewData);

        assertEquals(4, review1.getRate());
        assertEquals("Updated comment", review1.getComment());
        verify(reviewRepository, times(1)).save(review1);
    }

    @Test
    public void deleteReviewSuccessTest() {
        when(reviewRepository.getReviewById(1)).thenReturn(review1);

        reviewService.delete(1);

        verify(reviewRepository, times(1)).delete(review1);
    }

    @Test
    public void getReviewsBySpecialistSuccessTest() {
        when(specialistRepository.getSpecialistById(1)).thenReturn(specialist);
        when(reviewRepository.getReviewsBySpecialistId(1)).thenReturn(reviewList);

        List<Review> result = reviewService.getReviewsBySpecialist(1);

        assertFalse(result.isEmpty());
        assertEquals(1, result.get(0).getSpecialist().getId());
    }

    @Test
    public void getReviewByIdNotFoundTest() {
        when(reviewRepository.getReviewById(99)).thenReturn(null);

        assertThrows(ApiException.class, () -> {
            reviewService.getReviewById(99);
        });
    }
}
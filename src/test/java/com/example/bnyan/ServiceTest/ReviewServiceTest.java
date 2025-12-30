package com.example.bnyan.ServiceTest;

import com.example.bnyan.Api.ApiException;
import com.example.bnyan.Model.*;
import com.example.bnyan.Repository.*;
import com.example.bnyan.Service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
    @Mock
    private ProjectRepository projectRepository;

    private User customerUser;
    private User adminUser;
    private Customer customer;
    private Specialist specialist;
    private Project project;
    private Review review;

    @BeforeEach
    void setup() {
        customerUser = new User();
        customerUser.setId(1);
        customerUser.setRole("USER");

        adminUser = new User();
        adminUser.setId(2);
        adminUser.setRole("ADMIN");

        customer = new Customer();
        customer.setId(1);

        specialist = new Specialist();
        specialist.setId(10);

        project = new Project();
        project.setSpecialists(Set.of(specialist));
        project.setCustomer(customer);

        review = new Review();
        review.setId(100);
        review.setCustomer(customer);
        review.setSpecialist(specialist);
        review.setRate("5");
        review.setComment("Great!");
    }


    @Test
    public void getAllReviewsSuccessTest() {
        List<Review> list = new ArrayList<>();
        list.add(review);
        when(reviewRepository.findAll()).thenReturn(list);

        List<Review> result = reviewService.getAllReviews();

        assertEquals(1, result.size());
        verify(reviewRepository, times(1)).findAll();
    }

    @Test
    public void getAllReviewsEmptyTest() {
        when(reviewRepository.findAll()).thenReturn(new ArrayList<>());
        assertThrows(ApiException.class, () -> reviewService.getAllReviews());
    }


    @Test
    public void addReviewSuccessTest() {
        when(customerRepository.getCustomerById(customerUser.getId())).thenReturn(customer);
        when(specialistRepository.findSpecialistById(specialist.getId())).thenReturn(specialist);
        when(projectRepository.findProjectsByCustomer(customer)).thenReturn(List.of(project));
        when(reviewRepository.getReviewByCustomerIdAndSpecialistId(customer.getId(), specialist.getId()))
                .thenReturn(null);

        Review newReview = new Review();
        reviewService.add(customerUser, specialist.getId(), newReview);

        assertEquals(customer, newReview.getCustomer());
        assertEquals(specialist, newReview.getSpecialist());
        verify(reviewRepository, times(1)).save(newReview);
    }

    @Test
    public void addReviewUnauthorizedUserTest() {
        User otherUser = new User();
        otherUser.setRole("ADMIN");
        assertThrows(ApiException.class, () -> reviewService.add(otherUser, specialist.getId(), new Review()));
    }

    @Test
    public void addReviewNotWorkedWithSpecialistTest() {
        when(customerRepository.getCustomerById(customerUser.getId())).thenReturn(customer);
        when(specialistRepository.findSpecialistById(specialist.getId())).thenReturn(specialist);
        when(projectRepository.findProjectsByCustomer(customer)).thenReturn(new ArrayList<>()); // no projects

        assertThrows(ApiException.class, () -> reviewService.add(customerUser, specialist.getId(), new Review()));
    }


    @Test
    public void updateReviewSuccessTest() {
        when(reviewRepository.getReviewById(review.getId())).thenReturn(review);

        Review updated = new Review();
        updated.setRate("4");
        updated.setComment("Good");

        reviewService.update(customerUser, review.getId(), updated);

        assertEquals("4", review.getRate());
        assertEquals("Good", review.getComment());
        verify(reviewRepository, times(1)).save(review);
    }

    @Test
    public void updateReviewUnauthorizedTest() {
        when(reviewRepository.getReviewById(review.getId())).thenReturn(review);

        User otherUser = new User();
        otherUser.setId(99);
        otherUser.setRole("USER");

        assertThrows(ApiException.class, () -> reviewService.update(otherUser, review.getId(), new Review()));
    }


    @Test
    public void deleteReviewSuccessTest() {
        when(reviewRepository.getReviewById(review.getId())).thenReturn(review);

        reviewService.delete(customerUser, review.getId());

        verify(reviewRepository, times(1)).delete(review);
    }

    @Test
    public void deleteReviewUnauthorizedTest() {
        when(reviewRepository.getReviewById(review.getId())).thenReturn(review);

        User otherUser = new User();
        otherUser.setId(99);
        otherUser.setRole("USER");

        assertThrows(ApiException.class, () -> reviewService.delete(otherUser, review.getId()));
    }


    @Test
    public void getReviewsBySpecialistSuccessTest() {
        when(specialistRepository.getSpecialistById(specialist.getId())).thenReturn(specialist);
        when(reviewRepository.getReviewsBySpecialistId(specialist.getId())).thenReturn(List.of(review));

        List<Review> result = reviewService.getReviewsBySpecialist(specialist.getId());
        assertEquals(1, result.size());
    }

    @Test
    public void getReviewsBySpecialistNotFoundTest() {
        when(specialistRepository.getSpecialistById(specialist.getId())).thenReturn(null);
        assertThrows(ApiException.class, () -> reviewService.getReviewsBySpecialist(specialist.getId()));
    }


    @Test
    public void getReviewByIdSuccessTest() {
        when(reviewRepository.getReviewById(review.getId())).thenReturn(review);

        Review result = reviewService.getReviewById(review.getId());
        assertEquals(review, result);
    }

    @Test
    public void getReviewByIdNotFoundTest() {
        when(reviewRepository.getReviewById(review.getId())).thenReturn(null);
        assertThrows(ApiException.class, () -> reviewService.getReviewById(review.getId()));
    }

    @Test
    public void getSpecialistReviewsSuccessTest() {
        when(specialistRepository.findSpecialistById(specialist.getId())).thenReturn(specialist);
        when(reviewRepository.findReviewsBySpecialist(specialist)).thenReturn(List.of(review));

        List<Review> result = reviewService.getSpecialistReviews(specialist.getId());
        assertEquals(1, result.size());
    }

    @Test
    public void getSpecialistReviewsNotFoundTest() {
        when(specialistRepository.findSpecialistById(specialist.getId())).thenReturn(null);
        assertThrows(ApiException.class, () -> reviewService.getSpecialistReviews(specialist.getId()));
    }

    @Test
    public void getReviewsByCustomerSuccessTest() {
        when(customerRepository.getCustomerById(customer.getId())).thenReturn(customer);
        when(reviewRepository.findReviewsByCustomer(customer)).thenReturn(List.of(review));

        List<Review> result = reviewService.getReviewsByCustomer(customer.getId());
        assertEquals(1, result.size());
    }

    @Test
    public void getReviewsByCustomerNotFoundTest() {
        when(customerRepository.getCustomerById(customer.getId())).thenReturn(null);
        assertThrows(ApiException.class, () -> reviewService.getReviewsByCustomer(customer.getId()));
    }
}

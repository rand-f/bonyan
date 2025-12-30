package com.example.bnyan.ServiceTest;

import com.example.bnyan.Api.ApiException;
import com.example.bnyan.Model.Customer;
import com.example.bnyan.Model.Land;
import com.example.bnyan.Model.User;
import com.example.bnyan.Repository.CustomerRepository;
import com.example.bnyan.Repository.LandRepository;
import com.example.bnyan.Service.LandService;
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
public class LandServiceTest {

    @InjectMocks
    private LandService landService;

    @Mock
    private LandRepository landRepository;

    @Mock
    private CustomerRepository customerRepository;

    private User adminUser;
    private User customerUser;
    private Customer customer;
    private Land land;

    @BeforeEach
    void setup() {
        adminUser = new User();
        adminUser.setId(1);
        adminUser.setRole("ADMIN");

        customerUser = new User();
        customerUser.setId(2);
        customerUser.setRole("USER");

        customer = new Customer();
        customer.setId(2);

        land = new Land();
        land.setId(10);
        land.setCustomer(customer);
    }


    @Test
    public void getAllLandsSuccessTest() {
        List<Land> list = new ArrayList<>();
        list.add(land);
        when(landRepository.findAll()).thenReturn(list);

        List<Land> result = landService.getAllLands(adminUser);

        assertEquals(1, result.size());
        verify(landRepository, times(1)).findAll();
    }

    @Test
    public void getAllLandsUnauthorizedTest() {
        assertThrows(ApiException.class, () -> landService.getAllLands(customerUser));
    }


    @Test
    public void addLandSuccessTest() {
        when(customerRepository.getCustomerById(customerUser.getId())).thenReturn(customer);

        Land newLand = new Land();
        landService.add(customerUser, newLand);

        assertNotNull(newLand.getCustomer());
        assertNotNull(newLand.getCreatedAt());
        assertFalse(newLand.getAuthorizationStatus());
        verify(landRepository, times(1)).save(newLand);
    }

    @Test
    public void addLandUnauthorizedTest() {
        assertThrows(ApiException.class, () -> landService.add(adminUser, new Land()));
    }


    @Test
    public void updateLandSuccessTest() {
        when(landRepository.getLandById(10)).thenReturn(land);

        Land updated = new Land();
        updated.setLocation("New Location");
        updated.setSize("500.0");
        updated.setAuthorizationStatus(true);

        landService.update(customerUser, 10, updated);

        assertEquals("New Location", land.getLocation());
        assertEquals("500.0", land.getSize());
        assertTrue(land.getAuthorizationStatus());
        verify(landRepository, times(1)).save(land);
    }

    @Test
    public void updateLandUnauthorizedTest() {
        when(landRepository.getLandById(10)).thenReturn(land);
        User otherUser = new User();
        otherUser.setId(3);
        otherUser.setRole("USER");

        assertThrows(ApiException.class, () -> landService.update(otherUser, 10, new Land()));
    }


    @Test
    public void deleteLandSuccessTest() {
        when(landRepository.getLandById(10)).thenReturn(land);

        landService.delete(customerUser, 10);

        verify(landRepository, times(1)).delete(land);
    }

    @Test
    public void deleteLandUnauthorizedTest() {
        when(landRepository.getLandById(10)).thenReturn(land);
        User otherUser = new User();
        otherUser.setId(3);
        otherUser.setRole("USER");

        assertThrows(ApiException.class, () -> landService.delete(otherUser, 10));
    }

    @Test
    public void getMyLandsSuccessTest() {
        List<Land> list = new ArrayList<>();
        list.add(land);
        when(landRepository.getLandsByCustomerId(customerUser.getId())).thenReturn(list);

        List<Land> result = landService.getMyLands(customerUser);

        assertEquals(1, result.size());
    }


    @Test
    public void getLandByIdSuccessTest() {
        when(landRepository.getLandById(10)).thenReturn(land);

        Land result = landService.getLandById(customerUser, 10);

        assertEquals(land, result);
    }

    @Test
    public void getLandByIdUnauthorizedTest() {
        when(landRepository.getLandById(10)).thenReturn(land);
        User otherUser = new User();
        otherUser.setId(3);
        otherUser.setRole("USER");

        assertThrows(ApiException.class, () -> landService.getLandById(otherUser, 10));
    }
}

package com.example.bnyan.ServiceTest;

import com.example.bnyan.Api.ApiException;
import com.example.bnyan.Model.*;
import com.example.bnyan.Repository.*;
import com.example.bnyan.Service.BuildRequestService;
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
public class BuildRequestServiceTest {

    @InjectMocks
    private BuildRequestService buildRequestService;

    @Mock
    private BuildRequestRepository buildRequestRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private LandRepository landRepository;
    @Mock
    private ProjectRepository projectRepository;

    private BuildRequest buildRequest;
    private Customer customer;
    private Land land;
    private User admin;
    private Project project;

    @BeforeEach
    void setup() {
        customer = new Customer();
        customer.setId(1);

        land = new Land();
        land.setId(10);

        admin = new User();
        admin.setId(2);
        admin.setRole("ADMIN");

        project = new Project();
        project.setId(5);

        buildRequest = new BuildRequest();
        buildRequest.setId(1);
        buildRequest.setCustomer(customer);
        buildRequest.setLand(land);
        buildRequest.setProject(project);
        buildRequest.setStatus("PROCESSING");
    }

    @Test
    public void getAllBuildRequestsSuccessTest() {
        List<BuildRequest> list = new ArrayList<>();
        list.add(buildRequest);
        when(buildRequestRepository.findAll()).thenReturn(list);

        List<BuildRequest> result = buildRequestService.getAllBuildRequests();

        assertEquals(1, result.size());
        verify(buildRequestRepository, times(1)).findAll();
    }

    @Test
    public void addBuildRequestSuccessTest() {
        when(customerRepository.getCustomerById(1)).thenReturn(customer);
        when(landRepository.getLandById(10)).thenReturn(land);
        when(buildRequestRepository.getBuildRequestByLandId(10)).thenReturn(null);

        buildRequestService.add(1, 10, buildRequest);

        assertEquals("PROCESSING", buildRequest.getStatus());
        assertNotNull(buildRequest.getCreatedAt());
        verify(buildRequestRepository, times(1)).save(buildRequest);
    }

    @Test
    public void updateStatusSuccessTest() {
        when(userRepository.getUserById(2)).thenReturn(admin);
        when(buildRequestRepository.getBuildRequestById(1)).thenReturn(buildRequest);

        buildRequestService.updateStatus(1, 2, "APPROVED");

        assertEquals("APPROVED", buildRequest.getStatus());
        verify(buildRequestRepository, times(1)).save(buildRequest);
    }

    @Test
    public void updateStatusUnauthorizedTest() {
        User regularUser = new User();
        regularUser.setId(3);
        regularUser.setRole("CUSTOMER");

        when(userRepository.getUserById(3)).thenReturn(regularUser);

        assertThrows(ApiException.class, () -> {
            buildRequestService.updateStatus(1, 3, "APPROVED");
        });
    }

    @Test
    public void deleteSuccessTest() {
        when(customerRepository.getCustomerById(1)).thenReturn(customer);
        when(buildRequestRepository.getBuildRequestById(1)).thenReturn(buildRequest);

        buildRequestService.delete(1, 1);

        verify(buildRequestRepository, times(1)).delete(buildRequest);
    }

    @Test
    public void getBuildRequestsByStatusTest() {
        List<BuildRequest> list = new ArrayList<>();
        list.add(buildRequest);
        when(buildRequestRepository.getBuildRequestsByStatus("PROCESSING")).thenReturn(list);

        List<BuildRequest> result = buildRequestService.getBuildRequestsByStatus("processing");

        assertFalse(result.isEmpty());
        assertEquals("PROCESSING", result.get(0).getStatus());
    }

    @Test
    public void approveRequestSuccessTest() {
        when(buildRequestRepository.getBuildRequestById(1)).thenReturn(buildRequest);
        when(projectRepository.findProjectById(5)).thenReturn(project);
        when(landRepository.getLandById(10)).thenReturn(land);

        buildRequestService.approveRequest(1);

        assertEquals("approved", buildRequest.getStatus());
        assertEquals(land, project.getLand());
        assertEquals(project, land.getProject());

        verify(projectRepository, times(1)).save(project);
        verify(landRepository, times(1)).save(land);
        verify(buildRequestRepository, times(1)).save(buildRequest);
    }
}
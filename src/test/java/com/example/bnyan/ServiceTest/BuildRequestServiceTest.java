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
    private CustomerRepository customerRepository;
    @Mock
    private LandRepository landRepository;
    @Mock
    private ProjectRepository projectRepository;

    private BuildRequest buildRequest;
    private Customer customer;
    private Land land;
    private Project project;
    private User admin;
    private User user;

    @BeforeEach
    void setup() {
        customer = new Customer();
        customer.setId(1);

        land = new Land();
        land.setId(10);
        land.setCustomer(customer);

        project = new Project();
        project.setId(5);

        buildRequest = new BuildRequest();
        buildRequest.setId(1);
        buildRequest.setCustomer(customer);
        buildRequest.setLand(land);
        buildRequest.setProject(project);
        buildRequest.setStatus("PROCESSING");

        admin = new User();
        admin.setId(2);
        admin.setRole("ADMIN");

        user = new User();
        user.setId(1);
        user.setRole("USER");
    }

    @Test
    public void getAllBuildRequestsSuccessTest() {
        List<BuildRequest> list = new ArrayList<>();
        list.add(buildRequest);
        when(buildRequestRepository.findAll()).thenReturn(list);

        List<BuildRequest> result = buildRequestService.getAllBuildRequests(admin);

        assertEquals(1, result.size());
        verify(buildRequestRepository, times(1)).findAll();
    }

    @Test
    public void getAllBuildRequestsUnauthorizedTest() {
        assertThrows(ApiException.class, () -> buildRequestService.getAllBuildRequests(user));
    }

    @Test
    public void addBuildRequestSuccessTest() {
        when(customerRepository.getCustomerById(user.getId())).thenReturn(customer);
        when(landRepository.getLandById(10)).thenReturn(land);
        when(buildRequestRepository.getBuildRequestByLandId(10)).thenReturn(null);

        //buildRequestService.add(user, 10, buildRequest);

        assertEquals("PROCESSING", buildRequest.getStatus());
        assertNotNull(buildRequest.getCreatedAt());
        verify(buildRequestRepository, times(1)).save(buildRequest);
    }

    @Test
    public void addBuildRequestUnauthorizedTest() {
     //   assertThrows(ApiException.class, () -> buildRequestService.add(admin, 10, buildRequest));
    }

    @Test
    public void updateStatusSuccessTest() {
        when(buildRequestRepository.getBuildRequestById(1)).thenReturn(buildRequest);

        buildRequestService.updateStatus(admin, 1, "APPROVED");

        assertEquals("APPROVED", buildRequest.getStatus());
        verify(buildRequestRepository, times(1)).save(buildRequest);
    }

    @Test
    public void updateStatusUnauthorizedTest() {
        assertThrows(ApiException.class, () -> buildRequestService.updateStatus(user, 1, "APPROVED"));
    }

    @Test
    public void deleteSuccessTest() {
        when(buildRequestRepository.getBuildRequestById(1)).thenReturn(buildRequest);
        when(customerRepository.getCustomerById(user.getId())).thenReturn(customer);

        buildRequestService.delete(user, 1);

        verify(buildRequestRepository, times(1)).delete(buildRequest);
    }

    @Test
    public void deleteUnauthorizedTest() {
        User otherUser = new User();
        otherUser.setId(3);
        otherUser.setRole("USER");
        when(buildRequestRepository.getBuildRequestById(1)).thenReturn(buildRequest);

        Customer otherCustomer = new Customer();
        otherCustomer.setId(3);

        when(customerRepository.getCustomerById(otherUser.getId())).thenReturn(otherCustomer);

        assertThrows(ApiException.class, () -> buildRequestService.delete(otherUser, 1));
    }

    @Test
    public void getBuildRequestsByStatusSuccessTest() {
        List<BuildRequest> list = new ArrayList<>();
        list.add(buildRequest);
        when(buildRequestRepository.getBuildRequestsByStatus("PROCESSING")).thenReturn(list);

        List<BuildRequest> result = buildRequestService.getBuildRequestsByStatus(admin, "processing");

        assertEquals(1, result.size());
        assertEquals("PROCESSING", result.get(0).getStatus());
    }

    @Test
    public void getBuildRequestsByStatusUnauthorizedTest() {
        assertThrows(ApiException.class, () -> buildRequestService.getBuildRequestsByStatus(user, "PROCESSING"));
    }

    @Test
    public void approveRequestSuccessTest() {
        when(buildRequestRepository.getBuildRequestById(1)).thenReturn(buildRequest);

        buildRequestService.approveRequest(admin, 1);

        assertEquals("APPROVED", buildRequest.getStatus());
        assertEquals(land, project.getLand());
        assertEquals(project, land.getProject());

        verify(projectRepository, times(1)).save(project);
        verify(landRepository, times(1)).save(land);
        verify(buildRequestRepository, times(1)).save(buildRequest);
    }

    @Test
    public void approveRequestUnauthorizedTest() {
        assertThrows(ApiException.class, () -> buildRequestService.approveRequest(user, 1));
    }

    @Test
    public void getBuildRequestByIdSuccessTest() {
        when(buildRequestRepository.getBuildRequestById(1)).thenReturn(buildRequest);

        BuildRequest result = buildRequestService.getBuildRequestById(user, 1);
        assertEquals(buildRequest, result);
    }

    @Test
    public void getBuildRequestByIdUnauthorizedTest() {
        User otherUser = new User();
        otherUser.setId(3);
        otherUser.setRole("USER");
        when(buildRequestRepository.getBuildRequestById(1)).thenReturn(buildRequest);

        assertThrows(ApiException.class, () -> buildRequestService.getBuildRequestById(otherUser, 1));
    }

    @Test
    public void getMyBuildRequestsSuccessTest() {
        List<BuildRequest> list = new ArrayList<>();
        list.add(buildRequest);
        when(buildRequestRepository.getBuildRequestsByCustomerId(user.getId())).thenReturn(list);

        List<BuildRequest> result = buildRequestService.getMyBuildRequests(user);

        assertEquals(1, result.size());
        assertEquals(buildRequest, result.get(0));
    }

    @Test
    public void getMyBuildRequestsUnauthorizedTest() {
        assertThrows(ApiException.class, () -> buildRequestService.getMyBuildRequests(admin));
    }

    @Test
    public void getBuildRequestsByLandIdSuccessTest() {
        when(landRepository.getLandById(10)).thenReturn(land);
        when(buildRequestRepository.getBuildRequestsByLandId(10)).thenReturn(List.of(buildRequest));

        List<BuildRequest> result = buildRequestService.getBuildRequestsByLandId(user, 10);

        assertEquals(1, result.size());
    }

    @Test
    public void getBuildRequestsByLandIdUnauthorizedTest() {
        Land land2 = new Land();
        Customer otherCustomer = new Customer();
        otherCustomer.setId(3);
        land2.setCustomer(otherCustomer);
        when(landRepository.getLandById(10)).thenReturn(land2);

        assertThrows(ApiException.class, () -> buildRequestService.getBuildRequestsByLandId(user, 10));
    }
}

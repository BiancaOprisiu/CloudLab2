package de.msg.javatraining.donationmanager.controller.permission;

import de.msg.javatraining.donationmanager.controller.permision.PermisionController;
import de.msg.javatraining.donationmanager.persistence.model.PRole;
import de.msg.javatraining.donationmanager.persistence.model.Permision;
import de.msg.javatraining.donationmanager.service.permision.PermisionService;
import de.msg.javatraining.donationmanager.service.permision.PermisionServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PermissionControllerTest {

    @Mock
    private PermisionService permisionService;

    @InjectMocks
    private PermisionController permisionController;

    @Test
    void testFindAllWhenEmpty() throws PermisionServiceException{
        when(permisionService.findAll()).thenThrow(new PermisionServiceException("No permission found", "NO_PERMISSION_FOUND"));

        ResponseEntity<?> response = permisionController.findAll();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        String errorMessage = (String) response.getBody();
        assertEquals("No permission found", errorMessage);
    }

    @Test
    void testFindAllWhenNotEmpty() throws PermisionServiceException{
        List<Permision> permisions = new ArrayList<>();
        permisions.add(createTestPermission(1L));
        permisions.add(createTestPermission(2L));
        permisions.add(createTestPermission(3L));
        when(permisionService.findAll()).thenReturn(permisions);

        ResponseEntity<?> response = permisionController.findAll();

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(permisions,response.getBody());
    }

    private Permision createTestPermission(long id){
        return new Permision(id, PRole.PERMISSION_MANAGEMENT);
    }
}

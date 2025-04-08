package de.msg.javatraining.donationmanager.service.permission;

import de.msg.javatraining.donationmanager.persistence.model.PRole;
import de.msg.javatraining.donationmanager.persistence.model.Permision;
import de.msg.javatraining.donationmanager.persistence.repository.PermisionRepository;
import de.msg.javatraining.donationmanager.service.permision.PermisionService;
import de.msg.javatraining.donationmanager.service.permision.PermisionServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PermissionServiceTest {

    @Mock
    private PermisionRepository permisionRepository;

    @InjectMocks
    private PermisionService permisionService;

    @Test
    void testFindAllWhenEmpty() throws PermisionServiceException{
        when(permisionRepository.findAll()).thenReturn(new ArrayList<Permision>());
        assertThrows(PermisionServiceException.class, () -> permisionService.findAll());
        try{
            permisionService.findAll();
        }catch (PermisionServiceException pse){
            assertEquals("NO_PERMISSION_FOUND",pse.getErrorCode());
        }catch (Exception e){
            assert(false);
        }
    }

    @Test
    void testFindAllWhenNotEmpty() throws PermisionServiceException{
        List<Permision> permisions = new ArrayList<>();
        permisions.add(new Permision(1L, PRole.PERMISSION_MANAGEMENT));
        permisions.add(new Permision(2L, PRole.PERMISSION_MANAGEMENT));
        permisions.add(new Permision(3L, PRole.PERMISSION_MANAGEMENT));
        when(permisionRepository.findAll()).thenReturn(permisions);
        assertEquals(permisionService.findAll(),permisions);
    }
}

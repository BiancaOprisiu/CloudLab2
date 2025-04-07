package de.msg.javatraining.donationmanager.controller.auth;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SignInResponseTest {
    @Test
    void testGetAccessToken() {
        SignInResponse response = createSignInResponse(1L);
        assertEquals("dummy-token", response.getAccessToken());
    }

    @Test
    void testSetAccessToken() {
        SignInResponse response = createSignInResponse(1L);
        response.setAccessToken("new-token");
        assertEquals("new-token", response.getAccessToken());
    }

    @Test
    void testGetTokenType() {
        SignInResponse response = createSignInResponse(1L);
        assertEquals("Bearer", response.getTokenType());
    }

    @Test
    void testSetTokenType() {
        SignInResponse response = createSignInResponse(1L);
        response.setTokenType("new-type");
        assertEquals("new-type", response.getTokenType());
    }

    @Test
    void testGetId() {
        SignInResponse response = createSignInResponse(1L);
        assertEquals(1L, response.getId());
    }

    @Test
    void testSetId() {
        SignInResponse response = createSignInResponse(1L);
        response.setId(2L);
        assertEquals(2L, response.getId());
    }

    @Test
    void testGetEmail() {
        SignInResponse response = createSignInResponse(1L);
        assertEquals("email", response.getEmail());
    }

    @Test
    void testSetEmail() {
        SignInResponse response = createSignInResponse(1L);
        response.setEmail("new-email");
        assertEquals("new-email", response.getEmail());
    }

    @Test
    void testGetUsername() {
        SignInResponse response = createSignInResponse(1L);
        assertEquals("username", response.getUsername());
    }

    @Test
    void testSetUsername() {
        SignInResponse response = createSignInResponse(1L);
        response.setUsername("new-username");
        assertEquals("new-username", response.getUsername());
    }

    @Test
    void testGetRoles() {
        SignInResponse response = createSignInResponse(1L);
        assertEquals(List.of("ROLE_USER"), response.getRoles());
    }

    private SignInResponse createSignInResponse(long id){
        SignInResponse response = new SignInResponse("dummy-token", 1L, "username", "email", List.of("ROLE_USER"),0,false);
        return response;
    }

}
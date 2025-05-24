package com.eventbuddy.eventbuddy.controller;

import com.eventbuddy.eventbuddy.Utils.BuddyError;
import com.eventbuddy.eventbuddy.Utils.ErrorResponse;
import com.eventbuddy.eventbuddy.configuration.JwtGenerator;
import com.eventbuddy.eventbuddy.model.*;
import com.eventbuddy.eventbuddy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private JwtGenerator jwtGenerator;

    private User user;
    private UserToken token;
    private Card card;
    private Address address;
    private Ticket ticket;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setEmail("user@example.com");
        user.setPassword("pass");

        token = new UserToken(user, "token123");

        card = new Card();
        card.setCardNumber("123456789");

        address = new Address();
        address.setCity("CityName");

        ticket = new Ticket();
        ticket.setTicketId(123);
    }

    @Test
    void testLoginSuccess() throws BuddyError {
        when(userService.login(any(User.class))).thenReturn(user);
        when(jwtGenerator.generateToken(any(User.class))).thenReturn(token);

        ResponseEntity<?> response = userController.login(user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(token, response.getBody());
    }

    @Test
    void testLoginFailure() throws BuddyError {
        when(userService.login(any(User.class)))
                .thenThrow(new BuddyError("Login failed"));

        ResponseEntity<?> response = userController.login(user);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Login failed", ((ErrorResponse) response.getBody()).getError());
    }

    @Test
    void testRegisterSuccess() throws BuddyError {
        when(userService.register(any(User.class))).thenReturn(user);

        ResponseEntity<?> response = userController.register(user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    void testGetUserSuccess() throws BuddyError {
        when(userService.get("user@example.com")).thenReturn(user);

        ResponseEntity<?> response = userController.get("user@example.com");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    void testAddUserCardSuccess() throws BuddyError {
        when(userService.addCard(any(Card.class))).thenReturn(card);

        ResponseEntity<?> response = userController.addUserCard(card);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(card, response.getBody());
    }

    @Test
    void testGetUserCardSuccess() throws BuddyError {
        List<Card> cards = Arrays.asList(card);
        when(userService.getCard("user@example.com")).thenReturn(cards);

        ResponseEntity<?> response = userController.getUserCard("user@example.com");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cards, response.getBody());
    }

    @Test
    void testAddAddressSuccess() throws BuddyError {
        when(userService.addAddress(any(Address.class))).thenReturn(address);

        ResponseEntity<?> response = userController.addAddress(address);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(address, response.getBody());
    }

    @Test
    void testGetUserAddressSuccess() throws BuddyError {
        List<Address> addresses = Arrays.asList(address);
        when(userService.getAddress("user@example.com")).thenReturn(addresses);

        ResponseEntity<?> response = userController.getUserAddress("user@example.com");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(addresses, response.getBody());
    }

    @Test
    void testUpdatePasswordSuccess() throws BuddyError {
        doNothing().when(userService).updatePassword(any(User.class));

        ResponseEntity<?> response = userController.updatePassword(user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testDeleteUserSuccess() throws BuddyError {
        when(userService.deleteUser("user@example.com")).thenReturn(true);

        ResponseEntity<?> response = userController.deleteUser("user@example.com");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testDeleteUserFailure() throws BuddyError {
        when(userService.deleteUser("user@example.com")).thenReturn(false);

        ResponseEntity<?> response = userController.deleteUser("user@example.com");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("user deletion failed", ((ErrorResponse) response.getBody()).getError());
    }

    @Test
    void testGetTicketsSuccess() throws BuddyError {
        List<Ticket> tickets = Arrays.asList(ticket);
        when(userService.getTickets("user@example.com")).thenReturn(tickets);

        ResponseEntity<?> response = userController.getTickets("user@example.com");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tickets, response.getBody());
    }
}

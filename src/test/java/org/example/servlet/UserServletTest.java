package org.example.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.User;
import org.example.service.Service;
import org.example.servlet.dto.IncomingDtoUser;
import org.example.servlet.dto.OutGoingDtoUser;
import org.example.servlet.mapper.UserDtoMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.*;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServletTest {
    @Mock
    private Service<User> mockService;

    @Mock
    private HttpServletRequest mockRequest;

    @Mock
    private HttpServletResponse mockResponse;

    @Test
    void doGet_userFound() throws IOException, ServletException {
        UUID testUUID = UUID.randomUUID();
        User mockUser = new User(testUUID, "Test User", "test@example.com");
        when(mockRequest.getParameter("id")).thenReturn(testUUID.toString());
        when(mockService.findById(eq(testUUID))).thenReturn(mockUser);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(mockResponse.getWriter()).thenReturn(writer);

        UserServlet servlet = new UserServlet(mockService);

        servlet.doGet(mockRequest, mockResponse);

        verify(mockService).findById(eq(testUUID));
        String jsonResponse = stringWriter.toString();
        Gson gson = new Gson();
        OutGoingDtoUser outGoingDtoUser = gson.fromJson(jsonResponse, OutGoingDtoUser.class);
        assertEquals(mockUser.getName(), outGoingDtoUser.getName());
        assertEquals(mockUser.getEmail(), outGoingDtoUser.getEmail());
        verify(mockResponse, never()).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void doGet_userNotFound() throws IOException, ServletException {
        UUID testUUID = UUID.randomUUID();
        when(mockRequest.getParameter("id")).thenReturn(testUUID.toString());
        when(mockService.findById(eq(testUUID))).thenReturn(null);

        HttpServletResponse mockResponse = mock(HttpServletResponse.class);

        UserServlet servlet = new UserServlet(mockService);
        servlet.doGet(mockRequest, mockResponse);

        verify(mockService).findById(eq(testUUID));
        verify(mockResponse).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void doPost_userCreated() throws IOException, ServletException {
        Gson gson = new Gson();
        String jsonRequest = """
            {
                "name": "Test User Post",
                "email": "testpost@example.com"
            }
            """;
        // Имитируем тело запроса
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(jsonRequest.getBytes());
        BufferedReader reader = new BufferedReader(new InputStreamReader(byteArrayInputStream));
        when(mockRequest.getReader()).thenReturn(reader);

        User mockUser = UserDtoMapper.INSTANCE.incomingDtoToUser(gson.fromJson(jsonRequest, IncomingDtoUser.class));
        User savedUser = new User(UUID.randomUUID(), mockUser.getName(), mockUser.getEmail());
        when(mockService.save(eq(mockUser))).thenReturn(savedUser);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(mockResponse.getWriter()).thenReturn(writer);

        UserServlet servlet = new UserServlet(mockService);

        //Act
        servlet.doPost(mockRequest, mockResponse);

        //Assert
        verify(mockService).save(eq(mockUser));
        String jsonResponse = stringWriter.toString();
        OutGoingDtoUser outGoingDtoUser = gson.fromJson(jsonResponse, OutGoingDtoUser.class);
        assertEquals(savedUser.getName(), outGoingDtoUser.getName());
        assertEquals(savedUser.getEmail(), outGoingDtoUser.getEmail());
        verify(mockResponse).setStatus(HttpServletResponse.SC_CREATED);
    }
}
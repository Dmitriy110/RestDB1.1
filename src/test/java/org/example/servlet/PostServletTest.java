package org.example.servlet;


import com.google.gson.Gson;
import org.example.model.Post;
import org.example.service.Service;
import org.example.servlet.dto.IncomingDtoPost;
import org.example.servlet.dto.OutGoingDtoPost;
import org.example.servlet.mapper.PostDtoMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.*;

import java.io.*;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServletTest {

    @Mock
    private Service<Post> mockService;

    @Mock
    private HttpServletRequest mockRequest;

    @Mock
    private HttpServletResponse mockResponse;

    @Test
    void doGet_postFound() throws IOException, ServletException {
        UUID testUUID = UUID.randomUUID();
        Post mockPost = new Post(testUUID, "Test Post Title", "Test Post Content", UUID.randomUUID()); // Замените на ваши поля
        when(mockRequest.getParameter("id")).thenReturn(testUUID.toString());
        when(mockService.findById(eq(testUUID))).thenReturn(mockPost);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(mockResponse.getWriter()).thenReturn(writer);

        PostServlet servlet = new PostServlet(mockService);
        servlet.doGet(mockRequest, mockResponse);

        verify(mockService).findById(eq(testUUID));
        String jsonResponse = stringWriter.toString();
        Gson gson = new Gson();
        OutGoingDtoPost outGoingDtoPost = gson.fromJson(jsonResponse, OutGoingDtoPost.class);
        assertEquals(mockPost.getTitle(), outGoingDtoPost.getTitle());
        assertEquals(mockPost.getContent(), outGoingDtoPost.getContent());
        verify(mockResponse, never()).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void doGet_postNotFound() throws IOException, ServletException {
        UUID testUUID = UUID.randomUUID();
        when(mockRequest.getParameter("id")).thenReturn(testUUID.toString());
        when(mockService.findById(eq(testUUID))).thenReturn(null);

        PostServlet servlet = new PostServlet(mockService);
        servlet.doGet(mockRequest, mockResponse);

        verify(mockService).findById(eq(testUUID));
        verify(mockResponse, never()).getWriter(); // Проверяем, что getWriter не вызывается
        verify(mockResponse).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void doPost_postCreated() throws IOException, ServletException {
        Gson gson = new Gson();
        String jsonRequest = """
            {
                "title": "Test Post Title",
                "content": "Test Post Content",
                "userId": "a1b2c3d4-e5f6-7890-1234-567890abcdef" // UUID
            }
            """;

        IncomingDtoPost incomingDtoPost = gson.fromJson(jsonRequest, IncomingDtoPost.class);
        Post mockPost = PostDtoMapper.INSTANCE.incomingDtoToPost(incomingDtoPost);
        Post savedPost = new Post(UUID.randomUUID(), mockPost.getTitle(), mockPost.getContent(), mockPost.getUserId());

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(jsonRequest.getBytes());
        BufferedReader reader = new BufferedReader(new InputStreamReader(byteArrayInputStream));
        when(mockRequest.getReader()).thenReturn(reader);
        when(mockService.save(eq(mockPost))).thenReturn(savedPost);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(mockResponse.getWriter()).thenReturn(writer);

        PostServlet servlet = new PostServlet(mockService);

        // Act
        servlet.doPost(mockRequest, mockResponse);

        // Assert
        verify(mockService).save(eq(mockPost));
        verify(mockResponse).setStatus(HttpServletResponse.SC_CREATED);

        String jsonResponse = stringWriter.toString();
        OutGoingDtoPost outGoingDtoPost = gson.fromJson(jsonResponse, OutGoingDtoPost.class);
        assertEquals(savedPost.getTitle(), outGoingDtoPost.getTitle());
        assertEquals(savedPost.getContent(), outGoingDtoPost.getContent());
    }
}

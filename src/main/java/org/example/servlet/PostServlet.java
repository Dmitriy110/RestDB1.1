package org.example.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Post;
import org.example.service.Service;
import org.example.servlet.dto.IncomingDtoPost;
import org.example.servlet.dto.OutGoingDtoPost;
import org.example.servlet.mapper.PostDtoMapper;

import java.io.IOException;
import java.util.UUID;

@WebServlet(name = "PostServlet", value = "/post")
public class PostServlet extends HttpServlet {
    final transient Service<Post> service;

    public PostServlet(Service<Post> service) {
        this.service = service;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UUID uuid = UUID.fromString(req.getParameter("id"));// Our Id from request/Our Id random
        Post byId = service.findById(uuid);
        if(byId != null) {
            OutGoingDtoPost outGoingDtoPost = PostDtoMapper.INSTANCE.postToOutGoingDto(byId);
            Gson gson = new Gson();
            resp.setContentType("application/json");
            resp.getWriter().write(gson.toJson(outGoingDtoPost));
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        // return our DTO
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        IncomingDtoPost incomingDtoPost = gson.fromJson(req.getReader(), IncomingDtoPost.class);
        Post post = PostDtoMapper.INSTANCE.incomingDtoToPost(incomingDtoPost);
        Post savedPost = service.save(post);
        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.getWriter().write(gson.toJson(PostDtoMapper.INSTANCE.postToOutGoingDto(savedPost)));
        // return our DTO, not necessary
    }
}

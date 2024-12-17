package org.example.servlet;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Post;
import org.example.service.Service;
import org.example.servlet.dto.IncomingDtoPost;
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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UUID uuid = UUID.fromString(req.getParameter("id"));
        Post byId = service.findById(uuid);
        if(byId != null) {
            Gson gson = new Gson();
            resp.setContentType("application/json");
            resp.getWriter().write(gson.toJson(PostDtoMapper.INSTANCE.postToOutGoingDto(byId)));
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Gson gson = new Gson();
        IncomingDtoPost incomingDtoPost = gson.fromJson(req.getReader(), IncomingDtoPost.class);
        Post savedPost = service.save(PostDtoMapper.INSTANCE.incomingDtoToPost(incomingDtoPost));
        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.getWriter().write(gson.toJson(PostDtoMapper.INSTANCE.postToOutGoingDto(savedPost)));
    }
}

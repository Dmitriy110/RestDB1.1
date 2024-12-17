package org.example.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.User;
import org.example.service.Service;
import org.example.servlet.dto.IncomingDtoUser;
import com.google.gson.Gson;
import org.example.servlet.mapper.UserDtoMapper;

import java.io.IOException;
import java.util.UUID;


@WebServlet(name = "UserServlet", value = "/user")
public class UserServlet extends HttpServlet {
    final transient Service<User> service;

    public UserServlet(Service<User> service) {
        this.service = service;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UUID uuid = UUID.fromString(req.getParameter("id"));
        User byId = service.findById(uuid);
        if(byId != null) {
            Gson gson = new Gson();
            resp.setContentType("application/json");
            resp.getWriter().write(gson.toJson(UserDtoMapper.INSTANCE.userToOutGoingDto(byId)));
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Gson gson = new Gson();
        IncomingDtoUser incomingDtoUser = gson.fromJson(req.getReader(), IncomingDtoUser.class);
        User savedUser = service.save(UserDtoMapper.INSTANCE.incomingDtoToUser(incomingDtoUser));
        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.getWriter().write(gson.toJson(UserDtoMapper.INSTANCE.userToOutGoingDto(savedUser)));
    }
}

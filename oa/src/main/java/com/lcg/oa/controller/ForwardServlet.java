package com.lcg.oa.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ForwardServlet", urlPatterns = "/forward/*")
public class ForwardServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getRequestURI();
        /**
         * /forward/form
         * /forward/a/b/c/form
         */
        String subUri = uri.substring(1);// forward/form; forward/a/b/c/form
        String page = subUri.substring(subUri.indexOf("/"));// /form; /a/b/c/form
        request.getRequestDispatcher(page + ".ftl").forward(request, response);
    }
}

package de.l7r7.proto.bundle.magic;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FooServlet extends HttpServlet {
    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
        resp.getOutputStream().print("sadfsadfsadfjklewrweroiusdf");
        resp.flushBuffer();
        resp.setStatus(202);
    }
}

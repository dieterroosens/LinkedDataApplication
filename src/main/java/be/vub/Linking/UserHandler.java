package be.vub.Linking;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

public class UserHandler {
    //true is logged in
    public boolean userLoggedIn(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        HttpSession session = request.getSession();
        String userName= (String) session.getAttribute("userName");
        String userFirstName = (String)session.getAttribute("userFirstName");
        if(userName== null || userFirstName == null) {

            return userNeeded(response);
        }
        return true;
    }

    private boolean userNeeded(HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        response.sendRedirect("login");
        //out.println("<a href=\"login\" class=\"btn btn-primary btn-lg btn-block\">Login</a>");
        //out.println("<a href=\"register\" class=\"btn btn-primary btn-lg btn-block\">Register</a>");
        out.flush();
        return false;
    }

    public boolean logOutUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        HttpSession session = request.getSession();
        session.removeAttribute("userName");
        session.removeAttribute("userFirstName");
        return userNeeded(response);

    }
}

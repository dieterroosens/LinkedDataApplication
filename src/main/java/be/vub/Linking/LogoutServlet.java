package be.vub.Linking;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "LogoutServlet", urlPatterns = {"/logout"})
public class LogoutServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }



    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        out.println("<html>\n" +
                "<head>\n" +
                "    <title>Interlinking Tool - Index - Get</title>\n" +
                "</head>\n" +
                "<body>\n");
        out.println("<link rel='stylesheet' href='https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css' integrity='sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T' crossorigin='anonymous'>");
        out.println("<script src='https://code.jquery.com/jquery-3.3.1.slim.min.js' integrity='sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo' crossorigin='anonymous'></script>\n" +
                "    <script src='https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js' integrity='sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1' crossorigin='anonymous'></script>\n" +
                "    <script src='https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js' integrity='sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM' crossorigin='anonymous'></script>\n");

        UserHandler userHandler = new UserHandler();
        response.setContentType("text/html");
        userHandler.logOutUser(request, response);
        /*PrintWriter out = response.getWriter();
        printLoginForm(out);
        out.flush();*/
        out.println("</body>\n" +
                "</html>\n");
        out.flush();
    }

    private void printLoginForm(PrintWriter out) {
        //printLoginForm(out, "");
    }

    private void printLoginForm(PrintWriter out, String email) {
        out.println("<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Login Form</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<form method=\"post\" action=\"login\">\n" +
                "    <div>\n" +
                "        <h1>Login in</h1>\n" +
                "        <p>Please fill in this form to log in with your account.</p>\n" +
                "        <hr>\n" +
                "\n" +
                "        <label for=\"email\"><b>Email</b></label>\n" +
                "        <input type=\"text\" placeholder=\"Enter Email\" name=\"email\" value=\""+email+"\" required> <br/>\n" +
                "\n" +
                "        <label for=\"password\"><b>Password</b></label>\n" +
                "        <input type=\"password\" placeholder=\"Enter Password\" name=\"password\" required> <br/>\n" +
                "\n" +
                "        <p>By creating an account you agree to our <a href=\"#\">Terms & Privacy</a>.</p>\n" +
                "        <button type=\"submit\" class=\"loginbtn\">Login</button>\n" +
                "    </div>\n" +
                "\n" +
                "    <div>\n" +
                "        <p>Not yet an account? <a href=\"register\">register</a>.</p>\n" +
                "    </div>\n" +
                "</form>\n" +
                "</body>\n" +
                "</html>");
    }
}

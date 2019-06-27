package be.vub.Linking;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {
    UserStorage userStorage=new UserStorage();
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        boolean emailOk = isEmailValid(email);
        boolean userExists = userExists(email);

        PrintWriter out = response.getWriter();
        out.println("<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <link rel='stylesheet' href='https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css' integrity='sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T' crossorigin='anonymous'>\n" +
                "    <title>Interlinking tool -  Login - Post </title>\n" +
                "</head>\n" +
                "<body>\n" );
        out.println("<script src='https://code.jquery.com/jquery-3.3.1.slim.min.js' integrity='sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo' crossorigin='anonymous'></script>\n" +
                "    <script src='https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js' integrity='sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1' crossorigin='anonymous'></script>\n" +
                "    <script src='https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js' integrity='sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM' crossorigin='anonymous'></script>\n");

        if (emailOk && userExists)
        {
            if(loginCorrect(email,password)){
                //out.println("you can go to the map<br/>");
                User user = userStorage.getUserInfo(email);
                HttpSession session = request.getSession();
                session.setAttribute("userFirstName", user.getFirstName());
                session.setAttribute("userAgentID", user.getAgentID());
                session.setAttribute("userName", user.getName());
                session.setAttribute("userEmail", user.getEmail());
                session.setAttribute("userExperience", user.getExperience());
                out.println("<div class=\"alert alert-success\" role=\"alert\">Welcome "+session.getAttribute("userFirstName") + " "+ session.getAttribute("userName")+"</div>");
                MapHandler mapHandler = new MapHandler();
                mapHandler.writeMapBody(request, response);
                //out.println("<a href=\"index\" target=\"_top\">Go to site</a><br/><br/>");
                /*out.println("<a href=\"index\" target=\"_top\">Go to site</a><br/><br/>" +
                        "      <script>\n" +
                        "         setTimeout(function(){\n" +
                        "            window.location.href = 'index';\n" +
                        "         }, 2000);\n" +
                        "      </script>\n" +
                        "      <p>Web page redirects after 2 seconds.</p>");*/



            }
            else{
                out.println("<div class='alert alert-danger' role='alert'>Incorrect login</div>");
                //printLoginForm(out, email);
                card(out, email);
            }
        }
        else if(!emailOk)
        {
            out.println("<div class='alert alert-danger' role='alert'>Email is invalid</div>");
            //printLoginForm(out, email);
            card(out, email);
        }
        else if(!userExists )
        {
            out.println("<div class='alert alert-danger' role='alert'>Incorrect login</div>");
            //printLoginForm(out, email);
            card(out, email);
        }
        out.println("</body>\n" +
                "</html>");
        out.flush();
    }

    private boolean userExists(String email) {
        return userStorage.userAlreadyExcists(email);
    }

    private boolean loginCorrect(String email, String password) {
        return userStorage.userCorrect(email,password);
    }

    private boolean isEmailValid(String email) {
        Pattern p = Pattern.compile(
                "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
        Matcher m = p.matcher(email);
        boolean matchFound = m.matches();

        StringTokenizer st = new StringTokenizer(email, ".");
        String lastToken = null;
        while (st.hasMoreTokens()) {
            lastToken = st.nextToken();
        }
        if (matchFound && lastToken.length() >= 2 && email.length() - 1 != lastToken.length()) {
            return true;
        }
        else return false;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <link rel='stylesheet' href='https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css' integrity='sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T' crossorigin='anonymous'>\n" +
                "    <title>Login Form - GET</title>\n" +
                "</head>\n" +
                "<body>\n");
        out.println("<script src='https://code.jquery.com/jquery-3.3.1.slim.min.js' integrity='sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo' crossorigin='anonymous'></script>\n" +
                "    <script src='https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js' integrity='sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1' crossorigin='anonymous'></script>\n" +
                "    <script src='https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js' integrity='sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM' crossorigin='anonymous'></script>\n");

        card(out, "");
        //printLoginForm2(out);
        out.println("</body>\n" +
                "</html>");
        out.flush();
    }

    private void card(PrintWriter out, String email){
        out.println("<div class=\"container card w-50 text-center\">\n" +
                "  <div class=\"card-body\">\n" +
                "    <h5 class=\"card-title\">Login</h5>\n" +
                "    <p class=\"card-text\">");
        printLoginForm2(out, email);
        out.println("</p>\n" +
                "  </div>\n" +
                "</div>");
    }

    /*private void printLoginForm(PrintWriter out) {
        printLoginForm(out, "");
    }*/

    /*private void printLoginForm(PrintWriter out, String email) {
        out.println("<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Login Form</title>\n" +
        "</head>\n" +
                "<body>\n" +
                "<form method=\"post\" action=\"login\">\n" +
                "    <div>\n" +
                "        <h1>Login</h1>\n" +
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
    }*/

    private void printLoginForm2(PrintWriter out, String email) {
        out.println(
                "<form method=\"post\" action=\"login\">\n" +
                "  <div class=\"form-group\">\n" +
                "    <label for=\"exampleInputEmail1\">Email address</label>\n" +
                "    <input type=\"email\" class=\"form-control\" id=\"exampleInputEmail1\" aria-describedby=\"emailHelp\" placeholder=\"Enter email\" name=\"email\" value=\""+email+"\">\n" +
                "    <small id=\"emailHelp\" class=\"form-text text-muted\">We'll never share your email with anyone else.</small>\n" +
                "  </div>\n" +
                "  <div class=\"form-group\">\n" +
                "    <label for=\"exampleInputPassword1\">Password</label>\n" +
                "    <input type=\"password\" class=\"form-control\" id=\"exampleInputPassword1\" placeholder=\"Password\" name=\"password\">\n" +
                "  </div>\n" +
                "  <button type=\"submit\" class=\"btn btn-primary\">Login</button>\n" +
                "    <div>\n" +
                "        <p>Not yet an account? <a href=\"register\">register</a>.</p>\n" +
                "    </div>\n" +
                "</form>\n");
    }
}

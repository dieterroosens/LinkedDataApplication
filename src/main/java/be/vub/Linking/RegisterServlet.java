package be.vub.Linking;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {
    UserStorage userStorage=new UserStorage();
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String name = request.getParameter("name");
        String firstName = request.getParameter("firstName");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String experience = "0";
        String passwordRepeat = request.getParameter("passwordRepeat");

        boolean emailOk = isEmailValid(email);
        boolean passwordsOk = arePasswordsEqual(password, passwordRepeat);
        boolean userNew = newUser(email);
        PrintWriter out = response.getWriter();
        out.println("<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Registration Form</title>\n" +
                "</head>\n" +
                "<body>\n" );
        out.println("<link rel='stylesheet' href='https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css' integrity='sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T' crossorigin='anonymous'>");
        out.println("<script src='https://code.jquery.com/jquery-3.3.1.slim.min.js' integrity='sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo' crossorigin='anonymous'></script>\n" +
                "    <script src='https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js' integrity='sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1' crossorigin='anonymous'></script>\n" +
                "    <script src='https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js' integrity='sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM' crossorigin='anonymous'></script>\n");


        if (emailOk && passwordsOk && userNew)
        {
            //write to file
            User user = new User(name,firstName,password,email,experience);
            userStorage.createUser(user);
            //response.sendRedirect("index");
            out.println("<div class=\"alert alert-success\" role=\"alert\">User is correct registrated. You can <a href=\"login\">login</a> now.</div>");
            out.println("<script>\n" +
                    "         setTimeout(function(){\n" +
                    "            window.location.href = 'login';\n" +
                    "         }, 5000);\n" +
                    "      </script>\n" +
                    "      <p>Web page redirects after 5 seconds.</p>");

        }
        else if(!emailOk)
        {
            out.println("<div class='alert alert-danger' role='alert'>email is invalid</div>");
            card(out, name,firstName,email,experience);
        }
        else if(!userNew)
        {
            out.println("<div class='alert alert-danger' role='alert'>user already exists</div>");
            card(out, name,firstName,email,experience);
        }
        else if(!passwordsOk)
        {
            out.println("<div class='alert alert-danger' role='alert'>passwords aren't equal</div>");
            card(out, name,firstName,email,experience);
        }
        out.println("</body>\n" +
                "</html>");
        out.flush();
    }

    private boolean newUser(String email) {
        return !userStorage.userAlreadyExcists(email);
    }

    private boolean arePasswordsEqual(String pass1, String pass2) {
        return(pass1.equals(pass2));
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
                "    <title>Register Form</title>\n" +
                "</head>\n" +
                "<body>\n");
        out.println("<link rel='stylesheet' href='https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css' integrity='sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T' crossorigin='anonymous'>");
        out.println("<script src='https://code.jquery.com/jquery-3.3.1.slim.min.js' integrity='sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo' crossorigin='anonymous'></script>\n" +
                "    <script src='https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js' integrity='sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1' crossorigin='anonymous'></script>\n" +
                "    <script src='https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js' integrity='sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM' crossorigin='anonymous'></script>\n");

        card(out, "", "", "", "");
        out.println("</body>\n" +
                "</html>");
        //printRegistrationForm(out);
        out.flush();
    }

    private void card(PrintWriter out, String name, String firstName,String email, String experience){
        out.println("<div class=\"container card w-50 text-center\">\n" +
                "  <div class=\"card-body\">\n" +
                "    <h5 class=\"card-title\">Register</h5>\n" +
                "    <p class=\"card-text\">");
        printRegistrationForm2(out, name,firstName,email,experience);
        out.println("</p>\n" +
                "  </div>\n" +
                "</div>");
    }

    /*private void printRegistrationForm(PrintWriter out)
    {
        printRegistrationForm(out, "", "", "", "");
    }*/
    /*private void printRegistrationForm(PrintWriter out, String name, String firstname, String email, String experience) {
        out.println("<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Registration Form</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<form method=\"post\" action=\"register\">\n" +
                "    <div>\n" +
                "        <h1>Register</h1>\n" +
                "        <p>Please fill in this form to create an account.</p>\n" +
                "        <hr>\n" +
                "\n" +
                "        <label for=\"firstName\"><b>First name</b></label>\n" +
                "        <input type=\"text\" placeholder=\"First Name\" name=\"firstName\" value=\""+firstname+"\" required> <br/>\n" +
                "\n" +
                "        <label for=\"name\"><b>Name</b></label>\n" +
                "        <input type=\"text\" placeholder=\"Enter Name\" name=\"name\" value=\""+name+"\" required> <br/>\n" +
                "\n" +
                "        <label for=\"email\"><b>Email</b></label>\n" +
                "        <input type=\"text\" placeholder=\"Enter Email\" name=\"email\" value=\""+email+"\" required> <br/>\n" +
                "\n" +
                "        <label for=\"experience\"><b>Years of experience</b></label>\n" +
                "        <input type=\"text\" placeholder=\"experience\" name=\"experience\" value=\""+experience+"\" required> <br/>\n" +
                "\n" +
                "        <label for=\"password\"><b>Password</b></label>\n" +
                "        <input type=\"password\" placeholder=\"Enter Password\" name=\"password\" required> <br/>\n" +
                "\n" +
                "        <label for=\"passwordRepeat\"><b>Repeat Password</b></label>\n" +
                "        <input type=\"password\" placeholder=\"Repeat Password\" name=\"passwordRepeat\" required> <br/>\n" +
                "        <hr>\n" +
                "\n" +
                "        <p>By creating an account you agree to our <a href=\"#\">Terms & Privacy</a>.</p>\n" +
                "        <button type=\"submit\" class=\"registerbtn\">Register</button>\n" +
                "    </div>\n" +
                "\n" +
                "    <div>\n" +
                "        <p>Already have an account? <a href=\"login\">Sign in</a>.</p>\n" +
                "    </div>\n" +
                "</form>\n" +
                "</body>\n" +
                "</html>");
    }*/

    private void printRegistrationForm2(PrintWriter out, String name, String firstName,String email, String experience) {
        out.println("<form method=\"post\" action=\"register\">\n" +
                "  <div class=\"form-group row\">\n" +
                "    <label for=\"firstName\" class=\"col-sm-2 col-form-label\">First name</label>\n" +
                "    <div class=\"col-sm-10\">" +
                "         <input type=\"text\" class=\"form-control\" id=\"firstName\" placeholder=\"First Name\" name=\"firstName\" value=\""+firstName+"\" required>\n" +
                "    </div>\n" +
                "  </div>\n" +
                "  <div class=\"form-group row\">\n" +
                "    <label for=\"name\" class=\"col-sm-2 col-form-label\">Name</label>\n" +
                "    <div class=\"col-sm-10\">" +
                "         <input type=\"text\" class=\"form-control\" id=\"name\" placeholder=\"Name\" name=\"name\" value=\""+name+"\" required>\n" +
                "    </div>\n" +
                "  </div>\n" +
                "  <div class=\"form-group row\">\n" +
                "    <label for=\"email\" class=\"col-sm-2 col-form-label\">Email</label>\n" +
                "    <div class=\"col-sm-10\">" +
                "         <input type=\"text\" class=\"form-control\" id=\"email\" placeholder=\"Email\" name=\"email\" value=\""+email+"\" required>\n" +
                "    </div>\n" +
                "  </div>\n" +
                //"  <div class=\"form-group row\">\n" +
                //"    <label for=\"experience\" class=\"col-sm-2 col-form-label\">Years of experience</label>\n" +
                //"    <div class=\"col-sm-10\">" +
                //"         <input type=\"text\" class=\"form-control\" id=\"experience\" placeholder=\"Experience\" name=\"experience\" value=\""+experience+"\" required>\n" +
                //"    </div>\n" +
                //"  </div>\n" +
                "  <div class=\"form-group row\">\n" +
                "    <label for=\"password\" class=\"col-sm-2 col-form-label\">Password</label>\n" +
                "    <div class=\"col-sm-10\">" +
                "         <input type=\"password\" class=\"form-control\" id=\"password\" placeholder=\"Password\" name=\"password\" required>\n" +
                "    </div>\n" +
                "  </div>\n" +
                "  <div class=\"form-group row\">\n" +
                "    <label for=\"passwordRepeat\" class=\"col-sm-2 col-form-label\">Repeat Password</label>\n" +
                "    <div class=\"col-sm-10\">" +
                "         <input type=\"password\" class=\"form-control\" id=\"passwordRepeat\" placeholder=\"Repeat Password\" name=\"passwordRepeat\" required>\n" +
                "    </div>\n" +
                "  </div>\n" +
                "\n" +
                "      <button type=\"submit\" class=\"btn btn-primary\">Register</button>\n" +
                "    </div>\n" +
                "\n" +
                "    <div>\n" +
                "        <p>Already have an account? <a href=\"login\">Sign in</a>.</p>\n" +
                "    </div>\n" +
                "</form>");
    }
}

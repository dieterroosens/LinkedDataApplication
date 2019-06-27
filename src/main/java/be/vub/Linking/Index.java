package be.vub.Linking;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "IndexServlet", urlPatterns = {"/index"})
public class Index extends HttpServlet {

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
        if (userHandler.userLoggedIn(request, response)) {
            response.setContentType("text/html");
            HttpSession session = request.getSession();
            writeIndexPage(request, response);
        }
        out.println("</body>\n" +
        "</html>\n");
    }

    private void writeIndexPage(HttpServletRequest request, HttpServletResponse response) throws IOException {

        PrintWriter out = response.getWriter();
        css(request, response);
        String tab = request.getParameter("tab");
        HttpSession session = request.getSession();
        out.println("<div class=\"alert alert-success\" role=\"alert\">Welcome "+session.getAttribute("userFirstName") + " "+ session.getAttribute("userName")+"</div>");
        if(tab!=null && tab.equals("links")) {

            createTabsLink(request, response);
            createOwnLinks(request, response);
        }
        else{
            createTabsMap(request, response);
            createMap(request, response);
        }


    }

    /*
    private void javascriptIncluder(HttpServletRequest request, HttpServletResponse response, String fileName){
        //https://stackoverflow.com/questions/13124234/best-way-to-include-javascript-in-java-servlets
        try {
            PrintWriter out = response.getWriter();
            out.println("<script type='text/javascript'>");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/javascript/"+fileName);
            dispatcher.include(request, response);
            out.println("</script>");
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    private void createTabsMap(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();

        out.println("<ul class='nav nav-tabs'>" +
                "  <li class='nav-item'>" +
                "    <a class='nav-link active' href='index?tab=map'>Map</a>" +
                "  </li>" +
                "  <li class='nav-item'>" +
                "    <a class='nav-link' href='index?tab=links'>Manage interlinks</a>" +
                "  </li>" +
                "  <li class='nav-item'>" +
                "    <a class='nav-link' href='logout'>Logout</a>" +
                "  </li>" +
                "</ul>");
    }

    private void createTabsLink(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();

        out.println("<ul class='nav nav-tabs'>" +
                "  <li class='nav-item'>" +
                "    <a class='nav-link' href='index?tab=map'>Map</a>" +
                "  </li>" +
                "  <li class='nav-item'>" +
                "    <a class='nav-link active' href='index?tab=links'>Manage interlinks</a>" +
                "  </li>" +
                "  <li class='nav-item'>" +
                "    <a class='nav-link' href='logout'>Logout</a>" +
                "  </li>" +
                "</ul>");
    }

    private void createMap(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        out.println("<div id='mapContent' class='tabcontent'>"+
                "<iframe class='filter' name='filter' src='filtersearch6'></iframe>" +
                "<iframe class='map' name='map' src='leafletmap4' scrolling='no'  frameborder='0'></iframe>" +
                "</div>");
    }

    private void createOwnLinks(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        out.println("<div id='own_linksContent' class='tabcontent'>"+
                        "<iframe class='provenancedata' name='provenancedata' src='provenancedata'  frameborder='0' width='100%' height='80%'></iframe>\n" +
                     "</div>");
    }

    /*
    private String getAllParametersForFilter(HttpServletRequest request, HttpServletResponse response)throws IOException{
        URIprefix prefix = new URIprefix();
        PrintWriter out = response.getWriter();
        StringBuffer sb=new StringBuffer();
        Map<String, String[]> parameters = request.getParameterMap();
        for(String parameter : parameters.keySet()) {
            String[] values = parameters.get(parameter);
            for (String value : values) {
                sb.append(parameter+":" + value + ". ");
            }
            sb.append(" ");
        }
        return sb.toString();
    }*/

    public void css(HttpServletRequest request, HttpServletResponse response){
        try {
            PrintWriter out = response.getWriter();
            out.println("<style>");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/css/index.css");
            dispatcher.include(request, response);
            out.println("</style>");
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

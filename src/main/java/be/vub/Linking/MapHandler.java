package be.vub.Linking;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MapHandler {

    CommonHandler commonHandler = new CommonHandler();

    public void writeMapBody(HttpServletRequest request, HttpServletResponse response) throws IOException {

        PrintWriter out = response.getWriter();
        /*out.println("<html>\n" +
                "<head>\n" +
                "    <title>Interlinking Tool</title>\n" +
                "</head>\n" +
                "<body>\n");*/
        commonHandler.cssIndex(request, response);
        String tab = request.getParameter("tab");
//        createTabs(request, response);
        if(tab!=null && tab.equals("links")) {

            createTabsLink(request, response);
            createOwnLinks(request, response);
        }
        else{
            createTabsMap(request, response);
            createMap(request, response);
        }
        /*out.println("</body>\n" +
                "</html>\n");*/


    }

    private void createTabs(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();

        out.println("<ul class='nav nav-tabs'>" +
                "  <li class='nav-item'>" +
                "    <a class='nav-link' href='index?tab=map' target='_top'>Map</a>" +
                "  </li>" +
                "  <li class='nav-item'>" +
                "    <a class='nav-link' href='index?tab=links' target='_top'>Manage interlinks</a>" +
                "  </li>" +
                "  <li class='nav-item'>" +
                "    <a class='nav-link' href='logout' target='_top'>Logout</a>" +
                "  </li>" +
                "</ul>");
    }

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
                "<iframe class='provenancedata' name='provenancedata' src='provenancedata'  frameborder='0'></iframe>\n" +
                "</div>");
    }

}

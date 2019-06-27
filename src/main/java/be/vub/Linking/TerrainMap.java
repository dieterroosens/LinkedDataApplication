package be.vub.Linking;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@WebServlet(name = "TerrainMapServlet", urlPatterns = {"/map5","/terrainmap"})
public class TerrainMap extends HttpServlet {

        String dataLocation="D:/training/thesis/Parliament";
        String fileName="county.ttl";
    private Iterator<String> stringIterator;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserHandler userHandler = new UserHandler();
        if(userHandler.userLoggedIn(request, response)) {
            response.setContentType("text/html");
            makeMap(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserHandler userHandler = new UserHandler();
        if (userHandler.userLoggedIn(request, response)) {
            response.setContentType("text/html");
            makeMap(request, response);
        }
    }

    private String getAllParametersForSummary(HttpServletRequest request, HttpServletResponse response)throws IOException{
        URIprefix prefix = new URIprefix();
        PrintWriter out = response.getWriter();
        StringBuffer sb=new StringBuffer();
        Map<String, String[]> parameters = request.getParameterMap();
        if(!parameters.isEmpty())sb.append("Subjects filtered on: ");
        for(String parameter : parameters.keySet()) {
            //if(parameter.toLowerCase().startsWith("question")) {
            String[] values = parameters.get(parameter);
            /*if(parameter.toLowerCase().contains("polygon")) {
                sb.append("polygons and multipolygons will not be filtered ");
            }
            else {*/
            if (parameter.toLowerCase().startsWith("typecheckbox")) {
                //sb.append("Type: ");
                for (String value : values) {
                    sb.append("<li>type: "+ value + ".</li>");
                }
            }
            else {

                //sb.append(prefix.adaptPredicate(parameter) + ": ");
                for (String value : values) {
                    if(value.toLowerCase().contains("polygon")) {
                        sb.append("<li>POLYGONs and MULTIPOLYGONs will not be filtered otherwise we'll have a <b>Failed to load resource: net::ERR_INVALID_CHUNKED_ENCODING</b>.</li>");
                        request.removeAttribute(parameter);
                    }
                    else
                    {
                        sb.append("<li>predicate: "+ prefix.clearFilterDiv(parameter)+ "with object: " +value + ".<br></li>");
                        /*if (prefix.adaptPredicate(parameter).contains("asWKT")) {
                            sb.append("?geo ");
                        } else {
                            sb.append("?pred ");
                        }
                        if (value.contains("@") || value.contains("^^")) {
                            sb.append(prefix.adaptPredicate(parameter) + value + ". ");
                        } else {
                            sb.append(prefix.adaptPredicate(parameter) + " <" + value + ">. ");
                        }*/
                    }
                }
            }
            //}
            //sb.append("Param:"+parameter+" ");

            //sb.append(" ");
            // }
        }

        //out.println("<script type=\"text/javascript\">\n" +"console.log('"+sb.toString()+"');\ndocument.getElementById('filterResult').value=\""+sb.toString().replace("\"","'")+"\";\n"+"</script>");
        //out.println("<script type=\"text/javascript\">\n" +"\ndocument.getElementById('filterResult').value=\""+sb.toString().replace("\"","'")+"\";\n"+"</script>");
        return sb.toString();
    }

    private String getAllParametersForFilter(HttpServletRequest request, HttpServletResponse response)throws IOException{
        URIprefix prefix = new URIprefix();
        PrintWriter out = response.getWriter();
        StringBuffer sb=new StringBuffer();
        Map<String, String[]> parameters = request.getParameterMap();
        for(String parameter : parameters.keySet()) {
            //if(parameter.toLowerCase().startsWith("question")) {
            String[] values = parameters.get(parameter);
            /*if(parameter.toLowerCase().contains("polygon")) {
                sb.append("polygons and multipolygons will not be filtered ");
            }
            else {*/
                if (parameter.toLowerCase().startsWith("typecheckbox")) {
                    //sb.append("Type: ");
                    for (String value : values) {
                        sb.append("?pred ns:type " + prefix.adaptTypeValue(value) + ". ");
                    }
                }
                else {

                    //sb.append(prefix.adaptPredicate(parameter) + ": ");
                    for (String value : values) {
                        if(value.toLowerCase().contains("polygon")) {
                            sb.append("POLYGONs and MULTIPOLYGONs will not be filtered otherwise we'll have a <b>Failed to load resource: net::ERR_INVALID_CHUNKED_ENCODING</b>");
                            request.removeAttribute(parameter);
                        }
                        else
                        {
                            if (prefix.adaptPredicate(parameter).contains("asWKT")) {
                                sb.append("?geo ");
                            } else {
                                sb.append("?pred ");
                            }
                            if (value.contains("@") || value.contains("^^")) {
                                sb.append(prefix.adaptPredicate(parameter) + value + ". ");
                            } else {
                                sb.append(prefix.adaptPredicate(parameter) + " <" + value + ">. ");
                            }
                        }
                    }
                }
            //}
            //sb.append("Param:"+parameter+" ");

            sb.append(" ");
            // }
        }

        //out.println("<script type=\"text/javascript\">\n" +"console.log('"+sb.toString()+"');\ndocument.getElementById('filterResult').value=\""+sb.toString().replace("\"","'")+"\";\n"+"</script>");
        return sb.toString();
    }



    private void makeMap(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        out.println("<html>\n");
        head(request, response);
        out.println("<body>\n");
        javascriptIncluder(request, response, "leaflet/leaflet-providers.js");
        javascriptIncluder(request, response, "leaflet/wicket.js");
        javascriptIncluder(request, response, "leaflet/wicket-leaflet.js");
        javascriptIncluder(request, response, "leaflet/createTerrainMapElement.js");
        //javascriptIncluder(request, response, "leaflet/createMapElement.js");
        //javascriptIncluder(request, response, "leaflet/createMapElement2.js");
        javascriptIncluder(request, response, "mapElement/tabs.js");
        javascriptIncluder(request, response, "mapElement/bootstrap.min.js");
        javascriptIncluder(request, response, "mapElement/jquery-1.11.1.min.js");
        javascriptIncluder(request, response, "mapElement/addInterlinkElement.js");
        //createTabs(request, response);
        createLeafletMap(request, response);
        out.println("</body>\n");
        out.println("</html>\n");
    }

    private void createTabs(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        out.println("<div class='tab'>"+
          "<button class='tablinks' onclick=\"openTab(event, 'map')\">Map</button>"+
          "<button class='tablinks' onclick=\"openTab(event, 'own_links')\">Manage interlinks</button>"+
          "<button class='tablinks' onclick=\"openTab(event, 'Tokyo')\">Tokyo</button>"+
        "</div>");
    }

    private void createLeafletMap(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        out.println(" <div id=\"map\" class=\"wrapper\">\n" +
               "        <div id=\"canvas\"/>\n" +
               "        <textarea type=\"text\" name=\"wkt\" id=\"wkt\" style=\"display:none;\"></textarea>\n" +
               "        <textarea type=\"text\" name=\"content\" id=\"content\" style=\"display:none;\"></textarea>\n" +
               "        <script type=\"text/javascript\">\n" +
               //can run without this mapIt, then the default element in the js will not be loaded
               //"   app.map= app.init();app.mapIt(true, true);\n" +
               "             app.map= app.init();\n" +
               "         </script>"+
               "     </div>");
        //out.println("<div id='summary'><textarea type=\"text\" name=\"filterResult\" id=\"filterResult\" readonly  rows=\"5\" cols=\"70\"></textarea></div>");
        out.println("<div id='summary'><div name=\"filterResult\" id=\"filterResult\"><ul>"+getAllParametersForSummary(request, response)+"<ul></div></div>");
       //addElement(out);
       //Good function to add elements
       addPoints(request, response);
        //getAllParametersForSummary(request, response);
    }

    private void addElement(PrintWriter out, String wkt, String content){
        out.println("<script type=\"text/javascript\">\n" +
                "document.getElementById('wkt').value=\""+wkt+"\";\n"+
                "document.getElementById('content').value=\""+content+"\";\n"+
                "app.mapIt(true, true);"+
                "</script>");

    }
/*
    private void addElement(PrintWriter out){
        out.println("<script type=\"text/javascript\">\n" +
                "document.getElementById('wkt').value='MULTIPOLYGON (((40 40, 20 45, 45 30, 40 40)), ((20 35, 10 30, 10 10, 30 5, 45 20, 20 35), (30 20, 20 15, 20 25, 30 20)))';\n"+
                "document.getElementById('content').value='<iframe src=\"https://extranet.dienstencheques-vlaanderen.be\" height=\"450\"><p>Browser does not support iframes</p></iframe>';\n"+
                "app.mapIt(true, true);"+
                "</script>");

    }*/

    private void head(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        out.println("<head>\n" +
                "    <title>LeafletMap</title>\n" +
                "    <meta charset=\"utf-8\" />\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/leaflet.css\" />\n" +
                "    <script src=\"https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/leaflet.js\"></script>\n");
                css(request, response);
        out.println("</head>\n");
    }

    public void css(HttpServletRequest request, HttpServletResponse response){
        //https://stackoverflow.com/questions/13124234/best-way-to-include-javascript-in-java-servlets
        try {
            PrintWriter out = response.getWriter();
            out.println("<style>");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/css/mapStyle.css");
            dispatcher.include(request, response);
            out.println("</style>");
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

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
    }

    private void addPoints(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        CallRDF3 callRDF = new CallRDF3();
        String params=getAllParametersForFilter(request, response);
        if(!StringUtils.isBlank(params) && !params.contains("POLYGON"))
        {
            List<MapElement3> mapElements = callRDF.getAllElements3(params);
            for(MapElement3 mapElement: mapElements) {
                addElement(out, mapElement.getWkt(), mapElement.getPopupText());
            }
        }
    }
}

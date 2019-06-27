package be.vub.Linking.old;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

@WebServlet(name = "LeafletMapServlet", urlPatterns = {"/map","/leafletmap"})
public class LeafletMap extends HttpServlet {

        String dataLocation="D:/training/thesis/Parliament";
        String fileName="SHOPPING_CENTRE_WGS84.n3";
    private Iterator<String> stringIterator;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        }

        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            makeMap(out);
        }

    private void makeMap(PrintWriter out) {

        out.println("\n" +
                "\n" +
                "<!DOCTYPE html>\n" +
                "<html>\n");
        head(out);
        out.println("<body>\n");
        createMap(out);
        addPoints(out);
        clickonMapEffect(out);
        out.println("</body>\n" +
                "</html>\n");
    }

    private void createMap(PrintWriter out){
        //dublin: 53.33306, -6.24889
        //zoom: world=1, country=7, detail=15
        out.println("<div id=\"mapid\" style=\"width: 1600px; height: 800px;\"></div>\n" +
                "<script>\n" +
                "\n" +
                "    var mymap = L.map('mapid').setView([53.33306, -6.24889], 7);\n" +
                "\n" +
                "    L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw', {\n" +
                "        maxZoom: 18,\n" +
                "        attribution: 'Map data &copy; <a href=\"https://www.openstreetmap.org/\">OpenStreetMap</a> contributors, ' +\n" +
                "            '<a href=\"https://creativecommons.org/licenses/by-sa/2.0/\">CC-BY-SA</a>, ' +\n" +
                "            'Imagery © <a href=\"https://www.mapbox.com/\">Mapbox</a>',\n" +
                "        id: 'mapbox.streets'\n" +
                "    }).addTo(mymap);\n" +
                "\n"+
                "</script>\n");
    }

    private void head(PrintWriter out){
        out.println("<head>\n" +
                "    <title>LeafletMap</title>\n" +
                "    <meta charset=\"utf-8\" />\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "\n" +
                "    <link rel=\"shortcut icon\" type=\"image/x-icon\" href=\"docs/images/favicon.ico\" />\n" +
                "    <link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/leaflet.css\" />\n" +
                "    <script src=\"https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/leaflet.js\"></script>"+

                //"    <link rel=\"stylesheet\" href=\"https://unpkg.com/leaflet@1.4.0/dist/leaflet.css\" integrity=\"sha512-puBpdR0798OZvTTbP4A8Ix/l+A4dHDD0DGqYW6RQ+9jxkRFclaxxQb/SJAWZfWAkuyeQUytO7+7N4QKrDh+drA==\" crossorigin=\"\"/>\n" +
                //"    <script src=\"https://unpkg.com/leaflet@1.4.0/dist/leaflet.js\" integrity=\"sha512-QVftwZFqvtRNi0ZyCtsznlKSWOStnDORoefr1enyq5mVL4tmKB3S/EnC3rRJcxCPavG10IcrVGSmPh6Qw5lwrg==\" crossorigin=\"\"></script>\n" +
                "\n" +
                "\n" +
                "\n" +
                "</head>\n");
    }

    private void addPoints(PrintWriter out){
        CallRDF callRDF = new CallRDF();
        List<MapElement> mapElements = callRDF.doCallAll();
        for(MapElement mapElement: mapElements){
            out.println( "<script>\n" +
                    "    L.marker(["+mapElement.giveLeafletPoint()+"]).addTo(mymap)\n" +
                    "        .bindPopup(\""+mapElement.getPopupText()+"\").openPopup();\n" +
                    //"        .bindPopup(\"Testing popup\").openPopup();\n" +
                    "    var popup = L.popup({maxWidth:600});\n" +
                    "</script>\n" +
                    "\n");
        }

    }

    private void clickonMapEffect(PrintWriter out) {
        out.println( "<script>\n" +
                "    function onMapClick(e) {\n" +
                "        popup\n" +
                "            .setLatLng(e.latlng)\n" +
                "            .setContent(\"You clicked the map at \" + e.latlng.toString()+ \"<b>Create new element</b>\" +\n" +
                "                \"<iframe src=\\\"https://extranet.dienstencheques-vlaanderen.be\\\" height=\\\"450\\\"><p>Browser does not support iframes</p></iframe>\")\n" +
                "            .openOn(mymap);\n" +
                "    }\n" +
                "\n" +
                "mymap.on('click', onMapClick);\n"+
                "</script>\n");
    }

}

package be.vub.Linking;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.RDFNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "FilterSearch6Servlet", urlPatterns = {"/filtersearch6","/filterSearch6"})
public class FilterSearch6 extends HttpServlet {
    public static final PropertiesLoader propertiesLoader=new PropertiesLoader();
    public static final String ENDPOINT = propertiesLoader.getProp("elements.endpoint");


    private static final Logger log = LoggerFactory.getLogger( FilterSearch6.class );
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        }

        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            UserHandler userHandler = new UserHandler();
            if(userHandler.userLoggedIn(request, response)) {
                response.setContentType("text/html");

                PrintWriter out = response.getWriter();
                out.println("<link rel='stylesheet' href='https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css' integrity='sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T' crossorigin='anonymous'>");
                out.println("<script src='https://code.jquery.com/jquery-3.3.1.slim.min.js' integrity='sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo' crossorigin='anonymous'></script>\n" +
                        "    <script src='https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js' integrity='sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1' crossorigin='anonymous'></script>\n" +
                        "    <script src='https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js' integrity='sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM' crossorigin='anonymous'></script>\n");


                css(request, response);
                autoCompleteScript(request, response);
                sparqlJavascriptProcessor(request, response);
                stringScripts(request, response);
                scripts(out);

                out.println("<form autocomplete=\"off\" action=\"leafletmap4\" target=\"map\" method=\"post\">");
                out.println("<h3>Map Layer</h3>" +
                        "<select class='custom-select' id='MapLayer' name='MapLayer'>\n" +
                        "  <option value='StreetMap'>Street Map</option>\n" +
                        "  <option value='TerrainMap'>Terrain Map</option>\n" +
                        "  <option value='SatelliteMap'>Satellite Map</option>\n" +
                        "  <option value='WorldTopoMap'>WorldTopo Map</option>\n" +
                        "  <option value='HikeBikeMap'>HikeBike Map</option>\n" +
                        "</select>");
                out.println("<h3>Search</h3>" );
                out.println("<input type='submit' value='Submit' class='btn btn-primary'>");
                out.println("<input type='reset' value='Clear'class='btn btn-secondary'><br>");

                makeTypeFilter(out);
                makePredicateFilter(out);

                out.println("<input type='submit' value='Submit' class='btn btn-primary'>");
                out.println("<input type='reset' value='Clear' class='btn btn-secondary'><br>");
            }
        }

    private void stringScripts(HttpServletRequest request, HttpServletResponse response){
        try {
            PrintWriter out = response.getWriter();
            out.println("<script type='text/javascript'>");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/javascript/stringScripts.js");
            dispatcher.include(request, response);
            out.println("</script>");
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sparqlJavascriptProcessor(HttpServletRequest request, HttpServletResponse response){
        try {
            PrintWriter out = response.getWriter();
            out.println("<script type='text/javascript'>");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/javascript/sparql_processor.js");
            dispatcher.include(request, response);
            out.println("</script>");
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void scripts(PrintWriter out){
        URIprefix prefix = new URIprefix();
        out.println("<script>" +
                "function runProcessorValues(srcElement){\n" +
                "\n" +
                " var div = document.createElement(\"div\");\n" +
                " div.id = srcElement+'values';\n" +
                " div.style.display = \"block\";\n" +
                " document.getElementById(srcElement).appendChild(div);" +
                "\tvar list=document.createElement(\"ul\");\n" +
                "\tdiv.appendChild(list);"+
                "\t\n" +
                "\tvar summary=document.createElement(\"p\");\n" +
                "\t\t\n" +
                "\t//the query processor\n" +
                "\tfunction Processor(list, summary){\n" +
                "\t\tthis.n=0;\n" +
                "\t\tthis.list=list;\n" +
                "\t\tthis.summary=summary;\n" +
                "\t\t\t\t\n" +
                "\t\t//define the query\n" +
                "\t\tthis.query = \""+prefix.printAllForQuery()+" SELECT distinct ?obj WHERE { ?sub \"+adaptPredicate(srcElement)+\" ?obj. } order by asc(STR(?obj))\";\n" +
                "\t\t\n" +
                "\t\t//and methods to handle the query results testing refresh\n" +
                "\t\tthis.process = function(row){\n" +

                //create checkbox
                "\t\t\tvar item=document.createElement(\"input\");\n" +
                "\t\t\titem.type='checkbox';\n" +
                "\t\t\titem.name=srcElement;\n" +
                "if(row.obj['datatype']){" +
                "\t\t\titem.value='\"'+row.obj.value+'\"';" +
                "\t\t\titem.value+='^^<';" +
                "item.value+=row.obj['datatype'];\n" +
                "item.value+='>';\n" +
                "}else{" +
                "if(row.obj['xml:lang']){" +
                "\t\t\titem.value='\"'+row.obj.value+'\"';" +
                "\t\t\titem.value+='@';" +
                "item.value+=row.obj['xml:lang'];\n" +
                "}else{item.value=row.obj.value;}}" +
                "\t\t\tthis.list.appendChild(item);\n" +

                //create visibile value
                "\t\t\tvar emItem=document.createElement(\"em\");\n" +
                "\t\t\temItem.innerHTML=row.obj.value;\n" +
                "if(row.obj['xml:lang']){" +
                "\t\t\temItem.innerHTML+='@';" +
                "emItem.innerHTML+=row.obj['xml:lang'];\n" +
                "}" +
                "\t\t\tthis.list.appendChild(emItem);\n" +
                //create new line
                "\t\t\tvar brItem=document.createElement(\"br\");\n" +
                "\t\t\tthis.list.appendChild(brItem);\n" +

                "\t\t\tthis.n++;\n" +
                "\t\t};\n" +
                "\t\n" +
                "\t\t//show the results count as summary\n" +
                "\t}\n" +
                "\t//finally run the query\n" +
                "\t//sparql_query(\"http://dbpedia.org/sparql\", new Processor(list, summary));\n" +
                "\tsparql_query(\""+ENDPOINT+"\", new Processor(list, summary));\n" +
                "\t\n" +
                "\tsrcElement.disabled=true;\n" +
                "}" +
                "</script>");

        out.println("<script>function showHideFilterElements(element, button, loader) {\n" +
                "  var x = document.getElementById(element);\n" +
                "  var y = document.getElementById(button).value;\n" +
                "  if (y === \"-\"){\n" +
                "  document.getElementById(button).value=\"+\";\n" +
                "  " +
                "}\n" +
                "  else{\n" +
                "  document.getElementById(button).value=\"-\";\n" +
                "  if (document.getElementById(element+'values')==null){" +
                " runProcessorValues(element);\n"+

                " \n" +
                " } else{" +
                "document.getElementById(element+'values').style.display = \"block\";} }" +
                "}</script>");

        out.println("<script>function showHideFilterElements(element, button) {\n" +
                "  var x = document.getElementById(element);\n" +
                "  var y = document.getElementById(button).value;\n" +
                "  if (y === \"-\"){\n" +
                "  document.getElementById(button).value=\"+\";\n" +
                "  document.getElementById(element+'values').style.display = \"none\";\n" +
                "}\n" +
                "  else{\n" +
                "  document.getElementById(button).value=\"-\";\n" +
                "  if (document.getElementById(element+'values')==null){" +
                " runProcessorValues(element);\n"+

                " \n" +
                " } else{" +
                "document.getElementById(element+'values').style.display = \"block\";} }" +
                "}</script>");
    }
    private void makePredicateFilter(PrintWriter out) {
        Map<String, String> predHm = new HashMap<>();
        String queryTypes="PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> SELECT distinct ?pred (count ( ?sub) as ?count) WHERE { ?sub ?pred ?obj. filter(?pred != rdf:type)} GROUP BY ?pred " ;
        Query query = QueryFactory.create(queryTypes);
        QueryExecution qexec =   QueryExecutionFactory.sparqlService(ENDPOINT, query);

        try{
            ResultSet results= qexec.execSelect();
            while (results.hasNext()){

                QuerySolution soln = results.nextSolution();

                RDFNode pred = soln.get("pred");
                RDFNode count = soln.get("count");
                if(!predHm.containsKey(pred.toString()))
                {
                    //add new type
                    predHm.put(pred.toString(),""+count.asLiteral().getValue());
                }
            }
        }
        catch (Exception e){
            System.out.println("Exception: "+e);
        }
        finally {
            qexec.close();
        }

        out.println("Properties ");

        for (Map.Entry entry : predHm.entrySet()) {
            String count = (String) entry.getValue();
            String pred = (String)entry.getKey();
            URIprefix prefix = new URIprefix();
            if(prefix.containsPrefix(pred))
            {
                pred = prefix.replacePrefix(pred);
            }
            out.println("<div class='filterProperty' id='"+pred+"filterDiv'><input id=\""+pred+"Button\" type=\"button\" onclick=\"showHideFilterElements('"+pred+"filterDiv', '"+pred+"Button')\" value=\"+\">"+pred+"("+count+" element(s)) </div>");
        }
        out.println("<br>");
    }

    private void makeTypeFilter(PrintWriter out) {
        Map<String, String> typeHm = new HashMap<>();

        String queryTypes="PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> SELECT ?type (count ( ?sub) as ?count) WHERE { ?sub rdf:type ?type. } GROUP BY ?type " ;

        Query query = QueryFactory.create(queryTypes);
        QueryExecution qexec =   QueryExecutionFactory.sparqlService(ENDPOINT, query);

        try{
            ResultSet results= qexec.execSelect();
            while (results.hasNext()){

                QuerySolution soln = results.nextSolution();

                RDFNode type = soln.get("type");
                RDFNode count = soln.get("count");
                if(!typeHm.containsKey(type.toString()))
                {
                    //add new type
                    typeHm.put(type.toString(),""+count.asLiteral().getValue());
                }
            }
        }
        catch (Exception e){
            System.out.println("Exception: "+e);
        }
        finally {
            qexec.close();
        }

        out.println("Types ");

        for (Map.Entry entry : typeHm.entrySet()) {
            String count = (String) entry.getValue();
            String type = (String)entry.getKey();
            URIprefix prefix = new URIprefix();
            if(prefix.containsPrefix(type))
            {
                type = prefix.replacePrefix(type);
            }
            out.println("<div class='filterProperty'><input type=\"checkbox\" name=\"typeCheckbox\" value=\""+type+"\">"+type+"("+count+") </div>");
        }
        out.println("<br>");
    }

    public void css(HttpServletRequest request, HttpServletResponse response){
        //https://stackoverflow.com/questions/13124234/best-way-to-include-javascript-in-java-servlets
        try {
            PrintWriter out = response.getWriter();
            out.println("<style>");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/css/menuStyle.css");
            dispatcher.include(request, response);
            out.println("</style>");
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void autoCompleteScript(HttpServletRequest request, HttpServletResponse response){
        try {
            PrintWriter out = response.getWriter();
            out.println("<script type='text/javascript'>");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/javascript/autoCompleteScript.js");
            dispatcher.include(request, response);
            out.println("</script>");
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

package be.vub.Linking.old;

import be.vub.Linking.MapElement3;
import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.util.FileManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.lang.System.out;

public class CallRDF2 {

    String dataLocation="D:/training/thesis/Parliament";
    String fileName="county.ttl";
    private Iterator<String> stringIterator;

    String queryString="PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
            "PREFIX ns: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
            "PREFIX geo: <http://www.opengis.net/ont/geosparql#>\n" +
            "SELECT * WHERE {\n" +
            "  ?element rdfs:label ?label;\n" +
            "           ns:type ?type;\n" +
            "           geo:hasGeometry ?geo.\n" +
            "  ?geo geo:asWKT ?wkt.\n" +
            //"  ?sub ?pred ?obj .\n" +
            //" filter (regex(STR(?element), 'http://data.geohive.ie/resource/county/2AE19629144A13A3E055000000000001'))"+
            "} \n" ;

    /*String queryString="PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
            "PREFIX ns: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
            "PREFIX geo: <http://www.opengis.net/ont/geosparql#>\n" +
            "SELECT * WHERE {\n" +
            "  ?element rdfs:label ?label;\n" +
            "           ns:type ?type;\n" +
            "           geo:hasGeometry ?geo.\n" +
            "  ?geo geo:asWKT ?wkt.\n" +
            //"  ?sub ?pred ?obj .\n" +
            //" filter (regex(STR(?element), 'http://data.geohive.ie/resource/county/2AE19629144A13A3E055000000000001'))"+
            "} \n" ;*/


    String queryAllElements="PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
            "PREFIX geo: <http://www.opengis.net/ont/geosparql#>\n" +
            "PREFIX geoff: <http://ontologies.geohive.ie/geoff#>\n" +
            "PREFIX ns: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
            "PREFIX osi: <http://ontologies.geohive.ie/osi#>\n"+
            "\n" +
            "SELECT ?element ?label ?type ?form ?function ?geo ?wkt ?lastUpdate\n" +
            "{\n" +
            "  ?element rdfs:label ?label;\n" +
            "           ns:type ?type;\n" +
            "           geoff:hasForm ?form;\n" +
            "           geoff:hasFunction ?function;\n" +
            "           osi:lastUpdate ?lastUpdate;\n"+
            "           geo:hasGeometry ?geo.\n" +
            "  ?geo geo:asWKT ?wkt.\n" +
            "}\n";
            //"limit 100";

    public List<MapElement2> doCallAll() {
        return getAllElements();
    }

    private List<MapElement2> getAllElements() {
        List<MapElement2> elements= new ArrayList<>();
/*
        //With parliament
        Map<String, String> predHm = new HashMap<>();
        String queryTypes="PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> SELECT distinct ?pred (count ( ?sub) as ?count) WHERE { ?sub ?pred ?obj. filter(?pred != rdf:type)} GROUP BY ?pred " ;
        org.apache.jena.query.Query query = org.apache.jena.query.QueryFactory.create(queryTypes);
        org.apache.jena.query.QueryExecution qexec =   org.apache.jena.query.QueryExecutionFactory.sparqlService("http://localhost:8089/parliament/sparql", query);
*/
        //with file

        // with endPoint: https://stackoverflow.com/questions/25835640/how-to-write-sparql-endpoint-to-dbpedia-in-java
        FileManager.get().addLocatorClassLoader(FilterSearch.class.getClassLoader());
        Model model = FileManager.get().loadModel(dataLocation+"/"+fileName);

        //Query query = QueryFactory.create(queryAllElements);
        Query query = QueryFactory.create(queryString);

        QueryExecution qexec = QueryExecutionFactory.create(query, model);

        try{
            ResultSet results= qexec.execSelect();
            while (results.hasNext()){
                QuerySolution soln = results.nextSolution();

/*                RDFNode sub = soln.get("sub");
                RDFNode pred = soln.get("pred");
                RDFNode obj = soln.get("obj");*/


                RDFNode uri = soln.get("element");
                RDFNode label = soln.get("label");
                RDFNode type = soln.get("type");
                //RDFNode form = soln.get("form");
                //RDFNode function = soln.get("function");
                RDFNode geo = soln.get("geo");
                RDFNode wkt = soln.get("wkt");
                //RDFNode lastUpdate = soln.get("lastUpdate");*/
/*
                out.println(sub.toString()+"<br>");
                out.println(pred.toString()+"<br>");
                out.println(obj.toString()+"<br>");
*/

                MapElement2 element = new MapElement2(uri.toString(),
                        wkt.toString(),
                        type.toString(), label.toString(), label.toString(), "",
                        "", geo.toString(), "" );
//*/


                out.println("<br>------<br>");
                elements.add(element);
            }
        } finally {
            qexec.close();
        }
        /*
        out.println("<script>function narrowTheResults() {\n" +
                "  console.log('basic refresh of the page, checking the checked comboxes');\n" +
                "  window.location.reload();\n" +
                "}</script>");


        out.println("<button onclick=\"narrowTheResults()\">Narrow the results</button><br>");

        out.println("<script>function showHideFilterElements(element) {\n" +
                "  var x = document.getElementById(element);\n" +
                "  if (x.style.display === \"none\") {\n" +
                "    x.style.display = \"block\";\n" +
                "  } else {\n" +
                "    x.style.display = \"none\";\n" +
                "  }\n" +
                "}</script>");
        for (Map.Entry entry : hm.entrySet()) {
            List<FilterElement> l = (List)entry.getValue();
            String fullProp=entry.getKey().toString();
            String property= fullProp.substring(fullProp.lastIndexOf("#") + 1);

            out.println("<div class='filterProperty'>"+property +"("+ l.size()+ " different values) <button onclick=\"showHideFilterElements('"+property+"filterDiv')\">show/hide elements</button> </div>"+
                    "<div id=\""+property+"filterDiv"+"\" style=\"display:none\">");
            for(FilterElement filterElement : l) {
                out.println("<div class='filterElement'>"+filterElement+"</div>" );
            }
            //out.println(entry.getValue()+"<br>" + "</div>");
            out.println("</div>");
        }
        out.println("<button onclick=\"narrowTheResults()\">Narrow the results</button><br>");
        */
        return elements;
    }

    public List<MapElement3> getAllElements3() {
        List<MapElement3> elements= new ArrayList<>();
        // with endPoint: https://stackoverflow.com/questions/25835640/how-to-write-sparql-endpoint-to-dbpedia-in-java
        FileManager.get().addLocatorClassLoader(FilterSearch.class.getClassLoader());
        Model model = FileManager.get().loadModel(dataLocation+"/"+fileName);

        //Query query = QueryFactory.create(queryAllElements);
        Query query = QueryFactory.create(queryString);

        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        try{
            ResultSet results= qexec.execSelect();
            while (results.hasNext()){
                QuerySolution soln = results.nextSolution();



                RDFNode uri = soln.get("element");
                RDFNode label = soln.get("label");
                RDFNode type = soln.get("type");

                RDFNode geo = soln.get("geo");
                RDFNode wkt = soln.get("wkt");


                MapElement3 element = new MapElement3(uri.toString(),
                        wkt.toString(),
                        type.toString(), label.toString(), label.toString(), "",
                        "", geo.toString(), "" );
//*/


                out.println("<br>------<br>");
                elements.add(element);
            }
        } finally {
            qexec.close();
        }
        return elements;
    }


}

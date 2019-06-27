package be.vub.Linking;

import be.vub.Linking.old.FilterSearch;
import be.vub.Linking.old.MapElement2;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateRequest;
import org.apache.jena.util.FileManager;

import java.util.*;

import static java.lang.System.out;

//NOT OK
public class InsertRDF {

    String dataLocation="D:/training/thesis/Parliament";
    String fileName="users.ttl";
    private Iterator<String> stringIterator;

/*    String insertQuery="INSERT DATA\n" +
            "  { \n" +
            "    GRAPH <urn:sparql:tests:insert:data> \n" +
            "      { \n" +
            "        <#book1> <#price> 42 \n" +
            "      } \n" +
            "  }";
*/

    //String insertQuery="INSERT DATA { GRAPH <urn:sparql:tests:insert:data> { <#book1> <#price> 42 } }";
    String insertQuery="INSERT DATA {  ws:name    'test'. }";
    String queryStarter="SELECT ?pred ?label ?type ?geo ?wkt \n" +
            "where {\n";


    String queryFinisher="  ?pred rdfs:label ?label;\n" +
            "           ns:type ?type;\n" +
            "           geo:hasGeometry ?geo.\n" +
            "  ?geo geo:asWKT ?wkt.\n" +
            " FILTER langMatches(lang(?label), \'en\')\n" +
            "}\n";
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

    /*public List<MapElement2> doCallAll() {
        return insertInterlink();
    }*/

    private List<MapElement2> insertInterlink() {
        List<MapElement2> elements= new ArrayList<>();

        //With parliament
        Map<String, String> predHm = new HashMap<>();
        String queryTypes="PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> SELECT distinct ?pred (count ( ?sub) as ?count) WHERE { ?sub ?pred ?obj. filter(?pred != rdf:type)} GROUP BY ?pred " ;
        //with file

        // with endPoint: https://stackoverflow.com/questions/25835640/how-to-write-sparql-endpoint-to-dbpedia-in-java
        FileManager.get().addLocatorClassLoader(FilterSearch.class.getClassLoader());
        Model model = FileManager.get().loadModel(dataLocation+"/"+fileName);


        UpdateRequest qexec = UpdateFactory.create();
        qexec.add(insertQuery);

    try{
            //ResultSet results= qexec.execSelect();
            ResultSet results= null;
            while (results.hasNext()){
                QuerySolution soln = results.nextSolution();



                RDFNode uri = soln.get("element");
                RDFNode label = soln.get("label");
                RDFNode type = soln.get("type");
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
            //qexec.close();
        }

        return elements;
    }
}

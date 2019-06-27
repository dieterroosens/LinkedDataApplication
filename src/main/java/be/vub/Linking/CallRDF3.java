package be.vub.Linking;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.RDFNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CallRDF3 {


    public static final PropertiesLoader propertiesLoader=new PropertiesLoader();
    public static final String ENDPOINT = propertiesLoader.getProp("elements.endpoint");

    String queryStarter="SELECT ?pred ?label ?type ?geo ?wkt \n" +
            "where {\n";


    String queryFinisher="  ?pred rdfs:label ?label;\n" +
            "           ns:type ?type;\n" +
            "           geo:hasGeometry ?geo.\n" +
            "  ?geo geo:asWKT ?wkt.\n" +
            " FILTER langMatches(lang(?label), \'en\')\n" +
            "}\n";


    /*public List<MapElement3> doCallAll() {
        return getAllElements();
    }*/

    /*private List<MapElement3> getAllElements() {
        List<MapElement3> elements= new ArrayList<>();

        //With parliament
        Map<String, String> predHm = new HashMap<>();
        String queryTypes="PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> SELECT distinct ?pred (count ( ?sub) as ?count) WHERE { ?sub ?pred ?obj. filter(?pred != rdf:type)} GROUP BY ?pred " ;
        org.apache.jena.query.Query query = org.apache.jena.query.QueryFactory.create(queryTypes);
        org.apache.jena.query.QueryExecution qexec =   org.apache.jena.query.QueryExecutionFactory.sparqlService(ENDPOINT, query);


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

                out.println("<br>------<br>");
                elements.add(element);
            }
        } finally {
            qexec.close();
        }

        return elements;
    }*/

    public List<MapElement3> getAllElements3(String filter) {
        List<MapElement3> elements= new ArrayList<>();
        URIprefix prefix = new URIprefix();
        //With parliament
        Map<String, String> predHm = new HashMap<>();
        String sparqlQuery=prefix.printAllForQuery()+queryStarter+
                filter+
                queryFinisher+"";
        //queryAllElements
        System.out.println("SparqlQuery:"+sparqlQuery);
        org.apache.jena.query.Query query = org.apache.jena.query.QueryFactory.create(sparqlQuery);
        org.apache.jena.query.QueryExecution qexec =   org.apache.jena.query.QueryExecutionFactory.sparqlService(ENDPOINT, query);

        try{
            ResultSet results= qexec.execSelect();
            while (results.hasNext()){
                QuerySolution soln = results.nextSolution();



                RDFNode uri = soln.get("pred");
                RDFNode label = soln.get("label");
                RDFNode type = soln.get("type");

                RDFNode geo = soln.get("geo");
                RDFNode wkt = soln.get("wkt");


                MapElement3 element = new MapElement3(uri.toString(),
                        wkt.toString(),
                        type.toString(), label.toString(), label.toString(), "",
                        "", geo.toString(), "" );


                elements.add(element);
            }
        } finally {
            qexec.close();
        }
        return elements;
    }


}

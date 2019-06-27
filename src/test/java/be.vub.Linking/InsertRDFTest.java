package be.vub.Linking;


import com.hp.hpl.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.update.*;
import org.apache.jena.util.FileManager;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

import static java.lang.System.out;

public class InsertRDFTest {

    @Test
    public void testAdd() {
        Assert.assertEquals(17, 5+12);
    }

    String insertQuery="INSERT DATA\n" +
            "  { \n" +
            "    GRAPH <urn:sparql:tests:insert:data> \n" +
            "      { \n" +
            "        <#book1> <#price> 42 \n" +
            "      } \n" +
            "  }";

    String queryQuery="PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
            "PREFIX ns: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
            "PREFIX geo: <http://www.opengis.net/ont/geosparql#>\n" +
            "SELECT * where {"+
            "  ?sub ?pred ?obj .\n" +
            "} \n" ;


    @Test
    public void testInsert(){
        String dataLocation="D:/training/thesis/Parliament";
        String fileName="users.ttl";
        FileManager fileManager = new FileManager();
        File tempFile = new File(dataLocation+"/"+fileName);
        boolean exists = tempFile.exists();
        System.out.println("exists:"+exists);
        Model model = FileManager.get().loadModel(dataLocation+"/"+fileName);
        com.hp.hpl.jena.rdf.model.Model model2 = com.hp.hpl.jena.util.FileManager.get().loadModel(dataLocation+"/"+fileName);

        GraphStore graphStore = GraphStoreFactory.create(model) ;
        //String sparqlQuery = "PREFIX ns: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> INSERT DATA {  <element1_uri> ns:value 5. }";
        String sparqlQuery = "SPARQL INSERT  IN GRAPH <elements>{  <element1_uri> http://www.w3.org/1999/02/22-rdf-syntax-ns#value 5. }";


        UpdateRequest request = UpdateFactory.create(insertQuery);
        //request.add("CLEAR GRAPH <http://foo.bar>");
        //request.add("CREATE GRAPH <http://foo.bar>");
        //request.add("INSERT DATA { GRAPH <http://foo.bar> {  <#a> <#b> <#d> }}");
        UpdateProcessor u = UpdateExecutionFactory.createRemote(request, "http://localhost:3030/inferred");
        u.execute();



        Query query = QueryFactory.create(queryQuery);

        QueryExecution qexec = QueryExecutionFactory.create(query, model2);


        try{
            ResultSet results= qexec.execSelect();
            while (results.hasNext()){
                QuerySolution soln = results.nextSolution();


                out.println("<br>------<br>");
                //elements.add(element);
            }
        } finally {
            qexec.close();
        }
    }


}

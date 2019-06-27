package be.vub.Linking;


import org.apache.jena.graph.Triple;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateProcessor;
import org.apache.jena.update.UpdateRequest;
import org.junit.Assert;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.UUID;

public class InsertRDFArqJenaTest {

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
    String insertQuery2 = "PREFIX ns: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> INSERT DATA {  <element1_uri> ns:value 5. }";

    String insertQuery3=" INSERT DATA{  <http://www.semanticweb.org/vub/ois/sportswear#name> \"TEST\" .}";
    String insertQuery4 = " INSERT DATA { GRAPH <http://foo.bar> {  <#a> <#b> <#c> }}";



    String queryQuery="PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
            "PREFIX ns: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
            "PREFIX geo: <http://www.opengis.net/ont/geosparql#>\n" +
            "SELECT * where {"+
            "  ?sub ?pred ?obj .\n" +
            "} \n" ;

    String dataLocation="D:/training/thesis/Parliament/newDataset/";
    String fileName="users.ttl";

    @Test
    public void testMe(){
        UpdateRequest request = UpdateFactory.create();
        request.add("CLEAR GRAPH <http://foo.bar>");
        request.add("CREATE GRAPH <http://foo.bar>");
        request.add("INSERT DATA { GRAPH <http://foo.bar> {  <#a> <#b> <#d> }}");
        UpdateProcessor u = UpdateExecutionFactory.createRemote(request, "http://localhost:3030/inferred");
        u.execute();
    }

    @Test
    public void delete1graph(){
        String graphName="<http://foo.bar>";
        UpdateRequest request = UpdateFactory.create();
        request.add("CLEAR GRAPH "+graphName);
        UpdateProcessor u = UpdateExecutionFactory.createRemote(request, "http://localhost:3030/inferred");
        u.execute();
    }

    @Test
    public void deleteAllOfUsersGraph(){
        String graphName="<http://foo.bar>";
        UpdateRequest request = UpdateFactory.create();
        request.add("CLEAR GRAPH "+graphName);
        UpdateProcessor u = UpdateExecutionFactory.createRemote(request, "http://localhost:3030/inferred");
        u.execute();
    }

    @Test
    public void cleanGraph() {
        String graphName="urn:sparql:tests:insert:data";
        UpdateRequest request = UpdateFactory.create();
        String fs;
        fs = String.format("CLEAR GRAPH <%s>",graphName);
        request.add(fs);
        UpdateProcessor u = UpdateExecutionFactory.createRemote(request, "http://localhost:3030/inferred");
        u.execute();
    }


    @Test
    public void complexInsert(){
        UpdateRequest request = UpdateFactory.create();

        //Parameters
        String userName="participant10";
        String subject="<http://dbpedia.org/resource/Dublin>";
        String predicate="<http://www.opengis.net/def/function/geosparql/sfWithin>";
        String object="<http://dbpedia.org/resource/Belgium>";

        /*
        String userName="TestUser1";
        String subject="<http://data.geohive.ie/resource/building/6beb9b5341974ea185280e86bf8ad1c2>";
        String predicate="withIn";
        String object="<http://data.geohive.ie/resource/county/2AE19629144F13A3E055000000000001>";
*/
        //Fixed
        String graphName="urn:sparql:tests:interlink:"+UUID.randomUUID().toString();
        //String graphName="urn:sparql:tests:interlink";
        String activityID= UUID.randomUUID().toString();
        String agentID= UUID.randomUUID().toString();
        String entityID= UUID.randomUUID().toString();
        String statementID= UUID.randomUUID().toString();
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(new Date());
        String startedAtTime="\""+timeStamp+"\"^^xsd:dateTime";
        String endAtTime="\""+timeStamp+"\"^^xsd:dateTime";



        String fs;
        fs = String.format("prefix prov: <http://www.w3.org/ns/prov#> " +
                        "prefix xsd:  <http://www.w3.org/2001/XMLSchema#> "+
                        "prefix foaf: <http://xmlns.com/foaf/0.1/> " +
                        "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
                        "INSERT DATA\n" +
                        "  { \n" +
                        "    GRAPH <%s> \n" +
                        "      { \n" +
                        "        <urn:agent:%s> a foaf:Person, prov:Agent;\n" +
                        "                    foaf:givenName '%s'.\n" +
                        "        <urn:activity:%s> a prov:Activity;\n" +
                        "                       prov:wasAssociatedWith <urn:agent:%s>;\n" +
                        "                       prov:startAtTime  %s;\n" +
                        "                       prov:endedAtTime  %s.\n" +
                        "        <urn:statement:%s> a rdf:Statement;\n" +
                        "                        rdf:subject %s;\n" +
                        "                        rdf:predicate %s;\n" +
                        "                        rdf:object %s.\n" +
                        "        <urn:entity:%s> a prov:Entity;\n" +
                        "                        prov:wasAttributedTo <urn:agent:%s>;\n" +
                        "                        prov:value <urn:statement:%s>;\n"+
                        "                        prov:wasGeneratedBy  <urn:activity:%s>.\n" +
                        "\n" +
                        "      } \n" +
                        "  }"
                        , graphName, agentID, userName, activityID, agentID, startedAtTime, endAtTime,statementID, subject, predicate, object, entityID, agentID, statementID, activityID);
        System.out.println("query: "+fs);
        request.add(fs);
        UpdateProcessor u = UpdateExecutionFactory.createRemote(request, "http://localhost:3030/inferred");
        u.execute();

    }

    @Test
    public void getAllInterlinks(){
        String graphName="urn:sparql:tests:interlink";
        String selectAllFromAllGraphs="select ?graph ?subject ?predicate ?object where\n" +
                "{\n" +
                "  graph ?graph {?subject ?predicate ?object }\n" +
                "}";

        String sparqlQuery="prefix foaf: <http://xmlns.com/foaf/0.1/> \n" +
                "prefix prov: <http://www.w3.org/ns/prov#>\n" +
                "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +

                "select ?graph ?name ?time ?subject ?predicate ?object where \n" +
                "{\n" +
                " graph <"+graphName+">\n " +
                "{" +
                //"?subject ?predicate ?object " +
                //"?subject foaf:givenName ?name \n" +
                //"?subject foaf:givenName ?object \n" +

                "?element prov:wasAttributedTo ?agent;\n" +
                "   prov:value ?statement;\n"+
                "   prov:wasGeneratedBy ?activity.\n" +
                "?agent foaf:givenName ?name.\n"+
                "?activity prov:startAtTime ?time.\n"+
                "?statement rdf:subject ?subject;\n"+
                "   rdf:predicate ?predicate;\n"+
                "   rdf:object ?object.\n"+
                "}" +
                "}" +
                " ORDER BY ASC(?time)"
                ;
        System.out.println(sparqlQuery);
        Query query = org.apache.jena.query.QueryFactory.create(sparqlQuery);
        QueryExecution qexec =   QueryExecutionFactory.sparqlService("http://localhost:3030/inferred", query);
        try{
            ResultSet results= qexec.execSelect();
            while (results.hasNext()){
                QuerySolution soln = results.nextSolution();



                //RDFNode graph = soln.get("graph");
                RDFNode name = soln.get("name");
                RDFNode time = soln.get("time");

                RDFNode subject = soln.get("subject");
                RDFNode predicate = soln.get("predicate");
                RDFNode object = soln.get("object");

                //System.out.println("Line: "+graph.toString()+" - "+name.toString()+" - "+time.toString()+" - "+subject.toString()+" - "+predicate.toString()+" - "+object.toString());
                System.out.println("Line: "+name.toString()+" - "+time.toString()+" - "+subject.toString()+" - "+predicate.toString()+" - "+object.toString());



                //elements.add(element);
            }
        } finally {
            qexec.close();
        }
    }

    @Test
    public void getInterlinksOf1User(){
        String selectAllFromAllGraphs="select ?graph ?subject ?predicate ?object where\n" +
                "{\n" +
                "  graph ?graph {?subject ?predicate ?object }\n" +
                "}";

        String sparqlQuery="prefix foaf: <http://xmlns.com/foaf/0.1/> \n" +
                "prefix prov: <http://www.w3.org/ns/prov#>\n" +
                "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +

                "select ?name ?time ?subject ?predicate ?object where \n" +
                "{\n" +
                " graph ?graph\n " +
                "{" +
                //"?subject ?predicate ?object " +
                //"?subject foaf:givenName ?name \n" +
                //"?subject foaf:givenName ?object \n" +

                "?element prov:wasAttributedTo ?agent;\n" +
                "   prov:value ?statement;\n"+
                "   prov:wasGeneratedBy ?activity.\n" +
                "?agent foaf:givenName ?name.\n"+
                "?agent foaf:givenName 'n2'.\n"+
                "?activity prov:startAtTime ?time.\n"+
                "?statement rdf:subject ?subject;\n"+
                "   rdf:predicate ?predicate;\n"+
                "   rdf:object ?object.\n"+
                "}" +
                "}" +
                " ORDER BY ASC(?time)"
                ;
        System.out.println(sparqlQuery);
        Query query = org.apache.jena.query.QueryFactory.create(sparqlQuery);
        QueryExecution qexec =   QueryExecutionFactory.sparqlService("http://localhost:3030/inferred", query);
        try{
            ResultSet results= qexec.execSelect();
            while (results.hasNext()){
                QuerySolution soln = results.nextSolution();



                RDFNode graph = soln.get("graph");
                RDFNode name = soln.get("name");
                RDFNode time = soln.get("time");

                RDFNode subject = soln.get("subject");
                RDFNode predicate = soln.get("predicate");
                RDFNode object = soln.get("object");

                System.out.println("Line: "+graph.toString()+" - "+name.toString()+" - "+time.toString()+" - "+subject.toString()+" - "+predicate.toString()+" - "+object.toString());



                //elements.add(element);
            }
        } finally {
            qexec.close();
        }
    }

    @Test
    public void delete1triple(){
        String userName="n2";

        String deleteQuery1="WITH <urn:sparql:tests:delete:data> DELETE { ?agent ?activity ?statement }" +
                "WHERE {?agent foaf:givenName '"+userName+"'." +
                "}";

        String deleteQuery2="DELETE DATA FROM <urn:sparql:tests:delete:data> {" +
                "?agent foaf:givenName '"+userName+"'." +
                "}";

        String deleteQuery3="delete where { 'n2' ?x ?y }\n";

        System.out.println(deleteQuery3);
        Query query = org.apache.jena.query.QueryFactory.create(deleteQuery3);
        QueryExecution qexec =   QueryExecutionFactory.sparqlService("http://localhost:3030/inferred", query);
        try{
            ResultSet results= qexec.execSelect();
            while (results.hasNext()){
                QuerySolution soln = results.nextSolution();



                RDFNode name = soln.get("name");
                RDFNode time = soln.get("time");

                RDFNode subject = soln.get("subject");
                RDFNode predicate = soln.get("predicate");
                RDFNode object = soln.get("object");

                System.out.println("Line: "+name.toString()+" - "+time.toString()+" - "+subject.toString()+" - "+predicate.toString()+" - "+object.toString());



                //elements.add(element);
            }
        } finally {
            qexec.close();
        }
    }

    @Test
    public void testDeleteElementsOf1User(){

        ProvenanceHandling provenanceHandling=new ProvenanceHandling();
        String userName="TestUser1";
        String subject="http://data.geohive.ie/resource/building/6beb9b5341974ea185280e86bf8ad1c2";
        String predicate="withIn";
        String object="<http://data.geohive.ie/resource/county/2AE19629144F13A3E055000000000001>";
        String userAgentID ="TestUserAgentID";
        provenanceHandling.complexInsert(userName, subject, predicate, object, userAgentID);


        UpdateRequest request = UpdateFactory.create();
        //Parameters
        //Fixed
        String graphName="urn:sparql:tests:insert:data";
        String activityID= UUID.randomUUID().toString();
        String agentID= UUID.randomUUID().toString();
        String entityID= UUID.randomUUID().toString();
        String statementID= UUID.randomUUID().toString();
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(new Date());
        String startedAtTime="\""+timeStamp+"\"^^xsd:dateTime";
        String endAtTime="\""+timeStamp+"\"^^xsd:dateTime";



        String fs;
        /*fs = String.format("DELETE DATA FROM <urn:sparql:tests:delete:data> { +\n" +
                                " ?agent foaf:givenName '%s'. \n" +
                                " };", userName);*/
        fs = String.format("WITH <%s>\n" +
                "DELETE { ?person ?property ?value } \n" +
                "WHERE \n" +
                " { ?person ?property ?value ; " +
                " <http://xmlns.com/foaf/0.1/givenName> '%s'. " +
                " } ", graphName, userName);
        System.out.println("query: "+fs);
        request.add(fs);
        UpdateProcessor u = UpdateExecutionFactory.createRemote(request, "http://localhost:3030/inferred");
        u.execute();
    }

    @Test
    public void testDelete1Element(){

        ProvenanceHandling provenanceHandling=new ProvenanceHandling();
        String userName="TestUser1";
        String subject="http://data.geohive.ie/resource/building/6beb9b5341974ea185280e86bf8ad1c2";
        String predicate="withIn";
        String object="<http://data.geohive.ie/resource/county/2AE19629144F13A3E055000000000001>";
        String userAgentID ="TestUserAgentID";
        provenanceHandling.complexInsert(userName, subject, predicate, object, userAgentID);


        UpdateRequest request = UpdateFactory.create();
        //Parameters
        //Fixed
        String graphName="urn:sparql:tests:insert:data";
        String activityID= UUID.randomUUID().toString();
        String agentID= UUID.randomUUID().toString();
        String entityID= UUID.randomUUID().toString();
        String statementID= UUID.randomUUID().toString();
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(new Date());
        String startedAtTime="\""+timeStamp+"\"^^xsd:dateTime";
        String endAtTime="\""+timeStamp+"\"^^xsd:dateTime";



        String fs;
        /*fs = String.format("DELETE DATA FROM <urn:sparql:tests:delete:data> { +\n" +
                                " ?agent foaf:givenName '%s'. \n" +
                                " };", userName);*/
        fs = String.format("WITH <%s>\n" +
                "DELETE { ?person ?property ?value } \n" +
                "WHERE \n" +
                " { ?person ?property ?value ; " +
                " <http://xmlns.com/foaf/0.1/givenName> '%s' " +
                " } ", graphName, userName);
        System.out.println("query: "+fs);
        request.add(fs);
        UpdateProcessor u = UpdateExecutionFactory.createRemote(request, "http://localhost:3030/inferred");
        u.execute();
    }

    @Test
    public void testConstruct(){
        StringBuffer sb=new StringBuffer();
        String userName="n2";
        System.out.println("In getConstruct");
        String prefixes="PREFIX foaf: <http://xmlns.com/foaf/0.1/> \n" +
                "                PREFIX prov: <http://www.w3.org/ns/prov#>\n" +
                "                PREFIX geof: <http://www.opengis.net/def/function/geosparql/>\n" +
                "                PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n";
        sb.append(prefixes);
        String sparqlQuery=prefixes+
                "CONSTRUCT { ?subject ?predicate ?object.}\n" +
                "where \n" +
                "{\n" +
                " graph ?g\n" +
                " {?element prov:wasAttributedTo ?agent;\n" +
                "   prov:value ?statement;\n" +
                "   prov:wasGeneratedBy ?activity.\n" +
                "?agent foaf:givenName ?name.\n" +
                "?agent foaf:givenName '"+userName+"'.\n" +
                "?activity prov:startAtTime ?time.\n" +
                "?statement rdf:subject ?subject;\n" +
                "   rdf:predicate ?predicate;\n" +
                "   rdf:object ?object.\n" +
                "}} ORDER BY ASC(?time)";
        System.out.println(sparqlQuery);
        Query query = org.apache.jena.query.QueryFactory.create(sparqlQuery);
        QueryExecution qexec =   QueryExecutionFactory.sparqlService("http://localhost:3030/inferred", query);
        try{

            Iterator<Triple> triples = qexec.execConstructTriples();

            while(triples.hasNext()){
                Triple triple = triples.next();
                System.out.println("1:"+triple.getSubject());
                System.out.println("2:"+triple.getPredicate());
                System.out.println("3:"+triple.getObject());
                System.out.println("4:"+triple.getMatchSubject());
                System.out.println("5:"+triple.getMatchPredicate());
                System.out.println("6:"+triple.getMatchSubject());
                System.out.println("triple: "+triple.toString());
                sb.append("<"+triple.getSubject()+"> <"+triple.getPredicate()+"> <"+triple.getObject()+">. " );
            }
            //Model model = qexec.execConstruct();
            //System.out.println("model:"+model.toString());

        }
        finally {
            qexec.close();
        }
        System.out.println("FileContent:"+sb.toString());

    }

}

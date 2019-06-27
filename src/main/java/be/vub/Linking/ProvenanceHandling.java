package be.vub.Linking;

import org.apache.jena.graph.Triple;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateProcessor;
import org.apache.jena.update.UpdateRequest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.UUID;

public class ProvenanceHandling {

    public static final PropertiesLoader propertiesLoader=new PropertiesLoader();
    public static final String ENDPOINT = propertiesLoader.getProp("provenance.endpoint");

    public void clearGraph(String graph){
        System.out.println("clear Graph: "+graph);
        String graphName="<"+graph+">";
        UpdateRequest request = UpdateFactory.create();
        request.add("CLEAR GRAPH "+graphName);
        UpdateProcessor u = UpdateExecutionFactory.createRemote(request, propertiesLoader.getProp(ENDPOINT));
        u.execute();
    }

    public void createGraph(String graph){
        System.out.println("create Graph: "+graph);
        String graphName="<"+graph+">";
        UpdateRequest request = UpdateFactory.create();
        request.add("CREATE GRAPH "+graphName);
        UpdateProcessor u = UpdateExecutionFactory.createRemote(request, ENDPOINT);
        u.execute();
    }

    public void complexInsert(String userName, String subject, String predicate, String object, String agentID){
        UpdateRequest request = UpdateFactory.create();

        //Parameters
        /*String userName="TestUser1";
        String subject="<http://data.geohive.ie/resource/building/6beb9b5341974ea185280e86bf8ad1c2>";
        String predicate="withIn";
        String object="<http://data.geohive.ie/resource/county/2AE19629144F13A3E055000000000001>";
        */

        //Fixed
        String graphName="urn:sparql:interlink:"+UUID.randomUUID().toString();;
        String activityID= UUID.randomUUID().toString();
        //user.agentID
        //String agentID= UUID.randomUUID().toString();
        String entityID= UUID.randomUUID().toString();
        String statementID= UUID.randomUUID().toString();
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(new Date());
        String startedAtTime="\""+timeStamp+"\"^^xsd:dateTime";
        String endAtTime="\""+timeStamp+"\"^^xsd:dateTime";

        String[] parts=predicate.split(":");
        URIprefix urIprefix = new URIprefix();
        String predicateStart = urIprefix.replaceConstantByFullName(parts[0]);

        createGraph(graphName);

        String fs;
        fs = String.format("prefix prov: <http://www.w3.org/ns/prov#> \n" +
                        "prefix xsd:  <http://www.w3.org/2001/XMLSchema#> \n"+
                        "prefix foaf: <http://xmlns.com/foaf/0.1/> \n" +
                        "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"+
                        "PREFIX geof: <http://www.opengis.net/def/function/geosparql/> \n"+
                        "PREFIX owl:<http://www.w3.org/2002/07/owl#> \n"+
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
                        "                        rdf:subject <%s>;\n" +
                        "                        rdf:predicate <%s>;\n" +
                        "                        rdf:object %s.\n" +
                        "        <urn:entity:%s> a prov:Entity;\n" +
                        "                        prov:wasAttributedTo <urn:agent:%s>;\n" +
                        "                        prov:value <urn:statement:%s>;\n"+
                        "                        prov:wasGeneratedBy  <urn:activity:%s>.\n" +
                        "\n" +
                        "      } \n" +
                        "  }"
                , graphName, agentID, userName, activityID, agentID, startedAtTime, endAtTime,statementID, subject, predicateStart+parts[1], object, entityID, agentID, statementID, activityID);
        System.out.println("query: "+fs);
        request.add(fs);
        UpdateProcessor u = UpdateExecutionFactory.createRemote(request, ENDPOINT);
        u.execute();

    }

    public String getAllProvenanceData(String userName){
        System.out.println("In getAll");
        StringBuffer data=new StringBuffer();
        data.append("<script>"+
        "function download(text) {"+
            "var element = document.createElement('a');"+

            "element.setAttribute('href', 'data:text/plain;charset=utf-8,' + encodeURIComponent(text));"+
            "element.setAttribute('download', 'interlinks_"+userName+".ttl');"+
            "element.style.display = 'none';"+
            "document.body.appendChild(element);"+
            "element.click();"+
            "document.body.removeChild(element);"+
        "}"+
        "</script>");
        data.append("<table class='table table-striped table-sm'>\n" +
                "  <thead>\n" +
                "   <tr>" +
                "    <th scope='col'>Who</th>\n" +
                "    <th scope='col'>When</th>\n" +
                "    <th scope='col'>Subject</th>\n" +
                "    <th scope='col'>Predicate</th>\n" +
                "    <th scope='col'>Object</th>\n" +
                "    <th scope='col'>&nbsp;</th>\n" +
                "  </tr>" +
                "</thead>\n" +
                " <tbody>\n");
        String sparqlQuery="prefix foaf: <http://xmlns.com/foaf/0.1/> \n" +
                "prefix prov: <http://www.w3.org/ns/prov#>\n" +
                "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +

                "select ?graph ?name ?time ?subject ?predicate ?object where \n" +
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
                "?agent foaf:givenName '"+userName+"'.\n"+
                "?activity prov:startAtTime ?time.\n"+
                "?statement rdf:subject ?subject;\n"+
                "   rdf:predicate ?predicate;\n"+
                "   rdf:object ?object.\n"+
                "}" +
                "}" +
                " ORDER BY ASC(?time)";
        System.out.println(sparqlQuery);
        Query query = org.apache.jena.query.QueryFactory.create(sparqlQuery);
        QueryExecution qexec =   QueryExecutionFactory.sparqlService(ENDPOINT, query);
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
                data.append("<tr><td>"+name.toString()+"</td><td>"+time.toString()+"</td><td>"+subject.toString()+"</td><td>"+predicate.toString()+"</td><td>"+object.toString()+"</td><td><a class='badge badge-primary' href='deleteInterlink?graph="+graph.toString()+"'>Delete interlink</a></td></tr>");



                //elements.add(element);
            }
        }
        finally {
            qexec.close();
        }

        data.append("<tr><td colspan=6>" +
                "<form onsubmit=\"download(this['text'].value)\">\n" +
                "  <textarea name=\"text\" style=\"display:none;\">"+getConstructProvenanceData(userName)+"</textarea>\n" +
                "  <input type=\"submit\" class='btn btn-primary' value=\"Download triples\">\n" +
                "</form>");
                //"<button type='button' onclick='' class='btn btn-primary'>Download as RDF</button></td></tr>");
        data.append("</tbody></table>");
        return data.toString();
    }

    public String getConstructProvenanceData(String userName){
        System.out.println("In getConstruct");
        StringBuffer sb=new StringBuffer();
        System.out.println("In getConstruct");
        String prefixes="PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" +
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
        QueryExecution qexec =   QueryExecutionFactory.sparqlService(ENDPOINT, query);
        try{

            Iterator<Triple> triples = qexec.execConstructTriples();

            while(triples.hasNext()){
                Triple triple = triples.next();
                sb.append("<"+triple.getSubject()+"> <"+triple.getPredicate()+"> <"+triple.getObject()+">. " );
            }
            //Model model = qexec.execConstruct();
            //System.out.println("model:"+model.toString());

        }
        finally {
            qexec.close();
        }
        return sb.toString();
    }
}

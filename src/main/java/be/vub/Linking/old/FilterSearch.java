package be.vub.Linking.old;

import be.vub.Linking.FilterElement;
import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.util.FileManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@WebServlet(name = "FilterSearchServlet", urlPatterns = {"/filtersearch","/filterSearch"})
public class FilterSearch extends HttpServlet {

        String dataLocation="D:/training/thesis/Parliament";
        String fileName="SHOPPING_CENTRE_WGS84.n3";
    private Iterator<String> stringIterator;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        }

        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<h3>Select the visible element</h3>");

            makeFilter(out);

            out.println("<h3>Select the visible element above</h3>");
        }

    private void makeFilter(PrintWriter out) {
        Map<String, List> hm = new HashMap<String, List>();
// https://github.com/ncbo/sparql-code-examples/blob/master/java/src/org/ncbo/stanford/sparql/examples/JenaARQTest.java
// with endPoint: https://stackoverflow.com/questions/25835640/how-to-write-sparql-endpoint-to-dbpedia-in-java
        FileManager.get().addLocatorClassLoader(FilterSearch.class.getClassLoader());
        Model model = FileManager.get().loadModel(dataLocation+"/"+fileName);
        String queryString="PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "SELECT * WHERE {\n" +
                "  ?sub ?pred ?obj .\n" +
                "} \n" ;

/*
1. get all types/classes
    PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
    PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
    SELECT distinct ?a ?classLabel
    WHERE {
        ?class rdf:type ?a.
        optional {?a rdfs:label ?classLabel}
    }
2. get all the elements that are in those types by type
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
SELECT distinct ?element
WHERE {
?element rdf:type ?a.
}
3. get all properties of the elements that are in those types by type
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
SELECT distinct ?a ?element
WHERE {
?element rdf:type ?a.
?element ?pred ?obj .
}
*/
               // "LIMIT 100";
        Query query = QueryFactory.create(queryString);
        QueryExecution qexec =   QueryExecutionFactory.sparqlService("http://localhost:8089/parliament/sparql", query);

        //QueryExecution qexec = QueryExecutionFactory.create(query, model);
        try{
            ResultSet results= qexec.execSelect();
            /*results.getResultVars();
            for(String resultVar : results.getResultVars()) {
                out.println("resultVar: "+resultVar+"<br>");
            }*/

            while (results.hasNext()){

                QuerySolution soln = results.nextSolution();
                RDFNode pred = soln.get("pred");
                RDFNode obj = soln.get("obj");
                RDFNode sub = soln.get("sub");
                //out.println("get p:"+pred+"<br>");
                //out.println("get o:"+obj+"<br>");
                String elementName = pred.toString();
                //String elementName = sub.toString()+"-Dieter-"+pred.toString();
                if(!hm.containsKey(elementName))
                {
                    //New property
                    List<FilterElement> innerList = new ArrayList();
                    innerList.add(new FilterElement(elementName, obj.toString(),1));
                    hm.put(elementName,innerList);
                }
                else{
                    //existing property
                    List<FilterElement> innerList = hm.get(elementName);
                    Iterator<FilterElement> innerListIterator = innerList.iterator();
                    boolean found=false;
                    while (innerListIterator.hasNext()) {
                        //existing object
                        FilterElement fe = innerListIterator.next();
                        if(fe.getObjectField().equals(obj.toString())){
                            fe.increaseCounter();
                            found=true;
                        }
                    }
                    if(!found){
                        //new object
                        innerList.add(new FilterElement(elementName, obj.toString(),1));
                    }

                }
                //out.println("<br>------<br>");
            }
        } finally {
            qexec.close();
        }
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
            //String property= fullProp.substring(fullProp.lastIndexOf("#") + 1);
            String property= fullProp;

            out.println("<div class='filterProperty'>"+property +"("+ l.size()+ " different values) <button onclick=\"showHideFilterElements('"+property+"filterDiv')\">show/hide elements</button> </div>"+
                            "<div id=\""+property+"filterDiv"+"\" style=\"display:none\">");
                                    for(FilterElement filterElement : l) {
                                        out.println("<div class='filterElement'>"+filterElement+"</div>" );
                                    }
            //out.println(entry.getValue()+"<br>" + "</div>");
            out.println("</div>");
        }
        out.println("<button onclick=\"narrowTheResults()\">Narrow the results</button><br>");
    }

}

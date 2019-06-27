package be.vub.Linking.old;

import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.util.FileManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CallRDF {

    String dataLocation="D:/training/thesis/Parliament";
    String fileName="SHOPPING_CENTRE_WGS84.n3";
    private Iterator<String> stringIterator;

    String queryString="PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
            "SELECT * WHERE {\n" +
            "  ?sub ?pred ?obj .\n" +
            "} \n" ;

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

    public List<MapElement> doCallAll() {
        return getAllElements();
    }

    private List<MapElement> getAllElements() {
        List<MapElement> elements= new ArrayList<>();
        // with endPoint: https://stackoverflow.com/questions/25835640/how-to-write-sparql-endpoint-to-dbpedia-in-java
        FileManager.get().addLocatorClassLoader(FilterSearch.class.getClassLoader());
        Model model = FileManager.get().loadModel(dataLocation+"/"+fileName);

        Query query = QueryFactory.create(queryAllElements);
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        try{
            ResultSet results= qexec.execSelect();
            while (results.hasNext()){
                QuerySolution soln = results.nextSolution();
                RDFNode uri = soln.get("element");
                RDFNode label = soln.get("label");
                RDFNode type = soln.get("type");
                RDFNode form = soln.get("form");
                RDFNode function = soln.get("function");
                RDFNode geo = soln.get("geo");
                RDFNode wkt = soln.get("wkt");
                RDFNode lastUpdate = soln.get("lastUpdate");




                MapElement element = new MapElement(uri.toString(),
                        wkt.toString(),
                        type.toString(), label.toString(), label.toString(), form.toString(),
                        function.toString(), geo.toString(), lastUpdate.toString() );


                /*
                if(!hm.containsKey(pred.toString()))
                {
                    //New property
                    List<FilterElement> innerList = new ArrayList();
                    innerList.add(new FilterElement(pred.toString(), obj.toString(),1));
                    hm.put(pred.toString(),innerList);
                }
                else{
                    //existing property
                    List<FilterElement> innerList = hm.get(pred.toString());
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
                        innerList.add(new FilterElement(pred.toString(), obj.toString(),1));
                    }

                }*/
                //out.println("<br>------<br>");
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


}

package be.vub.Linking.old;

import be.vub.Linking.Element;
import be.vub.Linking.FilterElement;
import be.vub.Linking.TypeElement;
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

@WebServlet(name = "FilterSearch2Servlet", urlPatterns = {"/filtersearch2","/filterSearch2"})
public class FilterSearch2 extends HttpServlet {

        String dataLocation="D:/training/thesis/Parliament";
        String fileName="SHOPPING_CENTRE_WGS84.n3";
    private Iterator<String> stringIterator;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        }

        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();

            out.println(css());
            out.println(autoCompleteScript());
            makeFilter(out);
        }

    private void makeFilter(PrintWriter out) {
        Map<String, List> hm = new HashMap<String, List>();
        Map<String, TypeElement> typeHm = new HashMap<String, TypeElement>();
// with endPoint: https://stackoverflow.com/questions/25835640/how-to-write-sparql-endpoint-to-dbpedia-in-java
        FileManager.get().addLocatorClassLoader(FilterSearch2.class.getClassLoader());
        Model model = FileManager.get().loadModel(dataLocation+"/"+fileName);
        /*String queryString="PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "SELECT * WHERE {\n" +
                "  ?sub ?pred ?obj .\n" +
                "} \n" ;*/
        String queryString="PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "SELECT distinct ?type ?element ?pred ?value\n" +
                " WHERE {\n" +
                " ?element rdf:type ?type.\n"+
                " ?element ?pred ?value.\n" +
                //" bind (STR(?element) as ?element_str).\n" +
                //" LET ( ?element_str := STR(?element) )"+

                //" filter (regex(STR(?element), 'http://data.geohive.ie/resource/building/b56d90faf8e04036a64e88a39fc3f666') ||\n" +
                //" regex(STR(?element), 'urn:osi:build:geom:pnt:b56d90faf8e04036a64e88a39fc3f6662012-01-01T00%3A00%3A00'))"+
                "} \n" ;

/*
1. get all types/classes
    PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
    PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
    SELECT distinct ?type ?typeLabel
    WHERE {
        ?class rdf:type ?type.
        optional {?type rdfs:label ?typeLabel}
    }
2. get all the elements that are in those types by type
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
SELECT distinct ?type ?element
WHERE {
?element rdf:type ?type.
}
3. get all properties of the elements that are in those types by type
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
SELECT distinct ?type ?element ?pred ?value
WHERE {
?element rdf:type ?type.
?element ?pred ?value .
}

example:
PREFIX urn: <http://fliqz.com/>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
SELECT distinct ?type ?element ?pred ?value
WHERE {
?element rdf:type ?type.
?element ?pred ?value .
  bind (STR(?element) as ?element_str).
  filter (regex(?element_str, "http://data.geohive.ie/resource/building/b56d90faf8e04036a64e88a39fc3f666") ||
   regex(?element_str, "urn:osi:build:geom:pnt:b56d90faf8e04036a64e88a39fc3f6662012-01-01T00%3A00%3A00"))
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

                RDFNode type = soln.get("type");
                RDFNode element = soln.get("element");
                RDFNode pred = soln.get("pred");
                RDFNode value = soln.get("value");
                String predName = pred.toString();

                Element e=new Element(element.toString(), predName, value.toString());
                if(!typeHm.containsKey(type.toString()))
                {
                    //add new type
                    typeHm.put(type.toString(),new TypeElement(type.toString(),1, e));
                }
                else
                {
                    TypeElement te = typeHm.get(type.toString());

                    if(!te.containsElement(e)){
                        te.addElement(e);
                        te.increaseCounter();
                    }
                    else
                    {
                        te.updateElement(e, predName, value.toString());
                    }
                }

                if(!hm.containsKey(predName))
                {
                    //New element
                    List<FilterElement> innerList = new ArrayList();
                    innerList.add(new FilterElement(predName, value.toString(),1));
                    hm.put(predName,innerList);
                }
                else{
                    //existing property
                    List<FilterElement> innerList = hm.get(predName);
                    Iterator<FilterElement> innerListIterator = innerList.iterator();
                    boolean found=false;
                    while (innerListIterator.hasNext()) {
                        //existing object
                        FilterElement fe = innerListIterator.next();
                        if(fe.getObjectField().equals(value.toString())){
                            fe.increaseCounter();
                            found=true;
                        }
                    }
                    if(!found){
                        //new object
                        innerList.add(new FilterElement(predName, value.toString(),1));
                    }

                }
                //out.println("<br>------<br>");
            }
        } finally {
            qexec.close();
        }
        /*out.println("<script>function narrowTheResults() {\n" +
                "  console.log('basic refresh of the page, checking the checked comboxes');\n" +
                "  window.location.reload();\n" +
                "}</script>");*/


/*
Adapt the value of the button from + to - and the vice-versa
  var x = document.getElementById("myBtn").value;
  if (x === "-"){
  document.getElementById("myBtn").value="+";
  }
  else{
  document.getElementById("myBtn").value="-";
  }
*/
        out.println("<script>function showHideFilterElements(element, button) {\n" +
                "  var x = document.getElementById(element);\n" +
                "  var y = document.getElementById(button).value;\n" +
                "  if (y === \"-\"){\n" +
                "  document.getElementById(button).value=\"+\";\n" +
                "  }\n" +
                "  else{\n" +
                "  document.getElementById(button).value=\"-\";\n" +
                "  }" +
                //"  alert(button);" +
                "  if (x.style.display === \"none\") {\n" +
                "    x.style.display = \"block\";\n" +
                "  } else {\n" +
                "    x.style.display = \"none\";\n" +
                "  }\n" +
                "}</script>");
        out.println("<form autocomplete=\"off\" action=\"filtersearch2\">");
        //out.println("<button onclick=\"narrowTheResults()\">Narrow the results</button><br>");
        out.println("<input type=\"submit\" value=\"Narrow the results\"><br>");
        out.println("Types1");
        for (Map.Entry entry : typeHm.entrySet()) {
            TypeElement te = (TypeElement) entry.getValue();
            out.println("<div class='filterProperty'>"+te.toString()+" </div>");
        }
        out.println("<br>");
        out.println("Types2");
        for (Map.Entry entry : typeHm.entrySet()) {
            TypeElement te = (TypeElement) entry.getValue();
            out.println("<div class='filterProperty'>"+te.printAllProperties()+" </div>");
        }
        out.println("<br>");
        out.println("Types3");
        for (Map.Entry entry : typeHm.entrySet()) {
            TypeElement te = (TypeElement) entry.getValue();
            out.println("<div class='filterProperty'>"+te.printAllPropertiesAndValues()+" </div>");
        }
        out.println("<br>");
        out.println("Properties");
        for (Map.Entry entry : hm.entrySet()) {
            List<FilterElement> l = (List)entry.getValue();
            String fullProp=entry.getKey().toString();
            //String property= fullProp.substring(fullProp.lastIndexOf("#") + 1);
            String property= fullProp;

            out.println("<div class='filterProperty'>"+property +"("+ l.size()+ " different values) <input id=\""+property+"Button\" type=\"button\" onclick=\"showHideFilterElements('"+property+"filterDiv', '"+property+"Button')\" value=\"show/hide elements\"></div>"+
                    "<div id=\""+property+"filterDiv"+"\" style=\"display:none\">");
            for(FilterElement filterElement : l) {
                out.println("<div class='filterElement'>"+filterElement+"</div>" );
            }
            //out.println(entry.getValue()+"<br>" + "</div>");
            out.println("</div>");
        }
        //out.println("<button onclick=\"narrowTheResults()\">Narrow the results</button><br>");
        out.println("<input type=\"submit\" value=\"Narrow the results\"><br>");
    }

    public String css(){
        return "<style>" +
                "{ box-sizing: border-box; }\n" +
                "body {\n" +
                "  font: 16px Arial; \n" +
                "}\n" +
                ".autocomplete {\n" +
                "  /*the container must be positioned relative:*/\n" +
                "  position: relative;\n" +
                "  display: inline-block;\n" +
                "}\n" +
                "input {\n" +
                "  border: 1px solid transparent;\n" +
                "  background-color: #f1f1f1;\n" +
                "  padding: 10px;\n" +
                "  font-size: 16px;\n" +
                "}\n" +
                "input[type=text] {\n" +
                "  background-color: #f1f1f1;\n" +
                "  width: 100%;\n" +
                "}\n" +
                "input[type=submit] {\n" +
                "  background-color: DodgerBlue;\n" +
                "  color: #fff;\n" +
                "}\n" +
                ".autocomplete-items {\n" +
                "  position: absolute;\n" +
                "  border: 1px solid #d4d4d4;\n" +
                "  border-bottom: none;\n" +
                "  border-top: none;\n" +
                "  z-index: 99;\n" +
                "  /*position the autocomplete items to be the same width as the container:*/\n" +
                "  top: 100%;\n" +
                "  left: 0;\n" +
                "  right: 0;\n" +
                "}\n" +
                ".autocomplete-items div {\n" +
                "  padding: 10px;\n" +
                "  cursor: pointer;\n" +
                "  background-color: #fff; \n" +
                "  border-bottom: 1px solid #d4d4d4; \n" +
                "}\n" +
                ".autocomplete-items div:hover {\n" +
                "  /*when hovering an item:*/\n" +
                "  background-color: #e9e9e9; \n" +
                "}\n" +
                ".autocomplete-active {\n" +
                "  /*when navigating through the items using the arrow keys:*/\n" +
                "  background-color: DodgerBlue !important; \n" +
                "  color: #ffffff; \n" +
                "}" +
                "</style>";

    }

    public String autoCompleteScript(){
        return "<script>" +
                "function autocomplete(inp, arr) {\n" +
                "  /*the autocomplete function takes two arguments,\n" +
                "  the text field element and an array of possible autocompleted values:*/\n" +
                "  var currentFocus;\n" +
                "  /*execute a function when someone writes in the text field:*/\n" +
                "  inp.addEventListener(\"input\", function(e) {\n" +
                "      var a, b, i, val = this.value;\n" +
                "      /*close any already open lists of autocompleted values*/\n" +
                "      closeAllLists();\n" +
                "      if (!val) { return false;}\n" +
                "      currentFocus = -1;\n" +
                "      /*create a DIV element that will contain the items (values):*/\n" +
                "      a = document.createElement(\"DIV\");\n" +
                "      a.setAttribute(\"id\", this.id + \"autocomplete-list\");\n" +
                "      a.setAttribute(\"class\", \"autocomplete-items\");\n" +
                "      /*append the DIV element as a child of the autocomplete container:*/\n" +
                "      this.parentNode.appendChild(a);\n" +
                "      /*for each item in the array...*/\n" +
                "      for (i = 0; i < arr.length; i++) {\n" +
                "        /*check if the item starts with the same letters as the text field value:*/\n" +
                "        if (arr[i].substr(0, val.length).toUpperCase() == val.toUpperCase()) {\n" +
                "          /*create a DIV element for each matching element:*/\n" +
                "          b = document.createElement(\"DIV\");\n" +
                "          /*make the matching letters bold:*/\n" +
                "          b.innerHTML = \"<strong>\" + arr[i].substr(0, val.length) + \"</strong>\";\n" +
                "          b.innerHTML += arr[i].substr(val.length);\n" +
                "          /*insert a input field that will hold the current array item's value:*/\n" +
                "          b.innerHTML += \"<input type='hidden' value='\" + arr[i] + \"'>\";\n" +
                "          /*execute a function when someone clicks on the item value (DIV element):*/\n" +
                "              b.addEventListener(\"click\", function(e) {\n" +
                "              /*insert the value for the autocomplete text field:*/\n" +
                "              inp.value = this.getElementsByTagName(\"input\")[0].value;\n" +
                "              /*close the list of autocompleted values,\n" +
                "              (or any other open lists of autocompleted values:*/\n" +
                "              closeAllLists();\n" +
                "          });\n" +
                "          a.appendChild(b);\n" +
                "        }\n" +
                "      }\n" +
                "  });\n" +
                "  /*execute a function presses a key on the keyboard:*/\n" +
                "  inp.addEventListener(\"keydown\", function(e) {\n" +
                "      var x = document.getElementById(this.id + \"autocomplete-list\");\n" +
                "      if (x) x = x.getElementsByTagName(\"div\");\n" +
                "      if (e.keyCode == 40) {\n" +
                "        /*If the arrow DOWN key is pressed,\n" +
                "        increase the currentFocus variable:*/\n" +
                "        currentFocus++;\n" +
                "        /*and and make the current item more visible:*/\n" +
                "        addActive(x);\n" +
                "      } else if (e.keyCode == 38) { //up\n" +
                "        /*If the arrow UP key is pressed,\n" +
                "        decrease the currentFocus variable:*/\n" +
                "        currentFocus--;\n" +
                "        /*and and make the current item more visible:*/\n" +
                "        addActive(x);\n" +
                "      } else if (e.keyCode == 13) {\n" +
                "        /*If the ENTER key is pressed, prevent the form from being submitted,*/\n" +
                "        e.preventDefault();\n" +
                "        if (currentFocus > -1) {\n" +
                "          /*and simulate a click on the \"active\" item:*/\n" +
                "          if (x) x[currentFocus].click();\n" +
                "        }\n" +
                "      }\n" +
                "  });\n" +
                "  function addActive(x) {\n" +
                "    /*a function to classify an item as \"active\":*/\n" +
                "    if (!x) return false;\n" +
                "    /*start by removing the \"active\" class on all items:*/\n" +
                "    removeActive(x);\n" +
                "    if (currentFocus >= x.length) currentFocus = 0;\n" +
                "    if (currentFocus < 0) currentFocus = (x.length - 1);\n" +
                "    /*add class \"autocomplete-active\":*/\n" +
                "    x[currentFocus].classList.add(\"autocomplete-active\");\n" +
                "  }\n" +
                "  function removeActive(x) {\n" +
                "    /*a function to remove the \"active\" class from all autocomplete items:*/\n" +
                "    for (var i = 0; i < x.length; i++) {\n" +
                "      x[i].classList.remove(\"autocomplete-active\");\n" +
                "    }\n" +
                "  }\n" +
                "  function closeAllLists(elmnt) {\n" +
                "    /*close all autocomplete lists in the document,\n" +
                "    except the one passed as an argument:*/\n" +
                "    var x = document.getElementsByClassName(\"autocomplete-items\");\n" +
                "    for (var i = 0; i < x.length; i++) {\n" +
                "      if (elmnt != x[i] && elmnt != inp) {\n" +
                "      x[i].parentNode.removeChild(x[i]);\n" +
                "    }\n" +
                "  }\n" +
                "}\n" +
                "/*execute a function when someone clicks in the document:*/\n" +
                "document.addEventListener(\"click\", function (e) {\n" +
                "    closeAllLists(e.target);\n" +
                "});\n" +
                "}" +
                "</script>";
    }

}

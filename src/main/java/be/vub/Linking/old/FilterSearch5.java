package be.vub.Linking.old;

import be.vub.Linking.URIprefix;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.RDFNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "FilterSearch5Servlet", urlPatterns = {"/filtersearch5","/filterSearch5"})
public class FilterSearch5 extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger( FilterSearch5.class );
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        }

        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();

            out.println(css());
            out.println(autoCompleteScript());
            sparqlJavascriptProcessor(out);
            scripts(out);

            out.println("<h3>Select the visible element</h3>");
            out.println("<form autocomplete=\"off\" action=\"filtersearch4\">");
            out.println("<input type=\"submit\" value=\"Narrow the results\"><br>");

            makeTypeFilter(out);
            makePredicateFilter(out);

            out.println("<input type=\"submit\" value=\"Narrow the results\"><br>");


            out.println("<h3>Select the visible element above</h3>");
        }

    private void sparqlJavascriptProcessor(PrintWriter out){
        //https://stackoverflow.com/questions/13124234/best-way-to-include-javascript-in-java-servlets
        /*
            out.println("<script type='text/javascript'>");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/javascript/functions.js");
            out.println("</script>");
            dispatcher.include(request, response);

         */
        out.println("<script>" +
                "/**\n" +
                " * SPARQL processor is a javascript library to query a sparql endpoint.\n" +
                " * It has been developed in the context of the PON project PRISMA - PiattafoRme cloud Interoperbili per SMArt-government,\n" +
                " * and it is release under the CC-BY 2.0 license (see http://creativecommons.org/licenses/by/2.0/)\n" +
                " *\n" +
                " * @author Cristiano Longo, Andrea Costazza.\n" +
                " */\n" +
                "\n" +
                "/**\n" +
                " * Perform a query against the specified endpoint and process results by the\n" +
                " * given processor object. The queryProcessor object must have the attribute query,\n" +
                " * which returns the query which will be performed against the specified endpoint,\n" +
                " * and the two methods\n" +
                " * process(row) , which will be invoked to process each row in the result set (sequentially) and\n" +
                " * flush(), which is called  when all the result set rows has been processed.\n" +
                " *\n" +
                " * @param endpoint URI of the sparql endpoint \n" +
                " * @param queryProcessor is an object delegate to specify the uery and handle the query result\n" +
                " */\n" +
                "function sparql_query(endpoint, queryProcessor){\n" +
                "\tvar querypart = \"query=\" + escape(queryProcessor.query);\n" +
                "\t// Get our HTTP request object.\n" +
                "\tvar xmlhttp = getHTTPObject();\n" +
                "\t//Include POST OR GET\n" +
                "\txmlhttp.open('POST', endpoint, true); \n" +
                "\txmlhttp.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');\n" +
                "\txmlhttp.setRequestHeader(\"Accept\", \"application/sparql-results+json\");\t\n" +
                "\txmlhttp.onreadystatechange = function() {\n" +
                "\t\tif(xmlhttp.readyState==4 ){\n" +
                "\t\t\tif(xmlhttp.status==200){\t\t\t\t\n" +
                "\t\t\t\t//Request accept\t\t\t\t\n" +
                "\t\t\t\tvar resultAsJson=eval('(' + xmlhttp.responseText + ')');\n" +
                "\t\t\t\tfor(var i = 0; i<  resultAsJson.results.bindings.length; i++) {\n" +
                "\t\t\t\t\tqueryProcessor.process(resultAsJson.results.bindings[i]);\n" +
                "\t\t\t\t}\n" +
                "\t\t\t\tqueryProcessor.flush();\n" +
                "\t\t\t} else {\n" +
                "\t\t\t\t// Some kind of error occurred.\n" +
                "\t\t\t\t\talert(\"Sparql query error: \" + xmlhttp.status + \" \"\n" +
                "\t\t\t\t\t\t+ xmlhttp.responseText);\n" +
                "\t\t\t}\n" +
                "\t\t}\t\n" +
                "\t}\n" +
                "\txmlhttp.send(querypart);\n" +
                "}\n" +
                "\n" +
                "//Request HTTP\n" +
                "function getHTTPObject(){\n" +
                "\tvar xmlhttp;\n" +
                "\tif(!xmlhttp && typeof XMLHttpRequest != 'undefined'){\n" +
                "\t\ttry{\n" +
                "\t\t\t// Code for old browser\n" +
                "\t\t\txmlhttp=new ActiveXObject('Msxml2.XMLHTTP');\n" +
                "\t\t\t}\n" +
                "\t\tcatch(err){\n" +
                "\t\t\ttry{\n" +
                "\t\t\t\t// Code for IE6, IE5\n" +
                "\t\t\t\txmlhttp=new ActiveXObject(\"Microsoft.XMLHTTP\");\n" +
                "\t\t\t}\n" +
                "\t\t\tcatch(err2){\n" +
                "\t\t\t\ttry{\n" +
                "\t\t\t\t\t// Code for IE7+, Firefox, Chrome, Opera, Safari\n" +
                "\t\t\t\t\txmlhttp=new XMLHttpRequest();\n" +
                "\t\t\t\t}\n" +
                "\t\t\t\tcatch(err3){\n" +
                "\t\t\t\t\txmlhttp=false\n" +
                "\t\t\t\t}\n" +
                "\t\t\t}\t\t\t\n" +
                "\t\t}\n" +
                "\t}\n" +
                "\treturn xmlhttp;\n" +
                "}" +
                "</script>");
    }

    private void scripts(PrintWriter out){
        out.println("<script>" +
                "function runProcessorValues(srcElement){\n" +
                "\n" +
                " alert('0');\n"+
                ""+
                " alert('We are printing: '+srcElement);\n"+
                " var div = document.createElement(\"div\");\n" +
                //" div.style.width = \"100px\";\n" +
                //" div.style.height = \"100px\";\n" +
                " div.style.background = \"red\";\n" +
                " div.style.color = \"white\";\n" +
                //" div.innerHTML = \"Hello\";\n" +
                " div.id = srcElement+'values';\n" +
                " div.style.display = \"block\";\n" +
                //" alert('1');\n"+
                " document.getElementById(srcElement).appendChild(div);" +
                //" srcElement.appendChild(div);" +
                //" alert('2');\n"+
                "\tvar list=document.createElement(\"ul\");\n" +
                "\tdiv.appendChild(list);"+
                //"\tcontainer.insertBefore(list, srcElement);\n" +
                "\t\n" +
                "\tvar summary=document.createElement(\"p\");\n" +
                //"\tcontainer.insertBefore(summary, srcElement);\n" +
                "\t\t\n" +
                "\t//the query processor\n" +
                "\tfunction Processor(list, summary){\n" +
                "\t\tthis.n=0;\n" +
                "\t\tthis.list=list;\n" +
                "\t\tthis.summary=summary;\n" +
                "\t\t\t\t\n" +
                " alert('3');\n"+
                "\t\t//define the query\n" +
                //"\t\tthis.query = \"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> SELECT distinct ?concept ?pred WHERE { ?sub ?pred ?concept. filter(regex(STR(?pred),'http://www.w3.org/2000/01/rdf-schema#label')) }order by ?concept\";\n" +
                //"\t\tthis.query = \"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> SELECT distinct ?concept ?pred WHERE { ?sub ?pred ?concept. filter(regex(STR(?pred),'rdfs:label')) }order by ?concept\";\n" +
                "\t\tthis.query = \"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> SELECT distinct ?concept ?pred WHERE { ?sub rdfs:label ?concept.} order by ?jef\";\n" +
                "\t\t\n" +
                "\t\t//and methods to handle the query results\n" +
                "\t\tthis.process = function(row){\n" +
                "\t\t\tvar item=document.createElement(\"li\");\n" +
                //" alert('4');\n"+
                //"\t\t\titem.innerHTML=row.concept.value+\" (\"+row.pred.value+\")\";\n" +
                "\t\t\titem.innerHTML=row.concept.value;\n" +
                "\t\t\tthis.list.appendChild(item);\n" +
                //" alert('5');\n"+
                "\t\t\tthis.n++;\n" +
                "\t\t};\n" +
                "\t\n" +
                "\t\t//show the results count as summary\n" +
                "\t\tthis.flush = function(){\n" +
                "\t\t\tthis.summary.innerHTML=\"Found \"+this.n+\" classes.\";\n" +
                "\t\t};\n" +
                "\t}\n" +
                "\t\n" +
                "\t//finally run the query\n" +
                "\t//sparql_query(\"http://dbpedia.org/sparql\", new Processor(list, summary));\n" +
                "\tsparql_query(\"http://localhost:8089/parliament/sparql\", new Processor(list, summary));\n" +
                "\t\n" +
                "\tsrcElement.disabled=true;\n" +
                "}" +
                "</script>" +
                "<button onclick=\"runProcessorValues(this);\">Run Example</button>");

        out.println("<script>function showHideFilterElements(element, button) {\n" +
                "  var x = document.getElementById(element);\n" +
                "  var y = document.getElementById(button).value;\n" +
                "  if (y === \"-\"){\n" +
                "  document.getElementById(button).value=\"+\";\n" +
                "  document.getElementById(element+'values').style.display = \"none\";\n" +
                "  " +
                "}\n" +
                "  else{\n" +
                "  document.getElementById(button).value=\"-\";\n" +
                "  if (document.getElementById(element+'values')==null){" +
                /*" var div = document.createElement(\"div\");\n" +
                " div.style.width = \"100px\";\n" +
                " div.style.height = \"100px\";\n" +
                " div.style.background = \"red\";\n" +
                " div.style.color = \"white\";\n" +
                " div.innerHTML = \"Hello\";\n" +
                " div.id = element+'values';\n" +
                " div.style.display = \"block\";\n" +
                " document.getElementById(element).appendChild(div);" +*/
                "alert('if');"+
                " runProcessorValues(element);\n"+
                " \n" +
                " } else{" +
                "alert('else');" +
                "document.getElementById(element+'values').style.display = \"block\";} }" +

                //"document.body.appendChild(div);"+
/*
// show/hide element (currenty parent element)
                "  if (x.style.display === \"none\") {\n" +
                "    x.style.display = \"block\";\n" +
                "  } else {\n" +
                "    x.style.display = \"none\";\n" +
                "  }\n" +*/
                "}</script>");
    }
    private void makePredicateFilter(PrintWriter out) {
        Map<String, String> predHm = new HashMap<>();
        String queryTypes="PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> SELECT distinct ?pred (count ( ?sub) as ?count) WHERE { ?sub ?pred ?obj. filter(?pred != rdf:type)} GROUP BY ?pred " ;
        Query query = QueryFactory.create(queryTypes);
        QueryExecution qexec =   QueryExecutionFactory.sparqlService("http://localhost:8089/parliament/sparql", query);

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
                    //System.out.println(""+count.asLiteral().getValue());
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
        /*
        //get all pred
        PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
        SELECT distinct ?type ?element ?pred ?value
        WHERE {
                 ?element rdf:type ?type.
                    ?element ?pred ?value.
                    filter(?pred != rdf:type)
        }
        */

        String queryTypes="PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> SELECT ?type (count ( ?sub) as ?count) WHERE { ?sub rdf:type ?type. } GROUP BY ?type " ;
        //String queryTypes="PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> SELECT distinct ?type WHERE { ?sub rdf:type ?type. } " ;

        Query query = QueryFactory.create(queryTypes);
        QueryExecution qexec =   QueryExecutionFactory.sparqlService("http://localhost:8089/parliament/sparql", query);

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
        /*
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
        */
        //out.println("<button onclick=\"narrowTheResults()\">Narrow the results</button><br>");

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

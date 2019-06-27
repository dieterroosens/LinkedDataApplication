package be.vub.Linking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "InterlinkInputServlet", urlPatterns = {"/interlink_input","/interlinkinput"})
public class InterlinkInput extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger( InterlinkInput.class );
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        }

        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            response.setContentType("text/html");

            PrintWriter out = response.getWriter();

            javascriptIncluder(request, response, "mapElement/bootstrap.min.js");
            javascriptIncluder(request, response, "mapElement/jquery-1.11.1.min.js");
            javascriptIncluder(request, response, "mapElement/addInterlinkElement.js");

            out.println(constructScreen(request.getParameter("elementURI"), request.getParameter("labelEn"), request.getParameter("userName")));
        }

    private String constructScreen(String uri, String labelEn, String userName){
        return  "<b>label(en):</b> <a href='"+uri+"'>"+ labelEn+"</a><br>"+
                "<b>URI: </b><input type='text' value='"+uri+"' id='myURI'>" +
                "<button onclick='copyValueToClipboard()'>Copy URI</button>"+
                "<form name='interLinking' action='action_interlink' target='_self' onsubmit='return validateForm(this)'>" +
                "<table id='myTable' class='table order-list'>"+
                "<thead>"+
                "<tr>"+
                "<td>predicate</td>"+
                "<td>URI</td>"+
                "</tr>"+
                "<datalist id='interlinkingPredicates'>"+
                "<option>geo:sf-within</option>"+
                "<option>geo:sf-contains</option>"+
                "<option>geo:sf-touches</option>"+
                "<option>owl:sameAs</option>"+
                "<option>a</option>"+
                "</datalist>"+
                "</thead>"+
                "<tbody>"+
                "<tr>"+
                "<td class='col-sm-4'>"+
                "<input type='text' name='predicate' class='form-control' list='interlinkingPredicates'/>"+
                "<input type='hidden' id='elementURI' name='elementURI' value='"+uri+"'>" +
                "<input type='hidden' id='userName' name='userName' value='"+userName+"'>" +
                "</td>"+
                "<td class='col-sm-3'>"+
                "<input type='text' name='object'  class='form-control' title='example: http://dbpedia.org/resource/Dublin'/>"+
                "</td>"+
                "<td class='col-sm-2'><a class='deleteRow'></a>"+
                "</td>"+
                "</tr>"+
                "</tbody>"+
                "<tfoot>"+
                "<tr>"+
                "<td colspan='3' style='text-align: left;'>"+
                "<input type='button' class='btn btn-lg btn-block' id='addrow' value='Add Row' />"+
                "</td>"+
                "</tr>"+
                "<tr>"+
                "</tr>"+
                "</tfoot>"+
                "</table>"+
                "<input type='submit' value='Submit' onsubmit='return validateForm(this)'>" +
                "</form>";
    }

    private void javascriptIncluder(HttpServletRequest request, HttpServletResponse response, String fileName){
        try {
            PrintWriter out = response.getWriter();
            out.println("<script type='text/javascript'>");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/javascript/"+fileName);
            dispatcher.include(request, response);
            out.println("</script>");
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

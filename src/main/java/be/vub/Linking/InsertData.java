package be.vub.Linking;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@WebServlet(name = "InsertDataServlet", urlPatterns = {"/insertdata","/export", "/action_sameas" , "/action_within", "/action_interlink"})
public class InsertData extends HttpServlet {

        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            response.setContentType("text/html");

            PrintWriter out = response.getWriter();
            out.println("<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <link rel='stylesheet' href='https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css' integrity='sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T' crossorigin='anonymous'>\n" +
                    "    <title>Interlinking tool -  Login - Post </title>\n" +
                    "</head>\n" +
                    "<body>\n" );
            out.println("<script src='https://code.jquery.com/jquery-3.3.1.slim.min.js' integrity='sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo' crossorigin='anonymous'></script>\n" +
                    "    <script src='https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js' integrity='sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1' crossorigin='anonymous'></script>\n" +
                    "    <script src='https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js' integrity='sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM' crossorigin='anonymous'></script>\n");

            String params=getAllParametersForFilter(request, response);

            ProvenanceHandling provenanceHandling = new ProvenanceHandling();
            HttpSession session = request.getSession();
            String userName= (String) session.getAttribute("userName");
            String userAgentID= (String) session.getAttribute("userAgentID");

            String elementURI = request.getParameter("elementURI");
            String predicate = request.getParameter("predicate");
            String object = request.getParameter("object");
            boolean ok=true;
            try {
                String replace = object.replace("&lt;", "<").replace("&gt;", ">");
                if(!replace.startsWith("<")){
                    replace="<"+replace;
                }
                if(!replace.endsWith(">")){
                    replace=replace+">";
                }
                provenanceHandling.complexInsert(userName, elementURI, predicate, replace, userAgentID);
                for (int i = 0; i < 10; i++) {
                    if (request.getParameter("predicate" + i) != null && request.getParameter("object" + i) != null) {
                        predicate = request.getParameter("predicate" + i);
                        object = request.getParameter("object" + i);
                        replace = object.replace("&lt;", "<").replace("&gt;", ">");
                        if(!replace.startsWith("<")){
                            replace="<"+replace;
                        }
                        if(!replace.endsWith(">")){
                            replace=replace+">";
                        }
                        provenanceHandling.complexInsert(userName, elementURI, predicate, replace, userAgentID);
                    }
                }
            }
            catch (Exception e){
                ok=false;
                out.println("<div class='alert alert-danger' role='alert'>An error occurred - Interlink hasn't been added</div>");
                System.out.println("Error: "+e);
            }
            if(ok){
                out.println("<div class='alert alert-success' role='alert'>Interlinking ok</div>");
            }
            out.println("</body>\n" +
                    "</html>");
            out.flush();
        }

    private String getAllParametersForFilter(HttpServletRequest request, HttpServletResponse response)throws IOException{
        //URIprefix prefix = new URIprefix();
        //PrintWriter out = response.getWriter();
        StringBuffer sb=new StringBuffer();
        Map<String, String[]> parameters = request.getParameterMap();
        for(String parameter : parameters.keySet()) {
            String[] values = parameters.get(parameter);
            for (String value : values) {
                sb.append(parameter+":" + value + ". ");
            }
            sb.append(" ");
        }
        return sb.toString();
    }

}

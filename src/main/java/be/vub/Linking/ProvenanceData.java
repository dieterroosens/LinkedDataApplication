package be.vub.Linking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@WebServlet(name = "ProvenanceDataServlet", urlPatterns = {"/provenancedata"})
public class ProvenanceData extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger( ProvenanceData.class );
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        }

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

            ProvenanceHandling provenanceHandling = new ProvenanceHandling();

            HttpSession session = request.getSession();
            String userName= (String) session.getAttribute("userName");

            out.println(provenanceHandling.getAllProvenanceData(userName));
            out.println("</body>\n" +
                    "</html>");
            out.flush();
        }

    private String getAllParametersForFilter(HttpServletRequest request, HttpServletResponse response)throws IOException{
        URIprefix prefix = new URIprefix();
        PrintWriter out = response.getWriter();
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

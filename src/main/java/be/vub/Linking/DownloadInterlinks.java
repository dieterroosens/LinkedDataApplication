package be.vub.Linking;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;

@WebServlet(name = "DownloadInterlinksServlet", urlPatterns = {"/downloadInterlinks"})
public class DownloadInterlinks extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        }

        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            OutputStream out=response.getOutputStream();
            HttpSession session = request.getSession();
            String userName= (String) session.getAttribute("userName");
            ProvenanceHandling provenanceHandling = new ProvenanceHandling();
            out.write(provenanceHandling.getConstructProvenanceData(userName).getBytes());
            out.flush();
        }

        /*
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
        }*/

}

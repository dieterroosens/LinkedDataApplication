package be.vub.Linking;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class CommonHandler {

    public void cssIndex(HttpServletRequest request, HttpServletResponse response){
        try {
            PrintWriter out = response.getWriter();
            out.println("<style>");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/css/index.css");
            dispatcher.include(request, response);
            out.println("</style>");
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

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

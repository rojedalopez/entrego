package servletsSesion;

import bean.Usuario;
import datos.Aplicacion;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class login_usuario extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            response.setContentType("text/html;charset=UTF-8");
        String usuario = request.getParameter("nick");
        String contrasena = request.getParameter("pass");
        HttpSession session =  null;
 
        session = request.getSession(true);

        Usuario u = Aplicacion.obtenerUsuario(usuario, contrasena);
        
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            if(u!=null){
                if(u.getMensaje().equals("true")){
                    session.setAttribute("user", u);
                    session.setAttribute("cod", u.getCodigo());
                    session.setAttribute("session", true);
                    String url = "";
                    boolean url_forward = false;
                    
                    if(session.getAttribute("url_forward")!=null){
                        url_forward = true;
                        url = session.getAttribute("url_forward")+"";
                    }
                    

                    session.setAttribute("usr", u.getNombre());
                    if(u.getRol()==2){
                        response.sendRedirect((url_forward)?url:"/entrego/servicios.jsp");
                    }
                    
                 }else{
                    System.out.println("error en: " +u.getMensaje());
                    response.sendRedirect("/entrego/?mensaje=Error en la autenticacion.");
                }
            }else{
                response.sendRedirect("../entrego/?mensaje=No existe usuario.");                
            }
        }
        } catch (SQLException ex) {
            Logger.getLogger(login_usuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}

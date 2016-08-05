package datos;

import bean.Usuario;
import java.security.InvalidKeyException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import org.json.simple.JSONObject;

public class Aplicacion {
    
    static SimpleDateFormat formateador = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    public static Connection conexion() throws ClassNotFoundException{
        Connection conn = null;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            //Nombre del servidor. localhost:3306 es la ruta y el puerto de la conexión MySQL
            //panamahitek_text es el nombre que le dimos a la base de datos
            String servidor = "jdbc:mysql://45.55.254.29:3306/entrego?noAccessToProcedureBodies=true";
            //String servidor = "jdbc:mysql://localhost:3306/"+bd;
            //El root es el nombre de Usuario por default. No hay contraseña
            //Se inicia la conexión
            conn = DriverManager.getConnection(servidor, "entrego_platform", "5c026fde033e58a1747437ee8468f4b624dab3ee");
                      
        }catch ( SQLException excepcionSql){ //excepcionSql = puede ponerle otro nombre
            System.out.println("error en la conexion a la base de datos"+excepcionSql.getMessage());           
        }
        return conn;
    }
    
        public static Usuario obtenerUsuario(String correo, String pass) throws SQLException{
        Usuario u = null;
        Connection conn=null;
        PreparedStatement insertar=null;
        Statement stm=null;
        ResultSet datos=null;
             
                try{
                    conn=conexion();
                    String instruccion = "";
                    
                    instruccion="SELECT cod_usuario, p.id_proyecto, desc_proyecto, email_usuario, hash_usuario, passw_usuario, " +
                    "id_rolusuario, nombre_usuario, apellido_usuario, celular_usuario, tipo_doc_usuario, tipo_pers_usuario " +
                    "FROM tblUsuario AS u INNER JOIN tblProyecto AS p ON u.id_proyecto = p.id_proyecto "+
                    "WHERE email_usuario = ?;";
                    
                    insertar=conn.prepareStatement(instruccion);
                    insertar.setString(1, correo);
                    datos=insertar.executeQuery();
                    if (datos.next()) {
                        //valido las credenciales
                        String hash = datos.getString(5);
                        String pasw = datos.getString(6);
                        System.out.println(pasw);
                        System.out.println(Metodos.sha512(pass, hash));
                        if(pasw.equals(Metodos.sha512(pass, hash))){
                            //obtengo el Usuario
                            u = new Usuario();
                            u.setCodigo(datos.getString(1));
                            u.setProyecto(datos.getInt(2));
                            u.setName_proyecto(datos.getString(3));
                            u.setCorreo(datos.getString(4));
                            u.setRol(datos.getInt(7));
                            u.setNombre(datos.getString(8));
                            u.setApellido(datos.getString(9));
                            u.setTelefono(datos.getString(10));
                            u.setMensaje("true");
                            return u;
                        }else{
                            u = new Usuario();
                            u.setCodigo("-1");
                            u.setMensaje("autenticacion");
 
                            return u;
                        }
 
                    }
                    datos.close();
                    conn.close();
        }catch (SQLException e) {
            System.out.println("error SQLException en ObtenerUsuario");
                    System.out.println(e.getMessage());
        }catch (Exception e){
                    System.out.println("error Exception en ObtenerUsuario");
                    System.out.println(e.getMessage());
                }finally{
                    if(conn!=null){
                        if(!conn.isClosed()){
                            conn.close();
                        }
                    }
                }
                     
        return u;
    }

    public static JSONObject obtenerMensajeroJSON(String correo, String pass) throws SQLException{
        JSONObject u = null;
        Connection conn=null;
        PreparedStatement insertar=null;
        Statement stm=null;
        ResultSet datos=null;
             
                try{
                    conn=conexion();
                    String instruccion="SELECT cod_mensajero, email_mensajero, hash_mensajero, passw_mensajero, nombre_mensajero, apellido_mensajero, " +
                    "celular_mensajero, direccion_mensajero, creacion_mensajero, img_perfil_mensajero FROM tblMensajero " +
                    "WHERE email_mensajero = ? AND activo_mensajero = 1;";
                    insertar=conn.prepareStatement(instruccion);
                    insertar.setString(1, correo);
                    datos=insertar.executeQuery();
                    if (datos.next()) {
                        //valido las credenciales
                        String hash = datos.getString(3);
                        String pasw = datos.getString(4);
                        if(pasw.equals(Metodos.sha512(pass, hash))){
                            //obtengo el Usuario
                            u = new JSONObject();
                            u.put("cod",datos.getString(1));
                            u.put("correo",datos.getString(2));
                            u.put("nombre",datos.getString(5));
                            u.put("apellido",datos.getString(6));
                            u.put("celular",datos.getString(7));
                            u.put("direccion",datos.getString(8));
                            u.put("afiliado_desde",formateador.format(formateador.parse(datos.getString(9))));
                            u.put("url_imagen",datos.getString(10));
                            u.put("mensaje","true");
                            
                            return u;
                        }else{
                            u = new JSONObject();
                            u.put("cod","-1");
                            u.put("mensaje","autenticacion");
 
                            return u;
                        }
 
                    }
                    datos.close();
                    conn.close();
        }catch (SQLException e) {
            System.out.println("error SQLException en ObtenerUsuario");
                    System.out.println(e.getMessage());
        }catch (Exception e){
                    System.out.println("error Exception en ObtenerUsuario");
                    System.out.println(e.getMessage());
                }finally{
                    if(conn!=null){
                        if(!conn.isClosed()){
                            conn.close();
                        }
                    }
                }
                     
        return u;
    }
    
    public static boolean ActivarCuentaUsuario(String mail) throws ClassNotFoundException, SQLException, InvalidKeyException{
        boolean b=false;
        Connection conn=null;
        PreparedStatement insertar=null;
        
        try{
        conn=conexion();
        
        String instruccion="UPDATE tblUsuario SET tkn_ver_usuario = NULL, ver_usuario = 1 WHERE eml_usuario = ?;";
         
        insertar=conn.prepareStatement(instruccion);
        insertar.setString(1, mail);
                 
        if(insertar.executeUpdate()==1){
            b=true;
        }
         
        insertar.close();
        conn.close();
         
         
         
        }catch (SQLException ex) {
                System.out.println("error en InsertUsuario");
            System.out.println(ex.getMessage());
        }finally{
            if(!conn.isClosed()){
                conn.close();
            }
        }
         
        return b;
    }
    
        
    public static boolean ForgotPassword(String mail) throws ClassNotFoundException, SQLException, InvalidKeyException{
        boolean b=false;
        Connection conn=null;
        PreparedStatement insertar=null;
        
        try{
        conn=conexion();
        String hash = Metodos.RandomString(10, false);
        String instruccion="UPDATE tblUsuario SET tkn_ver_usuario = ?, hsh_usuario = NULL, pwd_usuario = NULL, ver_usuario = 0 WHERE eml_usuario = ?;";
         
        insertar=conn.prepareStatement(instruccion);
        
        insertar.setString(1, hash);
        insertar.setString(2, mail);         
        
        if(insertar.executeUpdate()==1){
            //Mails.SendMailForgot(mail, hash, "CAMBIO DE CONTRASEÑA", "CAMBIAR");
            b=true;
        }
         
        insertar.close();
        conn.close();
         
         
         
        }catch (SQLException ex) {
                System.out.println("error en InsertUsuario");
            System.out.println(ex.getMessage());
        }finally{
            if(!conn.isClosed()){
                conn.close();
            }
        }
         
        return b;
    }
    
    
    public static boolean validarReset(String mail, String tkn) throws  SQLException{
        Connection conn=null;
        PreparedStatement insertar=null;
        Statement stm=null;
        ResultSet datos=null;
             
            try{
                conn=conexion();
                String instruccion="SELECT eml_usuario FROM tblUsuario WHERE eml_usuario = ? AND tkn_ver_usuario = ? AND ver_usuario = 0;";
                insertar=conn.prepareStatement(instruccion);
                insertar.setString(1, mail);
                insertar.setString(2, tkn);
                datos=insertar.executeQuery();
                if (datos.next()) {
                    return true;
                }
                datos.close();
                conn.close();

            }catch (SQLException e) {
                System.out.println("error SQLException en ObtenerPropietario");
                        System.out.println(e.getMessage());
            }catch (Exception e){
                        System.out.println("error Exception en ObtenerPropietario");
                        System.out.println(e.getMessage());
            }finally{
                if(conn!=null){
                    if(!conn.isClosed()){
                        conn.close();
                    }
                }
            }
                     
        return false;
    }
    
     public static boolean ActivarUsuario(String nick, String passw) throws ClassNotFoundException, SQLException, InvalidKeyException{
        boolean b=false;
        Connection conn=null;
        PreparedStatement insertar=null;
        conn=conexion();
        try{
        
        String instruccion="UPDATE tblUsuario SET hsh_usuario = ?, pwd_usuario = ?, ver_usuario = 1, tkn_ver_usuario = NULL WHERE eml_usuario = ?;";
        
        String hash = Metodos.RandomString(25, true);
        String pass = Metodos.sha512(passw, hash);
        
        insertar=conn.prepareStatement(instruccion);
        insertar.setString(1, hash);
        insertar.setString(2, pass);
        insertar.setString(3, nick);
                
        if(insertar.executeUpdate()==1){
            b=true;
        }
        
        insertar.close();
        conn.close();
        
        
        
        }catch (SQLException ex) {
                System.out.println("error en InsertUsuario");
        	System.out.println(ex.getMessage());
        }finally{
            if(!conn.isClosed()){
                conn.close();
            }
        }
        
        return b;
    }
    
}

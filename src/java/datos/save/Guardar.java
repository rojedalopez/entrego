package datos.save;

import bean.Solicitud;
import datos.Aplicacion;
import static datos.Aplicacion.conexion;
import datos.Metodos;
import datos.json.Listas;
import java.security.InvalidKeyException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Guardar {
    
    public static JSONObject SaveServicio(String origen, float lat_origen, float lng_origen, String destino, float lat_destino,
            float lng_destino, String fecha_cargue_min, String fecha_cargue_max, String fecha_descargue_min, String fecha_descargue_max, 
            int no_equipos, int tipo_equipos, String orden, String nota_detalle, String empresa, float flete, String nota_pago, String kms, String time) throws ClassNotFoundException, SQLException{
            boolean b=false;
            Solicitud sol = new Solicitud();
            Connection conn=null;
            PreparedStatement insertar=null;
            JSONObject retorno = new JSONObject();
        
            conn=Aplicacion.conexion();
            try (CallableStatement cs = conn.prepareCall("{CALL logycus360.new_solicitude(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)};")) {
                cs.setString(1, origen);
                cs.setFloat(2, lat_origen);
                cs.setFloat(3, lng_origen);
                cs.setString(4, destino);
                cs.setFloat(5, lat_destino);
                cs.setFloat(6, lng_destino);
                cs.setString(7, fecha_cargue_min);
                cs.setString(8, fecha_cargue_max);
                cs.setString(9, fecha_descargue_min);
                cs.setString(10, fecha_descargue_max);
                cs.setInt(11, no_equipos);
                cs.setInt(12, tipo_equipos);
                cs.setString(13, orden);
                cs.setString(14, nota_detalle);
                cs.setString(15, empresa);
                cs.setFloat(16, flete);
                cs.setString(17, nota_pago);
                cs.setString(18, kms);
                cs.setString(19, time);
                
                sol.setOrigen(origen);
                sol.setLat_origen(lat_origen);
                sol.setLng_origen(lng_origen);
                sol.setDestino(destino);
                sol.setLat_destino(lat_destino);
                sol.setLng_destino(lng_destino);
                sol.setCarguemin(fecha_cargue_min);
                sol.setCarguemax(fecha_cargue_max);
                sol.setDescarguemin(fecha_descargue_min);
                sol.setDescarguemax(fecha_descargue_max);
                sol.setCarga(tipo_equipos);
                sol.setOrden(orden);
                sol.setNota_detalle(orden);
                sol.setFlete(flete);
                sol.setAdelanto((float)(flete*0.4));
                sol.setNota_pago(nota_pago);
                
                cs.registerOutParameter(20, Types.VARCHAR);
                cs.executeQuery();

                String num_solicitud = cs.getString(20);
                sol.setId(num_solicitud);
                
                if(!num_solicitud.equals("0")){
                    JSONObject objeto = Listas.listaVehiculos(lat_origen, lng_origen, tipo_equipos);
                    JSONArray lista = (JSONArray) objeto.get("lista");
                    JSONArray TOs = (JSONArray) objeto.get("TOs");
                    Metodos.EnvioNotificacion(sol, TOs);
                    retorno.put("lista", lista);
                    retorno.put("id_solicitud", num_solicitud);
                    retorno.put("mensaje", "OK");
                    return retorno;
                }else{
                    retorno.put("mensaje", "Error");
                    return retorno;
                }

            }catch (SQLException e) {
                System.out.println("error SQLException en SaveServicio");
                System.out.println(e.getMessage());
            }catch (Exception e){
                System.out.println("error Exception en SaveServicio");
                System.out.println(e.getMessage());
            }finally{
                if(!conn.isClosed()){
                    conn.close();
                }
            }
            
            retorno.put("mensaje", "Error");
            return retorno;

    }
    
    /*
    pedroperez@gmail.com
    pepino
    */
    public static void main(String[] args) throws ClassNotFoundException, SQLException, InvalidKeyException {
        InsertUsuario("900876543", 2, "drogashabib@gmail.com", "habib", 2, "Superdrogueria Habib SAS", 
                "", "318 2433264", "NIT", "JUD", "/");
    }
    
    
    public static boolean InsertCompany(String nit, String rzn_social, String dir, String tel, String url,
            String mail, String contrasena) throws ClassNotFoundException, SQLException, InvalidKeyException{
        boolean b=false;
        Connection conn=null;
        PreparedStatement insertar=null;
        String hash = Metodos.RandomString(25, false);
        String pass = Metodos.sha512(contrasena, hash);
        String token = Metodos.RandomString(50, false);
        
        conn=conexion();
            try (CallableStatement cs = conn.prepareCall("{CALL logycus360.save_register_company(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}")) {
                cs.setString(1, nit);
                cs.setString(2, rzn_social);
                cs.setString(3, dir);
                cs.setString(4, tel);
                cs.setString(5, url);
                cs.setString(6, mail);
                cs.setString(7, hash);
                cs.setString(8, pass);
                cs.setString(9, token);
                cs.registerOutParameter(10, Types.INTEGER);
                cs.executeQuery();

                int retorno = cs.getInt(10);
                
                if(retorno==1){
                    //Mails.SendMail(correo, token, "CONFIRMACIÓN DE CUENTA", "ACTIVAR");
                    return true;
                }else{
                    return false;
                }

            }catch (SQLException e) {
                System.out.println("error SQLException en INSERTAR USUARIO");
                System.out.println(e.getMessage());
            }catch (Exception e){
                System.out.println("error Exception en INSERTAR USUARIO");
                System.out.println(e.getMessage());
            }finally{
                if(!conn.isClosed()){
                    conn.close();
                }
            }
            return false;

    }
    
    public static boolean InsertUsuario( String codigo, int  proyecto, String  email,  String passw,  
    int id_rol, String nombre, String apellido, String celular, String tipo_doc, String tipo_persona,
    String path) throws ClassNotFoundException, SQLException, InvalidKeyException{
        boolean b=false;
        Connection conn=null;
        PreparedStatement insertar=null;
        String hash = Metodos.RandomString(25, false);
        String pass = Metodos.sha512(passw, hash);
        String token = Metodos.RandomString(50, false);
        
        conn=conexion();
            try (CallableStatement cs = conn.prepareCall("{CALL entrego.PROC_RegistrarUsuario(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}")) {
                cs.setString(1, codigo);
                cs.setInt(2, proyecto);
                cs.setString(3, email);
                cs.setString(4, hash);
                cs.setString(5, pass);
                cs.setInt(6, id_rol);
                cs.setString(7, nombre);
                cs.setString(8, apellido);
                cs.setString(9, celular);
                cs.setString(10, tipo_doc);
                cs.setString(11, tipo_persona);
                cs.setString(12, token);
                cs.registerOutParameter(13, Types.INTEGER);
                cs.executeQuery();

                int retorno = cs.getInt(13);
                System.err.println(retorno);
                if(retorno==1){
                    //Mails.SendMail(correo, token, "CONFIRMACIÓN DE CUENTA", "ACTIVAR");
                    return true;
                }else{
                    return false;
                }

            }catch (SQLException e) {
                System.out.println("error SQLException en INSERTAR USUARIO");
                System.out.println(e.getMessage());
            }catch (Exception e){
                System.out.println("error Exception en INSERTAR USUARIO");
                System.out.println(e.getMessage());
            }finally{
                if(!conn.isClosed()){
                    conn.close();
                }
            }
            return false;

    }
    
    public static boolean AsignTurno(String ticket, int bahia, int zona, String fecha, String nota) throws ClassNotFoundException, SQLException{
        boolean b=false;
        Connection conn=null;
        PreparedStatement insertar=null;
        System.out.println(ticket);
        System.out.println(bahia);
        System.out.println(zona);
        System.out.println(fecha);
        System.out.println(nota);
        conn=conexion();
            try (CallableStatement cs = conn.prepareCall("{CALL logycus360.asignTurno(?, ?, ?, ?, ?, ?)}")) {
                cs.setString(1, ticket);
                cs.setInt(2, zona);
                cs.setInt(3, bahia);
                cs.setString(4, fecha);
                cs.setString(5, nota);
                cs.registerOutParameter(6, Types.VARCHAR);
                cs.executeQuery();
                
                String retorno = cs.getString(6);
                
                if(!retorno.equals("false")){
                    String[] vector = retorno.split("\\|");
                    Metodos.EnvioNotificacion(vector[0], vector[1], vector[2], vector[3], vector[4]);
                    return true;
                }else{
                    return false;
                }
                
            }catch (SQLException e) {
                System.out.println("error SQLException en INSERTAR USUARIO");
                System.out.println(e.getMessage());
            }catch (Exception e){
                System.out.println("error Exception en INSERTAR USUARIO");
                System.out.println(e.getMessage());
            }finally{
                if(!conn.isClosed()){
                    conn.close();
                }
            }
            
            return false;
    }
    
    public static boolean InsertRegistro(String id, String imei, String cod, int tipo) throws ClassNotFoundException, SQLException, InvalidKeyException{
        boolean b=false;
        Connection conn=null;
        PreparedStatement insertar=null;
        
        
        conn=conexion();
            try (CallableStatement cs = conn.prepareCall("{CALL logycus360.new_device_id(?, ?, ?, ?)}")) {
                cs.setString(1, id);
                cs.setString(2, imei);
                cs.setString(3, cod);
                cs.setInt(4, tipo);
                
                cs.executeQuery();

                return true;
                

            }catch (SQLException e) {
                System.out.println("error SQLException en INSERTAR USUARIO");
                System.out.println(e.getMessage());
            }catch (Exception e){
                System.out.println("error Exception en INSERTAR USUARIO");
                System.out.println(e.getMessage());
            }finally{
                if(!conn.isClosed()){
                    conn.close();
                }
            }
            return false;

    }
    
    
    public static boolean finishTurno(String ticket, String nota) throws ClassNotFoundException, SQLException{
        boolean b=false;
        Connection conn=null;
        PreparedStatement insertar=null;
        
        
        conn=conexion();
            try (CallableStatement cs = conn.prepareCall("{CALL logycus360.finishTurno(?, ?, ?)}")) {
                cs.setString(1, ticket);
                cs.setString(2, nota);
                cs.registerOutParameter(3, Types.INTEGER);
                cs.executeQuery();
                
                int retorno = cs.getInt(3);
                
                if(retorno==1){
                    return true;
                }else{
                    return false;
                }
                

            }catch (SQLException e) {
                System.out.println("error SQLException en INSERTAR USUARIO");
                System.out.println(e.getMessage());
            }catch (Exception e){
                System.out.println("error Exception en INSERTAR USUARIO");
                System.out.println(e.getMessage());
            }finally{
                if(!conn.isClosed()){
                    conn.close();
                }
            }
            return false;

    }
    
    public static String InsertTicketProceso(String nit, int equipo_conductor, String operacion, String proceso) throws ClassNotFoundException, SQLException{
        boolean b=false;
        Connection conn=null;
        PreparedStatement insertar=null;
        
        
        conn=conexion();
            try (CallableStatement cs = conn.prepareCall("{CALL logycus360.giveEnturne(?, ?, ?, ?, ?)}")) {
                cs.setString(1, nit);
                cs.setString(2, operacion);
                cs.setInt(3, equipo_conductor);
                cs.setString(4, proceso);
                cs.registerOutParameter(5, Types.VARCHAR);
                cs.executeQuery();

                String retorno = cs.getString(5);

                return retorno;
                

            }catch (SQLException e) {
                System.out.println("error SQLException en INSERTAR USUARIO");
                System.out.println(e.getMessage());
            }catch (Exception e){
                System.out.println("error Exception en INSERTAR USUARIO");
                System.out.println(e.getMessage());
            }finally{
                if(!conn.isClosed()){
                    conn.close();
                }
            }
            return "";

    }
    
    public static boolean InsertArchivo(String id, String url) throws ClassNotFoundException, SQLException{
        boolean b=false;
        Connection conn=null;
        PreparedStatement insertar=null;
        
        
        conn=conexion();
            try (CallableStatement cs = conn.prepareCall("{CALL logycus360.upload_cumplido(?, ?, ?)}")) {
                cs.setString(1, id);
                cs.setString(2, url);
                cs.registerOutParameter(3, Types.INTEGER);
                cs.executeQuery();

                int retorno = cs.getInt(3);
                
                if(retorno==1){
                    //Mails.SendMail(correo, token, "CONFIRMACIÓN DE CUENTA", "ACTIVAR");
                    return true;
                }else{
                    return false;
                }
                

            }catch (SQLException e) {
                System.out.println("error SQLException en INSERTAR USUARIO");
                System.out.println(e.getMessage());
            }catch (Exception e){
                System.out.println("error Exception en INSERTAR USUARIO");
                System.out.println(e.getMessage());
            }finally{
                if(!conn.isClosed()){
                    conn.close();
                }
            }
            return false;

    }
    
    
    public static String AsignarSolicitud(String solicitud, int equipo_conductor, float latitud, float longitud, float velocidad) throws ClassNotFoundException, SQLException{
        boolean b=false;
        Connection conn=null;
        PreparedStatement insertar=null;
        
        
        conn=conexion();
            try (CallableStatement cs = conn.prepareCall("{CALL logycus360.take_solicitude(?, ?, ?, ?, ?, ?)}")) {
                cs.setString(1, solicitud);
                cs.setInt(2, equipo_conductor);
                cs.setFloat(3, latitud);
                cs.setFloat(4, longitud);
                cs.setFloat(5, velocidad);
                cs.registerOutParameter(6, Types.VARCHAR);
                
                cs.executeQuery();
                
                String retorno = cs.getString(6);
                
                return retorno;
                

            }catch (SQLException e) {
                System.out.println("error SQLException en INSERTAR USUARIO");
                System.out.println(e.getMessage());
            }catch (Exception e){
                System.out.println("error Exception en INSERTAR USUARIO");
                System.out.println(e.getMessage());
            }finally{
                if(!conn.isClosed()){
                    conn.close();
                }
            }
            return "0";

    }
    
    
    public static int CambioEstadoServicio(String servicio, int estado, int equipo_conductor, String imagen) throws ClassNotFoundException, SQLException{
        Connection conn=null;
        PreparedStatement insertar=null;
        System.out.println(imagen);
        System.out.println(servicio);
        
        conn=conexion();
            try (CallableStatement cs = conn.prepareCall("{CALL logycus360.change_state_service(?, ?, ?, ?, ?)}")) {
                cs.setString(1, servicio);
                cs.setInt(2, estado);
                cs.setInt(3, equipo_conductor);
                cs.setString(4, imagen);
                cs.registerOutParameter(5, Types.INTEGER);
                
                cs.executeQuery();
                
                int retorno = cs.getInt(5);
                
                return retorno;
                

            }catch (SQLException e) {
                System.out.println("error SQLException en INSERTAR USUARIO");
                System.out.println(e.getMessage());
            }catch (Exception e){
                System.out.println("error Exception en INSERTAR USUARIO");
                System.out.println(e.getMessage());
            }finally{
                if(!conn.isClosed()){
                    conn.close();
                }
            }
            return -1;

    }
    
}


package datos.json;

import static datos.Aplicacion.conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class Listas {
    static SimpleDateFormat formateador = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  
    public static JSONObject listaVehiculos(float lat, float lng, int tipo) throws SQLException{
        JSONArray lista=new JSONArray();        
        JSONArray TOs=new JSONArray();   
        JSONObject retorno= new JSONObject();       
        PreparedStatement st = null;
        Connection conn=null;
        ResultSet datos=null;
        
            try{
                conn=conexion();
                String instruccion="SELECT ec.id_equipoconductor, cod_conductor, ec.plca_equipo, lat_equipoconductor, long_equipoconductor, " +
                "velo_equipoconductor, ult_actualizacion, id_tipocarga, regs_imei_conductor "+
                "FROM tblEquipoConductor AS ec INNER JOIN tblEquipo AS eq ON ec.plca_equipo = eq.plca_equipo "+
                "WHERE id_tipocarga = "+tipo+" AND disp_equipoconductor = 1 AND " +
                "(acos(sin(radians("+lat+")) * sin(radians(lat_equipoconductor)) + " +
                "cos(radians("+lat+")) * cos(radians(lat_equipoconductor)) * " +
                "cos(radians("+lng+") - radians(long_equipoconductor))) * 6378)<100;";	        
                
                st=conn.prepareStatement(instruccion);
                datos=(ResultSet) st.executeQuery();
                while (datos.next()) {
                    JSONObject objeto= new JSONObject();
                    JSONArray posicion= new JSONArray();
                    objeto.put("id", datos.getInt(1));
                    objeto.put("cod", datos.getString(2));
                    objeto.put("placa", datos.getString(3));
                    posicion.add(datos.getFloat(4));
                    posicion.add(datos.getFloat(5));
                    objeto.put("position", posicion);
                    objeto.put("velocidad", datos.getFloat(6));                    
                    objeto.put("ult_reporte", formateador.format(formateador.parse(datos.getString(7))));
                    TOs.add(datos.getString(9));
                    lista.add(objeto);
                }
                
                retorno.put("lista",lista);
                retorno.put("TOs", TOs);
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
        return retorno;
    }
    
    public static JSONObject listaServicios(int proyecto, int porpage, int pageno, String q) throws SQLException{
        JSONArray lista=new JSONArray();        
        JSONObject retorno = new JSONObject();
        PreparedStatement st = null;
        Connection conn=null;
        ResultSet datos=null;
        
            try{
                conn=conexion();
                int desde = ((pageno-1)*porpage);
                
                String instruccion="SELECT cod_servicio, e.id_estadoservicio, e.desc_estadoservicio, do.id_direccion, "+
                " do.desc_direccion, dd.id_direccion, dd.desc_direccion, nota_servicio, creacion_servicio, " +
                "u.cod_usuario, nombre_usuario, apellido_usuario, celular_usuario, " +
                "m.cod_mensajero, nombre_mensajero, apellido_mensajero, celular_mensajero " +
                "FROM tblServicio AS s INNER JOIN tblUsuario AS u ON u.cod_usuario = s.cod_usuario " +
                "INNER JOIN tblDireccion AS do ON do.id_direccion = s.id_direccion_origen  " +
                "INNER JOIN tblDireccion AS dd ON dd.id_direccion = s.id_direccion_destino  " +
                "INNER JOIN tblEstadoServicio AS e ON e.id_estadoservicio = s.id_estadoservicio " +
                "LEFT JOIN tblMensajero AS m ON m.cod_mensajero = s.cod_mensajero " +
                "WHERE s.id_proyecto = ? ";
                
                if(!q.isEmpty()){
                    instruccion += " AND ( nombre_usuario like '%"+q+"%' OR apellido_usuario like '%"+q+"%' " +
                    " OR nombre_mensajero like '%"+q+"%' OR apellido_mensajero like '%"+q+"%' " +
                    " OR do.desc_direccion like '%"+q+"%' OR dd.desc_direccion like '%"+q+"%') ";
                }
                
                instruccion+=      " ORDER BY creacion_servicio DESC ";	        
                instruccion+=      " LIMIT "+desde+","+porpage+";";
                
                System.out.println(instruccion);
                
                st=conn.prepareStatement(instruccion);
                st.setInt(1, proyecto);
                datos=(ResultSet) st.executeQuery();
                while (datos.next()) {
                    JSONObject objeto= new JSONObject();
                    objeto.put("cod_servicio", datos.getString(1));
                    objeto.put("id_estado", datos.getInt(2));
                    objeto.put("desc_estado", datos.getString(3));
                    objeto.put("id_dir_origen", datos.getString(4));
                    objeto.put("dir_origen", datos.getString(5));
                    objeto.put("id_dir_destino", datos.getString(6));
                    objeto.put("dir_destino", datos.getString(7));                    
                    objeto.put("nota", datos.getString(8));
                    objeto.put("creacion", formateador.format(formateador.parse(datos.getString(9))));
                    objeto.put("cod_usuario", datos.getString(10));
                    objeto.put("nombre_usuario", datos.getString(11));
                    objeto.put("apellido_usuario", datos.getString(12));
                    objeto.put("celular_usuario", datos.getString(13));
                    objeto.put("cod_mensajero", datos.getString(14));
                    objeto.put("nombre_mensajero", datos.getString(15));
                    objeto.put("apellido_mensajero", datos.getString(16));
                    objeto.put("celular_mensajero", datos.getString(17));
                    lista.add(objeto);
                }
                
                retorno.put("total_count", totalServicios(proyecto, q));
                retorno.put("data", lista);
                retorno.put("error", 0);
                
                return retorno;
                
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
            
        retorno.put("total_count", 0);
        retorno.put("data", "[]");
        retorno.put("error", 0);

        return retorno;
    }
    
   public static int totalServicios(int proyecto, String q) throws SQLException{
        PreparedStatement st = null;
        Connection conn=null;
        ResultSet datos=null;
        
            try{
                conn=conexion();
                String instruccion="SELECT COUNT(1) " +
                "FROM tblServicio AS s INNER JOIN tblUsuario AS u ON u.cod_usuario = s.cod_usuario " +
                "INNER JOIN tblDireccion AS do ON do.id_direccion = s.id_direccion_origen  " +
                "INNER JOIN tblDireccion AS dd ON dd.id_direccion = s.id_direccion_destino  " +
                "INNER JOIN tblEstadoServicio AS e ON e.id_estadoservicio = s.id_estadoservicio " +
                "LEFT JOIN tblMensajero AS m ON m.cod_mensajero = s.cod_mensajero " +
                "WHERE s.id_proyecto = ? ";
                
                if(!q.isEmpty()){
                    instruccion += " AND ( nombre_usuario like '%"+q+"%' OR apellido_usuario like '%"+q+"%' " +
                    " OR nombre_mensajero like '%"+q+"%' OR apellido_mensajero like '%"+q+"%') ";
                }
                
                st=conn.prepareStatement(instruccion);
                st.setInt(1, proyecto);
                datos=(ResultSet) st.executeQuery();
                if (datos.next()) {
                    return datos.getInt(1);
                }
                
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
        return 0;
    }       
    
   public static JSONObject listaMensajeros(int proyecto, int porpage, int pageno, String q) throws SQLException{
        JSONArray lista=new JSONArray();        
        JSONObject retorno = new JSONObject();
        PreparedStatement st = null;
        Connection conn=null;
        ResultSet datos=null;
        
            try{
                conn=conexion();
                int desde = ((pageno-1)*porpage);
                
                String instruccion="SELECT cod_mensajero, nombre_mensajero, apellido_mensajero, celular_mensajero, " +
                                    "direccion_mensajero, img_perfil_mensajero, email_mensajero FROM entrego.tblMensajero ";
                
                if(!q.isEmpty()){
                    instruccion += " WHERE ";
                    if(q.split(" ").length>0){
                        String[] v = q.split(" ");
                        for(int i=0; i<v.length; i++){
                            if(i!=0){
                                instruccion += " OR ";
                            }
                            instruccion += " ( cod_mensajero like '%"+v[i]+"%' OR nombre_mensajero like '%"+v[i]+"%' OR apellido_mensajero like '%"+v[i]+"%' " +
                            " OR celular_mensajero like '%"+v[i]+"%' OR direccion_mensajero like '%"+v[i]+"%') ";
                        }
                    }else{
                        instruccion += " ( cod_mensajero like '%"+q+"%' OR nombre_mensajero like '%"+q+"%' OR apellido_mensajero like '%"+q+"%' " +
                        " OR celular_mensajero like '%"+q+"%' OR direccion_mensajero like '%"+q+"%') ";
                    }
                }
                
                instruccion+=      " ORDER BY cod_mensajero DESC ";	        
                instruccion+=      " LIMIT "+desde+","+porpage+";";
                
                System.out.println(instruccion);
                
                st=conn.prepareStatement(instruccion);
                datos=(ResultSet) st.executeQuery();
                while (datos.next()) {
                    JSONObject objeto= new JSONObject();
                    objeto.put("cod_mensajero", datos.getString(1));
                    objeto.put("nombre_mensajero", datos.getString(2));
                    objeto.put("apellido_mensajero", datos.getString(3));
                    objeto.put("celular_mensajero", datos.getString(4));
                    objeto.put("direccion_mensajero", datos.getString(5));
                    objeto.put("img_mensajero", datos.getString(6));
                    objeto.put("correo_mensajero", datos.getString(7));
                    lista.add(objeto);
                }
                
                retorno.put("total_count", totalMensajeros(proyecto, q));
                retorno.put("data", lista);
                retorno.put("error", 0);
                
                return retorno;
                
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
            
        retorno.put("total_count", 0);
        retorno.put("data", "[]");
        retorno.put("error", 0);

        return retorno;
    }
    
   public static int totalMensajeros(int proyecto, String q) throws SQLException{
        PreparedStatement st = null;
        Connection conn=null;
        ResultSet datos=null;
        
            try{
                conn=conexion();
                String instruccion="SELECT COUNT(1) FROM entrego.tblMensajero ";
                
                if(!q.isEmpty()){
                    instruccion += " WHERE ";
                    if(q.split(" ").length>0){
                        String[] v = q.split(" ");
                        for(int i=0; i<v.length; i++){
                            if(i!=0){
                                instruccion += " OR ";
                            }
                            instruccion += " ( cod_mensajero like '%"+v[i]+"%' OR nombre_mensajero like '%"+v[i]+"%' OR apellido_mensajero like '%"+v[i]+"%' " +
                            " OR celular_mensajero like '%"+v[i]+"%' OR direccion_mensajero like '%"+v[i]+"%') ";
                        }
                    }else{
                        instruccion += " ( cod_mensajero like '%"+q+"%' OR nombre_mensajero like '%"+q+"%' OR apellido_mensajero like '%"+q+"%' " +
                        " OR celular_mensajero like '%"+q+"%' OR direccion_mensajero like '%"+q+"%') ";
                    }
                }
                
                st=conn.prepareStatement(instruccion);
                datos=(ResultSet) st.executeQuery();
                if (datos.next()) {
                    return datos.getInt(1);
                }
                
            }catch (SQLException e) {
            System.out.println("error SQLException en ObtenerUsuarioc");
                    System.out.println(e.getMessage());
            }catch (Exception e){
                    System.out.println("error Exception en ObtenerUsuarioc");
                    System.out.println(e.getMessage());
            }finally{
                if(conn!=null){
                    if(!conn.isClosed()){
                        conn.close();
                    }
                }
            }
        return 0;
    }       
   
    public static JSONArray listaVehiculosByEmpresa(String q, int carga, int estado, String nit) throws SQLException{
        JSONArray lista=new JSONArray();
        PreparedStatement st = null;
        Connection conn=null;
        ResultSet datos=null;
        
            try{
                conn=conexion();
                String instruccion="SELECT ser.id_servicio, ser.id_solicitud, plca_equipo, CONCAT(nomb_conductor,' ', apll_conductor), " +
                "est.desc_estados, ult_esta_servicio, fech_inic_servicio, ser.ult_actu_servicio, ult_lat_servicio, ult_long_servicio, img_conductor  " +
                "FROM tblServicio AS ser INNER JOIN tblSolicitud AS sol ON ser.id_solicitud = sol.id_solicitud " +
                "INNER JOIN tblEquipoConductor AS eqpcon ON ser.id_equipoconductor = eqpcon.id_equipoconductor " +
                "INNER JOIN tblConductor AS con ON con.cod_conductor = eqpcon.cod_conductor " +
                "INNER JOIN tblEstados AS est ON ser.id_estados = est.id_estados " +
                "WHERE sol.nit_empresa = ? and ser.id_estados < 9 " ;
                if(!q.isEmpty()){
                    instruccion += " AND ( nomb_conductor like '%"+q+"%' OR apll_conductor like '%"+q+"%') ";
                }
                
                if(carga!=0){
                    instruccion += " AND id_tipocarga = " + carga;
                }
                
                if(estado!=0){
                    instruccion += " AND ser.id_estados = " + estado;
                }
                
                System.out.println(instruccion);
                
                st=conn.prepareStatement(instruccion);
                st.setString(1, nit);
                datos=(ResultSet) st.executeQuery();
                while (datos.next()) {
                    JSONObject objeto= new JSONObject();
                    JSONArray posicion= new JSONArray();
                    objeto.put("servicio", datos.getString(1));
                    objeto.put("solicitud", datos.getString(2));
                    objeto.put("placa", datos.getString(3));
                    objeto.put("nombre",datos.getString(4));
                    objeto.put("estado",datos.getString(5));
                    objeto.put("act_estado",datos.getString(6));
                    objeto.put("inicio_serv",datos.getString(7));
                    objeto.put("act_serv",datos.getString(8));
                    posicion.add(datos.getFloat(9));
                    posicion.add(datos.getFloat(10));
                    objeto.put("position", posicion);
                    objeto.put("conductor",datos.getString(11));
                    lista.add(objeto);
                }
                
                return lista;

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
        return lista;
    }
    
    public static int totalFiltrados(String nit, String q, String cargue, String descargue, int carga, int estado) throws SQLException{
        PreparedStatement st = null;
        Connection conn=null;
        ResultSet datos=null;
        
            try{
                conn=conexion();
                String instruccion="SELECT  count(1) ";
                instruccion+=      "FROM tblSolicitud AS s INNER JOIN tblEstados AS e ON s.id_estados = e.id_estados WHERE nit_empresa = ? " ;
                if(!q.isEmpty()){
                    instruccion += " AND (orig_solicitud like '%"+q+"%' OR dest_solicitud like '%"+q+"%') ";
                }
                
                if(!cargue.isEmpty()){
                    instruccion += " AND (fech_carg_solicitud  BETWEEN '"+cargue+" 00:00:00' AND '"+cargue+" 23:59:59') ";
                }
                
                if(!descargue.isEmpty()){
                    instruccion += " AND (fech_desc_solicitud  BETWEEN '"+descargue+" 00:00:00' AND '"+descargue+" 23:59:59') ";
                }
                
                if(carga!=-1){
                    instruccion += " AND id_tipocarga = " + carga;
                }
                
                if(estado!=-1){
                    instruccion += " AND e.id_estados = " + estado;
                }
                
                
                st=conn.prepareStatement(instruccion);
                st.setString(1, nit);
                datos=(ResultSet) st.executeQuery();
                if (datos.next()) {
                    return datos.getInt(1);
                }
                
                
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
        return 0;
    }
    
    public static void main(String[] args) throws SQLException {
        System.out.println(listaPuntos(""));
    }
    
    public static JSONArray listaEmpresasEnturne() throws SQLException{
        JSONArray lista=new JSONArray();
        PreparedStatement st = null;
        Connection conn=null;
        ResultSet datos=null;
        
            try{
                conn=conexion();
                String instruccion="SELECT nit_empresa, razn_soci_empresa, dir_empresa, tel_empresa, url_img_empresa " +
                                    "FROM logycus360.tblEmpresa WHERE acti_empresa = 1 AND enturn_empresa = 1;" ;
                
                st=conn.prepareStatement(instruccion);
                datos=(ResultSet) st.executeQuery();
                while (datos.next()) {
                    JSONObject objeto= new JSONObject();
                    objeto.put("nit", datos.getString(1));
                    objeto.put("razon_social", datos.getString(2));
                    objeto.put("direccion", datos.getString(3));
                    objeto.put("telefono",datos.getString(4));
                    objeto.put("url_imagen",datos.getString(5));
                    objeto.put("procesos", listaProcesosEmpresasEnturne(datos.getString(1)));
                    lista.add(objeto);
                }
                
                return lista;

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
        return lista;
    }
    
    public static JSONArray listaProcesosEmpresasEnturne(String empresa) throws SQLException{
        JSONArray lista=new JSONArray();
        PreparedStatement st = null;
        Connection conn=null;
        ResultSet datos=null;
        
            try{
                conn=conexion();
                
                String instruccion="SELECT id_tipocargue, nit_empresa, desc_tipocargue FROM tblTipoCargue "
                        + " WHERE nit_empresa = ?;" ;
                
                
                
                st=conn.prepareStatement(instruccion);
                st.setString(1, empresa);
                datos=(ResultSet) st.executeQuery();
                while (datos.next()) {
                    JSONObject objeto= new JSONObject();
                    objeto.put("id", datos.getString(1));
                    objeto.put("id_empresa", datos.getString(2));
                    objeto.put("desc", datos.getString(3));
                    lista.add(objeto);
                }
                
                return lista;

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
        return lista;
    }
    
    
    
    public static String listaPuntos(String servicio) throws SQLException{
        JSONArray lista=new JSONArray();  
        JSONObject objeto_= new JSONObject();
        PreparedStatement st = null;
        Connection conn=null;
        ResultSet datos=null;
        
            try{
                conn=conexion();
                String instruccion="SELECT id_mensaje, imei_mensaje, lati_mensaje, long_mensaje, vel_mensaje, " +
                "date_mensaje FROM logycus360.tblMensajes WHERE id_servicio = '00000001';";	        
                
                st=conn.prepareStatement(instruccion);
                datos=(ResultSet) st.executeQuery();
                while (datos.next()) {
                    JSONObject objeto= new JSONObject();
                    //JSONArray posicion= new JSONArray();
                    JSONObject posicion= new JSONObject();
                    /*objeto.put("id", datos.getInt(1));
                    objeto.put("imei", datos.getString(2));
                    objeto.put("latitud", datos.getFloat(3));
                    objeto.put("logitud", datos.getFloat(4));
                    objeto.put("velocidad", datos.getFloat(5));
                    objeto.put("datetime", formateador.format(formateador.parse(datos.getString(6))));*/
                    posicion.put("lat",datos.getFloat(3));
                    posicion.put("lng",datos.getFloat(4));
                    objeto.put("location", posicion);
                    
                    lista.add(objeto);
                }

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
        return lista.toJSONString();
    }
    
    public static JSONArray listaZonasEmpresa(String empresa) throws SQLException{
        JSONArray lista=new JSONArray();
        PreparedStatement st = null;
        Connection conn=null;
        ResultSet datos=null;
        
            try{
                conn=conexion();
                
                String instruccion="SELECT id_zonaempresa, nit_empresa, desc_zonaempresa FROM tblZonaEmpresa WHERE nit_empresa = ?;" ;
                
                
                
                st=conn.prepareStatement(instruccion);
                st.setString(1, empresa);
                datos=(ResultSet) st.executeQuery();
                while (datos.next()) {
                    JSONObject objeto= new JSONObject();
                    objeto.put("id", datos.getInt(1));
                    objeto.put("id_empresa", datos.getString(2));
                    objeto.put("desc", datos.getString(3));
                    objeto.put("bahias", listaBahiasByZonasEmpresa(datos.getInt(1)));
                    lista.add(objeto);
                }
                
                return lista;

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
        return lista;
    }
    
    public static JSONArray listaBahiasByZonasEmpresa(int bahia) throws SQLException{
        JSONArray lista=new JSONArray();
        PreparedStatement st = null;
        Connection conn=null;
        ResultSet datos=null;
        
            try{
                conn=conexion();
                
                String instruccion="SELECT id_bahia, id_zonaempresa, desc_habia, nota_bahia, cargues_bahia FROM tblBahia WHERE id_zonaempresa = ?;" ;
                
                
                
                st=conn.prepareStatement(instruccion);
                st.setInt(1, bahia);
                datos=(ResultSet) st.executeQuery();
                while (datos.next()) {
                    JSONObject objeto= new JSONObject();
                    objeto.put("id", datos.getInt(1));
                    objeto.put("id_zona", datos.getInt(2));
                    objeto.put("desc", datos.getString(3));
                    objeto.put("nota", datos.getString(4));
                    lista.add(objeto);
                }
                
                return lista;

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
        return lista;
    }
}

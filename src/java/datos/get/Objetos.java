package datos.get;

import static datos.Aplicacion.conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Objetos {
    public static JSONObject DatosEmpresaBySolicitud(String id) throws SQLException{
        JSONObject retorno= new JSONObject();       
        PreparedStatement st = null;
        Connection conn=null;
        ResultSet datos=null;
        
            try{
                conn=conexion();
                String instruccion="SELECT  e.nit_empresa, razn_soci_empresa, url_img_empresa, dir_empresa, tel_empresa " +
                "FROM tblSolicitud AS s INNER JOIN tblEmpresa AS e ON s.nit_empresa = e.nit_empresa " +
                "WHERE id_solicitud = ?;";	        
                
                st=conn.prepareStatement(instruccion);
                st.setString(1, id);
                datos=(ResultSet) st.executeQuery();
                if (datos.next()) {
                    retorno.put("nit", datos.getString(1));
                    retorno.put("empresa", datos.getString(2));
                    retorno.put("url", datos.getString(3));
                    retorno.put("direccion", datos.getString(4));
                    retorno.put("telefono", datos.getString(5));                    
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
        return retorno;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datos.pushy;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;

import java.util.Map;

public class PushyAPI {
    public static ObjectMapper mapper = new ObjectMapper();
    
    // Insert your Secret API Key here
    public static final String SECRET_API_KEY = "79abeae53ea7d50510640d89c8d2c4122c14f1f8be0b0cf8d7532f24fba42412";
    public static final String SECRET_API_KEY_ENTURNEX = "b057b33b0aabf0afb852a931c25635330758ab3ce9f44940d9aeb5fa20287081";

    public static void sendPush(PushyPushRequest req, int app) throws Exception {
        // Get custom HTTP client
        HttpClient client = new DefaultHttpClient();
        
        String secreto = "";                
        if(app==1){
            secreto = SECRET_API_KEY;
        }else{
            secreto = SECRET_API_KEY_ENTURNEX;
        }
        // Create POST request
        HttpPost request = new HttpPost("https://api.pushy.me/push?api_key=" + secreto);

        // Set content type to JSON
        request.addHeader("Content-Type", "application/json");

        // Convert post data to JSON
        String json = mapper.writeValueAsString(req);

        // Send post data as string
        request.setEntity(new StringEntity(json));

        // Execute the request
        HttpResponse response = client.execute(request, new BasicHttpContext());

        // Get response JSON as string
        String responseJSON = EntityUtils.toString(response.getEntity());

        // Convert JSON response into HashMap
        Map<String, Object> map = mapper.readValue(responseJSON, Map.class);

        // Got an error?
        if (map.containsKey("error")) {
            // Throw it
            throw new Exception(map.get("error").toString());
        }
    }
}

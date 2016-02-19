package com.pewpew.pewpew;

import com.google.gson.Gson;
import com.pewpew.pewpew.model.User;
import org.junit.Test;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class TestRegestrationService {

    private String targetURL = "http://localhost:8080/register";

    @Test
    public void testRegestration() throws  Exception {
        String urlParameters = "email=" + URLEncoder.encode("myemail@mail.ru", "UTF-8")
                + "&password=" + URLEncoder.encode("myPassword", "UTF-8")
                ;
        URL url;
        HttpURLConnection connection = null;
        try {
            url = new URL(targetURL);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/json");

            connection.setRequestProperty("Content-Length", "" +
                    Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches (false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream (
                    connection.getOutputStream ());
            wr.writeBytes (urlParameters);
            wr.flush ();
            wr.close ();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            System.out.println(response.toString());

        } catch (Exception e) {

            e.printStackTrace();
            System.err.println(e);

        } finally {

            if(connection != null) {
                connection.disconnect();
            }
        }
    }
}

package com.pewpew.pewpew.additional;

//import org.jetbrains.annotations.Nullable;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;

/**
 * Created by Leman on 19.02.16.
 */
public class BufferRead {
    private HttpServletRequest request;

    public BufferRead(HttpServletRequest request) {
        this.request = request;
    }

//    @Nullable
    public StringBuffer getStringBuffer() {
        StringBuffer jsonBuffer = new StringBuffer();
        try {
            BufferedReader reader = request.getReader();
            String line = null;
            while ((line = reader.readLine()) != null) {
                jsonBuffer.append(line);
            }
        } catch (Exception e) {
            System.err.println(e.toString());
            return null;
        }
        return jsonBuffer;
    }

}

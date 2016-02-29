package com.pewpew.pewpew.additional;

import org.jetbrains.annotations.Nullable;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

public class BufferRead {
    private final HttpServletRequest request;

    public BufferRead(HttpServletRequest request) {
        this.request = request;
    }

    @Nullable
    public StringBuffer getStringBuffer() {
        StringBuffer jsonBuffer = new StringBuffer();
        try {
            BufferedReader reader = request.getReader();
            String line = null;
            while ((line = reader.readLine()) != null) {
                jsonBuffer.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonBuffer;
    }

}

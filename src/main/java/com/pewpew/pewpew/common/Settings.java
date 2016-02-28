package com.pewpew.pewpew.common;

import javax.servlet.http.HttpServletResponse;

public class Settings {
    public static final String DB_ADDRESS = "127.0.0.1";
    public static final int DB_PORT = 27017;
    public static final String USERS_COLLECTION = "PewPewDataBase";
    public static final String MODEL_PACKAGE = "com.pewpew.pewpew.model";

    public static final int UNAUTHORIZED = HttpServletResponse.SC_UNAUTHORIZED;
    public static final int BAD_REQUEST = HttpServletResponse.SC_BAD_REQUEST;
    public static final int INTERNAL_ERROR = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
    public static final int FORBIDDEN = HttpServletResponse.SC_FORBIDDEN;
}

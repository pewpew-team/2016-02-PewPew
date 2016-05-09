package com.pewpew.pewpew.main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.pewpew.pewpew.serialize.ObjectIdDeserializer;
import com.pewpew.pewpew.serialize.ObjectIdSerializer;
import com.pewpew.pewpew.serialize.UserDeserializer;
import com.pewpew.pewpew.serialize.UserSerializer;
import com.pewpew.pewpew.model.User;
import org.bson.types.ObjectId;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Provider
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class GsonMessageBodyHandler implements MessageBodyWriter<Object>,
        MessageBodyReader<Object> {
    private static final String UTF_8 = "UTF-8";

    private Gson gson;

    private Gson getGson() {
        if (gson == null) {
            final GsonBuilder gsonBuilder = new GsonBuilder();
            gson = gsonBuilder.disableHtmlEscaping().
                    registerTypeAdapter(User.class, new UserSerializer()).
                    registerTypeAdapter(User.class, new UserDeserializer()).
                    registerTypeAdapter(ObjectId.class, new ObjectIdSerializer()).
                    registerTypeAdapter(ObjectId.class, new ObjectIdDeserializer()).
                    setPrettyPrinting().create();
        }
        return gson;
    }

    @Override
    public boolean isReadable(Class<?> type, Type genericType,
                              Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public Object readFrom(Class<Object> type, Type genericType,
                           Annotation[] annotations, MediaType mediaType,
                           MultivaluedMap<String, String> httpHeaders, InputStream entityStream) {
        //noinspection TryWithIdenticalCatches
        try (InputStreamReader streamReader = new InputStreamReader(entityStream, UTF_8)) {

            try {
                final Type jsonType;
                if (type.equals(genericType)) {
                    jsonType = type;
                } else {
                    jsonType = genericType;
                }
                try {
                    return getGson().fromJson(streamReader, jsonType);
                } catch (JsonIOException | JsonSyntaxException e) {
                    e.printStackTrace();
                }

            } finally {
                try {
                    streamReader.close();
                } catch (IOException | NullPointerException e) {
                    e.printStackTrace();
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Object();
    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType,
                               Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public long getSize(Object object, Class<?> type, Type genericType,
                        Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    @SuppressWarnings("OverlyBroadThrowsClause")
    @Override
    public void writeTo(Object object, Class<?> type, Type genericType,
                        Annotation[] annotations, MediaType mediaType,
                        MultivaluedMap<String, Object> httpHeaders,
                        OutputStream entityStream) throws IOException,
            WebApplicationException {
        try (OutputStreamWriter writer = new OutputStreamWriter(entityStream, UTF_8)) {
            final Type jsonType;
            if (type.equals(genericType)) {
                jsonType = type;
            } else {
                jsonType = genericType;
            }
            getGson().toJson(object, jsonType, writer);
        }
    }
}
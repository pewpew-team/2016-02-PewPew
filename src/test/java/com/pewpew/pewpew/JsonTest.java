package com.pewpew.pewpew;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.bson.types.ObjectId;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Created by Leman on 20.02.16.
 */

class TaskObject {

    @SerializedName("_id")
    public ObjectId _id;
}

public class JsonTest {
    @Test
    public void shouldWriteCorrectJSON() {
        // given
        TaskObject taskObject = new TaskObject();
        taskObject._id = new ObjectId("51eae100c2e6b6c222ec3431");

        Gson gson = new GsonBuilder().registerTypeAdapter(ObjectId.class, new ObjectIdTypeAdapter()).create();

        // when
        String gsonString = gson.toJson(taskObject);

        // then
//        assertThat(gsonString,{\"_id\":{\"$oid\":\"51eae100c2e6b6c222ec3431\"}}"));
    }

    private class ObjectIdTypeAdapter extends TypeAdapter<ObjectId> {
        @Override
        public void write(final JsonWriter out, final ObjectId value) throws IOException {
            out.beginObject()
                    .name("$oid")
                    .value(value.toString())
                    .endObject();
        }

        @Override
        public ObjectId read(final JsonReader in) throws IOException {
            in.beginObject();
            assert "$oid".equals(in.nextName());
            String objectId = in.nextString();
            in.endObject();
            return new ObjectId(objectId);
        }
    }
    @Test
    public void shouldReadFromJSON() {
        // given
        Gson gson = new GsonBuilder().registerTypeAdapter(ObjectId.class, new ObjectIdTypeAdapter()).create();

        // when
        String string = "{\"_id\":{\"$oid\":\"51eae100c2e6b6c222ec3431\"}}";
        TaskObject actualTaskObject = gson.fromJson(string, TaskObject.class);

        // then
        TaskObject taskObject = new TaskObject();
        taskObject._id = new ObjectId("51eae100c2e6b6c222ec3431");
        assertEquals(actualTaskObject._id, (taskObject._id));
//        assertThat(actualTaskObject._id, is(taskObject._id));
    }
}

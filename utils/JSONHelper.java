package com.ip.barcodescanner.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class JSONHelper {

    // ----------- null value handling codes -----------
    private static class NullStringToEmptyAdapterFactory<T> implements
            TypeAdapterFactory {
        @SuppressWarnings({"unchecked", "hiding"})
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {

            Class<T> rawType = (Class<T>) type.getRawType();
            if (rawType != String.class) {
                return null;
            }
            return (TypeAdapter<T>) new StringAdapter();
        }
    }

    private static class StringAdapter extends TypeAdapter<String> {
        public String read(JsonReader reader) throws IOException {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
                return "";
            }
            return reader.nextString();
        }

        public void write(JsonWriter writer, String value) throws IOException {
            if (value == null) {
                writer.nullValue();
                return;
            }
            writer.value(value);
        }
    }

    // -----------------------------------------------------

    public static Object Deserialize(String jsonString, Class<?> type) {
        if (jsonString == null) {
            return null;
        }
        // the below construct replaces null with "" string
        Gson serializer = new GsonBuilder().registerTypeAdapterFactory(
                new NullStringToEmptyAdapterFactory()).create();
        return serializer.fromJson(jsonString, type);
    }

    public static String Serialize(Object objToSerialize) {
        Gson serializer = new GsonBuilder().serializeNulls().create();
        return serializer.toJson(objToSerialize);
    }
}

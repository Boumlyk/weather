package com.test.metio.data.source.local.db;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class Converters {


    @TypeConverter
    public static String StringListToString(List<String> value) {
        return new Gson().toJson(value);
    }

    @TypeConverter
    public static List<String> stringToList(String value) {
        return new Gson().fromJson(value, new TypeToken<List<String>>() {
        }.getType());
    }


}

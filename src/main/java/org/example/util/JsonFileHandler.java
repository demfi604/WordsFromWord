package org.example.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.example.model.Config;
import org.example.model.LevelsOutput;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class JsonFileHandler {
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create();

    public static Config loadConfig(String path) throws IOException {
        String json = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
        return GSON.fromJson(json, Config.class);
    }

    public static List<String> loadStringList(String path) throws IOException {
        String json = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
        Type listType = new TypeToken<List<String>>() {}.getType();
        return GSON.fromJson(json, listType);
    }

    public static void saveOutput(String path, LevelsOutput output) throws IOException {
        try (FileWriter writer = new FileWriter(path)) {
            GSON.toJson(output, writer);
        }
    }
}
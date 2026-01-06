package com.engine.dialogue.io;

import com.engine.dialogue.graph.DialogueGraph;
import com.google.gson.Gson;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class DialogueLoader {
    private static final Gson gson = new Gson();

    public static DialogueGraph load(String path) {
        InputStream inputStream = DialogueLoader.class.getResourceAsStream(path);

        if (inputStream == null) {
            System.err.println("Could not find file: " + path);
            return null;
        }

        try (Reader reader = new InputStreamReader(inputStream)) {
            return gson.fromJson(reader, DialogueGraph.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

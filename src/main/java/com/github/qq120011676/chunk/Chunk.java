package com.github.qq120011676.chunk;

import com.google.gson.Gson;
import lombok.Data;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Data
public class Chunk implements Serializable {
    private Map<String, String> headers = new HashMap<>();
    private String content;

    private static final String HEADER_SEPARATOR = ": ";
    private static final Charset ENCODING = StandardCharsets.UTF_8;

    public String header(String name) {
        return this.headers.get(name);
    }

    public void header(String name, String value) {
        this.headers.put(name, value);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        Set<String> names = this.getHeaders().keySet();
        for (String name : names) {
            stringBuilder.append(MessageFormat.format("{0}{1}{2}", name, HEADER_SEPARATOR, this.getHeaders().get(name)));
            stringBuilder.append(System.lineSeparator());
        }
        stringBuilder.append(System.lineSeparator());
        stringBuilder.append(this.getContent());
        return stringBuilder.toString();
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    public void writer(String pathname) throws IOException {
        write(this.toString(), pathname);
    }

    public void writer(OutputStream outputStream) throws IOException {
        write(this.toString(), outputStream);
    }

    public void writerJson(String pathname) throws IOException {
        write(this.toJson(), pathname);
    }

    public void writerJson(OutputStream outputStream) throws IOException {
        write(this.toJson(), outputStream);
    }

    public static Chunk reader(String pathname) throws IOException {
        return parse(read(pathname));
    }

    public static Chunk readerUrl(String url) throws IOException {
        return parse(readUrl(url));
    }

    public static Chunk reader(InputStream inputStream) throws IOException {
        return parse(read(inputStream));
    }

    public static Chunk parse(String s) throws IOException {
        String[] array = s.split("(\\r?\\n)\\s*(\\r?\\n)", 2);
        Chunk chunk = new Chunk();
        chunk.setContent(array[1]);
        String[] headerLines = array[0].split("\\r?\\n");
        for (String line : headerLines) {
            String[] nv = line.split(HEADER_SEPARATOR);
            chunk.header(nv[0], nv[1]);
        }
        return chunk;
    }

    public static Chunk readerJson(String pathname) throws IOException {
        return parseJson(read(pathname));
    }

    public static Chunk readerJsonUrl(String url) throws IOException {
        return parseJson(readUrl(url));
    }

    public static Chunk readerJson(InputStream inputStream) throws IOException {
        return parseJson(read(inputStream));
    }

    public static Chunk parseJson(String json) {
        return new Gson().fromJson(json, Chunk.class);
    }

    private static String read(String pathname) throws IOException {
        return FileUtils.readFileToString(new File(pathname), ENCODING);
    }

    private static String read(InputStream inputStream) throws IOException {
        return IOUtils.toString(inputStream, ENCODING);
    }

    private static String readUrl(String url) throws IOException {
        return IOUtils.toString(new URL(url), ENCODING);
    }

    private static void write(String content, String pathname) throws IOException {
        FileUtils.writeStringToFile(new File(pathname), content, ENCODING);
    }

    private static void write(String content, OutputStream outputStream) throws IOException {
        IOUtils.write(content, outputStream, ENCODING);
    }
}

package com.github.qq120011676.chunk;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class ChunkTest {
    @Test
    void test() throws IOException {
        String chunkFileName = "/tmp/test.chunk";
        String jsonFileName = "/tmp/test.json";

        String content = "中午\n中文\n\n1";
        Chunk chunk = new Chunk();
        chunk.setContent(content);
        chunk.header("a", "a1");

        chunk.writer(chunkFileName);
        chunk.writerJson(jsonFileName);

        Chunk chunkRead = Chunk.reader(chunkFileName);
        Assertions.assertNotNull(chunkRead);
        Assertions.assertEquals(content, chunkRead.getContent());

        Chunk chunkJsonRead = Chunk.readerJson(jsonFileName);
        Assertions.assertNotNull(chunkJsonRead);
        Assertions.assertEquals(content, chunkJsonRead.getContent());
    }
}

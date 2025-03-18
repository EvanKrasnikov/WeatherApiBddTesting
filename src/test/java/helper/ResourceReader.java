package helper;

import lombok.Builder;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Builder
@Log4j2
public class ResourceReader {

    private String basePath;

    public String readJson(@NonNull String path) {
        String json;

        if (!basePath.isBlank()) {
            path = basePath.concat(path);
        }

        if (log.isDebugEnabled()) {
            log.debug("Resource path = " + path);
        }

        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path)){
            json = new String(Objects.requireNonNull(is).readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return json;
    }

}

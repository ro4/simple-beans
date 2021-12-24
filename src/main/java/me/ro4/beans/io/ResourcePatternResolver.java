package me.ro4.beans.io;

import java.io.IOException;
import java.util.List;

public interface ResourcePatternResolver {

    String CLASSPATH_ALL_URL_PREFIX = "classpath*:";

    List<String> getResources(String locationPattern) throws IOException;
}

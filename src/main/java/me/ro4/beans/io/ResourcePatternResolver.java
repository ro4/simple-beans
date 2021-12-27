package me.ro4.beans.io;

import java.io.IOException;
import java.util.List;

public interface ResourcePatternResolver {
    List<String> getResources(String locationPattern) throws IOException;
}

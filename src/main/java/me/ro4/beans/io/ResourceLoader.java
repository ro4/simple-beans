package me.ro4.beans.io;

import me.ro4.beans.util.ResourceUtils;

import java.io.InputStream;

public interface ResourceLoader {
    String CLASSPATH_URL_PREFIX = ResourceUtils.CLASSPATH_URL_PREFIX;

    InputStream getResource(String location);
}

package me.ro4.beans.io;

import me.ro4.beans.util.ClassUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SimpleResourcePatternResolver implements ResourcePatternResolver {
    @Override
    public List<String> getResources(String locationPattern) throws IOException {
        List<String> myClassName = new ArrayList<>();
        ClassLoader loader = ClassUtils.getDefaultClassLoader();
        String packagePath = locationPattern.replace(".", "/");
        URL url = loader.getResource(packagePath);
        if (null == url) {
            return myClassName;
        }
        File file = new File(url.getPath());
        File[] childFiles = file.listFiles();
        getClassNameByFile(childFiles, myClassName);;
        return myClassName;
    }

    protected void getClassNameByFile(File[] childFiles, List<String> result) {
        for (File childFile : childFiles) {
            if (childFile.isDirectory()) {
                getClassNameByFile(childFile.listFiles(), result);
            } else {
                String childFilePath = childFile.getPath();
                if (childFilePath.endsWith(".class")) {
                    childFilePath = childFilePath.substring(childFilePath.indexOf("\\classes") + 9, childFilePath.lastIndexOf("."));
                    childFilePath = childFilePath.replace("\\", ".");
                    result.add(childFilePath);
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        List<String> classNames =  new SimpleResourcePatternResolver().getResources("me.ro4");
        System.out.println(classNames);
    }

}

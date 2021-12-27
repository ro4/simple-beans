package me.ro4.beans.io;

import me.ro4.beans.annotation.Component;
import me.ro4.beans.util.ClassUtils;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@SuppressWarnings("unused")
public class SimpleResourcePatternResolver implements ResourcePatternResolver {

    @Override
    public List<String> getResources(String locationPattern) {
        List<String> clazzList = new ArrayList<>();
        ClassLoader loader = ClassUtils.getDefaultClassLoader();
        String packagePath = locationPattern.replace(".", "/");
        URL url = loader.getResource(packagePath);
        if (null == url) {
            return clazzList;
        }
        File file = new File(url.getPath());
        File[] childFiles = file.listFiles();
        getClassNameByFile(childFiles, clazzList);
        return clazzList;
    }

    protected void getClassNameByFile(File[] childFiles, List<String> result) {
        if (null == childFiles) {
            return;
        }
        for (File childFile : childFiles) {
            if (childFile.isDirectory() && null != childFile.listFiles()) {
                getClassNameByFile(Objects.requireNonNull(childFile.listFiles()), result);
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

    public static void main(String[] args) {
        List<String> classNames = new SimpleResourcePatternResolver().getResources("me.ro4");
        System.out.println(classNames);
    }

}

package org.schlunzis.neuroevolution.util;

import lombok.SneakyThrows;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PluginLoader {

    private static final String pluginFolder = "plugins";

    private static File getPluginFolder() {
        String path = getProgramPath();
        File file = new File(path + File.separatorChar + pluginFolder);
        return file;
    }

    public static void init() {
        File folder = getPluginFolder();
        if (!folder.exists())
            folder.mkdir();
    }

    /**
     * Because of the call to {@link #classImplementsInterface} this method will
     * only work for interfaces
     * 
     * @param <T>   The interface type of the plugin
     * @param clazz
     * @return
     */
    public static <T> List<T> loadPlugins(Class<T> clazz) {
        try {
            // Get all the files in plugins folder
            File folder = getPluginFolder();
            File[] plugins = folder.listFiles();

            System.out.println("found " + plugins.length + " plugins");

            ArrayList<T> classes = new ArrayList<>();

            for (int i = 0; i < plugins.length; i++) {
                // Skip if the file is not a jar
                if (!plugins[i].getName().endsWith(".jar"))
                    continue;

                // Create a JarFile
                JarFile jarFile = new JarFile(plugins[i]);

                // Get the entries
                Enumeration<?> e = jarFile.entries();

                // Create a URL for the jar
                URL[] urls = { new URL("jar:file:" + plugins[i].getAbsolutePath() + "!/") };
                ClassLoader cl = URLClassLoader.newInstance(urls);

                while (e.hasMoreElements()) {
                    JarEntry je = (JarEntry) e.nextElement();

                    // Skip directories
                    if (je.isDirectory() || !je.getName().endsWith(".class")) {
                        continue;
                    }

                    // -6 because of .class
                    String className = je.getName().substring(0, je.getName().length() - 6);
                    className = className.replace('/', '.');

                    // Load the class
                    Class<?> c = cl.loadClass(className);

                    // if class is instance of IPlayer
                    if (classImplementsInterface(c, clazz)) {
                        // Create an instance of the class
                        Constructor<?> constructor = c.getConstructor();
                        @SuppressWarnings("unchecked")
                        T player = (T) constructor.newInstance();
                        classes.add(player);
                    }
                }
                jarFile.close();

            }
            return classes;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private static boolean classImplementsInterface(Class<?> clazz, Class<?> interfaceClass) {
        return (interfaceClass.isAssignableFrom(clazz) && !clazz.isInterface());
    }

    /**
     * <a href=https://stackoverflow.com/a/11166880> source </a>
     * 
     * @return
     */
    @SneakyThrows(UnsupportedEncodingException.class)
    private static String getProgramPath() {
        URL url = PluginLoader.class.getProtectionDomain().getCodeSource().getLocation();
        String jarPath = URLDecoder.decode(url.getFile(), "UTF-8");
        String parentPath = new File(jarPath).getParentFile().getPath();
        return parentPath;
    }
}

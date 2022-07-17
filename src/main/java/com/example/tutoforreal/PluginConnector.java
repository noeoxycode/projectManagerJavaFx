package com.example.tutoforreal;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarFile;

public class PluginConnector {
    static HashSet<Plugin> plugins = new HashSet<>();


    public static URLClassLoader start() throws Exception {
        URLClassLoader classLoaderToreturn = null;
        String myPluginPath = "C:/Users/noe/IdeaProjects/tutoForReal/src/main/java/com/example/tutoforreal/plugins" ;
        File pluginDirectory = new File(myPluginPath);
        if (!pluginDirectory.exists()) pluginDirectory.mkdir();
        File[] files = pluginDirectory.listFiles((dir, name) -> name.endsWith(".jar"));
        VBox loadedPlugins = new VBox(6);
        loadedPlugins.setAlignment(Pos.CENTER);
        if (files != null && files.length > 0) {
            ArrayList<String> classes = new ArrayList<>();
            ArrayList<URL> urls = new ArrayList<>(files.length);
            for (File file : files) {
                JarFile jar = new JarFile(file);
                jar.stream().forEach(jarEntry -> {
                    if (jarEntry.getName().endsWith(".class")) {
                        classes.add(jarEntry.getName());
                    }
                });
                URL url = file.toURI().toURL();
                urls.add(url);
            }
            URLClassLoader urlClassLoader = new URLClassLoader(urls.toArray(new URL[urls.size()]));
            classLoaderToreturn = urlClassLoader;
            classes.remove(4);
            //classes.remove(4);
            System.out.println("Ensemble des classes"+classes+"\n\n");

            classes.forEach(className -> {
                System.out.println(className);
                try {
                    Class cls = urlClassLoader.loadClass(className.replaceAll("/", ".").replace(".class", ""));
                    Class[] interfaces = cls.getInterfaces();
                    for (Class intface : interfaces) {
                        if (intface.equals(Plugin.class)) {
                            Plugin plugin = (Plugin) cls.newInstance();
                            plugins.add(plugin);
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            if (!plugins.isEmpty()) loadedPlugins.getChildren().add(new Label("Loaded plugins:"));
            plugins.forEach(plugin -> {
                try {
                    plugin.initialize();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                loadedPlugins.getChildren().add(new Label(plugin.name()));
            });
        }
        return classLoaderToreturn;
    }

    public static void closePlugin() throws IOException {
        Data.plugin.close();
        System.out.println("test close");
        Data.plugin = null;
    }
}
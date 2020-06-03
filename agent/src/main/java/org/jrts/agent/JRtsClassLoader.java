package org.jrts.agent;

import java.net.URL;
import java.net.URLClassLoader;

public class JRtsClassLoader extends URLClassLoader {

    public JRtsClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

}

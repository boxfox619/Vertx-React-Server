package com.boxfox.support.vertx.router;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class RouterPackageLoader {

    public static String[] getRoutingPackages() {
        List<String> packages = new ArrayList();
        BufferedReader reader = new BufferedReader(new InputStreamReader(RouterPackageLoader.class.getClassLoader().getResourceAsStream("router.packages")));
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                packages.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return packages.toArray(new String[packages.size()]);
    }

}

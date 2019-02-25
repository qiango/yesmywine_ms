package com.yesmywine.util.basic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by WANG, RUIQING on 9/22/16.
 * twitter: @taylorwang789
 * e-mail: i@wrqzn.com
 */
public class Shell {
    public static String run(String command) {
        Process process;
        StringBuffer output = new StringBuffer();
        try {
            process = Runtime.getRuntime().exec(command);
            process.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = "";
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return output.toString();
    }
}

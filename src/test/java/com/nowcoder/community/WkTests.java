package com.nowcoder.community;

import java.io.IOException;

public class WkTests {

    public static void main(String[] args) {
        String cmd = "c:/soft/wkhtmltopdf/bin/wkhtmltoimage --quality 75 https://www.nowcoder.com c:/workspace/data/wk/images/2.png";
        try {
            Runtime.getRuntime().exec(cmd);
            System.out.println("success!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

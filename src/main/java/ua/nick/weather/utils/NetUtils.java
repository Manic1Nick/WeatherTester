package ua.nick.weather.utils;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class NetUtils {

    public static void load(String url, String dest)
            throws URISyntaxException, IOException {

        try (InputStream is = new URI(url).toURL().openStream();
             OutputStream os = new FileOutputStream(dest)) {

            byte[] buff = new byte[8000];
            int count = 0;

            System.out.println(is.toString());

            while((count = is.read(buff)) != -1){
                os.write(buff, 0, count);
                os.flush();

            }
        }
    }

    public static String urlToString(URL url) throws IOException {

        InputStream is = url.openStream();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = "";
        while((line = bufferedReader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

}

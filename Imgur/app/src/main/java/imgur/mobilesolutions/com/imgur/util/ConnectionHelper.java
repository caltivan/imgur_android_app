package imgur.mobilesolutions.com.imgur.util;


import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


/**
 * Created by jgomez on 4/23/17.
 */
public class ConnectionHelper {

    final int TIME_OUT_CONNECTION = 10000;


    public String getImgurGallerySevice(boolean hot, boolean viral) {
        String filter = "/user";
        if (hot) {
            filter += "/hot";
        }
        if (viral) {
            filter = "/in_most_viral";
        }
        String urlService = String.format("https://api.imgur.com/3/gallery%s/0.json", filter);
        String serverResponse = null;
        try {
            URL url = new URL(urlService);
            HttpsURLConnection httpsUrlConClient = (HttpsURLConnection) url.openConnection();
            httpsUrlConClient.setRequestMethod("GET");
            httpsUrlConClient.setConnectTimeout(TIME_OUT_CONNECTION);
            httpsUrlConClient.setReadTimeout(TIME_OUT_CONNECTION);
            httpsUrlConClient.addRequestProperty("Authorization", AppSettings.CLIENT_ID);
            httpsUrlConClient.addRequestProperty("User-Agent", AppSettings.USER_AGENT);
            int responseCode = httpsUrlConClient.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                InputStream in = new BufferedInputStream(httpsUrlConClient.getInputStream());
                serverResponse = new String(readBytes(in));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serverResponse;
    }

    private byte[] readBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
}

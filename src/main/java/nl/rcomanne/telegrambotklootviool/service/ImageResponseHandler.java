package nl.rcomanne.telegrambotklootviool.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Random;

@Slf4j
public class ImageResponseHandler implements ResponseHandler<String> {

    @Override
    public String handleResponse(HttpResponse response) throws IOException {
        final int status = response.getStatusLine().getStatusCode();

        if (status >= 200 && status < 300) {
            HttpEntity entity = response.getEntity();
            String entityString = EntityUtils.toString(entity);

            JSONArray array = new JSONObject(entityString).getJSONArray("items");
            final String selected = array.getJSONObject(new Random().nextInt(array.length())).getString("link");
            log.debug("selected image url: {}", selected);
            return selected;
        } else {
            throw new ClientProtocolException("Unexpected response status: " + status);
        }
    }
}

package dogapi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

/**
 * BreedFetcher implementation that relies on the dog.ceo API.
 * Note that all failures get reported as BreedNotFoundException
 * exceptions to align with the requirements of the BreedFetcher interface.
 */
public class DogApiBreedFetcher implements BreedFetcher {
    private final OkHttpClient client = new OkHttpClient();
    private static final String API_URL = "https://dog.ceo/api/breed";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String APPLICATION_JSON = "application/json";

    /**
     * Fetch the list of sub breeds for the given breed from the dog.ceo API.
     * @param breed the breed to fetch sub breeds for
     * @return list of sub breeds for the given breed
     * @throws BreedNotFoundException if the breed does not exist (or if the API call fails for any reason)
     */
    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        // TODO Task 1: Complete this method based on its provided documentation
        //      and the documentation for the dog.ceo API. You may find it helpful
        //      to refer to the examples of using OkHttpClient from the last lab,
        //      as well as the code for parsing JSON responses.
        // return statement included so that the starter code can compile and run.
        final Request request = new Request.Builder()
                .url(String.format("%s/%s/list", API_URL, breed))
                .addHeader(CONTENT_TYPE, APPLICATION_JSON)
                .build();
        try {
            final Response response = client.newCall(request).execute();
            final JSONObject responseBody = new JSONObject(response.body().string());
            if (responseBody.getString("status").equals("success")) {
                final JSONArray breeds = responseBody.getJSONArray("message");
                final List<String> breedList = new ArrayList<>();
                for (int i = 0; i < breeds.length(); i++) {
                    breedList.add(breeds.getString(i));
                }
                return breedList;
            }
            else {
                throw new BreedNotFoundException(breed);
            }
        }
        catch (IOException | JSONException event){
            throw new BreedNotFoundException(breed);
        }

    }
}
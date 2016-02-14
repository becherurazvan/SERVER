import Google.GoogleSecret;
import Google.TokenUtil;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import com.google.api.services.script.model.*;
import com.google.api.services.script.Script;

import java.util.Arrays;
import java.util.List;

public class GoogleScriptService {
    /** Application name. */
    private static final String APPLICATION_NAME =
            "SCRUMcompanion";


    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY =
            JacksonFactory.getDefaultInstance();

    /** Global instance of the HTTP transport. */
    private static HttpTransport HTTP_TRANSPORT;

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }


    private static HttpRequestInitializer setHttpTimeout(
            final HttpRequestInitializer requestInitializer) {
        return new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest httpRequest) throws IOException {
                requestInitializer.initialize(httpRequest);
                // This allows the API to call (and avoid timing out on)
                // functions that take up to 6 minutes to complete (the maximum
                // allowed script run time), plus a little overhead.
                httpRequest.setReadTimeout(380000);
            }
        };
    }

    /**
     * Build and return an authorized Script client service.
     *
     * @param {Credential} credential an authorized Credential object
     * @return an authorized Script client service
     */
    public static Script getScriptService() throws Exception {
        Credential credential = authorize();
        return new Script.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, setHttpTimeout(credential))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public static Credential authorize(){
        return  TokenUtil.getCredentials("ya29.iAKn-ukTG74iJATp6v4Xs7EAySkChep0PFYey1bM-e2KxtLjOxdJ2tkPRsW33HlNTuBu", "1/BCqOIuJc1F2OY1g_r9_CP01Zy-5x7s92fwfL-gWcV-lIgOrJDtdun6zK6XiATCKT");
    }

    public static void main(String[] args) throws Exception {
        new GoogleSecret();
        String scriptId = "MmaKzw8TVLaFD69ImsbODnfsKBwR_Wyo5";
        String docId = "1dYr2QCP-ukuLwXTR3a1qbOBp1LEcaVzJY0R22c0Pga8";
        String value = "HEHEHEH";
        Script service = getScriptService();

        System.out.println(service.getApplicationName());

        String functionName = "myFunction";


        List<Object> params = new ArrayList<Object>();
        params.add(docId);
        params.add(value);

        ExecutionRequest request = new ExecutionRequest()
                .setFunction(functionName)
                .setParameters(params);

        try{

            Operation op = service.scripts().run(scriptId,request).execute();





        }catch (GoogleJsonResponseException e){
            e.printStackTrace();
        }

    }

}
package hackatum.de.checkcrash.models;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

public class AccidentProcedure {

    public static AccidentProcedure accidentProcedure;
    public String emergencyNumber;
    public String policeNumber;
    public String rootPage;
    public HashMap<String, Page> pages;

    public static void load(String json) {
        if (accidentProcedure == null) {
            Gson gson = new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .create();
            accidentProcedure = gson.fromJson(json, AccidentProcedure.class);
        }
    }

}

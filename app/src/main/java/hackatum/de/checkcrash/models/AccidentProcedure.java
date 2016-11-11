package hackatum.de.checkcrash.models;

import com.google.gson.Gson;

import java.util.HashMap;

/**
 * Created by matthias on 11/11/16.
 */

public class AccidentProcedure {

    public static AccidentProcedure accidentProcedure;
    public String emergencyNumber;
    public String policeNumber;
    public String rootPage;
    public HashMap<String, Page> pages;

    public static void load(String json) {
        if (accidentProcedure == null) {
            Gson gson = new Gson();
            accidentProcedure = gson.fromJson(json, AccidentProcedure.class);
        }
    }

}

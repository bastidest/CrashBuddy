package hackatum.de.checkcrash.models;

public class AccidentData {

    private static AccidentData accidentData;

    private AccidentData() {

    }

    public AccidentData getInstance() {
        if (accidentData == null) {
            accidentData = new AccidentData();
        }
        return accidentData;
    }
}

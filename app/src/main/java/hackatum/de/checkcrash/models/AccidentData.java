package hackatum.de.checkcrash.models;

import java.util.ArrayList;

public class AccidentData {

    private static AccidentData accidentData;
    public String timestamp;
    public String locationGps, locationName;
    private ArrayList<String> photos = new ArrayList<>();
    private ArrayList<String> audios = new ArrayList<>();
    private ArrayList<QuestionAndAnswer> questions = new ArrayList<>();
    private AccidentData() {

    }

    public AccidentData getInstance() {
        if (accidentData == null) {
            accidentData = new AccidentData();
        }
        return accidentData;
    }

    public void clear() {
        timestamp = "";
        locationGps = "";
        locationName = "";
        photos.clear();
        audios.clear();
        questions.clear();
    }

    public void addPhoto(String filename) {
        photos.add(filename);
    }

    public void addAudio(String filename) {
        audios.add(filename);
    }

    public void addQuestion(String question, String answer) {
        questions.add(new QuestionAndAnswer(question, answer));
    }

    public String generateHTML() {
        StringBuilder builder = new StringBuilder();
        builder.append("<!DOCTYPE html><html><head><title>Accident log</title><style>body {font-family:" +
                " sans-serif;}h1 {display: block;text-align: center;}h3 {display: block;text-align: center;}" +
                "</style></head><body>\n");
        builder.append("<h1>" + timestamp + "</h1>");
        builder.append("<h3>" + locationName + "</h3>");
        builder.append("<h3>" + locationGps + "</h3>");
        builder.append("<br><br><br><table>");
        for (QuestionAndAnswer qa : questions) {
            builder.append("<tr><td>" + qa.question + "</td><td><strong>" + qa.answer + "</strong></td></tr>");
        }
        builder.append("</table><br><br><br>");
        for (String photo : photos) {
            builder.append("<img src='" + photo + "' width='200px'></img>");
        }
        builder.append("</body></html>");
        return builder.toString();
    }

    private class QuestionAndAnswer {
        public String question;
        public String answer;

        public QuestionAndAnswer(String question, String answer) {
            this.question = question;
            this.answer = answer;
        }
    }





}

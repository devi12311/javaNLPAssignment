import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        String folderPath = args[0];

        LanguageModel model = new LanguageModel();
        model.addLanguage("english", folderPath+"/lang-en");
        model.addLanguage("french", folderPath+"/lang-fr");
        model.addLanguage("spanish", folderPath+"/lang-es");

        System.out.println(model.classifyText(folderPath));
    }
}
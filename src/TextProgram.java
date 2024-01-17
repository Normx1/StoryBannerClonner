import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextProgram {
    String pathOfFile;


    public TextProgram(String pathOfFile) {
        this.pathOfFile = pathOfFile;
    }

    ArrayList<String> readText(String pathOfFile) {
        ArrayList arrayList = new ArrayList();
        try {
            BufferedReader fileOutputStream = new BufferedReader(new FileReader(pathOfFile));
            while (fileOutputStream.read() != -1) {
                arrayList.add(fileOutputStream.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return arrayList;
    }


    ArrayList<String> rewriteInJson(List<String> list) {
        ArrayList<String> rewriteMassive = new ArrayList<>();
        for (String i : list
        ) {
            String newText = "{" + '"' + "title" + '"' + ':' + i + "}" + ",";
            System.out.println(newText);
            rewriteMassive.add(newText);
        }
        return rewriteMassive;
    }

    //Заменяет часть текста кодом
    String replaceText(String originalText, String searchText, String replacementText) {
        Pattern pattern = Pattern.compile(searchText, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(originalText);

        System.out.println("Replace is" + matcher.replaceAll(replacementText));
        return matcher.replaceAll(replacementText);
    }


    //  Добавляет уникальный ИД в код
    ArrayList<String> addUnicsId(ArrayList<String> list) {
        System.out.println("Call add Uncial ID");
        for (int i = 0; i < list.size(); i++) {
            String s = list.get(i);
            {
                if (s != null && s.contains("RandomId")) {
                    Random random = new Random();
                    String newRandomId = String.valueOf(random.nextInt(1000000));
                    s = replaceText(s, "RandomId", newRandomId);
                    list.set(i, s);
                }
            }
        }
        return list;

    }

    //Добавляет уникальный ScreenName
    ArrayList<String> addUnicsScreenName(String screenName, String pathOfReadFile) {
        System.out.println("Call method of Replacing ScreenName");
        ArrayList<String> listOfStory = readText(pathOfReadFile);

        for (int i = 0; i < listOfStory.size(); i++) {
            String s = listOfStory.get(i);
            {
                if (s != null && s.contains("ScreenNameTitle")) {
                    s = replaceText(s, '"' + "ScreenNameTitle" + '"', screenName);
                    listOfStory.set(i, s);
                }
            }
        }
        return listOfStory;
    }


    ArrayList<ArrayList> createNewStoryArray(String pathOfReadFile) {
        ArrayList<ArrayList> newStoryArray = new ArrayList<>();
        ArrayList<String> listOfScreenNames = readText("src/ListOfScreenNames");
        listOfScreenNames.forEach(screenName -> {
            newStoryArray.add(addUnicsId(addUnicsScreenName(screenName, pathOfReadFile)));
        });
        return newStoryArray;
    }


    //Печать массива в указанный файл
    void printFile(ArrayList<ArrayList> list, String whereToWrite) {
        try {
            PrintWriter fileWriter = new PrintWriter(new FileOutputStream(whereToWrite));

            list.forEach(arrayList1 -> arrayList1.forEach(s -> {
                fileWriter.write(s + "\n");
                fileWriter.flush();
            }));
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }

    }


    public static void main(String[] args) {

        String pathOfScreenName = "C:\\Users\\qa\\IdeaProjects\\ReWriteText\\src\\ListOfScreenNames";
        String pathOfOneStory = "src/Story";
        String pathOneBanner = "src/Banner";
        TextProgram textProgram = new TextProgram(pathOfScreenName);

        ArrayList<ArrayList> storyArrayList = textProgram.createNewStoryArray(pathOfOneStory);
        ArrayList<ArrayList> BannerArrayList = textProgram.createNewStoryArray(pathOneBanner);

        textProgram.printFile(storyArrayList, "src/ChangedFileStory");
        textProgram.printFile(BannerArrayList, "src/ChangedFileBanner");


////        ArrayList<String> arrayList = textProgram.readText(textProgram.getPathOfFile());
////        ArrayList<String> rewriteMassive = textProgram.rewriteInJson(arrayList);
////
////        PrintWriter fileWriter = new PrintWriter(new FileOutputStream("ChangedFile"));
//
//        rewriteMassive.forEach(s -> {
//            fileWriter.write(s + "\n");
//            fileWriter.flush();
//        });

    }
}

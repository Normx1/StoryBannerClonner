import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextProgram {
    String pathOfWritingFile;
    String pathOfReadingFile;
    String pathOfReplacementList;


    public String getPathOfWritingFile() {
        return pathOfWritingFile;
    }

    public String getPathOfReadingFile() {
        return pathOfReadingFile;
    }

    public String getPathOfReplacementList() {
        return pathOfReplacementList;
    }

    public TextProgram(String pathOfWritingFile, String pathOfReadingFile, String pathOfReplacementList) {
        this.pathOfWritingFile = pathOfWritingFile;
        this.pathOfReadingFile = pathOfReadingFile;
        this.pathOfReplacementList = pathOfReplacementList;
    }


    ArrayList<String> readText(String pathOfFile) {
        ArrayList arrayList = new ArrayList();
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(pathOfFile));
            while (scanner.hasNextLine()) {
                arrayList.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


//                try {

//            BufferedReader fileOutputStream = new BufferedReader(new FileReader(pathOfFile));
//            while (fileOutputStream.read() != -1) {
//                arrayList.add(fileOutputStream.readLine());
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        System.out.println("read text:" + arrayList);
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

    void createFinalString(ArrayList<ArrayList> arrayList) {
        StringBuilder finalFile = new StringBuilder("[");
        System.out.println("Beauty file");
        for (int i = 0; i < arrayList.size(); i++) {
            for (int j = 0; j < arrayList.get(i).size(); j++) {
                finalFile.append((arrayList.get(i)).get(j));
            }
            if (i < arrayList.size() - 1) finalFile.append(",");
        }
        finalFile.append("]");
         try {
            PrintWriter fileWriter = new PrintWriter(new FileWriter("NewFile"));
             fileWriter.write(finalFile.toString());
            fileWriter.flush();
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }

    }

    //Добавляет уникальный ScreenName
    ArrayList<String> addUnicsScreenName(String screenName) {
        System.out.println("Call method of Replacing ScreenName");
        ArrayList<String> listOfStory = readText(getPathOfReadingFile());

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


    ArrayList<ArrayList> createNewStoryArray() {
        ArrayList<ArrayList> newStoryArray = new ArrayList<>();
        ArrayList<String> listOfScreenNames = readText(getPathOfReplacementList());
        listOfScreenNames.forEach(screenName ->
                newStoryArray.add(addUnicsId(addUnicsScreenName(screenName))));
        return newStoryArray;
    }


    //Печать массива в указанный файл
    void printFile(ArrayList<ArrayList> list) {
        try {
            PrintWriter fileWriter = new PrintWriter(new FileOutputStream(getPathOfWritingFile()));

            list.forEach(arrayList1 -> arrayList1.forEach(s -> {
                fileWriter.write(s + "\n");
                fileWriter.flush();
            }));
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }


    public static void main(String[] args) {

        String pathOfReadingFile = "src/Story";
        String pathOfReplacementList = "src/ListOfScreenNames";
        String pathOfWritingFile = "src/ChangedFileStory";


        TextProgram textProgram = new TextProgram(pathOfWritingFile, pathOfReadingFile, pathOfReplacementList);

        ArrayList<ArrayList> storyArrayList = textProgram.createNewStoryArray();

        textProgram.printFile(storyArrayList);


        TextProgram textProgramBanner = new TextProgram("src/ChangedFileBanner", "src/banner", pathOfReplacementList);
        textProgramBanner.printFile(textProgramBanner.createNewStoryArray());

        textProgramBanner.createFinalString(storyArrayList);

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

package pl.edu.agh.lab8.zad1;

import pl.edu.agh.util.Utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class WordsCounter {
    private String filePath;
    private String content;

    public WordsCounter(String filePath) {
        this.filePath = filePath;
        try
        {
            content = new String ( Files.readAllBytes( Paths.get(filePath) ) );
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        //this.content = Utils.loadText(filePath);
    }

    public int count() {
        return countWords(content);
        //return Utils.loadText(filePath).stream().mapToInt(this::countWords).sum();
    }

    public int countWords(String string) {
        boolean whitespace = true;
        int count = 0;
        for (int i = 0; i < string.length(); i++) {
            if (whitespace && string.charAt(i) != ' ') {
                count++;
                whitespace = false;
            }
            if (string.charAt(i) == ' ') {
                whitespace = true;
            }
        }
        return count;
//        return content.split("\\s+").length;
    }

    public int countParallel() {
        return Utils.loadText(filePath).parallelStream().mapToInt(this::countWords).sum();
    }

//    private List<String> splitContent(int pieces) {
//        List<String> result = new ArrayList<>();
//        int start = 0, end;
//        int step = content.length() / pieces;
//        for (int i = 0 ; i < pieces - 1; i++) {
//            end = start + step;
//            while (content.charAt(end) != ' ') {
//                end++;
//            }
//            result.add(content.substring(start, end));
//            start = end;
//        }
//        result.add(content.substring(start));
//        return result;
//    }

    public static void main(String[] args) {
        WordsCounter wordsCounter = new WordsCounter("/home/pawel/Documents/thousand");
        System.out.println(wordsCounter.count());
//        System.out.println(wordsCounter.countParallel());
        Utils.printExecutionTime(wordsCounter::count);
//        Utils.printExecutionTime(wordsCounter::countParallel);
//        Spliterators
    }
}

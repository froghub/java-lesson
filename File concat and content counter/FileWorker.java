
import java.io.*;
import java.util.*;


public class FileWorker {

    /**
     * Reading file to bytes array
     *
     * @param filename   name of file to open
     * @param bytesCount bytes count to read
     * @return byte[]   array of bytes with size = bytesCount
     */
    public static byte[] readBytesToArray(String filename, int bytesCount) {

        byte[] resultArray;
        if (bytesCount > 0) {
            resultArray = new byte[bytesCount];
            try (FileInputStream fis = new FileInputStream(filename)) {
                fis.read(resultArray);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            resultArray = new byte[]{0};
        }
        return resultArray;
    }

    /**
     * Concatinations array of files in one
     *
     * @param destinationFile dest. file
     * @param sourceFiles     String[] of file names
     */
    public static void concatFiles(String destinationFile, String[] sourceFiles) {

        if (sourceFiles.length == 0) {
            System.out.println("Массив с файлами для объединения пуст");
            return;
        }

        for (int i = 0; i < sourceFiles.length; i++) {
            File file = new File(sourceFiles[i]);
            if (file.exists()) {
                concatFile(destinationFile, file);
            } else {
                System.out.println("Файл " + file.getName() + " не существует");
            }
        }


    }


    private static void concatFile(String destinationFile, File sourceFile) {
        try (FileReader fileReader = new FileReader(sourceFile);
             FileWriter fileWriter = new FileWriter(destinationFile, true)
        ) {
            int nextChar;
            while ((nextChar = fileReader.read()) > 0) {
                fileWriter.write((char) nextChar);
                fileWriter.flush();
            }
            fileWriter.write("\n");


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generates file with random content
     * @param count count of files
     * @param size count of chars in each file
     * @return array with file names
     */
    public static String[] generateRandomFiles(int count, int size) {
        String[] fileNames = new String[count];
        for (int i = 0; i < count; i++) {
            fileNames[i] = i + ".txt";
            try (FileWriter fw = new FileWriter(i + ".txt")) {
                for (int j = 0; j < size; j++) {
                    fw.write(new Random().nextInt(25) + 97);
                    fw.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileNames;
    }

    /**
     * Counts unique words in file
     * @param fileName name of file
     * @return count of unique words
     */
    public static int wordsCounter(String fileName) {
        HashSet<String> wordsSet = new HashSet<>();
        String[] wordsArray;
        StringBuilder word = new StringBuilder();
        try (FileReader fr = new FileReader(fileName)) {
            int next;
            while ((next = fr.read()) > 0) {
                word.append((char) next);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        wordsArray = word.toString().split(" ");
        for (int i = 0; i < wordsArray.length; i++) {
            wordsSet.add(wordsArray[i]);
        }
        return wordsSet.size();
    }

    /**
     * Counts and print count of each char in file
     * @param fileName file name
     */
    public static void charCounter(String fileName) {
        String charsPattern = "abcdefghijklmnopqrstuvwxyz";
        HashMap<Character, Integer> counter = new HashMap<>();
        initMap(counter, charsPattern);
        try (FileReader fr = new FileReader(fileName)) {
            int next;
            while ((next = fr.read()) > 0) {
                if (counter.get((char) next) != null) {
                    int oldValue = counter.get((char) next);
                    counter.replace((char) next, ++oldValue);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Map.Entry<Character, Integer> entry : counter.entrySet()) {
            System.out.println("Символ " + entry.getKey() + " встречается " + entry.getValue() + " раз.");
        }

    }

    private static void initMap(HashMap map, String pattern) {
        char[] pattChars = new char[pattern.length()];
        pattern.getChars(0, pattern.length(), pattChars, 0);
        for (int i = 0; i < pattChars.length; i++) {
            map.put(pattChars[i], 0);
        }

    }

}

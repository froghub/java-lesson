import java.util.Arrays;

public class App {
    public static void main(String[] args) {
        //Тест чтения файла в байтовый массив
        System.out.println(Arrays.toString(FileWorker.readBytesToArray("file.txt", 128)));
        //Объединение нескольких файлов
        String[] filesToConcat = FileWorker.generateRandomFiles(10, 256);
        FileWorker.concatFiles("comparedfiles.txt", filesToConcat);
        //Количество уникальных слов в файле
        System.out.println("Words count : " + FileWorker.wordsCounter("words.txt"));
        //Количество букв в файле
        FileWorker.charCounter("lorem");
    }
}

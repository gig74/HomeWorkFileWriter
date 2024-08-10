package org.example.students;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

import static org.example.students.InitialSettings.*; // Класс с начальными initial параметрами

public class FileUtilities {
    /**
     * Checks, whether the child directory is a subdirectory of the base
     * directory.
     *
     * @param base the base directory.
     * @param child the suspected child directory.
     * @return true, if the child is a subdirectory of the base directory.
     * @throws IOException if an IOError occured during the test.
     */
    public static boolean isSubDirectory(File base, File child)
            throws IOException {
        base = base.getCanonicalFile();
        child = child.getCanonicalFile();
        // .getCanonicalFile() - Канонический файл всегда является абсолютным и уникальным. Функция удаляет символы «.» и «..» из пути файла, если они присутствуют.
        File parentFile = child;
        int countCycle = 0 ;
        while ((parentFile != null)||(countCycle > MAX_CYCLE_FIND_DIRECTORY_COUNT)) {
            countCycle++;
            if (base.equals(parentFile)) {
                return true;
            }
            parentFile = parentFile.getParentFile();
        }
        return false;
    }

    /**
     * Проверка и/или создание файла для вывода
     * @param fileOut
     * @return
     */
    public static void verifyFileOut(File fileOut) throws IOException {
        // Проверка что файл непустой
        // Теперь сам файл
        if (fileOut == null) {
            throw new RuntimeException("Передан пустой файл");
        }
        // Проверка, что файл создаётся где-то в базовой директории
        if (!FileUtilities.isSubDirectory(getBaseDir(),fileOut))
        {
            throw new RuntimeException("Для файла должна использоваться директория " + getBaseDir().getCanonicalFile());
        }
        // Проверка на существование файла
        if(!fileOut.exists()) {
            // Вначале создаём все поддиректории если требуется
            File parent = fileOut.getParentFile().getCanonicalFile();
            if (parent != null && !parent.exists() ) {
                 if (!parent.mkdirs()) {
                     throw new RuntimeException("не могу создать директорию " + parent);
                 }
            }
            // Теперь сам файл
            if (fileOut != null && !fileOut.exists() ) {
                if (!fileOut.createNewFile()) {
                    throw new RuntimeException("не могу создать файл " + fileOut.getCanonicalFile());
                }
            }
        }
        // Проверка на "директорию"
        if(fileOut.isDirectory()) {
            throw new RuntimeException("Не могу создать файл - уже есть директория с именем " + fileOut.getCanonicalFile());
        }
        // Проверка на доступность по записи
        if(!fileOut.canWrite()) {
            throw new RuntimeException("Не могу изменять файл " + fileOut.getCanonicalFile());
        }
        // Предупреждение (без исключительной ситуации) если файл непустой и будет перезаписан
        if(fileOut.length() > 0) {
            System.out.println("Файл непустой и будет перезаписан " + fileOut.getCanonicalFile());
        }
    }
    /**
     * Программа для записи переданной коллекции в текстовый файл
     * Используется Writer потому что так было указано в видео по заданию (хотя в текстовке этого указания нет)
     * @return ResultWritingFile - перечень затребованных итоговых параметров записи файла
     * @param fileOut
     * @param listStringOut
     * @throws IOException
     */
    public static ResultWritingFile writeListOut(File fileOut, List<String> listStringOut) throws IOException {
        verifyFileOut(fileOut);
        long start = System.currentTimeMillis();
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(fileOut, StandardCharsets.UTF_8, false))) {
//        try (FileWriter fileWriter = new FileWriter(fileOut, Charset.forName("UTF8"), false)) {
            for (String stringOut : listStringOut) {
                fileWriter.write(stringOut + System.lineSeparator());
            }
            fileWriter.flush(); // Без этого не сохранялось
        } catch (IOException ex) {
            System.out.println(ex.getMessage()); // Написали про возникшее исключение
            throw ex; // И "прокинули" выше
        }
        long finish = System.currentTimeMillis();
        ResultWritingFile resultWritingFile = new ResultWritingFile((long) fileOut.length(),finish - start);
        return resultWritingFile;
    }


    /**
     * Программа для чтения текстового файла в коллекцию
     * @param requestDir
     * @param requestNameFile
     * @return
     * @throws IOException
     */
    public static List<String> readTextFileInList( File requestDir, String requestNameFile) throws IOException {

        File file = findFileInDir(requestDir, requestNameFile);
        List<String> resultList = new ArrayList<>();

//            StringBuilder result = new StringBuilder();
//        try (FileReader fileReader = new FileReader(file)) {
//            int charCode = fileReader.read();
//            while(charCode != -1) {
//                    result.append((char) charCode);
//                charCode = fileReader.read();
//            }
//        } catch (IOException ex) {
//            System.out.println(ex.getMessage()); // Написали про возникшее исключение
//            throw ex; // И "прокинули" выше
//        }
//            List<String> resultList = Arrays.asList(result.toString().split("\\r?\\n"));

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String lineFromFile;
                while ((lineFromFile = reader.readLine()) != null) {
                    resultList.add(lineFromFile);
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage()); // Написали про возникшее исключение
                throw ex; // И "прокинули" выше
            }
        return resultList;
    }

    /**
     * Метод для поиска файла с указанным имененм в директории(используются вызов рекурсивного метода)
     * @param rootDirectory
     * @param fileName
     * @return
     * @throws IOException
     */
    public static File findFileInDir(File rootDirectory, String fileName) throws IOException {
        File resultFile = null;
        final List<File> foundFilesList = new ArrayList<>();
        findFilesByName(rootDirectory, fileName, foundFilesList, 0);
        if ((foundFilesList == null) || (foundFilesList.size() == 0)){
            throw new RuntimeException("Читаемый файл не найден");
        }
        if (foundFilesList.size() > 1) {
            throw new RuntimeException("Найдено более одного файла с указанным именем");
        }
        resultFile = foundFilesList.get(0);
        System.out.println ("Найденный файл " + resultFile.getCanonicalFile());
        return resultFile;
    }

    /* рекурсивный метод для поиска файла в директории и поддиректориях */
    public static void findFilesByName(File rootDir, String filenameToSearch, List<File> foundFilesList, int countOfRecursion) {
        if(countOfRecursion > MAX_CYCLE_FIND_DIRECTORY_COUNT) {
            throw new RuntimeException("Превышен уровень допустимой вложенности директорий");
        }
        List<File> files = List.of(rootDir.listFiles());
        for (Iterator iterator = files.iterator(); iterator.hasNext(); ) {
            File file = (File) iterator.next();
            if (file.isDirectory()) {
                countOfRecursion++;
                findFilesByName(file, filenameToSearch, foundFilesList, countOfRecursion);
                countOfRecursion--;
            } else if(file.getName().equalsIgnoreCase(filenameToSearch)){
                foundFilesList.add(file);
            }
        }
    }

}

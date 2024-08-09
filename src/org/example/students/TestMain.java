package org.example.students;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class TestMain {
    public static void main(String[] args) throws IOException {
        DataTest dataTest = new DataTest();
        File testFileOut = dataTest.newAbsolutePathTestFile();
        List<String> testStringList = dataTest.newTestList();

        System.out.println(">>>>> Тест 1. Запись текстового файла");
        ResultWritingFile resultWritingFile = FileUtilities.writeListOut(testFileOut,testStringList);
        System.out.println("Информация выгружена . Размер файла " + testFileOut.getCanonicalFile() + " - " + resultWritingFile.lengthFileOut() +
                " байт. Время записи " + resultWritingFile.timeWritingFileMilliSec() + " (в миллисекундах) ");

        System.out.println(">>>>> Тест 2. Чтение текстового файла");
        List<String> listResultTextFile= FileUtilities.readTextFileInList(dataTest.newTestDir(), dataTest.newTestNameFile());
        System.out.println(listResultTextFile);
    }
}
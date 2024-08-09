package org.example.students;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataTest {
    private final String TEST_SUB_DIRECTORY = "\\6\\7\\8\\";
    private final String TEST_NAME_FILE = "2.txt";
    public List<String> newTestList() {
        List<String> stringList = new ArrayList<>();
        stringList.add("BEGIN");
        stringList.add("fkjsdfkds");
        stringList.add("ёёёёёёЁЁЁЁЁЁЁЁ~!@#$%^&*()_+");
        stringList.add("рвыашывфопекрцпзав");
        stringList.add("lfdls/.,flkfvlfdl;");
        for(int i = 0 ; i < 10; i++) {
            stringList.add("DFSGDFHRTHGЫВПФФАВЫПКПУЕИ ЛОДОПРТВ ЕАВРАРВАИТ");
        }
        stringList.add("THE END");
        return stringList;
    }
    public File newAbsolutePathTestFile() throws IOException {
        return new File(InitialSettings.getBaseDir().getCanonicalPath() + TEST_SUB_DIRECTORY + TEST_NAME_FILE );
    }
    public String newTestNameFile() throws IOException {
        return TEST_NAME_FILE;
    }
    public File newTestDir() throws IOException {
        return new File(InitialSettings.getBaseDir().getCanonicalPath() );
    }

}

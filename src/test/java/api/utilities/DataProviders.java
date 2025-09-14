package api.utilities;

import java.io.IOException;

import org.testng.annotations.DataProvider;

public class DataProviders {
    @DataProvider(name = "Data")
    public String[][] getAllData() throws IOException {
        String path = System.getProperty("user.dir") + "//testdata//Userdata.xlsx";
        String sheetName = "Sheet1";

        int totalrows = XLUtility.getRowCount(path, sheetName);
        int totalcols = XLUtility.getCellCount(path, sheetName, 1);

        String[][] userData = new String[totalrows][totalcols];

        for (int i = 1; i <= totalrows; i++) {
            for (int j = 0; j < totalcols; j++) {
                String cellValue = XLUtility.getCellData(path, sheetName, i, j);
                userData[i - 1][j] = cellValue != null ? cellValue : "";
            }
        }
        return userData;
    }

    @DataProvider(name = "UserNames")
    public String[][] getUserNames() throws IOException {
        String path = System.getProperty("user.dir") + "//testdata//Userdata.xlsx";
        String sheetName = "Sheet1";

        int totalrows = XLUtility.getRowCount(path, sheetName);
        String[][] userData = new String[totalrows][1];

        for (int i = 1; i <= totalrows; i++) {
            String cellValue = XLUtility.getCellData(path, sheetName, i, 1);
            userData[i - 1][0] = cellValue != null ? cellValue : "";
        }
        return userData;
    }
}
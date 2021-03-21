import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Importer {

    public static void main(String[] args) throws IOException {
        String folder = "C:\\projects\\mireaweb\\20_years_format\\";

        File outFile = new File(folder, "out.sql");
        FileWriter writer = new FileWriter(outFile);

        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File f, String name) {
                // We want to find only .json files
                return name.endsWith(".json");
            }
        };

        File[] files = new File(folder).listFiles(filter);

        int city_id = 1;
        for (File file : files) {
            Gson gson = new Gson();
            Reader reader = Files.newBufferedReader(file.toPath());
            List<Double> values = gson.fromJson(reader, List.class);


            String name = file.getName().substring(0, file.getName().indexOf('.'));

            writer.write("INSERT INTO city(id, name) " +
                    "VALUES(" + city_id + ", '" + name + "');\n");


            int i = 0;
            for (int year = 1; year <= 20; year++) {
                for (int month = 1; month <= 12; month++) {
                    int days_in_month = 0;
                    switch (month) {
                        case 1:
                        case 3:
                        case 5:
                        case 7:
                        case 8:
                        case 10:
                        case 12:
                            days_in_month = 31;
                            break;

                        case 4:
                        case 6:
                        case 9:
                        case 11:
                            days_in_month = 30;
                            break;


                        case 2:
                            days_in_month = 28;
                            break;
                    }

                    for (int day = 1; day <= days_in_month; day++) {
                        double value = values.get(i);
                        writer.write("INSERT INTO mesure(city_id, year, month, day, value) " +
                                "VALUES(" + city_id + ", " + year + ", " + month + ", " + day + ", " + value + ");\n");
                        i++;
                    }
                }
            }
            city_id++;
        }
        writer.close();
    }

}

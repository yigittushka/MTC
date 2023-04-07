import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.zip.GZIPInputStream;
public class Main {
    public static void main(String[] args) {

        LocalDateTime now = LocalDateTime.now();
        String filename = String.format("smb_%s_app01.log.gz", now.format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")));

        try (GZIPInputStream gzis = new GZIPInputStream(new FileInputStream(String.format("/log/%s", filename)));
             BufferedReader br = new BufferedReader(new InputStreamReader(gzis))) {

            StringBuilder content = new StringBuilder();
            String line;


            while ((line = br.readLine()) != null) {
                content.append(line);
            }


            File tempFile = new File("temp.txt");
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(content.toString()
                                 .getBytes());
            }


            int count = 0;
            try (BufferedReader tempFileReader = new BufferedReader(new InputStreamReader(new FileInputStream(tempFile)))) {
                while ((line = tempFileReader.readLine()) != null) {
                    if (line.contains("Success")) {
                        count++;
                    }
                }
            }


            System.out.printf("Количество строк со словом 'Success': %d%n", count);


            if (!tempFile.delete()) {
                System.err.println("Ошибка при удалении временного файла");
            }

        } catch (IOException ioe) {
            System.err.println("Ошибка при чтении архива: " + ioe.getMessage());
        }
    }
}


package org.redapps.netmon.util;

import org.redapps.netmon.model.IpUsage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Vector;

@Component
public class ProcessIpUsageFiles {

    private static String reportPath;

    /*
     * Read file path from properties file.
    */
    @Value("${report.path}")
    public void setReportPath(String value) {
        reportPath = value;
    }

    /**
     * Read data from files and calculate ip usage for ip address
     * @param startDate the start date to calculate ip usage
     * @param endDate the end date to calculate ip usage
     * @param ipAddress the ip address to process
     * @param type up.csv or dl.csv
     * @return a list of usage value for every day
     */
    public static Vector<IpUsage> processFiles(LocalDate startDate, LocalDate endDate, String ipAddress,
                                               String type){

        Vector<IpUsage> ipUsages = new Vector<>(10);
        IpUsage ipUsage;

        while (endDate.isAfter(startDate)) {

            double usage = 0;

            Path filePath = Paths.get(reportPath, String.format("%02d", startDate.getYear()),
                    String.format("%02d", startDate.getMonthValue()),
                    String.format("%02d", startDate.getDayOfMonth()), type);

            File file = new File(filePath.toString());

            if(file.exists() && !file.isDirectory()) {
                BufferedReader bufferedReader;
                try {
                    bufferedReader = new BufferedReader(new FileReader(file));

                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        String[] splitedStr = line.split(",");
                        if(splitedStr[0].trim().compareToIgnoreCase(ipAddress) == 0) {
                            usage += Integer.parseInt(splitedStr[2].trim());
                        }
                    }

                    ipUsage = new IpUsage(startDate, usage);
                    ipUsages.add(ipUsage);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            startDate = startDate.plusDays(1);
        }
        return ipUsages;
    }
}

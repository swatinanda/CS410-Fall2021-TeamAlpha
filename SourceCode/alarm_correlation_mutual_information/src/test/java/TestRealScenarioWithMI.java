import com.illinois.cs410.project.AlarmTemplates;
import com.illinois.cs410.project.DataProcessor;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class TestRealScenarioWithMI {
    List<AlarmTemplates> alarms;
    @Test
    public void testKafkaScenarioWithRealData() throws IOException, CsvValidationException {
        LinkedHashMap<Integer, BitSet> templates = getAlarmTemplates();
        DataProcessor processor = new DataProcessor(templates, 288);
        processor.computerMutualInformation();
        double[][] mi = processor.getMutualInfomation();
        // find out the the correlated alarms based on mutual information for the first template
        int[] indexes = indexesOfTopElements(mi[0],6);
        System.out.println(Arrays.toString(indexes));
        //System.out.println(alarms.get(0));
        for(int i=0; i<indexes.length; i++)
        {
            System.out.println(alarms.get(indexes[i]));
        }

        System.out.println("Their Mutual Information Matrix is : ");
        for(int i=0; i< indexes.length; i++)
        {
            for(int j=0; j<indexes.length; j++)
                System.out.printf("%.3f ", mi[indexes[i]][indexes[j]]);
            System.out.print("\n");
        }
    }
    static int[] indexesOfTopElements(double[] orig, int nummax) {
        double[] copy = Arrays.copyOf(orig,orig.length);
        Arrays.sort(copy);
        double[] honey = Arrays.copyOfRange(copy,copy.length - nummax, copy.length);
        int[] result = new int[nummax+1];
        result[0] = 0;
        int resultPos = 1;
        for(int i = 0; i < orig.length; i++) {
            if(resultPos == nummax+1)
                break;
            double onTrial = orig[i];
            int index = Arrays.binarySearch(honey,onTrial);
            if(index < 0) continue;
            result[resultPos++] = i;
        }
        return result;
    }
    private LinkedHashMap<Integer, BitSet> getAlarmTemplates() throws IOException, CsvValidationException {
        LinkedHashMap<Integer, BitSet> templates = new LinkedHashMap<>();
        try (CSVReader sparseReader = new CSVReaderBuilder(new FileReader("src/test/resources/TestAlarmData.csv"))
                .withCSVParser(new CSVParserBuilder().withSeparator(';')
                        .build()).build()) {
            String[] lines;
            int count =0;
            alarms = new ArrayList<>();
            while ((lines = sparseReader.readNext()) != null) {
                AlarmTemplates a = new AlarmTemplates();
                String b = lines[4].toString();
                String[] bitArray = b.replace("}","").replace("{","").split(",");
                BitSet bitSet = new BitSet();
                for(int k=0; k<bitArray.length; k++)
                {
                    String bit = bitArray[k].trim();
                    bitSet.set(Integer.parseInt(bit));
                }
                templates.put(count, bitSet);
                a.setId((long)count);
                a.setHost(lines[0]);
                a.setService(lines[3]);
                a.setMessage(lines[1]);
                a.setSource(lines[2]);
                alarms.add(a);
                count++;
            }
        }
        return templates;
    }

}

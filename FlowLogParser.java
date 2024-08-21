import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FlowLogParser {

    public static void main(String[] args) {
        String lookupTablePath = "lookup_table.txt";
        String flowLogPath = "flow_logs.txt";
        
        Map<String, Integer> tagsCounts = new HashMap<>();
        Map<String, Integer> portProtocolCounts = new HashMap<>();
        
        parseFlowLog(flowLogPath, lookupTablePath, tagsCounts, portProtocolCounts);
        saveFiles(tagsCounts, portProtocolCounts);
    }

    public static Map<String, String> loadLookupTable(String filepath) {
        Map<String, String> lookupTable = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length == 3) {
                    String key = values[0] + "," + values[1].toLowerCase();
                    lookupTable.put(key, values[2]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lookupTable;
    }

    public static void parseFlowLog(String filepath, String lookupTablePath, 
                                    Map<String, Integer> tagsCounts, Map<String, Integer> portProtocolCounts) {
        Map<String, String> lookupTable = loadLookupTable(lookupTablePath);
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] columns = line.trim().split("\\s+");
                if (columns.length >= 7) {
                    String dstport = columns[5];
                    String protocolNumber = columns[7];
                    
                    String protocol = ProtocolMapper.getProtocolName(protocolNumber).toLowerCase();
                    String key = dstport + "," + protocol.toLowerCase();
                    
                    String tag = lookupTable.getOrDefault(key, "Untagged");
                    
                    tagsCounts.put(tag, tagsCounts.getOrDefault(tag, 0) + 1);
                    
                    portProtocolCounts.put(key, portProtocolCounts.getOrDefault(key, 0) + 1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveFiles(Map<String, Integer> tagsCounts, Map<String, Integer> portProtocolCounts) {
        try (FileWriter tagWriter = new FileWriter("tag_counts.csv");
             FileWriter portProtocolWriter = new FileWriter("port_protocol_counts.csv")) {

            tagWriter.write("Tag,Count\n");
            for (Map.Entry<String, Integer> entry : tagsCounts.entrySet()) {
                tagWriter.write(entry.getKey() + "," + entry.getValue() + "\n");
            }

            portProtocolWriter.write("Port,Protocol,Count\n");
            for (Map.Entry<String, Integer> entry : portProtocolCounts.entrySet()) {
                portProtocolWriter.write(entry.getKey() + "," + entry.getValue() + "\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

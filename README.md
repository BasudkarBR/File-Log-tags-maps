### Flow Log Tagging Program

## Project Overview

This program is designed to parse a file containing flow log data and map each row to a tag based on a lookup table. The lookup table is a plain text file with three columns: `dstport`, `protocol`, and `tag`. The combination of `dstport` and `protocol` determines the appropriate tag for each flow log entry.

## Assumptions and Limitations

### Supported Log Format
- **Default Log Format (Version 2):** The program only supports the provided sample log format, which is the default VPC flow log format (version 2). Any deviations from this format, such as custom fields or different log versions, are not supported.
- **Fixed Columns:** The program assumes that the log entries have a fixed structure where certain fields (e.g., destination port, protocol) are always present at specific positions. The program will not work correctly with logs that have a different column order or additional/omitted fields.

### Case Sensitivity
- **Case Insensitivity:** The program is case-insensitive when handling protocol names. This means that whether the protocol is listed as `TCP`, `tcp`, or any other case variation in the lookup table, it will be matched correctly with the flow log entries.

### Output
- **Untagged Entries:** If a flow log entry does not match any `dstport` and `protocol` combination in the lookup table, it will be categorized as "Untagged" in the output.
- **Strict Lookup:** The program requires an exact match of `dstport` and `protocol` in the lookup table to assign a tag. If either the `dstport` or `protocol` does not match exactly, the entry will be considered untagged.

## How to Compile and Run

1. **Compile the Java Files:**
   - Ensure that the Java Development Kit (JDK) is installed on your system.
   - Open a terminal or command prompt and navigate to the directory containing the source files.
   - Compile the Java files using the following command:
     ```sh
     javac ProtocolMapper.java FlowLogParser.java
     ```

2. **Run the Program:**
   - After compiling, run the program using the following command:
     ```sh
     java FlowLogParser
     ```
   - The program will read `lookup_table.txt` and `flow_logs.txt` from the current directory and generate two output files:
     - `tag_counts.csv`: Contains the count of matches for each tag.
     - `port_protocol_counts.csv`: Contains the count of matches for each port/protocol combination.

## Output Files

- **tag_counts.csv:** This file lists each tag found in the lookup table and the number of times it was matched against the flow logs.
- **port_protocol_counts.csv:** This file contains each unique combination of destination port and protocol found in the flow logs, along with the number of times each combination was encountered.

## Analysis of the Program

### Performance Considerations
- **Efficient Processing:** The program is designed to handle flow log files up to 10 MB in size and lookup tables with up to 10,000 entries efficiently. It reads and processes the files line by line, ensuring that memory usage remains manageable even with large files.
- **HashMap Usage:** The lookup table is stored in a `HashMap`, allowing for constant-time `O(1)` lookups when matching flow log entries to tags. This approach minimizes the time complexity of the program, making it suitable for processing large volumes of log data.

### Testing
- **Test Cases:** The program was tested with the provided sample `flow_logs.txt` and `lookup_table.txt` files. It successfully mapped flow log entries to tags and generated the expected output files.
- **Case Insensitivity:** Tests were conducted to ensure that protocol names in different cases (`tcp`, `TCP`, `Tcp`, etc.) were correctly matched.
- **Untagged Entries:** The program was tested with flow logs that did not have corresponding entries in the lookup table to verify that they were correctly categorized as "Untagged."

## Conclusion

This program provides a robust and efficient solution for tagging flow log data based on a lookup table. While it is limited to the default log format and requires well-formed input files, it handles large datasets effectively and produces accurate results. Future enhancements could further expand its capabilities and error-handling features. 

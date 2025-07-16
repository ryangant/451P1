Install Prerequisites:

Java JDK 17+

Apache Maven

Download the Project, then open a terminal and navigate into the project folder.

To Run the Benchmark Generator:

bash
mvn compile exec:java -Dexec.mainClass=com.example.BenchmarkSorts

This will create .dat and _summary.dat files for MergeSort and ShellSort.

To View the Results in a GUI Table:

bash
mvn compile exec:java -Dexec.mainClass=com.example.ReportViewer

In the GUI:

Click “Open .dat file…”

Select a _summary.dat file (e.g., shellsort_summary.dat)
package org.lobo.census.graph;

public class Main {
    public static void main(String[] args) {
        // Parse command line options
        if (args.length < 1) {
            System.out.println("Usage: java ... <country-2-char-code> ...");
            System.out.println("Example: java IN");
            System.exit(1);
        }

        System.out.print("Getting data for: ");
        for (int i = 0; i < args.length; i++)
            System.out.print(args[i] + ", ");
        System.out.println();

        CountryData[] dataArray = QueryCensus.getData(args);

        DrawGraph.draw(dataArray);
    }
}

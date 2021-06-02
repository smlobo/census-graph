package org.lobo.census.graph;

public class Main {
    public static void main(String[] args) {
        // Parse command line options
        if (args.length < 1) {
            System.out.println("Usage: java ... [start-year] <country-2-char-code> ...");
            System.out.println("Example: java IN");
            System.exit(1);
        }

        // User gave a start year
        int startYear = 1950;
        int startIndex = 0;
        try {
            startYear = Integer.parseInt(args[0]);
            startIndex++;
        }
        catch (NumberFormatException nfe) {
            startYear = 1950;
        }

        // Create the array of countries requested
        String countryCodes[] = new String[args.length - startIndex];
        System.arraycopy(args, startIndex, countryCodes, 0, args.length-startIndex);

        System.out.print("Getting data for: ");
        for (String code : countryCodes)
            System.out.print(code + ", ");
        System.out.println();

        CountryData[] dataArray = QueryCensus.getData(startYear, countryCodes);

        DrawGraph.draw(dataArray);
    }
}

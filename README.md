# census-graph
Java query census.gov international database for population, parse with Jackson, draw a graph with JavaFX.

# Build
`mvn clean package`

# Run
`java -jar target/census-graph-*-jar-with-dependencies.jar <countryCode1> [ <countryCode2> ...]`

# Examples
- `java -jar target/census-graph-*-jar-with-dependencies.jar US JP BR MX IN CN ID`

# References
- [census.gov API](https://www.census.gov/data/developers/data-sets/international-database.html)
- [JavaFX Charts](https://docs.oracle.com/javafx/2/charts/jfxpub-charts.htm)
- [Jackson](https://github.com/FasterXML/jackson)

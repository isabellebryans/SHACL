// Jena takes graph of data and Shapes data type for the shapes
package org.example.Apache;

import org.apache.jena.graph.Graph;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.shacl.ShaclValidator;
import org.apache.jena.shacl.Shapes;
import org.apache.jena.shacl.ValidationReport;
import org.apache.jena.shacl.lib.ShLib;

import org.example.loadData;
import java.io.FileNotFoundException;


public class Main_Jena {

    public static void main(String[] args) throws FileNotFoundException {
        // Load graphs
        Graph dataGraph = loadData.initAndLoadModelFromResource("meteo1.ttl", Lang.TURTLE).getGraph();
        Graph shapesGraph = loadData.initAndLoadModelFromResource("meteoShapes.ttl", Lang.TURTLE).getGraph();
        Shapes shapes = Shapes.parse(shapesGraph);

        ValidationReport report = ShaclValidator.get().validate(shapes, dataGraph);
        ShLib.printReport(report);
        System.out.println();
        RDFDataMgr.write(System.out, report.getModel(), Lang.TTL);
    }
}

package org.example.Apache;

import org.apache.jena.graph.Graph;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.shacl.ShaclValidator;
import org.apache.jena.shacl.Shapes;
import org.apache.jena.shacl.ValidationReport;
import org.apache.jena.shacl.lib.ShLib;
import org.apache.jena.vocabulary.RDF;
import org.example.loadData;
import java.io.FileNotFoundException;
import java.io.InputStream;
import org.apache.jena.shacl.validation.*;
import org.apache.jena.shacl.vocabulary.SHACL;

public class Main_Apache {

    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("Hello world!");
        Graph dataGraph = loadData.initAndLoadModelFromResource("db_incorrect.ttl", Lang.TURTLE).getGraph();
        Graph shapesGraph = loadData.initAndLoadModelFromResource("shapes.ttl", Lang.TURTLE).getGraph();
        Shapes shapes = Shapes.parse(shapesGraph);

        ValidationReport report = ShaclValidator.get().validate(shapes, dataGraph);
        ShLib.printReport(report);
        System.out.println();
        RDFDataMgr.write(System.out, report.getModel(), Lang.TTL);
    }
}

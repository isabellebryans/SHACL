// Uses a Reader (set of characters in a stream)
package org.example.RDF4J;

import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.shacl.ShaclValidator;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.vocabulary.RDF4J;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryException;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.sail.memory.MemoryStore;
import org.eclipse.rdf4j.sail.shacl.ShaclSail;
import org.eclipse.rdf4j.sail.shacl.ShaclSailValidationException;
import org.eclipse.rdf4j.sail.shacl.results.ValidationReport;
import org.example.TopQuadrant.Main_TopQuadrant;
import org.example.loadData;

import java.io.*;

public class Main_RDF4J {
    public static File getFileFromResource(String fileName) {
        return new File(Main_RDF4J.class.getClassLoader().getResource(fileName).getFile());
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
        System.out.println("Hello world!");
        ShaclSail shaclSail = new ShaclSail(new MemoryStore());
        Repository repo = new SailRepository(shaclSail);

        try (RepositoryConnection connection = repo.getConnection()){

            connection.begin();

            // Convert shapes file to a Reader (string of characters)
            Reader shaclRules = new FileReader(Main_RDF4J.getFileFromResource("shapes.ttl"));

            connection.add(shaclRules, "", RDFFormat.TURTLE, RDF4J.SHACL_SHAPE_GRAPH);
            connection.commit();

            Reader data = new FileReader(Main_RDF4J.getFileFromResource("db.ttl"));
            connection.add(data, "", RDFFormat.TURTLE);

            try { connection.commit(); }
            catch (RepositoryException exception){
                Throwable cause = exception.getCause();
                if (cause instanceof ShaclSailValidationException){
                    System.out.println("SHALC VIOLATION!!!");
                    ValidationReport report = ((ShaclSailValidationException) cause).getValidationReport();
                    Model reportModel = ((ShaclSailValidationException) cause).validationReportAsModel();
                    Rio.write(reportModel, System.out, RDFFormat.TURTLE);
                } throw exception;
            }



        }



    }
}

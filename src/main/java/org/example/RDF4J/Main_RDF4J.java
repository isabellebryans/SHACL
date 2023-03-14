// Uses a Reader (set of characters in a stream)
package org.example.RDF4J;


import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.vocabulary.RDF4J;
import org.eclipse.rdf4j.repository.RepositoryException;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.repository.sail.SailRepositoryConnection;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.sail.memory.MemoryStore;
import org.eclipse.rdf4j.sail.shacl.ShaclSail;
import org.eclipse.rdf4j.sail.shacl.ShaclSailValidationException;
import org.eclipse.rdf4j.sail.shacl.results.ValidationReport;


import java.io.*;


public class Main_RDF4J {
    public static File getFileFromResource(String fileName) {
        return new File(Main_RDF4J.class.getClassLoader().getResource(fileName).getFile());
    }
    public static void main(String[] args) throws IOException {

        ShaclSail shaclSail = new ShaclSail(new MemoryStore());

        SailRepository sailRepository = new SailRepository(shaclSail);
        sailRepository.init();

        try (SailRepositoryConnection connection = sailRepository.getConnection()) {

            connection.begin();

            Reader shapes = new FileReader(Main_RDF4J.getFileFromResource("shapes.ttl"));

            connection.add(shapes, "", RDFFormat.TURTLE, RDF4J.SHACL_SHAPE_GRAPH);
            connection.commit();
            connection.begin();

            Reader data = new FileReader(Main_RDF4J.getFileFromResource("db_incorrect.ttl"));

            connection.add(data, "", RDFFormat.TURTLE);

            try { connection.commit(); }
            catch (RepositoryException exception) {
                Throwable cause = exception.getCause();
                if (cause instanceof ShaclSailValidationException) {
                    ValidationReport validationReport = ((ShaclSailValidationException) cause).getValidationReport();
                    Model validationReportModel = ((ShaclSailValidationException) cause).validationReportAsModel();
                    // use validationReport or validationReportModel to understand validation violations
                    Rio.write(validationReportModel, System.out, RDFFormat.TURTLE);
                }
                throw exception;
            }
        }
    }
}

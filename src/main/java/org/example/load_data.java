package org.example;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

import java.io.*;

public class load_data {
    public static Model initAndLoadModelFromResource(String filename, Lang lang){
        // Turn file into input stream to be read
        InputStream dataModelIS = load_data.class.getClassLoader().getResourceAsStream(filename);
        // Create empty RDF model
        Model dataModel = ModelFactory.createDefaultModel();
        // From RDF io lib (riot), Read input stream into new model
        RDFDataMgr.read(dataModel, dataModelIS, lang);
        return dataModel;
    }
}

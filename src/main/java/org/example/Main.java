package org.example;

import org.topbraid.shacl.validation.ValidationUtil;
import org.topbraid.shacl.vocabulary.SH;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;


public class Main {
    public static Model initAndLoadModelFromResource(String filename, Lang lang){
        // Turn file into input stream to be read
        InputStream dataModelIS = Main.class.getClassLoader().getResourceAsStream(filename);
        // Create empty RDF model
        Model dataModel = ModelFactory.createDefaultModel();
        // From RDF io lib (riot), Read input stream into new model
        RDFDataMgr.read(dataModel, dataModelIS, lang);
        return dataModel;
    }
    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("Hello world!");
        // Read data file and load RDF model
        // coloured shapes downloaded
        Model dataGraph = Main.initAndLoadModelFromResource("db_incorrect.ttl", Lang.TURTLE);

        //Read shapes data from file and load RDF model with shapes constraints
        Model shapesGraph = Main.initAndLoadModelFromResource("shapes.ttl", Lang.TURTLE);

        Resource validationResult = ValidationUtil.validateModel(dataGraph, shapesGraph, false);

        //write validation report
        Model resultModel = validationResult.getModel();
        resultModel.setNsPrefix(SH.PREFIX, SH.NS);
        RDFDataMgr.write(new FileOutputStream("report.ttl"), resultModel, Lang.TURTLE);

    }
}
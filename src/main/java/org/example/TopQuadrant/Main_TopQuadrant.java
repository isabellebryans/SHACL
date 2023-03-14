package org.example.TopQuadrant;

import org.topbraid.shacl.validation.ValidationUtil;
import org.topbraid.shacl.vocabulary.SH;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.example.loadData;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;


public class Main_TopQuadrant {
    public static void main(String[] args) throws FileNotFoundException {

        // Read data file and load RDF model
        // coloured shapes downloaded
        Model dataGraph = loadData.initAndLoadModelFromResource("db_incorrect.ttl", Lang.TURTLE);
        Model shapesGraph = loadData.initAndLoadModelFromResource("shapes.ttl", Lang.TURTLE);
        Resource validationResult = ValidationUtil.validateModel(dataGraph, shapesGraph, false);

        //write validation report
        Model resultModel = validationResult.getModel();
        resultModel.setNsPrefix(SH.PREFIX, SH.NS);
        RDFDataMgr.write(new FileOutputStream("report.ttl"), resultModel, Lang.TURTLE);

    }
}
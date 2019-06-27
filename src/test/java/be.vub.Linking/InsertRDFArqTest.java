package be.vub.Linking;


import org.apache.jena.query.Dataset;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.update.UpdateAction;
import org.junit.Assert;
import org.junit.Test;

public class InsertRDFArqTest {

    @Test
    public void testAdd() {
        Assert.assertEquals(17, 5+12);
    }

    String insertQuery="INSERT DATA\n" +
            "  { \n" +
            "    GRAPH <urn:sparql:tests:insert:data> \n" +
            "      { \n" +
            "        <#book1> <#price> 42 \n" +
            "      } \n" +
            "  }";
    String insertQuery2 = "PREFIX ns: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> INSERT DATA {  <element1_uri> ns:value 5. }";


    String queryQuery="PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
            "PREFIX ns: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
            "PREFIX geo: <http://www.opengis.net/ont/geosparql#>\n" +
            "SELECT * where {"+
            "  ?sub ?pred ?obj .\n" +
            "} \n" ;

    String dataLocation="D:/training/thesis/Parliament/newDataset/";
    String fileName="users.ttl";


//https://www.programcreek.com/java-api-examples/?code=KepaJRodriguez/lucene-skos-ehri/lucene-skos-ehri-master/src/main/java/at/ac/univie/mminf/luceneSKOS/skos/impl/SKOSEngineImpl.java
//http://vos.openlinksw.com/owiki/wiki/VOS/VirtTipsAndTricksSPARQL11Insert
//https://docs.oracle.com/database/nosql-12.1.3.2/RDFGraph/example13.html
// ARQ https://jena.apache.org/documentation/query/update.html
// TDB https://jena.apache.org/documentation/tdb/datasets.html

//CTTE: https://www.youtube.com/watch?v=CTdzmzmBFK0
    @Test
    public void testInsertArq(){
        Dataset dataset = TDBFactory.createDataset(dataLocation);
        Model model = dataset.getNamedModel("http://example.org/userElements");
        //Create RDFS Inference Model, or use other Reasoner e.g. OWL.
        InfModel infModel = ModelFactory.createRDFSModel(model);
        //UpdateAction.readExecute(fileName, dataset) ;
        //UpdateAction.parseExecute(insertQuery2, dataset) ;
        UpdateAction.parseExecute(insertQuery2, infModel) ;

    }

}

package Services.MDPatternDetection.AnalyticalQueriesClasses

import java.util

import Services.MDPatternDetection.AnnotationClasses.MDGraphAnnotated
import Services.MDPatternDetection.ConsolidationClasses.ConsolidationParallel
import Services.MDfromLogQueries.Declarations.Declarations
import Services.MDfromLogQueries.Util.{Constants2, TdbOperation}
import org.apache.jena.query.Dataset
import org.apache.jena.rdf.model.Model
import org.apache.jena.tdb.{TDB, TDBFactory}

import scala.collection.{JavaConverters, mutable}
import scala.io.Source


object AnalyticQueriesScala  {

  var nb_model = 0


  def executeAnalyticQueriesList(endpoint: String)={
    var nb_line = 0
    new Constants2
    //for statistical needs
    var nb = 0
    val modelHashSet = new util.HashSet[Model]

    val QueriesList = Source.fromFile(Declarations.paths.get("AnalyticQueriesFile")).getLines

    QueriesList.grouped(15).foreach {

      groupOfLines => {

        val treatedGroupOfLines = groupOfLines.par.map {

          line => {
            try {
              nb_line += 1
              //queryStr = line;
              println("requete num : " + nb_line)
              modelHashSet.addAll(AnalyticQueries.executeAnalyticQuery(line, endpoint))
            } catch {
              case e: Exception =>
                e.printStackTrace()
                System.out.println("erreur")
                nb += 1
            }
          }
        }
        println("--------------------- un group finished ---------------------------------- ")
        writeInTdb(JavaConverters.asScalaSet(modelHashSet),Declarations.paths.get("dataSetAnalytic"))
        modelHashSet.clear()

      }
    }
  }


  def AnalyticQueriesAnnotation(): Unit = {
    new Constants2
    new TdbOperation
    var modelHashMap = new util.HashMap[String,Model]
    println("Annotation *********************************")
    modelHashMap = TdbOperation.unpersistModelsMap(Declarations.paths.get("dataSetAnalytic"))
    var modelHashMapAnnotated = new util.HashMap[String, Model]

    if (modelHashMap != null) modelHashMapAnnotated = MDGraphAnnotated.constructMDGraphs(modelHashMap)
    if (modelHashMapAnnotated.size()>0){
      writeInTdb(ConsolidationParallel.convertToScalaMap(modelHashMapAnnotated),TdbOperation.dataSetAnalyticAnnotated)

    }else
        println("ERROR")

    print("######################## fin d annotation")
   // TdbOperation.persistHashMap(modelHashMapAnnotated, TdbOperation.dataSetAnalyticAnnotated)

  }

  def writeInTdb(models: mutable.Set[Model], datasetName: String) = {
    val dataset = TDBFactory.createDataset(datasetName)


    models.foreach(m => {

      if (m != null) {
        nb_model += 1
        println("write " + nb_model)
        dataset
          //.originalDataSetTest
          .addNamedModel("model_" + nb_model,
          m)
      }
    })
    TDB.sync(dataset)
    dataset.close()

  }
  def writeInTdb(models: mutable.Map[String,Model], dataset: Dataset) = {

    val keys = models.keysIterator

    for (elem <- keys) {
      if (models.get(elem) != null)
        {
          nb_model +=1
          println("write " + nb_model)
          dataset
            //.originalDataSetTest
            .addNamedModel(elem,
            models(elem))
        }
    }

    TDB.sync(dataset)

  }



}

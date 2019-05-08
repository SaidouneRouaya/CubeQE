package Services.MDPatternDetection.GraphConstructionClasses

import java.io.{File, FileOutputStream, PrintWriter}

import Services.MDfromLogQueries.Declarations.Declarations
import Services.MDfromLogQueries.Util.Constants2
import org.apache.jena.query.{Query, QueryFactory}

import scala.collection.parallel.ParSeq
import scala.io.Source

object Queries2GraphesParallel extends App {

  var queriesNumber=0
  var queriesNumberNonConstructed=0

  val t1 = System.currentTimeMillis()

  //: util.ArrayList[Query]
  def TransformQueriesInFile(filePath: String) = {


    new Constants2()

    val lines = Source.fromFile(filePath).getLines

    lines.grouped(100000).foreach {

      groupOfLines => {

        var nb_req = 0

        val treatedGroupOfLines = groupOfLines.par.map {
          line => {
            nb_req = nb_req + 1
            println("* " + nb_req)
            var constructedQuery = QueryFactory.create()
            try {
              val query = QueryFactory.create(line)
              if (query.isConstructType)
                query.setQuerySelectType();
              val queryUpdate = new QueryUpdate(query)
              constructedQuery = queryUpdate.toConstruct(query)
              /* Some meaning if there is a result != null */
              Some(constructedQuery)
              //  Some(query)

            } catch {
              case unknown => {
                println("une erreur\n\n\n\n\n\n\n\n\n")
                queriesNumberNonConstructed+=1
                writeInLogFile(Declarations.paths.get("constructLogFileParallel"), constructedQuery)
                None
              }
            }

          }

        }

        println("--------------------- un group finished ---------------------------------- ")

        val constructedQueries: ParSeq[Query]= treatedGroupOfLines.collect { case Some(x) => x }
        writeInFile(Declarations.paths.get("constructQueriesFile2"), constructedQueries )
        queriesNumber+=constructedQueries.size

        //writeInFile(constructQueriesFileTest, treatedGroupOfLines.collect { case Some(x) => x })
      }
    }

  }

  /** Function that writes into destinationFilePath the list passed as parameter **/
  def writeInFile(destinationFilePath: String, queries: ParSeq[Query]) = {


    val writer = new PrintWriter(new FileOutputStream(new File(destinationFilePath), true))

    queries.foreach(query => writer.write(query.toString().replaceAll("[\n\r]", "\t") + "\n"))

    writer.close()
  }

  TransformQueriesInFile(Declarations.paths.get("syntaxValidFile2"))

  def writeInLogFile(destinationFilePath: String, query: Query) = {

    val writer = new PrintWriter(new FileOutputStream(new File(destinationFilePath), true))

    writer.write(query.toString().replaceAll("[\n\r]", "\t") + "\n")

    writer.close()
  }

  val duration = System.currentTimeMillis() - t1

  println(duration)

}

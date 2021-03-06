package Services.MDfromLogQueries.SPARQLSyntacticalValidation

import java.io.{File, FileOutputStream, PrintWriter}

import Services.MDfromLogQueries.Declarations.Declarations

import scala.collection.parallel.ParSeq
import scala.io.Source

object SyntacticValidationParallel   {

  var queriesNumber = 0



  //: util.ArrayList[Query]
  def valideQueriesInFile(filePath: String) = {
    var queryList =Source.fromFile(filePath).getLines

    queryList.grouped(100000).foreach {
      groupOfLines => {
        var nb_req = 0
        val treatedGroupOfLines = groupOfLines.par.map {

          line => {
            nb_req = nb_req + 1

            try {
              val verifiedQuery = Validate(line)
              if (verifiedQuery != null) {
                println("* " + nb_req)
                Right(Some(verifiedQuery))
              } else Right(None)

            } catch {
              case e : Exception => {
                println("une erreur\n")
                Left(line)
              }
            }
          }
        }
        println("--------------------- un group finished ---------------------------------- ")

        val (correct, errors) = treatedGroupOfLines.partition(_.isRight)
        writeInFile(Declarations.paths.get("syntaxValidFile2"), correct.collect { case Right(Some(x)) => x })
        writeInLogFile(Declarations.paths.get("syntaxNonValidFile2"), errors.collect { case Left(line) => line })

      }
    }
  }

  /** Function that writes into destinationFilePath the list passed as parameter **/
  def writeInFile(destinationFilePath: String, queries: ParSeq[String]) = {


    val writer = new PrintWriter(new FileOutputStream(new File(destinationFilePath), true))

    queries.foreach{
      query => {
        queriesNumber += 1
        writer.write(query.replaceAll("[\n\r]", "\t") + "\n")
      }
    }

    writer.close()
  }

  def writeInLogFile(destinationFilePath: String, queries: ParSeq[String]) = {

    val writer = new PrintWriter(new FileOutputStream(new File(destinationFilePath), true))

    queries.foreach(query => writer.write(query.replaceAll("[\n\r]", "\t") + "\n"))

    writer.close()
  }



  private def Validate(queryStr: String) = {
    val queryStr2 = QueryFixer.get.fix(queryStr)
    /*System.out.println(queryStr2);*/

    QueryFixer.toQuery(queryStr2).toString
  }




}

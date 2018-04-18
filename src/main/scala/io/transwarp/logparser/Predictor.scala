package io.transwarp.logparser

import org.datavec.api.util.ClassPathResource
import org.deeplearning4j.models.embeddings.inmemory.InMemoryLookupTable
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer
import org.deeplearning4j.models.word2vec.VocabWord
import org.deeplearning4j.text.documentiterator.FileLabelAwareIterator
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory

import scala.collection.JavaConversions._

/**
  * Author: stk
  * Date: 2018/4/18
  */
object Predictor {
  private val modelPath = "model"

  def predict(): Unit = {
    val unClassifiedResource = new ClassPathResource("paravec/unlabeled")
    val unClassifiedIterator = new FileLabelAwareIterator.Builder().addSourceFolder(unClassifiedResource.getFile).build
    val model = WordVectorSerializer.readParagraphVectors(modelPath)
    val tokenizerFactory = new DefaultTokenizerFactory
    tokenizerFactory.setTokenPreProcessor(new CommonPreprocessor)
    val meansBuilder = new MeansBuilder(model.getLookupTable.asInstanceOf[InMemoryLookupTable[VocabWord]], tokenizerFactory)
    val labels: java.util.List[String] = new java.util.ArrayList[String]
    labels.add("9e17dfbb-4f86-4b3d-a649-6b45735d66c0")
    labels.add("29314361-5059-42f6-86b1-828c47885e9d")
    labels.add("ce7f05af-6204-4406-b7ac-a9e17058eb90")
    labels.add("f7adfc98-d4ad-4e33-8ace-96e87d94eab8")
    labels.add("f7d9d2b4-8cbb-45af-832c-c246077f0d14")
    val seeker = new LabelSeeker(labels, model.getLookupTable.asInstanceOf[InMemoryLookupTable[VocabWord]])
    while (unClassifiedIterator.hasNext) {
      val document = unClassifiedIterator.nextDocument
      val documentAsCentroid = meansBuilder.documentAsVector(document)
      val scores = seeker.getScores(documentAsCentroid)
      println("Document '" + document.getLabels + "' falls into the following categories: ")
      for (score <- scores) println("        " + score.getFirst + ": " + score.getSecond)
    }
  }
}

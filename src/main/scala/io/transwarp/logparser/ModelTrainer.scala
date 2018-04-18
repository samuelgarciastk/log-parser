package io.transwarp.logparser

import org.datavec.api.util.ClassPathResource
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer
import org.deeplearning4j.models.paragraphvectors.ParagraphVectors
import org.deeplearning4j.text.documentiterator.FileLabelAwareIterator
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory

/**
  * Author: stk
  * Date: 2018/4/18
  */
object ModelTrainer {
  private val dataPath = "labeled"
  private val modelPath = "model"

  def train(): Unit = {
    val resource = new ClassPathResource(dataPath)
    val iterator = new FileLabelAwareIterator.Builder().addSourceFolder(resource.getFile).build
    val tokenizerFactory = new DefaultTokenizerFactory
    tokenizerFactory.setTokenPreProcessor(new CommonPreprocessor)
    val paragraphVectors = new ParagraphVectors.Builder()
      .learningRate(0.025)
      .minLearningRate(0.001)
      .batchSize(1000)
      .epochs(20)
      .iterate(iterator)
      .trainWordVectors(true)
      .tokenizerFactory(tokenizerFactory)
      .build
    paragraphVectors.fit()
    WordVectorSerializer.writeParagraphVectors(paragraphVectors, modelPath)
  }
}

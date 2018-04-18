package io.transwarp.logparser

import java.util.concurrent.atomic.AtomicInteger

import org.deeplearning4j.models.embeddings.inmemory.InMemoryLookupTable
import org.deeplearning4j.models.word2vec.VocabWord
import org.deeplearning4j.models.word2vec.wordstore.VocabCache
import org.deeplearning4j.text.documentiterator.LabelledDocument
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory
import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.factory.Nd4j

import scala.collection.JavaConversions._

/**
  * Author: stk
  * Date: 2018/4/18
  */
class MeansBuilder(lookupTable: InMemoryLookupTable[VocabWord], tokenizerFactory: TokenizerFactory) {
  val vocabCache: VocabCache[VocabWord] = lookupTable.getVocab.asInstanceOf[VocabCache[VocabWord]]

  def documentAsVector(document: LabelledDocument): INDArray = {
    val documentAsTokens = tokenizerFactory.create(document.getContent).getTokens
    val cnt = new AtomicInteger(0)
    for (word <- documentAsTokens) if (vocabCache.containsWord(word)) cnt.incrementAndGet
    val allWords = Nd4j.create(cnt.get, lookupTable.layerSize)
    cnt.set(0)
    for (word <- documentAsTokens) if (vocabCache.containsWord(word)) allWords.putRow(cnt.getAndIncrement, lookupTable.vector(word))
    allWords.mean(0)
  }
}

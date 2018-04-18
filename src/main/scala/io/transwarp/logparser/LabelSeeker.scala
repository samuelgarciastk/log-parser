package io.transwarp.logparser

import org.deeplearning4j.models.embeddings.inmemory.InMemoryLookupTable
import org.deeplearning4j.models.word2vec.VocabWord
import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.ops.transforms.Transforms
import org.nd4j.linalg.primitives.Pair
import scala.collection.JavaConversions._

/**
  * Author: stk
  * Date: 2018/4/18
  */
class LabelSeeker(labelsUsed: java.util.List[String], lookupTable: InMemoryLookupTable[VocabWord]) {
  if (labelsUsed.isEmpty) throw new IllegalStateException("You can't have 0 labels used for ParagraphVectors")

  def getScores(vector: INDArray): java.util.List[Pair[String, Double]] = {
    val result: java.util.List[Pair[String, Double]] = new java.util.ArrayList
    for (label <- labelsUsed) {
      val vecLabel = lookupTable.vector(label)
      if (vecLabel == null) throw new IllegalStateException(s"Label '$label' has no known vector!")
      val sim = Transforms.cosineSim(vector, vecLabel)
      result.add(new Pair[String, Double](label, sim))
    }
    result
  }
}

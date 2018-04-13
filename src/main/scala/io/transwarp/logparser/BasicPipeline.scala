package io.transwarp.logparser

import edu.stanford.nlp.simple.Sentence

/**
  * Author: stk
  * Date: 2018/4/13
  */
object BasicPipeline {
  val text: String = "Joe Smith was born in California. " +
    "In 2017, he went to Paris, France in the summer. " +
    "His flight left at 3:00pm on July 10th, 2017. " +
    "After eating some escargot for the first time, Joe said, \"That was delicious!\" " +
    "He sent a postcard to his sister Jane Smith. " +
    "After hearing about Joe's trip, Jane decided she might go to France one day."

  def main(args: Array[String]): Unit = {
    val s = new Sentence("Lucy is in the sky with diamonds.")
    println(s.nerTags)
    println(s.posTag(0))
  }
}

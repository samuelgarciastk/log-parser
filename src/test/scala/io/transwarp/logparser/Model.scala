package io.transwarp.logparser

import org.junit.Test

/**
  * Author: stk
  * Date: 2018/4/18
  */
class Model {
  @Test
  def train(): Unit = {
    ModelTrainer.train()
  }

  @Test
  def predict(): Unit = {
    Predictor.predict()
  }
}

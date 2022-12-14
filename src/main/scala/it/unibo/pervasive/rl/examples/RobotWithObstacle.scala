package it.unibo.pervasive.rl.examples

object RobotWithObstacle extends App:
  import it.unibo.pervasive.rl.model.QMatrix.Action.*
  import it.unibo.pervasive.rl.model.QMatrix
  import it.unibo.pervasive.rl.model.QMatrix.*

  val obstacles = Set((2,1), (3,3), (4,0), (5,1), (5,3))

  val rl: QMatrix.Facade = Facade(
    width = 20,
    height = 4,
    initial = (0,0),
    terminal = { case (19, 3) => true; case _ => false },
    reward = { case (pos,_) if obstacles.contains(pos) => -10; case _ => -1},
    jumps = { PartialFunction.empty },
    gamma = 0.9,
    alpha = 0.5,
    epsilon = 0.3,
    v0 = 1
  )

  val q0 = rl.qFunction
  println(rl.show(q0.vFunction,"%2.1f"))
  val q1 = rl.makeLearningInstance().learn(20000,100,q0)
  println(rl.show(q1.vFunction,"%2.1f"))
  println(rl.show(s => actionToString(q1.bestPolicy(s)),"%7s"))

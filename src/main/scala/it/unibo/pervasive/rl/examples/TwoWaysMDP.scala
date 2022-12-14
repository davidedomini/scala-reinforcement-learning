package it.unibo.pervasive.rl.examples

import it.unibo.pervasive.rl.model.QRLImpl

object TwoWaysMDP extends App:
  case class State(n: Int)
  enum Action:
    case left, right

  import Action.*

  val target = -5
  val reward = 10
  val penalty = -0.2
  val maxRight = 10

  val terminalTW: State => Boolean = {
    case State(n) => (n <= target | n >= maxRight)
  }

  object rl extends QRLImpl[State, Action]:

    def qfTW(): Q = QFunction(Action.values.toSet, 1.0, terminalTW)

    def mdpTW(): Environment = MDP.ofFunction {
      case State(n) => Set((left, 1, if (n == target + 1) reward else penalty, State(n - 1)),
        (right, 1, penalty, if (n == maxRight) State(n) else State(n + 1)))
    }

    def rlTW() = QLearning(
      system = QSystem(mdpTW(), State(0), terminalTW),
      gamma = 0.9,
      alpha = 0.5,
      epsilon = 0.0,
      q0 = qfTW()
    )
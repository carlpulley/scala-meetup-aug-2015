package cakesolutions

import akka.actor.{ActorLogging, Actor}
import java.io.{PrintWriter, StringWriter}

/**
 * Trait to add specialised common logging to Actors
 */
trait LoggingActor extends Actor with ActorLogging {

  /**
   * Used to generate exception string messages for use (typically) during logging.
   *
   * @param cause the exception that has been thrown/generated
   * @return      the string representation of this exception
   */
  def exceptionString(cause: Throwable): String = {
    val sw = new StringWriter()
    val pw = new PrintWriter(sw, true)
    cause.printStackTrace(pw)
    sw.getBuffer.toString
  }

  /**
   * We log when the actor has started
   */
  override def preStart() = {
    log.info(s"${getClass.getSimpleName} started")
    super.preStart()
  }

  /**
   * We log when the actor restarts
   *
   * @param cause the exception that has caused this restart
   * @param msg   (optional) the message that this actor was processing when it restarted
   */
  override def preRestart(cause: Throwable, msg: Option[Any]) = {
    log.info(s"${getClass.getSimpleName} restarting whilst handling ${msg.map(_.getClass.getSimpleName)} - reason: ${exceptionString(cause)}")
    log.debug(s"${getClass.getSimpleName} restarting whilst handling $msg - reason: ${exceptionString(cause)}")
    super.preRestart(cause, msg)
  }

  /**
   * We log when the actor stops
   */
  override def postStop() = {
    log.info(s"${getClass.getSimpleName} stopping")
    super.postStop()
  }

  /**
   * We intercept and DEBUG log all received messages
   */
  override def aroundReceive(receive: Actor.Receive, msg: Any) = {
    log.debug(s"${getClass.getSimpleName} received $msg from ${sender()}")
    super.aroundReceive(receive, msg)
  }

  /**
   * We log unhandled messages
   */
  override def unhandled(msg: Any) = {
    log.warning(s"${getClass.getSimpleName} received the unhandled message $msg")
    super.unhandled(msg)
  }

}

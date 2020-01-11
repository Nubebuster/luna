package world.player.skill.firemaking

import api.predef.*
import io.luna.game.event.impl.ItemOnItemEvent
import io.luna.game.event.impl.PlayerEvent
import io.luna.game.model.Position
import io.luna.game.model.`object`.GameObject
import io.luna.game.model.`object`.ObjectDirection
import io.luna.game.model.`object`.ObjectType
import io.luna.game.model.def.ObjectDefinition
import io.luna.game.model.item.GroundItem
import io.luna.game.model.item.Item
import io.luna.game.model.mob.Animation
import io.luna.game.model.mob.Player
import io.luna.util.ExecutorUtils
import world.player.skill.woodcutting.cutTree.Tree
import java.util.*

// TODO Blocked until stationary entity system is complete


val tinderbox = 590

fun lightLog(id: Int, event: PlayerEvent) {


}

/*
on(ItemOnItemEvent::class) {
    world.addObject(5249, plr.position, ObjectType.DEFAULT)


    for(i in 1..10000) {
        if(objectDef(i).name == "Fire") {
           // println(i)
            if(objectDef(i).examine.contains("Looks")) println(i)

            //world.addObject(i, Position(plr.position.x + rand(1, 10), plr.position.y, plr.position.z), ObjectType.DEFAULT)
        }
    }
}
*/

on(ItemOnItemEvent::class)
    .filter { matches(tinderbox) }
    .then {
        for(tree in Tree.VALUES) {

            if(matches(1511)) {
                val fire = world.addObject(5249, plr.position, ObjectType.DEFAULT)
                plr.firemaking.addExperience(40.0) //TODO make right xp per log
                ExecutorUtils.threadFactory("FireTimer").newThread(Runnable {

                    val level = tree.value.level * 2
                    if(level > 5)
                        Thread.sleep( level * 4000L + rand(0, 8000))
                    else
                        Thread.sleep( 5 * 4000L + rand(0, 8000))
                    world.removeObject(fire)
                    world.addItem(592, 1, fire.position, plr)
                }).start()
                break
            }
        }
    }

/*

  }



   TODO:
    -> Placing object after lighting fire
    -> Dropping logs before lighting them
    -> Right clicking log on ground and using "Light" option
    -> Using tinderbox with a log on the ground
    -> Collision detection for stepping after a light (W -> E -> S -> N)
    -> Not being able to light fires on top of existing objects


import io.luna.game.event.Event
import io.luna.game.event.impl .ItemOnItemEvent
import io.luna.game.model.item.Item
import io.luna.game.model.mobile.Skill.FIREMAKING
import io.luna.game.model.mobile.{ Animation, Player }


private case class Log(id: Int, level: Int, exp: Double, lightTime: Int)


private val TINDERBOX = 590
private val ASHES = 592
private val LIGHT_ANIMATION = new Animation (733)

private val BURN_TIME = 30 to 90

private val LOG_TABLE = Map(

)

private val ID_TO_LOG = LOG_TABLE.values.map { it => it.id -> it }.toMap


private def burnLog(log: Log) = {
  val burnTime = BURN_TIME.randomElement

  world.scheduleOnce(burnTime) {

  }
}

private def lightLog(plr: Player, log: Log) = {
  val skill = plr.skill(FIREMAKING)

  val levelRequired = log.level
  if (skill.getLevel < levelRequired) {
      plr.sendMessage(s"You need a Firemaking level of $levelRequired to light these logs.")
      return
  }

  plr.inventory.remove(new Item (log.id))
  plr.stopWalking
  plr.interruptAction

  var loopCount: Int = ???
  world.scheduleTimes(3, loopCount) {
      if (loopCount == 0) {
          skill.addExperience(log.exp)
          burnLog(log)
      } else {
          plr.animation(LIGHT_ANIMATION)
          loopCount -= 1
      }
  }
}

private def lookupLog(id: Int, evt: Event) = {
  ID_TO_LOG.get(id).foreach {
      it =>
      burnLog(plr, it)
      evt.terminate
  }
}


intercept[ItemOnItemEvent] {
  (msg, plr) =>
  if (msg.getUsedId == TINDERBOX) {
      lookupLog(msg.getTargetId)
  } else if (msg.getTargetId == TINDERBOX) {
      lookupLog(msg.getUsedId)
  }
}
*/
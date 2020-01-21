import api.predef.button
import api.predef.object2
import api.predef.on
import io.luna.game.event.impl.ButtonClickEvent
import io.luna.game.event.impl.ObjectClickEvent
import io.luna.net.msg.out.CloseWindowsMessageWriter
import io.luna.net.msg.out.DialogueInterfaceMessageWriter
import io.luna.net.msg.out.WidgetItemModelMessageWriter


on(ObjectClickEvent.ObjectSecondClickEvent::class) {
    println("second click" + gameObject.id)
}
on(ObjectClickEvent.ObjectThirdClickEvent::class) {
    println("third click " + gameObject.id)
}

on(ButtonClickEvent::class){
    when(id) {
        in 3987..3988 -> {
            plr.queue(CloseWindowsMessageWriter())
            plr.sendMessage("yeah")
        }
    }
}

object2(2781) {

    plr.queue(DialogueInterfaceMessageWriter(2400))
    plr.queue(WidgetItemModelMessageWriter(2405, 190, 2349))
    plr.queue(WidgetItemModelMessageWriter(2406, 190, 2351))
    plr.queue(WidgetItemModelMessageWriter(2407, 190, 2355))
    plr.queue(WidgetItemModelMessageWriter(2409, 190, 2353))
    plr.queue(WidgetItemModelMessageWriter(2410, 190, 2357))
    plr.queue(WidgetItemModelMessageWriter(2411, 190, 2359))
    plr.queue(WidgetItemModelMessageWriter(2412, 190, 2361))
    plr.queue(WidgetItemModelMessageWriter(2413, 190, 2363))


}

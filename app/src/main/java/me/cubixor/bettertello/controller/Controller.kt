package me.cubixor.bettertello.controller

import me.cubixor.bettertello.tello.TelloAction
import java.io.Serializable

class Controller(val descriptor: String, val name: String, val inAppID: Int) : Serializable {
    val mappings = mutableMapOf<Int, TelloAction>()

    fun getKeyByAction(telloAction: TelloAction): Int {
        for (key in mappings.keys) {
            if (mappings[key] == telloAction) {
                return key
            }
        }
        return -1
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Controller

        if (descriptor != other.descriptor) return false

        return true
    }

    override fun hashCode(): Int {
        return descriptor.hashCode()
    }


}
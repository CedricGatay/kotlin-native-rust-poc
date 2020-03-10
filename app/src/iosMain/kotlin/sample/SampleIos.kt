package sample
import kotlinx.cinterop.*
import bridge.*

actual class Sample {
    actual fun checkMe() = 7

    actual fun callBridge(){
        println(">>> Entering call bridge")
        memScoped {
            val input = "this is kotlin speaking"
            val sayHello = say_hello(input)?.toKStringFromUtf8()
            println(">>> Say Hello ${sayHello}")
        }
        val themeSongGenerate = theme_song_generate(8.toUByte())?.toKStringFromUtf8()
        println(">>> toKString ${themeSongGenerate}")
        val callChacha20 = call_chacha20("hello from kotlin")
        callChacha20?.apply {
            for (index in 0..6) {
                println("${index} - ${this[index]}")
            }
        }
        println("callChacha20 = ${callChacha20}")
        val readBytes = callChacha20?.readBytes(7)
        println("readBytes = ${readBytes?.map { it.toShort() }}")

    }
}

actual object Platform {
    actual val name: String = "iOS"
}

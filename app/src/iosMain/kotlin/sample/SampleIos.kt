package sample
import bridge.call_chacha20
import bridge.say_hello
import bridge.theme_song_generate
import kotlinx.cinterop.*
import libsecp256k1.SECP256K1_EC_COMPRESSED
import libsecp256k1.secp256k1_ec_pubkey_create
import libsecp256k1.secp256k1_ec_pubkey_serialize
import libsecp256k1.secp256k1_pubkey
import platform.posix.size_tVar

actual class Sample {
    actual fun checkMe() = 7

    actual fun callBridge(){
        val secp256k1ContextCreate = libsecp256k1.secp256k1_context_create(((1 shl 9) + (1 shl 8) + 1).toUInt())
        memScoped {
            val secp256k1Pubkey = nativeHeap.alloc<secp256k1_pubkey>()
            val ctmp = nativeHeap.allocArray<UByteVar>(32)
            var i = 0
            while (i < 32) {
                ctmp[i] = (33 + i).toUByte()
                i++
            }
            println("ctmp = ${ctmp.readBytes(32).map { it.toUByte() }}")

            val secp256k1EcPubkeyCreate = secp256k1_ec_pubkey_create(secp256k1ContextCreate, secp256k1Pubkey.ptr, ctmp)

            println("secp256k1EcPubkeyCreate = ${secp256k1EcPubkeyCreate}")
            println("secp256k1Pubkey = ${secp256k1Pubkey.data}")


            val output = nativeHeap.alloc<secp256k1_pubkey>()
            var length = nativeHeap.alloc<size_tVar>()
            length.value = 33.toULong()
            secp256k1_ec_pubkey_serialize(secp256k1ContextCreate, output.data, length.ptr , secp256k1Pubkey.ptr, SECP256K1_EC_COMPRESSED)
            println("output = ${output}")

            val dataStr = ArrayList<UByte>()
            for (i in 0..33) {
                val uByte = output.data[i]
                dataStr.add(uByte)
            }
            println(">>>> data ${dataStr}")
            libsecp256k1.secp256k1_context_destroy(secp256k1ContextCreate!!)

        }
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
        println("readBytes = ${readBytes?.map { it.toUByte() }}")

    }
}

actual object Platform {
    actual val name: String = "iOS"
}

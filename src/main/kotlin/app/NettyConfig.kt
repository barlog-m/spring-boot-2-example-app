package app

fun nettyUnsafeDisable() {
    System.setProperty("io.netty.noUnsafe", true.toString())
}

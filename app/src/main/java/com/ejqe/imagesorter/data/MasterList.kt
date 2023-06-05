package com.ejqe.imagesorter.data

import androidx.compose.ui.graphics.Color
import com.ejqe.imagesorter.R


data class Player(
    val name: String = "",
    val image: Int = R.drawable.error,
    val color: Color = Color.Unspecified,
    var score: Double = 0.0,
    var rank: Int = 0,
    var tieBreak: Double = 0.0,
    var wins: Int = 0
)

data class PlayerOp(
    val name: String = "",
    val defeated: MutableList<String> = mutableListOf(),
    val draw: MutableList<String> = mutableListOf(),
)

data class Backup(
    var player: Pair<Player,Player> = Player() to Player(),
    var playerOp: Pair<PlayerOp, PlayerOp> = PlayerOp() to PlayerOp()
)

object MasterList {
    val players = mutableListOf(
        Player("Tokino Sora", R.drawable.tokino_sora, Color(0xFF245efe)),
        Player("Robocosan", R.drawable.robocosan, Color(0xFFa36594)),
        Player("Yozora Mel", R.drawable.yozora_mel, Color(0xFFffde17)),
        Player("Aki Rosenthal", R.drawable.aki_rosenthal, Color(0xFFfe1c9a)),
        Player("Akai Haato", R.drawable.akai_haato, Color(0xFFfb123f)),
        Player("Shirakami Fubuki", R.drawable.shirakami_fubuki, Color(0xFF76dfff)),
        Player("Natsuiro Matsuri", R.drawable.natsuiro_matsuri, Color(0xFFffa228)),
        Player("Minato Aqua", R.drawable.minato_aqua, Color(0xFFffa6ea)),
        Player("Murasaki Shion", R.drawable.murasaki_shion, Color(0xFFad6ce0)),
        Player("Nakiri Ayame", R.drawable.nakiri_ayame, Color(0xFFed3a4f)),
        Player("Yuzuki Choco", R.drawable.yuzuki_choco, Color(0xFFff6d9a)),
        Player("Oozora Subaru", R.drawable.oozora_subaru, Color(0xFFe1ff2d)),
        Player("AZKi", R.drawable.azki, Color(0xFFf4348a)),
        Player("Ookami Mio", R.drawable.ookami_mio, Color(0xFFff304a)),
        Player("Sakura Miko", R.drawable.sakura_miko, Color(0xFFff9bb3)),
        Player("Nekomata Okayu", R.drawable.nekomata_okayu, Color(0xFFe27dfd)),
        Player("Inugami Korone", R.drawable.inugami_korone, Color(0xFFffcc2a)),
        Player("Hoshimachi Suisei", R.drawable.hoshimachi_suisei, Color(0xFF50e5f9)),
        Player("Usada Pekora", R.drawable.usada_pekora, Color(0xFF88d2ff)),
        Player("Shiranui Flare", R.drawable.shiranui_flare, Color(0xFFff5027)),
        Player("Shirogane Noel", R.drawable.shirogane_noel, Color(0xFFacabb1)),
        Player("Houshou Marine", R.drawable.houshou_marine, Color(0xFFca3c28)),
        Player("Amane Kanata", R.drawable.amane_kanata, Color(0xFF99d9ff)),
        Player("Tsunomaki Watame", R.drawable.tsunomaki_watame, Color(0xFFfef29e)),
        Player("Tokoyami Towa", R.drawable.tokoyami_towa, Color(0xFFcabbff)),
        Player("Himemori Luna", R.drawable.himemori_luna, Color(0xFFff85c2))
    )


val colors = mutableListOf(
    Color(0xFF245eff), Color(0xffa36694), Color(0xffffdd17), Color(0xffff1c9a), Color(0xfffc123f), Color(0xff76dfff), Color(0xffffa227), Color(0xffffa6ea), Color(0xffad6ce0), Color(0xffed3a4f), Color(0xffff6e9b), Color(0xffe0ff2c), Color(0xfff4348b), Color(0xffff314a), Color(0xffff9cb4), Color(0xffe27dfd), Color(0xffffcc29), Color(0xff50e5f9), Color(0xff88d2ff), Color(0xffff5028), Color(0xffacabb2), Color(0xffca3c28), Color(0xff99d8ff), Color(0xfffef29e), Color(0xffcabaff), Color(0xfffe84c2), Color(0xff6bcdf8), Color(0xffffb65d), Color(0xffa3e5cf), Color(0xffcf2830), Color(0xff936cc6), Color(0xff831550), Color(0xffffacd3), Color(0xffcf4c4a), Color(0xff93dcd8), Color(0xfff6bbbb), Color(0xffb19ddc), Color(0xffb3ee55), Color(0xffd60e54), Color(0xfff2c95c), Color(0xff0f52ba), Color(0xffbab9c3), Color(0xffff3d3d), Color(0xff393464), Color(0xffc90d40), Color(0xffff511c), Color(0xff62567e), Color(0xff5d81c7), Color(0xfff8db92), Color(0xffe10e5b), Color(0xff32c965), Color(0xff1c1797), Color(0xffc29371), Color(0xfffe3a2d), Color(0xfffe8b04), Color(0xffd583ab), Color(0xff2b2b6e), Color(0xffe3f1cd)
)

}


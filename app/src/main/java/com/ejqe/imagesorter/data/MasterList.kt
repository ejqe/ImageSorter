package com.ejqe.imagesorter.data

import androidx.compose.ui.graphics.Color
import com.ejqe.imagesorter.R


data class Player(
    val name: String = "",
    val image: Int = R.drawable.error,
    val color: Color = Color.Unspecified,
    var score: Double = 1000.0,
    var rank: Int = 0,
    var defeated: MutableList<String> = mutableListOf(),
    var tbScore: Double = 0.0,
)

//data class PlayerUI(
//    val name: String = "",
//    val image: Int = R.drawable.error,
//    val color: Color = Color.Unspecified,
//)
//
//data class PlayerOp(
//    val name: String = "",
//    val defeated: MutableList<String> = mutableListOf(),
//    val draw: MutableList<String> = mutableListOf(),
//)

data class Backup(
    var player: Pair<Player,Player> = Player() to Player(),
//    var playerOp: Pair<PlayerOp, PlayerOp> = PlayerOp() to PlayerOp()
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
//        Player("Yuzuki Choco", R.drawable.yuzuki_choco, Color(0xFFff6d9a)),
//        Player("Oozora Subaru", R.drawable.oozora_subaru, Color(0xFFe1ff2d)),
//        Player("AZKi", R.drawable.azki, Color(0xFFf4348a)),
//        Player("Ookami Mio", R.drawable.ookami_mio, Color(0xFFff304a)),
//        Player("Sakura Miko", R.drawable.sakura_miko, Color(0xFFff9bb3)),
//        Player("Nekomata Okayu", R.drawable.nekomata_okayu, Color(0xFFe27dfd)),
//        Player("Inugami Korone", R.drawable.inugami_korone, Color(0xFFffcc2a)),
//        Player("Hoshimachi Suisei", R.drawable.hoshimachi_suisei, Color(0xFF50e5f9)),
//        Player("Usada Pekora", R.drawable.usada_pekora, Color(0xFF88d2ff)),
//        Player("Shiranui Flare", R.drawable.shiranui_flare, Color(0xFFff5027)),
//        Player("Shirogane Noel", R.drawable.shirogane_noel, Color(0xFFacabb1)),
//        Player("Houshou Marine", R.drawable.houshou_marine, Color(0xFFca3c28)),
//        Player("Amane Kanata", R.drawable.amane_kanata, Color(0xFF99d9ff)),
//        Player("Tsunomaki Watame", R.drawable.tsunomaki_watame, Color(0xFFfef29e)),
//        Player("Tokoyami Towa", R.drawable.tokoyami_towa, Color(0xFFcabbff)),
//        Player("Himemori Luna", R.drawable.himemori_luna, Color(0xFFff85c2))
    )



}


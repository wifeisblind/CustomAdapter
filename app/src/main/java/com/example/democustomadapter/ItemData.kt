package com.example.democustomadapter

data class ItemData(
        val imgCommodityUrl: String,
        val title: String,
        val price: String,
        val isFavorite: Boolean
) {
    companion object {
        fun create(index: Int): ItemData = ItemData(
            imgCommodityUrl = imgList[index.rem(imgList.size)],
            title = "Commodity $index",
            price = "${index*100}",
            isFavorite = false
        )
    }
}

val imgList: List<String> = listOf(
    "https://img1.momoshop.com.tw/goodsimg/0006/588/107/6588107_L.jpg?t=1572527583",
    "https://img1.momoshop.com.tw/goodsimg/0008/117/318/8117318_L.jpg?t=1601055080",
    "https://img2.momoshop.com.tw/goodsimg/0007/952/197/7952197_L.jpg?t=1596655257",
    "https://img2.momoshop.com.tw/goodsimg/0008/177/540/8177540_L.jpg?t=1603106588",
    "https://img1.momoshop.com.tw/goodsimg/0007/415/631/7415631_L.jpg?t=1589921597",
    "https://img4.momoshop.com.tw/goodsimg/0008/173/038/8173038_L.jpg?t=1602860081",
    "https://img3.momoshop.com.tw/goodsimg/0007/477/591/7477591_L.jpg?t=1583168477",
    "https://img1.momoshop.com.tw/goodsimg/0006/839/086/6839086_L.jpg?t=1603141905",
)
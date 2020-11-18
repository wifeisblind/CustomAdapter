package com.example.democustomadapter

data class HorizontalData(
        val id: Int,
        val imgUrl: String,
        val title: String,
) {
    companion object {
        fun create(index: Int): HorizontalData = HorizontalData(
                id = index,
                imgUrl = horizontalImg[index.rem(horizontalImg.size)],
                title = "Title $index"
        )
    }
}

val horizontalImg: List<String> = listOf(
        "https://img1.momoshop.com.tw/goodsimg/0006/588/107/6588107_L.jpg?t=1572527583",
        "https://img1.momoshop.com.tw/goodsimg/0008/117/318/8117318_L.jpg?t=1601055080",
        "https://img2.momoshop.com.tw/goodsimg/0007/952/197/7952197_L.jpg?t=1596655257",
        "https://img2.momoshop.com.tw/goodsimg/0008/177/540/8177540_L.jpg?t=1603106588",
        "https://img1.momoshop.com.tw/goodsimg/0007/415/631/7415631_L.jpg?t=1589921597",
        "https://img4.momoshop.com.tw/goodsimg/0008/173/038/8173038_L.jpg?t=1602860081",
        "https://img3.momoshop.com.tw/goodsimg/0007/477/591/7477591_L.jpg?t=1583168477",
        "https://img1.momoshop.com.tw/goodsimg/0006/839/086/6839086_L.jpg?t=1603141905",
)
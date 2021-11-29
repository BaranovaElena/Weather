package com.example.weather.domain.repo.city.lists

import com.example.weather.domain.model.City

class CityListsRepoRus: CityListsRepo {
    private val worldCities = listOf(
        City("Лондон", 51.5085300, -0.1257400, "https://all.accor.com/magazine/imagerie/1-f546.jpg"),
        City("Токио", 35.6895000, 139.6917100, "https://allintravels.nl/wp-content/uploads/2018/09/destination-tokyo-02.jpg"),
        City("Париж", 48.8534100, 2.3488000, "https://topgid.net/uploads/posts/2019-09/1568213750_parizh-francija.jpg"),
        City("Берлин", 52.52000659999999, 13.404953999999975, "https://kidpassage.com/images/publications/berlin-avguste-otdyh-pogoda/cover_original.jpg"),
        City("Рим", 41.9027835, 12.496365500000024, "https://5slov.ru/images/foto/100/76-rim.jpg"),
        City("Минск", 53.90453979999999, 27.561524400000053, "https://static.homesoverseas.ru/pic/complex/3/7/6/5/37655.jpg"),
        City("Стамбул", 41.0082376, 28.97835889999999 ,"https://www.sibdom.ru/images/photo_crop_1050_700/gallery/2a/2a70/2a705fe85d926fbabbde94382987d11b.jpg"),
        City("Вашингтон", 38.9071923, -77.03687070000001, "https://tripplanet.ru/wp-content/uploads/america/usa/washington/dostoprimechatelnosti-vashingtona.jpg"),
        City("Киев", 50.4501, 30.523400000000038, "https://cdni.rt.com/russian/images/2021.01/article/600d412fae5ac904513a05c0.jpg"),
        City("Пекин", 39.90419989999999, 116.40739630000007, "https://www.okaytravel.ru/wp-content/uploads/2019/12/pekin.jpg")
    )
    private val rusCities = listOf(
        City("Москва", 55.755826, 37.617299900000035, "https://cs13.pikabu.ru/avatars/3228/x3228885-1825799536.png"),
        City("Санкт-Петербург", 59.9342802, 30.335098600000038, "https://s.zagranitsa.com/images/articles/1889/870x486/697f70da9c1d5b30acfbbba10345c75f.jpg?1445850065"),
        City("Новосибирск", 55.00835259999999, 82.93573270000002, "https://sib.fm/storage/article/November2019/z5fxvsaIxesnvhtfZvjg.jpg"),
        City("Екатеринбург", 56.83892609999999, 60.60570250000001, "https://beinrussia.ru/content/images/pages/787/787.jpg"),
        City("Нижний Новгород", 56.2965039, 43.936059, "https://pp.userapi.com/c846320/v846320914/192021/hqDByuy-Yys.jpg"),
        City("Казань", 55.8304307, 49.06608060000008, "https://realnoevremya.ru/uploads/news/10/1a/3db22c408b1df69c.jpg"),
        City("Челябинск", 55.1644419, 61.4368432, "https://cdn-p.cian.site/images/3/572/208/802275310-7.jpg"),
        City("Омск", 54.9884804, 73.32423610000001, "https://cs4.pikabu.ru/post_img/big/2016/07/06/3/1467777313163078032.jpg"),
        City("Ростов-на-Дону", 47.2357137, 39.701505, "https://upload.wikimedia.org/wikipedia/commons/6/6f/Rostov_City_Hall_2021.jpg"),
        City("Уфа", 54.7387621, 55.972055400000045, "https://ufaved.info/upload/iblock/7ee/7eeeb8505ef6089f4b05e2570949fe11.jpeg")
    )

    override fun getLocalCitiesList() = rusCities
    override fun getWorldCitiesList() = worldCities
}
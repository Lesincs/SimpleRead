package com.lesincs.simpleread.util

import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by Administrator on 2017/12/15.
 */
class CalenderUtil {

    companion object {
        val nowCalendar = Calendar.getInstance()

        fun getZHDatePickerMinTime(): Long {
            val calendar = Calendar.getInstance()
            calendar.set(2013, 4, 19)
            return calendar.timeInMillis
        }

        fun getZHDatePickerMaxTime(): Long {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_MONTH, -1)
            return calendar.timeInMillis

        }


        fun zhDateToFriendlyDate(sDate: String): String {
            val sdf = SimpleDateFormat("yyyyMMdd", Locale("chinese"))
            val date = sdf.parse(sDate)
            val calender = Calendar.getInstance()
            calender.time = date
            val year = calender.get(Calendar.YEAR)
            val month = calender.get(Calendar.MONTH) + 1
            val day = calender.get(Calendar.DATE)
            val week = when (calender.get(Calendar.DAY_OF_WEEK)) {
                1 -> "星期日"
                2 -> "星期一"
                3 -> "星期二"
                4 -> "星期三"
                5 -> "星期四"
                6 -> "星期五"
                7 -> "星期六"
                else -> "世界末日"
            }
            return if (year == nowCalendar.get(Calendar.YEAR))
                "${month}月${day}日 $week"
            else
                "${month}月${day}日 ${year}年"
        }

        fun zhDateToFriendlyDate2(sDate: String): String {
            val sdf = SimpleDateFormat("yyyyMMdd", Locale("chinese"))
            val date = sdf.parse(sDate)
            val calender = Calendar.getInstance()
            calender.time = date
            val year = calender.get(Calendar.YEAR)
            val month = calender.get(Calendar.MONTH) + 1
            val day = calender.get(Calendar.DATE)
            val week = when (calender.get(Calendar.DAY_OF_WEEK)) {
                1 -> "星期日"
                2 -> "星期一"
                3 -> "星期二"
                4 -> "星期三"
                5 -> "星期四"
                6 -> "星期五"
                7 -> "星期六"
                else -> "世界末日"
            }
            return if (year == nowCalendar.get(Calendar.YEAR))
                "${month}月${day}日 $week"
            else
                "${year}年${month}月${day}日"
        }

        fun getARandomZHDate(): String {
            val MIN_YEAR = 2013
            val MIN_MONTH = 5
            val MIN_DATE = 20
            val minCalendar = Calendar.getInstance()
            minCalendar.set(MIN_YEAR, MIN_MONTH, MIN_DATE)
            val minTime = minCalendar.timeInMillis
            val nowTime = Calendar.getInstance().timeInMillis
            val randomTime: Long = (minTime + Math.random() * (nowTime - minTime)).toLong()
            val date = Date()
            date.time = randomTime
            val calenderRandom = Calendar.getInstance()
            calenderRandom.time = date
            val randomYear = calenderRandom.get(Calendar.YEAR)
            val randomMonth = calenderRandom.get(Calendar.MONTH) + 1
            val randomDate = calenderRandom.get(Calendar.DATE)
            val sb = StringBuilder()
            sb.append(randomYear)
                    .append(if (randomMonth < 10)
                        "0$randomMonth" else {
                        randomMonth
                    })
                    .append(
                            if (randomDate < 10)
                                "0$randomDate"
                            else randomDate
                    )
            return sb.toString()
        }


        fun getTheDayAfter(year_: Int, month_: Int, date_: Int): String {
            val calender = Calendar.getInstance()
            calender.set(year_, month_, date_)
            calender.add(Calendar.DATE, 1)
            val year = calender.get(Calendar.YEAR)
            val month = calender.get(Calendar.MONTH) + 1
            val date = calender.get(Calendar.DATE)
            val sb = StringBuilder()
            sb.append(year)
                    .append(if (month < 10)
                        "0$month" else {
                        month
                    })
                    .append(
                            if (date < 10)
                                "0$date"
                            else date
                    )
            return sb.toString()
        }

        fun getCollectionFriendlyDate(date: Long): String {
            val calendar = Calendar.getInstance()
            calendar.time = Date(date)
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH) + 1
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val sb = StringBuilder()
            sb.append(year)
                    .append(if (month < 10)
                        "0$month" else {
                        month
                    })
                    .append(
                            if (day < 10)
                                "0$day"
                            else day
                    )
            return zhDateToFriendlyDate(sb.toString())

        }

        fun jdDateToComparableString(sDate: String): String {
            val smf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale("Chinese"))
            val dDate = smf.parse(sDate)
            val c = Calendar.getInstance()
            c.time = dDate
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH) + 1
            val day = c.get(Calendar.DAY_OF_MONTH)
            val sb = StringBuilder()
            sb.append(year)
                    .append(if (month < 10)
                        "0$month" else {
                        month
                    })
                    .append(
                            if (day < 10)
                                "0$day"
                            else day
                    )
            return sb.toString()
        }

        fun jdDateToFriendlyDate(sDate: String): String {
            return zhDateToFriendlyDate(jdDateToComparableString(sDate))
        }

        fun jdDateToFriendlyDate2(sDate: String): String {
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale("Chinese"))
            val timeString: String
            val parse = simpleDateFormat.parse(sDate)
            val distanceTime = Date().time - parse.time
            timeString = if (distanceTime < 0L) {
                "0分钟前"
            } else {
                val n2 = distanceTime / 60000L
                val simpleDateFormat2 = SimpleDateFormat("MM-dd", Locale("Chinese"))
                if (n2 < 60L) {
                    n2.toString() + "分钟前"
                } else if (n2 < 720L) {
                    (n2 / 60L).toString() + "小时前"
                } else {
                    simpleDateFormat2.format(parse)
                }
            }
            return timeString
        }
    }

}
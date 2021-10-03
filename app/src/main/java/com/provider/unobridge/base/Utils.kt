package com.provider.unobridge.base

import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.text.InputFilter
import android.text.Spanned
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.google.android.material.textfield.TextInputLayout
import com.provider.unobridge.R
import de.hdodenhof.circleimageview.CircleImageView
import jp.wasabeef.glide.transformations.BlurTransformation
import org.joda.time.*
import java.io.InputStream
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class Utils private constructor() {

    private var datePickerDialog: DatePickerDialog? = null

    private object HOLDER {
        val INSTANCE = Utils()
    }

    companion object {
        val init: Utils by lazy { HOLDER.INSTANCE }

        @JvmStatic
        @BindingAdapter("bind:imageUrl")
        fun setImage(image: ImageView, url: String?) {
            if (!url.isNullOrEmpty()) {
                Glide.with(image.context)
                    .apply {
                        this.setDefaultRequestOptions(postRequestOptions(url))
                    }
                    .load(url)
                    .thumbnail(0.5f)
//                    .centerCrop()
                    .into(image)
            }
        }

        @JvmStatic
        @BindingAdapter("bind:circleImageUrl")
        fun setCircleImage(image: CircleImageView, url: String?) {
            if (!url.isNullOrEmpty()) {
                Glide.with(image.context)
                    .apply {
                        this.setDefaultRequestOptions(postRequestOptions(url))
                    }
                    .load(url)
                    .thumbnail(0.5f)
//                    .centerCrop()
                    .into(image)
            }
        }

        fun postRequestOptions(imageUrl: String): RequestOptions {
            return RequestOptions().also {
                it.placeholder(R.drawable.placeholder_white)
                it.error(R.drawable.placeholder_white)
                it.diskCacheStrategy(DiskCacheStrategy.ALL)
                it.signature(ObjectKey(imageUrl))
                it.format(DecodeFormat.PREFER_RGB_565)
            }
        }

        @JvmStatic
        @BindingAdapter("bind:blurimageUrl")
        fun setBlurImage(image: ImageView, url: String?) {
            if (!url.isNullOrEmpty()) {
                Glide.with(image.context)
                    .apply {
                        this.setDefaultRequestOptions(postRequestOptions(url))
                    }.load(url)
                    .centerCrop()
                    .thumbnail(0.5f)
                    .apply(
                        RequestOptions.bitmapTransform(
                            BlurTransformation(
                                10, 3
                            )
                        )
                    )
                    .into(image)
            }
        }
    }


    fun centimeterToFeet(centemeter: String?): String? {
        try {
            var feetPart = 0
            var inchesPart = 0
            if (!TextUtils.isEmpty(centemeter)) {
                val dCentimeter = java.lang.Double.valueOf(centemeter.toString())
                feetPart = Math.floor(dCentimeter / 2.54 / 12).toInt()
                println(dCentimeter / 2.54 - feetPart * 12)
                inchesPart = Math.round(dCentimeter / 2.54 - feetPart * 12).toInt()
            }
            return String.format("%d' %d''", feetPart, inchesPart)
        } catch (e: java.lang.NumberFormatException) {
            return ""
        } catch (e: Exception) {
            return ""
        }

    }

    fun centimeterToFeetInches(centemeter: String?): String? {
        var feetPart = 0
        var inchesPart = 0
        if (!TextUtils.isEmpty(centemeter)) {
            val dCentimeter = java.lang.Double.valueOf(centemeter.toString())
            feetPart = Math.floor(dCentimeter / 2.54 / 12).toInt()
            println(dCentimeter / 2.54 - feetPart * 12)
            inchesPart = Math.round(dCentimeter / 2.54 - feetPart * 12).toInt()
        }
        return "${feetPart}.${inchesPart}"
    }


    fun convertFeetandInchesToCentimeter(feet: String?, inches: String?): Int {
        var heightInFeet = 0.0
        var heightInInches = 0.0
        try {
            if (feet != null && feet.trim { it <= ' ' }.length != 0) {
                heightInFeet = feet.toDouble()
            }
            if (inches != null && inches.trim { it <= ' ' }.length != 0) {
                heightInInches = inches.toDouble()
            }
        } catch (nfe: NumberFormatException) {
        }
        return Math.round(heightInFeet * 30.48 + heightInInches * 2.54).toInt()
    }

    fun checkIfHasNetwork(): Boolean {
        val connectivityManager =
            MainApplication.get()
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }



    fun setFullWidth(inputLayout: TextInputLayout) {
        val errorTextView = inputLayout.findViewById<TextView>(R.id.textinput_error)
        var params = errorTextView?.layoutParams
        params?.width = 20
        params?.height = 20
        errorTextView?.layoutParams = params
    }

    fun getselectAge(day: Int, month: Int, year: Int, dobString: String): String {

        if (day == 0 || day > 31) {
            return "0 years"
        } else if (month == 0 || month > 12) {
            return "0 years"
        } else if (year == 0) {
            return "0 years"
        }

        Log.e("DOBBB", dobString)
        try {
            var date: Date? = null
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            try {

                date = sdf.parse(dobString)
            } catch (e: ParseException) {
                e.printStackTrace()
                return "0 years"
            }
            if (date == null) return "0 years"
            val dob = Calendar.getInstance()
            val today = Calendar.getInstance()



            Log.e("Month", date.month.toString())
            Log.e("Date", date.date.toString())
            dob.time = date


            val days = Days.daysBetween(LocalDate(date), LocalDate(today)).getDays()
            val months = Months.monthsBetween(LocalDate(date), LocalDate(today)).months
            val years = Years.yearsBetween(LocalDate(date), LocalDate(today)).years

            if (years > 0) {
                if (years == 1) {
                    return "1 year"
                } else {
                    return "${years} years"
                }
            } else if (months > 0) {
                if (months == 1) {
                    return "1 month"
                } else {
                    return "${months} months"
                }

            } else {
                if (days < 0) {
                    return "0 years"
                }
                if (days == 1) {
                    return "1 day"
                } else if (days == 0) {
                    return "0 day"
                } else {

                    return "${days} days"
                }

            }
        } catch (e: Exception) {
            return "0 years"
        }
    }

    fun checkDobAge(day: Int, month: Int, year: Int, dobString: String): Boolean {
        if (day == 0 || day > 31) {
            return false
        } else if (month == 0 || month > 12) {
            return false
        } else if (year == 0) {
            return false
        }
        try {
            var date: Date? = null
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            try {
                date = sdf.parse(dobString)
            } catch (e: ParseException) {
                return false
                e.printStackTrace()
            }
            if (date == null) return false
            val dob = Calendar.getInstance()
            val today = Calendar.getInstance()
            dob.time = date

            val days = Days.daysBetween(LocalDate(date), LocalDate(today)).getDays()
            val months = Months.monthsBetween(LocalDate(date), LocalDate(today)).months
            val years = Years.yearsBetween(LocalDate(date), LocalDate(today)).years

            if (years < 18 || years > 100) {
                return false
            }
        } catch (e: Exception) {
            return false
        }
        return true
    }

    fun getAge(dobString: String): Int {
        try {
            var date: Date? = null
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            val utcZone = TimeZone.getTimeZone("UTC")
            sdf.setTimeZone(utcZone)
            try {
                date = sdf.parse(dobString)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            if (date == null) return 0
            val dob = Calendar.getInstance()
            val today = Calendar.getInstance()
            dob.time = date
            val year = dob[Calendar.YEAR]
            val month = dob[Calendar.MONTH]
            val day = dob[Calendar.DAY_OF_MONTH]
            return getUserAge(year, month, day)
        } catch (e: Exception) {
            return 0
        }
    }

    private fun getUserAge(year: Int, month: Int, day: Int): Int {
        val dob = Calendar.getInstance()
        val today = Calendar.getInstance()
        dob[year, month] = day
        var age = today[Calendar.YEAR] - dob[Calendar.YEAR]
        if (today[Calendar.DAY_OF_YEAR] < dob[Calendar.DAY_OF_YEAR]) {
            age--
        }
        val ageInt = age

        return if (ageInt == -1) return 0 else ageInt
    }

    fun setupFullScreen(activity: Activity) {
        activity.window.apply {
            decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            statusBarColor = Color.TRANSPARENT
        }
    }

    fun hideKeyBoard(context: Context, view: View) {
        val inputManager: InputMethodManager? =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        inputManager?.hideSoftInputFromWindow(view.applicationWindowToken, 0)
    }

    fun readJSONFromAsset(context: Context, path: String): String? {
        val json: String
        try {
            val inputStream: InputStream = context.assets.open(path)
            json = inputStream.bufferedReader().use { it.readText() }
        } catch (ex: Exception) {
            ex.printStackTrace()
            return null
        }
        return json
    }


    fun getFilter(limit: Int, spaceEnable: Boolean): Array<InputFilter> {
        val inputFilter = InputFilter(fun(
            source: CharSequence,
            start: Int,
            end: Int,
            _: Spanned,
            _: Int,
            _: Int
        ): String? {
            for (index in start until end) {
                val type = Character.getType(source[index])
                if (spaceEnable) {
                    if (type == Character.SURROGATE.toInt() || type == Character.NON_SPACING_MARK.toInt()) {
                        return ""
                    }
                } else {
                    if (type == Character.SURROGATE.toInt() || type == Character.NON_SPACING_MARK.toInt() || type == Character.OTHER_SYMBOL.toInt() || Character.isWhitespace(
                            source[index]
                        )
                    ) {
                        return ""
                    }
                }
            }
            return null
        })
        return arrayOf(inputFilter, InputFilter.LengthFilter(limit))
    }

    fun selectDate(
        context: Context?,
        startFrom: String,
        tvDate: TextView,
        disableFutureDate: Boolean
    ) {
        datePickerDialog?.let {
            if (it.isShowing) {
                return
            }
        }
        val calendar = Calendar.getInstance()
        val dateFormats = listOf("yyyy-MM-dd", "yyyy/MM/dd", "dd/MM/yyyy")
        var date: Date? = null
        var startDate: Date? = null
        for (dateFormat in dateFormats) {
            try {
                startDate = SimpleDateFormat(dateFormat).parse(startFrom)
                date = SimpleDateFormat(dateFormat).parse(startFrom)
            } catch (e: java.lang.Exception) {
                Log.d("Exception", "getFormattedDate: unable to parse date.")
            }
        }
        if (date != null) {
            calendar.time = date
        }
        val mYear = calendar[Calendar.YEAR]
        val mMonth = calendar[Calendar.MONTH]
        val mDay = calendar[Calendar.DAY_OF_MONTH]
        datePickerDialog = DatePickerDialog(
            context!!,
           R.style.Theme_AppCompat_Light_Dialog,
            OnDateSetListener { _: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->

                Log.e("year", year.toString())
                Log.e("month", month.toString())
                Log.e("day", dayOfMonth.toString())

                var calendar = Calendar.getInstance();
                calendar.set(Calendar.MONTH, (month));

                var month_date = SimpleDateFormat("MMMM");

                var day =
                    if (dayOfMonth.toString().length < 2) "0${dayOfMonth.toString()}" else dayOfMonth.toString()

                var monthh =
                    if (month + 1 < 2) "0${(month + 1).toString()}" else (month + 1).toString()

                tvDate.setText("${year}-${monthh}-${day}")


            },
            mYear,
            mMonth,
            mDay
        )
        if (disableFutureDate) {
            val dob = Calendar.getInstance()
            datePickerDialog?.datePicker?.maxDate = dob.time.time
        } else {
            startDate?.let {
                datePickerDialog?.datePicker?.minDate = it.time
            }
        }
        datePickerDialog?.show()
    }

    fun selectHangoutDate(
        context: Context?,
        startFrom: String,
        tvDate: TextView,
        tvTime: TextView,
        disableFutureDate: Boolean
    ) {
        datePickerDialog?.let {
            if (it.isShowing) {
                return
            }
        }
        val calendar = Calendar.getInstance()
        val dateFormats = listOf("yyyy-MM-dd", "yyyy/MM/dd", "dd/MM/yyyy", "d MMMM, yyyy")
        var date: Date? = null
        var startDate: Date? = null
        for (dateFormat in dateFormats) {
            try {
                startDate = SimpleDateFormat(dateFormat).parse(startFrom)
                date = SimpleDateFormat(dateFormat).parse(startFrom)
            } catch (e: java.lang.Exception) {
                startDate = calendar.time
                Log.d("Exception", "getFormattedDate: unable to parse date.")
            }
        }
        if (date != null) {
            calendar.time = date
        }
        val mYear = calendar[Calendar.YEAR]
        val mMonth = calendar[Calendar.MONTH]
        val mDay = calendar[Calendar.DAY_OF_MONTH]
        datePickerDialog = DatePickerDialog(
            context!!,
//            R.style.MyTimePickerDialogTheme,
            OnDateSetListener { _: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                Log.e("year", year.toString())
                Log.e("month", month.toString())
                Log.e("day", dayOfMonth.toString())

                var calendar = Calendar.getInstance();
                calendar.set(Calendar.MONTH, (month));

                var month_date = SimpleDateFormat("MMMM");

                var day =
                    if (dayOfMonth.toString().length < 2) "${dayOfMonth.toString()}" else dayOfMonth.toString()

                tvDate.setText("${day} ${month_date.format(calendar.time)}, ${year}")
                tvTime.setText("")

            },
            mYear,
            mMonth,
            mDay
        )
        if (disableFutureDate) {
            val dob = Calendar.getInstance()
            datePickerDialog?.datePicker?.maxDate = dob.time.time
        } else {
            startDate?.let {
                datePickerDialog?.datePicker?.minDate = it.time
            }
        }

        datePickerDialog?.datePicker?.minDate = Calendar.getInstance().timeInMillis
        datePickerDialog?.show()
    }


    fun selectDateWithAge(
        context: Context?,
        startFrom: String,
        tvDate: TextView,
        totalAge: TextView,
        disableFutureDate: Boolean
    ) {
        datePickerDialog?.let {
            if (it.isShowing) {
                return
            }
        }
        val calendar = Calendar.getInstance()
        val dateFormats = listOf("yyyy-MM-dd", "yyyy/MM/dd", "dd/MM/yyyy")
        var date: Date? = null
        var startDate: Date? = null
        for (dateFormat in dateFormats) {
            try {
                startDate = SimpleDateFormat(dateFormat).parse(startFrom)
                date = SimpleDateFormat(dateFormat).parse(startFrom)
            } catch (e: java.lang.Exception) {
                Log.d("Exception", "getFormattedDate: unable to parse date.")
            }
        }
        if (date != null) {
            calendar.time = date
        }
        val mYear = calendar[Calendar.YEAR]
        val mMonth = calendar[Calendar.MONTH]
        val mDay = calendar[Calendar.DAY_OF_MONTH]
        datePickerDialog = DatePickerDialog(
            context!!,
//            R.style.MyTimePickerDialogTheme,
            OnDateSetListener { _: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->

                Log.e("year", year.toString())
                Log.e("month", month.toString())
                Log.e("day", dayOfMonth.toString())

                var calendar = Calendar.getInstance();
                calendar.set(Calendar.MONTH, (month));

                var month_date = SimpleDateFormat("MMMM");

                var day =
                    if (dayOfMonth.toString().length < 2) "0${dayOfMonth.toString()}" else dayOfMonth.toString()

                var monthh =
                    if (month + 1 < 10) "0${(month + 1).toString()}" else (month + 1).toString()

                tvDate.setText("${day}/${monthh}/${year}")
                totalAge.setText(
                    "You are ${
                        getselectAge(
                            day.toInt(),
                            month.toInt(),
                            year.toInt(),
                            "${day}/${monthh}/${year}"
                        )
                    } old."
                )


            },
            mYear,
            mMonth,
            mDay
        )
        if (disableFutureDate) {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.YEAR, -18);
            var dob = calendar

            datePickerDialog?.datePicker?.maxDate = dob.time.time
        }
        val cal = Calendar.getInstance()
        cal.add(Calendar.YEAR, -100);

        datePickerDialog?.datePicker?.minDate = cal.time.time
        datePickerDialog?.show()
    }


    fun showTimePickerDialog(
        context: Context?,
        startFrom: String,
        editText: TextView,
        selectedDate: String
    ) {
        var mcurrentTime = Calendar.getInstance();
        val dateFormats = listOf("yyyy-MM-dd", "yyyy/MM/dd", "dd/MM/yyyy", "d MMMM, yyyy")


        try {
            var time = SimpleDateFormat("hh:mm aa").parse(startFrom)
            mcurrentTime.time = time

        } catch (e: java.lang.Exception) {
            e.printStackTrace()

        }


        var hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        var minute = mcurrentTime.get(Calendar.MINUTE);
        var mTimePicker = TimePickerDialog(
            context,
            object : TimePickerDialog.OnTimeSetListener {
                override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
                    var datetime = Calendar.getInstance();
                    var c = Calendar.getInstance();
                    datetime.set(Calendar.HOUR_OF_DAY, p1);
                    datetime.set(Calendar.MINUTE, p2);

                    if (selectedDate.isNotEmpty()) {

                        var date = SimpleDateFormat("d MMMM, yyyy").parse(selectedDate)

                        val monthNumber = SimpleDateFormat("MM").format(date)
                        val day = SimpleDateFormat("dd").format(date)

                        Log.e("monthNumberr", monthNumber)
                        Log.e("Dayaaa", day)
                        datetime.set(Calendar.MONTH, monthNumber.toInt() - 1);
                        datetime.set(Calendar.DAY_OF_MONTH, day.toInt());
                    }


                    var tTime = SimpleDateFormat("hh:mm aa").format(datetime.time);

                    if (datetime.timeInMillis < c.timeInMillis - 1000) {
                        Toast.makeText(
                            context,
                            "Please select the time which is equal or greater than the current time.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        editText.setText(tTime);
                    }

                }


            },
            hour,
            minute,
            false
        );//Yes 24 hour time
        mTimePicker.show();

    }


    fun getFormattedDate(time: String): String {
        val dateFormats = listOf("yyyy-MM-dd", "yyyy/MM/dd", "dd/MM/yyyy")

        var currentTime = ""
        var date: Date? = null
        for (dateFormat in dateFormats) {
            try {
                date = SimpleDateFormat(dateFormat).parse(time)
            } catch (e: java.lang.Exception) {
                Log.d("Exception", "getFormattedDate: unable to parse date.")
            }
        }
        if (date != null) {
            currentTime = SimpleDateFormat("yyyy-MM-dd").format(date)
        }
        return currentTime
    }

    fun getServerFormattedDate(time: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")

        var currentTime = ""
        var date: Date? = null
        try {
            date = dateFormat.parse(time)
        } catch (e: java.lang.Exception) {
            Log.d("Exception", "getFormattedDate: unable to parse date.")
        }

        if (date != null) {
            currentTime = SimpleDateFormat("yyyy-MM-dd hh:mm a").format(date)
        }
        return currentTime
    }

    fun getServerFormattedDateHangout(time: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")

        var currentTime = ""
        var date: Date? = null
        try {
            date = dateFormat.parse(time)
        } catch (e: java.lang.Exception) {
            Log.d("Exception", "getFormattedDate: unable to parse date.")
        }

        if (date != null) {
            currentTime = SimpleDateFormat("EEEE d MMMM yyyy").format(date)
        }
        return currentTime
    }

    fun getServerFormattedDateCreateHangout(time: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")

        var currentTime = ""
        var date: Date? = null
        try {
            date = dateFormat.parse(time)
        } catch (e: java.lang.Exception) {
            Log.d("Exception", "getFormattedDate: unable to parse date.")
        }

        if (date != null) {
            currentTime = SimpleDateFormat("d MMMM, yyyy").format(date)
        }
        return currentTime
    }

    fun getServerFormattedDateHangoutTime(time: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")

        var currentTime = ""
        var date: Date? = null
        try {
            date = dateFormat.parse(time)
        } catch (e: java.lang.Exception) {
            Log.d("Exception", "getFormattedDate: unable to parse date.")
        }

        if (date != null) {
            currentTime = SimpleDateFormat("hh:mm aa").format(date)
        }
        return currentTime
    }


    fun getHangoutFormattedDate(time: String): String {
        var currentTime = ""
        var date: Date? = null
        try {
            date = SimpleDateFormat("dd MMM, yyyy").parse(time)
        } catch (e: java.lang.Exception) {
            Log.d("Exception", "getFormattedDate: unable to parse date.")
        }

        if (date != null) {
            currentTime = SimpleDateFormat("yyyy-MM-dd").format(date)
        }
        return currentTime!!
    }

    fun getCurrentDate(): String {
        val df = SimpleDateFormat("yyyy-MM-dd")
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = Calendar.getInstance().timeInMillis
        return df.format(calendar.time)
    }

    fun getTime(date: Date): String {
        val df = SimpleDateFormat("yyyy-MM-dd")
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = date.time
        return df.format(calendar.time)
    }


    fun getDate(time: Long): String {
        val df = SimpleDateFormat("yyyy-MM-dd")
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = time
        return df.format(calendar.time)
    }

    fun getYear(time: Date): String {
        var yearFormat = SimpleDateFormat("yyyy");
        return yearFormat.format(time)
    }

    fun getMonth(time: Date): String {
        var monthFormat = SimpleDateFormat("MMMM");
        return monthFormat.format(time)
    }


    fun getDayName(time: Date): String {
        var monthFormat = SimpleDateFormat("EEEE");
        return monthFormat.format(time)
    }

    fun getDay(time: Date): String {
        var monthFormat = SimpleDateFormat("dd");
        return monthFormat.format(time)
    }


    fun getDate(dateString: String): Date {
        val dateFormats = listOf("yyyy-MM-dd", "yyyy/MM/dd", "dd/MM/yyyy")
        val calendar = Calendar.getInstance()
        var date: Date? = null
        for (dateFormat in dateFormats) {
            try {
                date = SimpleDateFormat(dateFormat).parse(dateString)
            } catch (e: java.lang.Exception) {
                Log.d("Exception", "getFormattedDate: unable to parse date.")
            }
        }
        date?.let {
            calendar.time = date
        }
        return calendar.time
    }

    fun getTaskTypeDate(dateString: String): String {
        val dateFormats = listOf("yyyy-MM-dd", "yyyy/MM/dd", "dd/MM/yyyy")
        var currentTime = ""
        var date: Date? = null
        for (dateFormat in dateFormats) {
            try {
                date = SimpleDateFormat(dateFormat).parse(dateString)
            } catch (e: java.lang.Exception) {
                Log.d("Exception", "getFormattedDate: unable to parse date.")
            }
        }
        if (date != null) {

            val df = SimpleDateFormat("yyyy-MM-dd").format(date)
            Log.e("CurrenTimeee", df)

            var monthName = SimpleDateFormat("MMM").format(date)
            var dayName = SimpleDateFormat("EEEE").format(date)
            var day = SimpleDateFormat("dd").format(date)


            currentTime = "${dayName} ${monthName} ${day}"
        }
        return currentTime
    }

    fun get24HourTime(time: String): String {

        var tTime = time
        var orignalformat = SimpleDateFormat("hh:mm aa");
        var date: Date? = null
        try {
            date = orignalformat.parse(tTime);
            tTime = SimpleDateFormat("HH:mm").format(date);
        } catch (e: ParseException) {
            e.printStackTrace();
        }
        return tTime
    }


    fun get12HourTime(time: String): String {
        var tTime = time
        var orignalformat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss"/*,Locale.getDefault()*/);
//        orignalformat.timeZone= TimeZone.getTimeZone("UTC")
        var date: Date? = null
        try {
            date = orignalformat.parse(tTime);
            tTime = SimpleDateFormat("hh:mm aa").format(date);
        } catch (e: ParseException) {
            e.printStackTrace();
        }
        return tTime;
    }

    fun get12HourDateTime(time: String): String {
        var tTime = time
        var orignalformat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        orignalformat.timeZone = TimeZone.getTimeZone("UTC")
        var date: Date? = null
        try {
            date = orignalformat.parse(tTime);
            tTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        } catch (e: ParseException) {
            e.printStackTrace();
        }
        return tTime;
    }

    fun printDifference(startDate: Date, activeTime: String): String {

        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        try {
            val date1: Date =
                dateFormat.parse(activeTime.toString())
            val dtOrg = DateTime(date1)
            val endDate = dtOrg.plusDays(3).toDate()

            //milliseconds
            var different = endDate.time - startDate.time
            println("startDate : $startDate")
            println("endDate : $endDate")
            println("different : $different")
            val secondsInMilli: Long = 1000
            val minutesInMilli = secondsInMilli * 60
            val hoursInMilli = minutesInMilli * 60
            val daysInMilli = hoursInMilli * 24
            val elapsedDays = different / daysInMilli
            different = different % daysInMilli
            val elapsedHours = different / hoursInMilli
            different = different % hoursInMilli
            val elapsedMinutes = different / minutesInMilli
            different = different % minutesInMilli
            val elapsedSeconds = different / secondsInMilli
            var timeLeft = ""
            if (elapsedDays > 0) {
                if (elapsedDays == 1L) {
                    timeLeft = "1 day, "
                } else {
                    timeLeft = "${elapsedDays} days, "
                }
            }

            if (elapsedHours > 0) {
                if (elapsedHours == 1L) {
                    timeLeft += "1 hour, "
                } else {
                    timeLeft += "${elapsedHours} hours, "

                }
            }

            if (elapsedMinutes > 0) {
                if (elapsedMinutes == 1L) {
                    timeLeft += "1 minute"
                } else {
                    timeLeft += "${elapsedMinutes} minutes"

                }
            }

            System.out.printf(
                "%d days, %d hours, %d minutes",
                elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds
            )
            var time = if (timeLeft.toString().trim().endsWith(",")) timeLeft.toString()
                .removeSuffix(",") else timeLeft
            return time.toString().trim()

        } catch (e: Exception) {
            return ""
            e.printStackTrace()

        }


    }

    fun printTimeDifferenceInSingleChat(startDate: Date, activeTime: String): Long {

        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        try {
            val date1: Date =
                dateFormat.parse(activeTime.toString())
            val dtOrg: DateTime = DateTime(date1)
            val endDate = dtOrg.plusDays(3).toDate()
            //milliseconds
            var different = endDate.time - startDate.time
            println("startDate : $startDate")
            println("endDate : $endDate")
            println("different : $different")
            val hours = (different / (1000 * 60 * 60))
            val mins = (different / (1000 * 60) % 60)

            var timeLeft =
                "${if (hours < 10) "0${hours}" else hours}:${if (mins < 10) "0${mins}" else mins}:00"

            return different
        } catch (e: Exception) {
            e.printStackTrace()

            return 0L

        }
        return 0L

    }

    fun printHoursDifferenceInSingleChat(startDate: Date, activeTime: String): Boolean {

        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        try {
            val date1: Date =
                dateFormat.parse(activeTime.toString())
            val dtOrg: DateTime = DateTime(date1)
            val endDate = dtOrg.plusDays(3).toDate()
            //milliseconds
            var different = endDate.time - startDate.time
            println("startDate : $startDate")
            println("endDate : $endDate")
            println("different : $different")
            val hours = (different / (1000 * 60 * 60))
            val elapsedMinutes: Long = TimeUnit.MILLISECONDS.toMinutes(different)
            Log.e("ElapsedMinutes", elapsedMinutes.toString())
            return elapsedMinutes <= 1440
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
        return false

    }


    fun getPercentageTimeDifference(startDate: Date, endTime: String): Int {
        val addedTime = 15
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")

        try {
            val date1: Date =
                dateFormat.parse(endTime)
            val dtOrg = DateTime(date1)
            val endDate = dtOrg.plusDays(3).toDate()
            //milliseconds
            var different = endDate.time - startDate.time
            println("startDate : $startDate")
            println("endDate : $endDate")
            println("different : $different")

            val elapsedSeconds = different / 1000

            val elapsedHours = (different / (1000 * 60 * 60))

            val elapsedMinutes: Long = TimeUnit.MILLISECONDS.toMinutes(different)


            /*  //15 minutes calculation
              Log.e("DoubleHours", elapsedHours.toString())
              var minsUsed = 15 - elapsedMinutes
              Log.e("hoursUsed", minsUsed.toString())
              var percent = (minsUsed.toDouble() / 15.00) * 100
              Log.e("DoublePercent", percent.toString())
              var int = Math.round(100 - percent.toDouble()).toInt()
              Log.e("Percent", int.toString())
      */
            //72 Hours Calculation

            Log.e("diffInMin", elapsedMinutes.toString())
            var minutesUsed = 4320 - elapsedMinutes
            Log.e("minutesUsed", minutesUsed.toString())
            var percent = (minutesUsed.toDouble() / 4320.00) * 100
            Log.e("DoublePercent", percent.toString())
            var preInt = DecimalFormat("##.##").format(100.00 - percent.toDouble())
            Log.e("PreInt", (preInt.toDouble() * 100).toLong().toString())

            return (preInt.toDouble() * 100).toInt()
        } catch (e: Exception) {
            e.printStackTrace()
            return 0
        } catch (e: NumberFormatException) {
            e.printStackTrace()
            return 0
        }


    }


    fun getTaskTime(dateString: String): String {
        val dateFormats =
            listOf("yyyy-MM-dd HH:mm:ss", "yyyy/MM/dd HH:mm:ss", "dd/MM/yyyy HH:mm:ss")
        var currentTime = ""
        var date: Date? = null
        for (dateFormat in dateFormats) {
            try {
                date = SimpleDateFormat(dateFormat).parse(dateString)
            } catch (e: java.lang.Exception) {
                Log.d("Exception", "getFormattedDate: unable to parse date.")
            }
        }
        if (date != null) {
            currentTime = SimpleDateFormat("HH:mm").format(date)
        }
        return currentTime
    }


    fun View.DisableClick(view: View) {
        view.isEnabled = false
        android.os.Handler().postDelayed(Runnable {
            view.isEnabled = true

        }, 1000)

    }


    fun hideCursorOnClickDone(context: Context, editText: EditText) {
        editText.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                v.clearFocus()
                hideKeyBoard(context, editText.rootView)
                return@OnEditorActionListener true
            }
            false
        })
    }


    fun getServerFormattedDateinChat(time: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")

        var currentTime = ""
        var date: Date? = null
        try {
            date = dateFormat.parse(time)
        } catch (e: java.lang.Exception) {
            Log.d("Exception", "getFormattedDate: unable to parse date.")
        }

        if (date != null) {
            val smsTime = Calendar.getInstance()
            smsTime.timeInMillis = date.time

            val now = Calendar.getInstance()
            if (now.get(Calendar.DATE) == smsTime.get(Calendar.DATE)) {

                return "Today"
            } else if (now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) == 1) {
                return "Yesterday"
            } else {
                currentTime = SimpleDateFormat("yyyy-MM-dd").format(date)

            }

        }

        return currentTime
    }


    fun getServerFormattedOnlyDate(time: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        var currentTime = ""
        var date: Date? = null
        try {
            date = dateFormat.parse(time)
        } catch (e: java.lang.Exception) {
            Log.d("Exception", "getFormattedDate: unable to parse date.")
        }

        if (date != null) {
            currentTime = SimpleDateFormat("yyyy-MM-dd").format(date)
        }
        return currentTime
    }
}




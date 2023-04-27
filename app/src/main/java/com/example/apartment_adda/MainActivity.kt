package com.example.apartment_adda

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.TimePicker
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import java.util.Calendar
import kotlin.math.abs


class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var sport_spinner : Spinner
    private lateinit var club_time_spinner : Spinner
    private lateinit var tennis_time_tv1 : TextView
    private lateinit var tennis_time_tv2 : TextView

    private lateinit var selectDatetv : TextView
    private lateinit var selectTimetv : TextView

    private lateinit var infoArray : ArrayList<String>

    private lateinit var confirm_button : Button

    private var sports : String? = "ClubHouse"
    private var date : String? = null

    private var time1 : String? = null
    private var time2 : String? = null
    private var clubh_time : String? = "ClubHouse"


    private var sport_array = arrayOf("ClubHouse","Tennis Court")
    private var sport_time_array = arrayOf("10am to 4pm","4pm to 10pm")
    private var tennis_time_array = arrayOf("1 hr","2 hr","3 hr","4 hr","5 hr","6 hr","7 hr","8 hr")



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val drawable = ColorDrawable(Color.parseColor("#5C5CFF"))
        supportActionBar?.setBackgroundDrawable(drawable)

        infoArray = ArrayList()

        initfunction()

        setSportAdapter()
        setClubTimeAdapter()

        selectDatetv.setOnClickListener {
            showDataPickerDialog()

        }

        tennis_time_tv1.setOnClickListener {
          showTimePickerDialog(tennis_time_tv1)
//            Log.i("Main tennis_time_tv1 ", time1!!)

        }

        tennis_time_tv2.setOnClickListener {
              showTimePickerDialog(tennis_time_tv2)
//            Log.i("Main tennis_time_tv2 ", time2!!)


        }

        confirm_button.setOnClickListener{

            insertInfoToArray()
        }


    }

    private fun insertInfoToArray() {

        val actualTime : Int
        var actualCost : Int
        var message : String = "Amount"


        Log.i("Main insertInfoToArray ", "$sports $date $time1 $time2 $clubh_time")


        if(date!=null) {


            if (infoArray.contains("$sports $date $time1 $time2 $clubh_time")) {
                //Show Alert box , Booking failed
                AlertDialog.Builder(this)
                    .setTitle("Booking Status")
                    .setMessage("Booking failed, Already booked")
                    .setCancelable(true)
                    .setPositiveButton(
                        "ok"
                    ) { dialog, which -> dialog.dismiss() }.show()

            } else {

                if (sports == "Tennis Court") {

                    if(time1 != null && time2 != null) {

//                        if (time1!!.toInt() > time2!!.toInt() && time1!!.toInt() < time2!!.toInt()) {

                            actualTime = abs( (time1!!.toInt() - time2!!.toInt()))
                            actualCost = actualTime * 50

                            infoArray.add("$sports $date $time1 $time2 $clubh_time")

                           showDialogInfo("for $actualTime hrs, Rs ${actualCost}")


                    }
                    else{

                        showDialogInfo("Select time!!")

                    }
                }

                if (sports == "ClubHouse") {
                    if (clubh_time == "10am to 4pm") {
                        actualCost = 6 * 100
                        message = " Rs ${actualCost}"
                        infoArray.add("$sports $date $time1 $time2 $clubh_time")

                    } else {
                        actualCost = 6 * 500
                        message = " Rs ${actualCost}"

                        infoArray.add("$sports $date $time1 $time2 $clubh_time")

                    }

                }

            }
        }
        else{

            showDialogInfo("Select Date!!")

        }

        Log.d("MainActivity ",infoArray.toString())
    }

    private fun showDialogInfo(message: String) {

        AlertDialog.Builder(this)
            .setTitle("Booking Status")
            .setMessage("Booked, ${message}")
            .setCancelable(true)
            .setPositiveButton(
                "OK"
            ) { dialog, which -> dialog.dismiss() }.show()

    }

    private fun showTimePickerDialog(tv: TextView)  {

        var newTime :String = ""
        val mcurrentTime = Calendar.getInstance()
        val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
        val minute = mcurrentTime[Calendar.MINUTE]
        val mTimePicker: TimePickerDialog = TimePickerDialog(this, object :TimePickerDialog.OnTimeSetListener
            {

                override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {

                    if(tennis_time_tv1 == tv) {
                        tv.setText("$hourOfDay")
                        time1 = "$hourOfDay"
                    }else{
                        tv.setText("$hourOfDay")
                        time2 = "$hourOfDay"
                    }

                    Log.i("Main inside", newTime)

                }
            },
            hour,
            minute,
            false
        )

        mTimePicker.setTitle("Select Time")
        mTimePicker.show()

    }

    private fun showDataPickerDialog() {

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

            // Display Selected date in textbox
            selectDatetv.setText("" + dayOfMonth + ", " + monthOfYear + ", " + year)

            date = "" + dayOfMonth + ", " + monthOfYear + ", " + year

        }, year, month, day)

        dpd.getDatePicker().setMinDate(System.currentTimeMillis())


        dpd.show()
    }


    private fun initfunction() {

        val constraintLl = findViewById<ConstraintLayout>(R.id.con_layout)

        selectDatetv = constraintLl.findViewWithTag<TextView>("tv")
        sport_spinner = constraintLl.findViewById<Spinner>(R.id.sports_spinner)
        club_time_spinner = constraintLl.findViewById<Spinner>(R.id.clubh_time_spinner)
        tennis_time_tv1 = constraintLl.findViewById<TextView>(R.id.tenn_time_tv1)
        tennis_time_tv2 = constraintLl.findViewById<TextView>(R.id.tenn_time_tv2)


        confirm_button = constraintLl.findViewById<Button>(R.id.confirm_button)

    }


    private fun setClubTimeAdapter() {
        club_time_spinner.setOnItemSelectedListener(this)

        val time_spin = ArrayAdapter(this, android.R.layout.simple_spinner_item, sport_time_array)
        time_spin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        club_time_spinner.setAdapter(time_spin)

    }

    private fun setSportAdapter() {
        sport_spinner.setOnItemSelectedListener(this)

        val sport_spin = ArrayAdapter(this, android.R.layout.simple_spinner_item, sport_array)
        sport_spin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sport_spinner.setAdapter(sport_spin)

    }


    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

            if(parent?.id == R.id.sports_spinner){

                if(sport_array[position] == "Tennis Court")
                {
                    Log.i("Main ", sport_array[position])

                    sports = sport_array[position]

                    club_time_spinner.visibility = View.GONE
                    tennis_time_tv1.visibility = View.VISIBLE
                    tennis_time_tv2.visibility = View.VISIBLE


                }
                else{
                    Log.i("Main ", sport_array[position])

                    sports = sport_array[position]

                    club_time_spinner.visibility = View.VISIBLE
                    tennis_time_tv1.visibility = View.GONE
                    tennis_time_tv2.visibility = View.GONE

                }


            }
        else if(parent?.id == R.id.clubh_time_spinner){

                clubh_time = sport_time_array[position]

        }
        else  if(parent?.id == R.id.tenn_time_tv1){}
    }


    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

}
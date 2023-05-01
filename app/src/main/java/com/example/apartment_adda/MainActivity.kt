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
import androidx.lifecycle.ViewModelProvider
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
    private lateinit var time1ArrayTennis :ArrayList<Int>
    private lateinit var time2ArrayTennis : ArrayList<Int>


    private lateinit var confirm_button : Button

    private var sports : String? = "ClubHouse"
    private var date : String? = null

    private var time1 : String? = null
    private var time2 : String? = null
    private var clubh_time : String? = "ClubHouse"


    private var sport_array = arrayOf("ClubHouse","Tennis Court")
    private var sport_time_array = arrayOf("10am to 4pm","4pm to 10pm")

    private lateinit var viewModel: MainViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val drawable = ColorDrawable(Color.parseColor("#5C5CFF"))
        supportActionBar?.setBackgroundDrawable(drawable)

         viewModel = this.run {
             ViewModelProvider(this).get(MainViewModel::class.java)
         }

        infoArray = ArrayList()
        time1ArrayTennis = ArrayList()
        time2ArrayTennis =ArrayList()

        initfunction()
        setSportAdapter()
        setClubTimeAdapter()

        selectDatetv.setOnClickListener {
            showDataPickerDialog()

        }

        tennis_time_tv1.setOnClickListener {
          showTimePickerDialog(tennis_time_tv1)

        }

        tennis_time_tv2.setOnClickListener {
              showTimePickerDialog(tennis_time_tv2)

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

        viewModel.datelivedata.observe(this) {

            date = it
            Log.i("Main insert ", " $it ")

        }

        viewModel.sportslivedata.observe(this) {

            sports = it

            Log.i("Main insert ", " $it ")

        }
        viewModel.time1livedata.observe(this) {

            time1 = it

            Log.i("Main insert ", " $it ")

        }
        viewModel.time2livedata.observe(this) {

            time2 = it

            Log.i("Main insert ", " $it ")

        }
        viewModel.clubhouselivedata.observe(this) {

            clubh_time = it

            Log.i("Main insert ", " $it ")

        }


        if(date!=null) {

            if (infoArray.contains("$sports $date $time1 $time2 $clubh_time")) {
                //Show Alert box , Booking failed
                showDialogInfo("Booking failed, Already booked")


            } else {

                if (sports == "Tennis Court") {

                    time1ArrayTennis.add(time1!!.toInt())
                    time2ArrayTennis.add(time2!!.toInt())

                    Log.d("Main","time1 - $time1ArrayTennis, time2 - $time2ArrayTennis")

                    if(time1 != null && time2 != null) {

                            actualTime = abs( (time1!!.toInt() - time2!!.toInt()))
                            actualCost = actualTime * 50

                        val checktime = checkfor1hr(time1!!, time2!!)
                        Log.d("Main","$checktime")

                        if(checktime) {
                            infoArray.add("$sports $date $time1 $time2 $clubh_time")
                            showDialogInfo("Booked,for $actualTime hrs, Rs ${actualCost}")

                        }
                        else{
                            time1ArrayTennis.removeLast()
                            time2ArrayTennis.removeLast()
                            Log.d("Main last removed","time1 - $time1ArrayTennis, time2 - $time2ArrayTennis")

                            showDialogInfo("Booking failed, Already booked")

                        }

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

    private fun checkfor1hr(time1: String, time2: String) : Boolean {

        //start here

        if(time1ArrayTennis.contains(time1.toInt()+1) || time1ArrayTennis.contains(time1.toInt()-1))
        {
            return false

        }else
            return !(time2ArrayTennis.contains(time2.toInt()+1) || time2ArrayTennis.contains(time2.toInt()-1))




    }

    private fun showDialogInfo(message: String) {

        AlertDialog.Builder(this)
            .setTitle("Booking Status")
            .setMessage("$message")
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
//                        time11 = "$hourOfDay"
                        viewModel.time1livedata.value = "$hourOfDay"

                    }else{
                        tv.setText("$hourOfDay")
//                        time22 = "$hourOfDay"
                        viewModel.time2livedata.value = "$hourOfDay"
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


        val dpd = DatePickerDialog(this, { view, year, monthOfYear, dayOfMonth ->

            // Display Selected date in textbox
            selectDatetv.text = "$dayOfMonth, $monthOfYear, $year"


            viewModel.datelivedata.value = "$dayOfMonth, $monthOfYear, $year"

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

                    viewModel.sportslivedata.value = sport_array[position]

                    club_time_spinner.visibility = View.GONE
                    tennis_time_tv1.visibility = View.VISIBLE
                    tennis_time_tv2.visibility = View.VISIBLE


                }
                else{
                    Log.i("Main ", sport_array[position])

                    viewModel.sportslivedata.value = sport_array[position]


                    club_time_spinner.visibility = View.VISIBLE
                    tennis_time_tv1.visibility = View.GONE
                    tennis_time_tv2.visibility = View.GONE

                }


            }
        else if(parent?.id == R.id.clubh_time_spinner){

//                clubh_time = sport_time_array[position]
//                  clubh_timee = sport_time_array[position]
                viewModel.clubhouselivedata.value = sport_time_array[position]


            }
        else  if(parent?.id == R.id.tenn_time_tv1){}
    }


    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

}
package com.example.helixproject

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import java.util.*

class Fragment1 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 알람 설정 버튼 클릭 시 알람 설정
        btnSetAlarm.setOnClickListener {
            setAlarm()
        }
    }

    private fun setAlarm() {
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // 현재 TimePicker에서 선택된 시간 가져오기
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, timePicker.currentHour)
        calendar.set(Calendar.MINUTE, timePicker.currentMinute)
        calendar.set(Calendar.SECOND, 0)

        // 현재 시간보다 과거의 시간으로 설정하거나 알람 시간이 이미 지난 경우 리턴
        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            showToast("설정된 시간이 이미 지났습니다.")
            return
        }

        // 알림에 전달할 PendingIntent 생성
        val intent = Intent(requireContext(), AlarmReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(requireContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        // 알람 설정
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)

        showToast("알람이 설정되었습니다.")
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
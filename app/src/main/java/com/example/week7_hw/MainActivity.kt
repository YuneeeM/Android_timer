package com.example.week7_hw

import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import com.example.week7_hw.databinding.ActivityMainBinding

var started =false
var stop =false

class MainActivity : AppCompatActivity() {
    //나중에 초기화 하겠다
    private lateinit var viewBinding: ActivityMainBinding
    private var currentCountDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //xml파일을 읽어와서 해석을 하겠다 == inflate
        viewBinding =ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        bindViews()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun bindViews() {
        viewBinding.btn1.setOnClickListener{
            started =true

            viewBinding.minuteTextView.text="00'"
            viewBinding.secondTextView.text="00"
            completeCountDown()
            stopCountDown()


        }

        viewBinding.btn2.setOnClickListener{
            stop =true
            stopCountDown()
        }

        //각각의 뷰에 대한 리스너와 코드를 연결
        viewBinding.seekBar.setOnSeekBarChangeListener(
            //object로 선언하면 클래스 선언과 동시에 객체가 생성됨
            //object 객체 역시 다른 class를 상속하거나 interface를 구현함



            object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // from User : 사용자에 의한 변경인지
                if (fromUser) {
                    //updateSeekBar에서 변경되는 경우도 있기 때문에 유저가 만질때 만
                    //프로그레스바를 조정하고 있으면 초를 0으로 맞춰주기 위해 추가
                    //(textView 갱신)
                    updateRemainTime(progress * 60 * 1000L)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                //조정하기 시작하면 기존 타이머가 있을 때 cancel 후 null
                stopCountDown()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                seekBar ?: return     //seekBar가 null인 경우 return
                if (seekBar.progress == 0) {
                    stopCountDown()
                }
                else {
                    startCountDown()

                }
            }
            }
        )
    }

    private fun stopCountDown(){
        currentCountDownTimer?.cancel()
        currentCountDownTimer = null
        started=false
    }

    private fun createCountDownTimer(initialMillis: Long): CountDownTimer {
        return object : CountDownTimer(initialMillis, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                //1초마다 한번씩 불림
                updateRemainTime(millisUntilFinished)
                updateSeekBar(millisUntilFinished)
            }

            override fun onFinish() {
                completeCountDown()
            }
        }

    }

    //사용자가 바에서 손을 떼는 순간 새로운 타이머 생성
    private fun startCountDown() {
        currentCountDownTimer = createCountDownTimer(viewBinding.seekBar!!.progress * 60 * 1000L).start()
        currentCountDownTimer?.start()
    }

    private fun completeCountDown() {
        updateRemainTime(0)
        updateSeekBar(0)
        Toast.makeText(this,"타이머 종료!!", Toast.LENGTH_SHORT).show()
    }

    private fun updateRemainTime(remainMillis: Long) {
        val remainSecond = remainMillis / 1000

        viewBinding.minuteTextView.text = "%02d`".format(remainSecond / 60)
        viewBinding.secondTextView.text = "%02d".format(remainSecond % 60)
    }

    private fun updateSeekBar(remainMillis: Long) {
        viewBinding.seekBar.progress = (remainMillis / 1000 / 60).toInt() //분
    }
}
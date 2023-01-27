package me.cubixor.bettertello

import android.content.Intent
import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat
import androidx.fragment.app.DialogFragment
import me.cubixor.bettertello.api.OnSwipeListener
import me.cubixor.bettertello.bar.BarState
import me.cubixor.bettertello.controller.ControllerManager
import me.cubixor.bettertello.databinding.ActivityMainBinding
import me.cubixor.bettertello.tello.TelloAction
import me.cubixor.bettertello.utils.BarAnimationUtils
import me.cubixor.bettertello.utils.Utils
import me.cubixor.telloapi.api.FlipDirection
import me.cubixor.telloapi.api.Tello

class MainActivity : AppCompatActivity() {
    private val viewModel: MainActivityViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var tello: Tello
    private lateinit var controllerManager: ControllerManager


    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        overridePendingTransition(R.anim.slide_in_main, R.anim.slide_out_home)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        setContentView(binding.root)
        Utils.fullScreen(this)


        /* if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
             // You can use the API that requires the permission.
         } else {
             // You can directly ask for the permission.
             // The registered ActivityResultCallback gets the result of this request.
             requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 0)
         }*/


        Utils.setupBlur(this, binding.blurView, 0.1f)

        controllerManager = (application as App).controllerManager
        tello = (application as App).tello
        val videoView = VideoView(this, binding.videoView)
        tello.addVideoListener(videoView)


        setupJoysticks()
        setupFlipsView()

        viewModel.currentBar.observe(this) {
            changeBarMainActivity(it)
        }

        viewModel.photoModeRes.observe(this) {
            val view = binding.changePhotoVideoButton
            if (view.rotation % 360 == 0f) {
                view.animate().rotationBy(-360f).duration = 500
            }
        }
    }

    private fun setupJoysticks() {
        val joystickLeft = binding.joystickLeft
        joystickLeft.setOnMoveListener { _: Int, _: Int ->
            tello.droneAxis.yaw = joystickLeft.normalizedX
            tello.droneAxis.throttle = joystickLeft.normalizedY
        }
        val joystickRight = binding.joystickRight
        joystickRight.setOnMoveListener { _: Int, _: Int ->
            tello.droneAxis.roll = joystickRight.normalizedX
            tello.droneAxis.pitch = joystickRight.normalizedY
        }
    }

    private fun setupFlipsView() {
        //Hide flips view when it's not active
        val flipsView = binding.flipsView
        viewModel.flipsModeRunning.observe(this) {
            flipsView.visibility =
                if (it) View.VISIBLE
                else View.INVISIBLE
        }

        //Detect swipe direction and flip the drone according to it
        val detector = GestureDetectorCompat(this, object : OnSwipeListener() {
            override fun onSwipe(direction: FlipDirection): Boolean {
                println(direction)
                TelloAction.handleFlip(tello, direction)
                return true
            }
        })

        //Update touch indicator
        val touchIndicatorView = binding.flipsTouchIndicatorView
        flipsView.setOnTouchListener { view: View, event: MotionEvent ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    updateFlipsTouchIndicatorPosition(touchIndicatorView, event)
                    touchIndicatorView.visibility = View.VISIBLE
                }
                MotionEvent.ACTION_UP -> touchIndicatorView.visibility = View.INVISIBLE
                MotionEvent.ACTION_MOVE -> updateFlipsTouchIndicatorPosition(touchIndicatorView, event)
            }
            view.performClick()
            detector.onTouchEvent(event)
        }
    }

    private fun updateFlipsTouchIndicatorPosition(touchIndicatorView: View, event: MotionEvent) {
        val x = event.x.toInt() - touchIndicatorView.width / 2
        val y = event.y.toInt() - touchIndicatorView.height / 2
        touchIndicatorView.x = x.toFloat()
        touchIndicatorView.y = y.toFloat()
    }

    override fun onGenericMotionEvent(event: MotionEvent): Boolean {
        return controllerManager.onGenericMotionEvent(event) || super.onGenericMotionEvent(event)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return controllerManager.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event)
    }

    fun onVideoSettingsButtonClick(view: View) {
        val icon = view.findViewById<View>(R.id.videoSettingsImage)
        icon.animate().rotationBy(360f).duration = 500

        val newFragment: DialogFragment = VideoSettingsWindow()
        newFragment.show(supportFragmentManager, "videoSettings")
    }

    fun onHomePageClick(view: View?) {
        val intent = Intent(this, HomePageActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_home, R.anim.slide_out_main)
    }

    fun onButtonAI(view: View?) {
        val running = viewModel.smartModeRunning.value!!
        if (!running) {
            val intent = Intent(this, FlightModesActivity::class.java)
            startActivity(intent)
        }
        viewModel.onButtonAiClick(running)
    }

    private fun changeBarMainActivity(to: BarState) {
        val bar = binding.topBackground
        val previousBarDrawable = bar.drawable
        val newBarDrawable = to.barColor.getMainViewDrawable(this)
        val barDrawables = arrayOf(previousBarDrawable, newBarDrawable)
        val transitionDrawable = TransitionDrawable(barDrawables)
        transitionDrawable.isCrossFadeEnabled = true
        bar.setImageDrawable(transitionDrawable)
        transitionDrawable.startTransition(600)

        val stateText = binding.stateText
        BarAnimationUtils.changeText(this, to, stateText)
    }

    override fun onBackPressed() {
        finishAffinity()
    }
}
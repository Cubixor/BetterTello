package me.cubixor.bettertello


import android.content.Intent
import android.graphics.Typeface
import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import android.widget.AdapterView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import me.cubixor.bettertello.api.MenuAdapter
import me.cubixor.bettertello.api.SpinnerBackgroundCreator
import me.cubixor.bettertello.bar.BarState
import me.cubixor.bettertello.databinding.ActivityHomePageBinding
import me.cubixor.bettertello.utils.BarAnimationUtils
import me.cubixor.bettertello.utils.Utils

class HomePageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomePageBinding
    private val viewModel: HomePageViewModel by viewModels()
    private val stateBars: MutableList<TextView> = ArrayList()


    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        overridePendingTransition(R.anim.slide_in_home, R.anim.slide_out_main)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePageBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        setContentView(binding.root)
        Utils.fullScreen(this)

        setupSpinner()
        val bar = viewModel.currentBar.value
        if (bar != null) {
            setupHomeActivity(bar)
        }


        viewModel.expandButtonShown.observe(this) { buttonShown: Boolean ->
            if (buttonShown) showExpandButton()
            else hideExpandButton()
        }

        viewModel.statesExpanded.observe(this) { expanded: Boolean ->
            if (expanded) expandStates()
            else shrinkStates()
        }

        viewModel.currentBar.observe(this) {
            if (binding.stateText2.text == "")
                setupHomeActivity(it)
            else
                changeBarHomeActivity(it)
        }
        viewModel.addedStates.observe(this) {
            addToExpanded(it.barState, it.index)
        }
        viewModel.removedStates.observe(this) {
            removeFromExpanded(it.index)
        }

    }


    private fun setupSpinner() {
        val states: Map<String, Int> = mapOf(
            getString(R.string.home_home) to R.drawable.ic_round_home_48,
            getString(R.string.home_sensors) to R.drawable.ic_round_sensors_24,
            getString(R.string.home_battery) to R.drawable.ic_round_battery_std_24,
            getString(R.string.home_motors) to R.drawable.ic_round_electric_bolt_24,
            getString(R.string.home_vgps) to R.drawable.ic_round_location_on_24,
            getString(R.string.home_info) to R.drawable.ic_round_info_24
        )

        val spinner = binding.spinner
        val spinnerBackgroundCreator = SpinnerBackgroundCreator()
        spinner.setPopupBackgroundDrawable(
            spinnerBackgroundCreator.createBackground(
                this, states.size
            )
        )
        val adapter = MenuAdapter(applicationContext, states)
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}

        }
    }

    private fun showExpandButton() {
        val expandButton = binding.expandStatesButton
        if (expandButton.visibility == View.VISIBLE) return
        expandButton.visibility = View.VISIBLE

        val inAnim = AnimationUtils.loadAnimation(this, R.anim.slide_in_expand)
        inAnim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationRepeat(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                expandButton.elevation = 11f
            }
        })
        expandButton.startAnimation(inAnim)
    }

    private fun hideExpandButton() {
        val expandButton = binding.expandStatesButton
        if (expandButton.visibility == View.INVISIBLE) return
        expandButton.elevation = 0f

        val outAnim = AnimationUtils.loadAnimation(this, R.anim.slide_out_expand)
        outAnim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationRepeat(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                expandButton.visibility = View.INVISIBLE
            }

        })
        expandButton.startAnimation(outAnim)
    }

    private fun expandStates() {
        val activeStates: List<BarState> = viewModel.activeStates.value!!
        if (activeStates.size < 2) return

        binding.expandStatesButton.animate().rotationBy(-180f).duration = 300

        for (index in 1 until activeStates.size) {
            createStateBar(activeStates[index], index)
        }
    }

    private fun shrinkStates() {
        if (binding.expandStatesButton.visibility == View.INVISIBLE) return

        binding.expandStatesButton.animate().rotationBy(180f).duration = 300
        binding.homeStateLayout.bringToFront()

        for (textView in stateBars) {
            slideOutState(textView)
        }

        stateBars.clear()
    }

    private fun createStateBar(barState: BarState, index: Int) {
        val constraintLayout = binding.homePage
        val id = View.generateViewId()
        val textView = TextView(this)
        val constraintSet = ConstraintSet()

        textView.id = id
        textView.width = Utils.dpToPixels(this, 440)
        textView.height = Utils.dpToPixels(this, 40)
        textView.setText(barState.textPath)
        textView.setTextColor(getColor(R.color.white))
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f)
        textView.setTypeface(textView.typeface, Typeface.BOLD)
        textView.gravity = Gravity.CENTER_VERTICAL
        textView.background = barState.barColor.getHomeViewDrawable(this)
        textView.setPaddingRelative(Utils.dpToPixels(this, 20), 0, 0, 0)
        textView.outlineProvider = null
        textView.elevation = (10 - index).toFloat()
        constraintLayout.addView(textView, index)
        constraintSet.clone(constraintLayout)
        constraintSet.connect(id, ConstraintSet.BOTTOM, R.id.homeStateLayout, ConstraintSet.BOTTOM)
        constraintSet.connect(id, ConstraintSet.TOP, R.id.homeStateLayout, ConstraintSet.TOP)
        constraintSet.connect(id, ConstraintSet.END, R.id.homeStateLayout, ConstraintSet.END)
        constraintSet.connect(id, ConstraintSet.START, R.id.homeStateLayout, ConstraintSet.START)
        constraintSet.applyTo(constraintLayout)
        stateBars.add(index - 1, textView)
        val inAnim = getAnimation(
            0, -Utils.dpToPixels(this, index * 50)
        )
        inAnim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationRepeat(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                textView.bringToFront()
            }
        })
        textView.startAnimation(inAnim)
    }

    private fun slideOutState(textView: TextView) {
        val out = getAnimation(
            -Utils.dpToPixels(
                this, (stateBars.indexOf(textView) + 1) * 50
            ), 0
        )
        out.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationRepeat(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                (textView.parent as ViewGroup).removeView(textView)
            }

        })
        textView.startAnimation(out)
    }

    private fun slideOneStep(index: Int, up: Boolean) {
        val textView: TextView = stateBars[index]

        val slide = getAnimation(
            -Utils.dpToPixels(this, (index + (if (!up) 2 else 0)) * 50),
            -Utils.dpToPixels(this, (index + (if (up) 1 else 1)) * 50)
        )
        textView.startAnimation(slide)
    }

    private fun changeBarHomeActivity(to: BarState) {

        val stateBar = binding.homeStateBar
        val previousBarDrawable = stateBar.foreground
        val newBarDrawable = to.barColor.getHomeViewDrawable(this)
        val barDrawables = arrayOf(previousBarDrawable, newBarDrawable)
        val barTransition = TransitionDrawable(barDrawables)
        barTransition.isCrossFadeEnabled = true
        stateBar.foreground = barTransition
        barTransition.startTransition(600)

        val expandButton = binding.expandStatesButton
        val previousExpandDrawable = expandButton.foreground
        val newExpandDrawable = to.barColor.getExpandDrawable(this)
        val expandDrawables = arrayOf(previousExpandDrawable, newExpandDrawable)
        val expandTransition = TransitionDrawable(expandDrawables)
        expandTransition.isCrossFadeEnabled = true
        expandButton.foreground = expandTransition
        expandTransition.startTransition(600)

        //int previousGlowColor = currentBar.getBarColor().getGlowColor();
        val newGlowColor = to.barColor.getGlowColor(this)

        Handler(Looper.getMainLooper()).postDelayed({
            stateBar.glowColor = newGlowColor
            expandButton.glowColor = newGlowColor
        }, 300)

        val stateText = binding.stateText2
        BarAnimationUtils.changeText(this, to, stateText)
    }


    private fun removeFromExpanded(index: Int) {
        if (!viewModel.statesExpanded.value!! || viewModel.activeStates.value!!.isEmpty()) return

        // If the current (main) bar is removed
        // we actually need to remove first bar from expanded
        // (to later display it as a current bar) and slide everything down
        val toRemoveIndex: Int = if (index == 0) 0 else index - 1

        slideOutState(stateBars[toRemoveIndex])
        stateBars.remove(stateBars[toRemoveIndex])

        for (i in toRemoveIndex until stateBars.size) {
            slideOneStep(i, false)
        }
    }

    private fun addToExpanded(barState: BarState, index: Int) {
        if (!viewModel.statesExpanded.value!!) return
        val activeStates: List<BarState> = viewModel.activeStates.value!!
        if (activeStates.size == 1) return


        var toAddBar: BarState = barState
        var toAddIndex = index - 1

        // If the bar is added as a current (main) bar
        // we actually need to add the bar which was "current"
        // a moment ago
        if (index == 0) {
            toAddBar = activeStates[1]
            toAddIndex = 0
        }

        createStateBar(toAddBar, toAddIndex + 1)

        for (i in toAddIndex + 1 until stateBars.size) {
            slideOneStep(i, true)
        }

    }

    private fun setupHomeActivity(currentBar: BarState) {
        val stateText = binding.stateText2
        stateText.text = Utils.getStr(currentBar.textPath)
        val stateBar = binding.homeStateBar
        stateBar.foreground = currentBar.barColor.getHomeViewDrawable(this)
        stateBar.glowColor = currentBar.barColor.getGlowColor(this)
        val expandButton = binding.expandStatesButton
        expandButton.foreground = currentBar.barColor.getExpandDrawable(this)
        expandButton.glowColor = currentBar.barColor.getGlowColor(this)
    }

    private fun getAnimation(fromY: Int, toY: Int): Animation {
        val anim: Animation = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0F,
            Animation.RELATIVE_TO_SELF, 0F,
            if (fromY == 0) Animation.RELATIVE_TO_SELF else Animation.ABSOLUTE, fromY.toFloat(),
            if (toY == 0) Animation.RELATIVE_TO_SELF else Animation.ABSOLUTE, toY.toFloat()
        ).apply {
            duration = 300
            fillAfter = true
        }

        return anim
    }

    override fun onBackPressed() {
        finishAffinity()
    }

    fun onImageClickEvent() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_main, R.anim.slide_out_home)
    }

    fun onSettingsClick() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    fun onStateClick() {
        println("CLICK")
    }
}
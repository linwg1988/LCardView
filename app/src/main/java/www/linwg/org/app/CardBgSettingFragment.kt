package www.linwg.org.app

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import www.linwg.org.lib.LCardView

class CardBgSettingFragment : Fragment() {

    private val viewModel: DemoViewModel by lazy {
        ViewModelProvider(requireActivity())[DemoViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_card_bg_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val sbSW: SeekBar = view.findViewById<SeekBar>(R.id.sbSW)
        sbSW.setOnSeekBarChangeListener(object : OnSeekBarChangeAdapter() {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                viewModel.strokeWidth.value = progress
            }
        })
        val cbRes: CheckBox = view.findViewById<CheckBox>(R.id.cbRes)
        cbRes.setOnCheckedChangeListener { _, isChecked ->
            viewModel.cardBackgroundDrawableRes.value = if (isChecked) R.mipmap.girl else 0
        }
        val cbDrawable: CheckBox = view.findViewById<CheckBox>(R.id.cbDrawable)
        cbDrawable.setOnCheckedChangeListener { _, isChecked ->
            viewModel.cardBackground.value = if (isChecked) ResourcesCompat.getDrawable(
                resources,
                R.drawable.test,
                null
            ) else null
        }

        val tvLabel: TextView = view.findViewById<TextView>(R.id.tvLabel)
        val rgGradientDirection: RadioGroup =
            view.findViewById<RadioGroup>(R.id.rgGradientDirection)
        val rgStart: RadioGroup = view.findViewById<RadioGroup>(R.id.rgStart)
        val rgCenter: RadioGroup = view.findViewById<RadioGroup>(R.id.rgCenter)
        val rgEnd: RadioGroup = view.findViewById<RadioGroup>(R.id.rgEnd)
        val llColor: View = view.findViewById<View>(R.id.llColor)
        val cbGradient: CheckBox = view.findViewById<CheckBox>(R.id.cbGradient)
        val cbGradientSync: CheckBox = view.findViewById<CheckBox>(R.id.cbGradientSync)
        cbGradient.setOnCheckedChangeListener { _, isChecked ->
            tvLabel.visibility = if (isChecked) View.VISIBLE else View.GONE
            rgGradientDirection.visibility = if (isChecked) View.VISIBLE else View.GONE
            llColor.visibility = if (isChecked) View.VISIBLE else View.GONE
            if (!isChecked) {
                viewModel.gradientColors.value = intArrayOf()
            }
        }
        cbGradientSync.setOnCheckedChangeListener { _, isChecked ->
            viewModel.gradientSizeFollowView.value = isChecked
        }
        rgGradientDirection.setOnCheckedChangeListener { _, checkedId ->
            viewModel.gradientDirection.value = when (checkedId) {
                R.id.rbLTR -> LCardView.LEFT_TO_RIGHT
                R.id.rbTTB -> LCardView.TOP_TO_BOTTOM
                R.id.rbLTTRB -> LCardView.LEFT_TOP_TO_RIGHT_BOTTOM
                else -> LCardView.LEFT_BOTTOM_TO_RIGHT_TOP
            }
        }
        rgStart.setOnCheckedChangeListener { _, checkedId ->
            val endId = rgEnd.checkedRadioButtonId
            val centerId = rgCenter.checkedRadioButtonId
            val startColor =
                if (checkedId == R.id.rbRedStart) Color.RED else if (checkedId == R.id.rbGreenStart) Color.GREEN else Color.BLUE
            val endColor =
                if (endId == R.id.rbRedEnd) Color.WHITE else if (endId == R.id.rbGrayEnd) Color.GRAY else Color.BLACK
            val centerColor = when (centerId) {
                R.id.rbRedCenter -> Color.YELLOW
                R.id.rbGreenCenter -> Color.parseColor(
                    "#ff00ff"
                )
                else -> Color.parseColor("#00ffff")
            }
            viewModel.gradientColors.value = intArrayOf(startColor, centerColor, endColor)
        }
        rgCenter.setOnCheckedChangeListener { group, checkedId ->
            val endId = rgEnd.checkedRadioButtonId
            val startId = rgStart.checkedRadioButtonId
            val startColor =
                if (startId == R.id.rbRedStart) Color.RED else if (startId == R.id.rbGreenStart) Color.GREEN else Color.BLUE
            val endColor =
                if (endId == R.id.rbRedEnd) Color.WHITE else if (endId == R.id.rbGrayEnd) Color.GRAY else Color.BLACK
            val centerColor = when (checkedId) {
                R.id.rbRedCenter -> Color.YELLOW
                R.id.rbGreenCenter -> Color.parseColor(
                    "#ff00ff"
                )
                else -> Color.parseColor("#00ffff")
            }
            viewModel.gradientColors.value = intArrayOf(startColor, centerColor, endColor)
        }
        rgEnd.setOnCheckedChangeListener { _, checkedId ->
            val centerId = rgCenter.checkedRadioButtonId
            val startId = rgStart.checkedRadioButtonId
            val startColor =
                if (startId == R.id.rbRedStart) Color.RED else if (startId == R.id.rbGreenStart) Color.GREEN else Color.BLUE
            val endColor =
                if (checkedId == R.id.rbRedEnd) Color.WHITE else if (checkedId == R.id.rbGrayEnd) Color.GRAY else Color.BLACK
            val centerColor = when (centerId) {
                R.id.rbRedCenter -> Color.YELLOW
                R.id.rbGreenCenter -> Color.parseColor(
                    "#ff00ff"
                )
                else -> Color.parseColor("#00ffff")
            }
            viewModel.gradientColors.value = intArrayOf(startColor, centerColor, endColor)
        }
    }
}
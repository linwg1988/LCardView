package www.linwg.org.app

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

class ShadowColorSettingFragment: Fragment() {

    private val viewModel :DemoViewModel by lazy {
        ViewModelProvider(requireActivity())[DemoViewModel::class.java]
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_shadow_color_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val sbAlpha = view.findViewById<SeekBar>(R.id.sbAlpha)
        val sbR = view.findViewById<SeekBar>(R.id.sbR)
        val sbG = view.findViewById<SeekBar>(R.id.sbG)
        val sbB = view.findViewById<SeekBar>(R.id.sbB)
        sbAlpha.setOnSeekBarChangeListener(object : OnSeekBarChangeAdapter() {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                viewModel.shadowStartAlpha.value = progress
            }
        })
        val colorChange = object : OnSeekBarChangeAdapter() {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                viewModel.shadowColor.value = Color.argb(
                    sbAlpha.progress,
                    sbR.progress,
                    sbG.progress,
                    sbB.progress
                )
            }
        }
        sbR.setOnSeekBarChangeListener(colorChange)
        sbG.setOnSeekBarChangeListener(colorChange)
        sbB.setOnSeekBarChangeListener(colorChange)
    }
}
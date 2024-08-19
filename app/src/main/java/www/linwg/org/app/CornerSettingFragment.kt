package www.linwg.org.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

class CornerSettingFragment: Fragment() {

    private val viewModel :DemoViewModel by lazy {
        ViewModelProvider(requireActivity())[DemoViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_corner_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val sbLT = view.findViewById<SeekBar>(R.id.sbLT)
        val sbRT = view.findViewById<SeekBar>(R.id.sbRT)
        val sbRB = view.findViewById<SeekBar>(R.id.sbRB)
        val sbLB = view.findViewById<SeekBar>(R.id.sbLB)
        val sbAll = view.findViewById<SeekBar>(R.id.sbAll)
        sbLT.setOnSeekBarChangeListener(object : OnSeekBarChangeAdapter() {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                viewModel.leftTopCornerRadius.value = progress.toFloat()
            }
        })
        sbRT.setOnSeekBarChangeListener(object : OnSeekBarChangeAdapter() {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                viewModel.rightTopCornerRadius.value = progress.toFloat()
            }
        })
        sbLB.setOnSeekBarChangeListener(object : OnSeekBarChangeAdapter() {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                viewModel.leftBottomCornerRadius.value = progress.toFloat()
            }
        })
        sbRB.setOnSeekBarChangeListener(object : OnSeekBarChangeAdapter() {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                viewModel.rightBottomCornerRadius.value = progress.toFloat()
            }
        })
        sbAll.setOnSeekBarChangeListener(object : OnSeekBarChangeAdapter() {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                viewModel.cornerRadius.value = progress.toFloat()
            }
        })
    }
}
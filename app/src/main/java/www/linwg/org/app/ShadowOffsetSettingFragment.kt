package www.linwg.org.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

class ShadowOffsetSettingFragment: Fragment() {

    private var offset = 0

    private val viewModel :DemoViewModel by lazy {
        ViewModelProvider(requireActivity())[DemoViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_shadow_offset_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val sbLO = view.findViewById<SeekBar>(R.id.sbLO)
        val sbRO = view.findViewById<SeekBar>(R.id.sbRO)
        val sbTO = view.findViewById<SeekBar>(R.id.sbTO)
        val sbBO = view.findViewById<SeekBar>(R.id.sbBO)
        val sbFourO = view.findViewById<SeekBar>(R.id.sbFourO)
        viewModel.shadowSize.observe(viewLifecycleOwner){
            offset = 100 - it / 2
        }
        sbLO.setOnSeekBarChangeListener(object : OnSeekBarChangeAdapter() {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                viewModel.leftOffset.value = progress.toFloat() - offset
            }
        })
        sbRO.setOnSeekBarChangeListener(object : OnSeekBarChangeAdapter() {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                viewModel.rightOffset.value = progress.toFloat() - offset
            }
        })
        sbTO.setOnSeekBarChangeListener(object : OnSeekBarChangeAdapter() {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                viewModel.topOffset.value = progress.toFloat() - offset
            }
        })
        sbBO.setOnSeekBarChangeListener(object : OnSeekBarChangeAdapter() {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                viewModel.bottomOffset.value = progress.toFloat() - offset
            }
        })
        sbFourO.setOnSeekBarChangeListener(object : OnSeekBarChangeAdapter() {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                viewModel.shadowOffsetCenter.value = progress.toFloat() - offset
            }
        })
    }
}
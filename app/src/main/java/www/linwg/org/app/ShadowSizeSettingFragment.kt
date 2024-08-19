package www.linwg.org.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

class ShadowSizeSettingFragment : Fragment() {

    private val viewModel: DemoViewModel by lazy {
        ViewModelProvider(requireActivity())[DemoViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_shadow_size_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val sbSS = view.findViewById<SeekBar>(R.id.sbSS)
        val sbDLSS = view.findViewById<SeekBar>(R.id.sbDLSS)
        val sbDTSS = view.findViewById<SeekBar>(R.id.sbDTSS)
        val sbDRSS = view.findViewById<SeekBar>(R.id.sbDRSS)
        val sbDBSS = view.findViewById<SeekBar>(R.id.sbDBSS)
        sbSS.setOnSeekBarChangeListener(object : OnSeekBarChangeAdapter() {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                viewModel.shadowSize.value = progress
            }
        })
        sbDLSS.setOnSeekBarChangeListener(object : OnSeekBarChangeAdapter() {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                viewModel.leftWidthDecrement.value = progress.toFloat()
            }
        })
        sbDTSS.setOnSeekBarChangeListener(object : OnSeekBarChangeAdapter() {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                viewModel.topWidthDecrement.value = progress.toFloat()
            }
        })
        sbDRSS.setOnSeekBarChangeListener(object : OnSeekBarChangeAdapter() {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                viewModel.rightWidthDecrement.value = progress.toFloat()
            }
        })
        sbDBSS.setOnSeekBarChangeListener(object : OnSeekBarChangeAdapter() {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                viewModel.bottomWidthDecrement.value = progress.toFloat()
            }
        })
    }
}
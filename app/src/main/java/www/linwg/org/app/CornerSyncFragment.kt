package www.linwg.org.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

class CornerSyncFragment: Fragment() {

    private val viewModel :DemoViewModel by lazy {
        ViewModelProvider(requireActivity())[DemoViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_corner_sync, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val cbSync = view.findViewById<CheckBox>(R.id.cbSync)
        val sbCardCN = view.findViewById<SeekBar>(R.id.sbCardCN)
        cbSync.setOnCheckedChangeListener { _, isChecked ->
            viewModel.paperSyncCorner.value = isChecked
        }
        sbCardCN.setOnSeekBarChangeListener(object : OnSeekBarChangeAdapter() {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                viewModel.paperCorner.value = progress.toFloat()
            }
        })
    }
}
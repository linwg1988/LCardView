package www.linwg.org.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import www.linwg.org.lib.LCardView

class BottomBookStyleSettingFragment : Fragment() {

    private val viewModel: DemoViewModel by lazy {
        ViewModelProvider(requireActivity())[DemoViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_book_style_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val sbAngle = view.findViewById<SeekBar>(R.id.sbAngle)
        val cbBook = view.findViewById<CheckBox>(R.id.cbBook)
        cbBook.setOnCheckedChangeListener { _, isChecked ->
            viewModel.linearBookEffect.value = isChecked
        }
        viewModel.curveShadowEffect.observe(viewLifecycleOwner) {
            if (it) {
                cbBook.isChecked = false
            }
        }
        sbAngle.setOnSeekBarChangeListener(object : OnSeekBarChangeAdapter() {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                viewModel.bookRadius.value = progress / 100f
            }
        })
    }
}
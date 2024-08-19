package www.linwg.org.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

class BottomMeshSettingFragment : Fragment() {

    private val viewModel: DemoViewModel by lazy {
        ViewModelProvider(requireActivity())[DemoViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_mesh_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val cbMesh = view.findViewById<CheckBox>(R.id.cbMesh)
        val sbCur = view.findViewById<SeekBar>(R.id.sbCur)
        cbMesh.setOnCheckedChangeListener { _, isChecked ->
            viewModel.curveShadowEffect.value = isChecked
        }
        viewModel.linearBookEffect.observe(viewLifecycleOwner) {
            if (it) {
                cbMesh.isChecked = false
            }
        }
        sbCur.setOnSeekBarChangeListener(object : OnSeekBarChangeAdapter() {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                viewModel.curvature.value = progress / 10f
            }
        })
    }
}
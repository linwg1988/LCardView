package www.linwg.org.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import www.linwg.org.lib.LCardView

class ShadowShapeSettingFragment : Fragment() {

    private val viewModel: DemoViewModel by lazy {
        ViewModelProvider(requireActivity())[DemoViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_shadow_shape_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val rgShape = view.findViewById<RadioGroup>(R.id.rgShape)
        viewModel.shadowFluidShape.observe(viewLifecycleOwner) {
            if (it == LCardView.ADSORPTION) {
                rgShape.check(R.id.rbOne)
            } else {
                rgShape.check(R.id.rbTwo)
            }
        }
        rgShape.setOnCheckedChangeListener { _, checkedId ->
            viewModel.shadowFluidShape.value =
                if (checkedId == R.id.rbOne) LCardView.ADSORPTION else LCardView.LINEAR
        }
    }
}
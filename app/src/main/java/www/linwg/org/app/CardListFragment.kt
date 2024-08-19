package www.linwg.org.app

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class CardListFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_card_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<View>(R.id.tvToList1).setOnClickListener { startActivity(Intent(requireActivity(), CardListActivity::class.java)) }
        view.findViewById<View>(R.id.tvToList2).setOnClickListener { startActivity(Intent(requireActivity(), TestActivity::class.java)) }
    }
}
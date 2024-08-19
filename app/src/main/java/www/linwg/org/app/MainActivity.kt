package www.linwg.org.app

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import www.linwg.org.lib.LCardView

class MainActivity : AppCompatActivity() {
    private val viewModel :DemoViewModel by lazy {
        ViewModelProvider(this)[DemoViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val cardBg = findViewById<LCardView>(R.id.cardView)

        viewModel.shadowFluidShape.observe(this){
            cardBg.setShadowFluidShape(it)
        }
        viewModel.shadowStartAlpha.observe(this){
            cardBg.setShadowAlpha(it)
        }
        viewModel.shadowColor.observe(this){
            cardBg.setShadowColor(it)
        }
        viewModel.shadowSize.observe(this){
            cardBg.setShadowSize(it)
        }
        viewModel.leftOffset.observe(this){
            cardBg.setLeftOffset(it)
        }
        viewModel.rightOffset.observe(this){
            cardBg.setRightOffset(it)
        }
        viewModel.topOffset.observe(this){
            cardBg.setTopOffset(it)
        }
        viewModel.bottomOffset.observe(this){
            cardBg.setBottomOffset(it)
        }
        viewModel.shadowOffsetCenter.observe(this){
            cardBg.setShadowOffsetCenter(it)
        }
        viewModel.leftTopCornerRadius.observe(this){
            cardBg.setLeftTopCornerRadius(it)
        }
        viewModel.rightTopCornerRadius.observe(this){
            cardBg.setRightTopCornerRadius(it)
        }
        viewModel.rightBottomCornerRadius.observe(this){
            cardBg.setRightBottomCornerRadius(it)
        }
        viewModel.leftBottomCornerRadius.observe(this){
            cardBg.setLeftBottomCornerRadius(it)
        }
        viewModel.cornerRadius.observe(this){
            cardBg.setCornerRadius(it)
        }
        viewModel.paperCorner.observe(this){
            cardBg.setPaperCorner(it)
        }
        viewModel.paperSyncCorner.observe(this){
            cardBg.setPaperSyncCorner(it)
        }
        viewModel.curveShadowEffect.observe(this){
            cardBg.setCurveShadowEffect(it)
        }
        viewModel.curvature.observe(this){
            cardBg.setCurvature(it)
        }
        viewModel.linearBookEffect.observe(this){
            cardBg.setLinearBookEffect(it)
        }
        viewModel.bookRadius.observe(this){
            cardBg.setBookRadius(it)
        }
        viewModel.gradientSizeFollowView.observe(this){
            cardBg.setGradientSizeFollowView(it)
        }
        viewModel.gradientDirection.observe(this){
            cardBg.setGradientDirection(it)
        }
        viewModel.gradientColors.observe(this){
            cardBg.setGradientColors(*it)
        }
        viewModel.strokeWidth.observe(this){
            cardBg.setStrokeWidth(it)
        }
        viewModel.cardBackgroundDrawableRes.observe(this){
            cardBg.setCardBackgroundDrawableRes(it)
        }
        viewModel.cardBackground.observe(this){
            cardBg.setCardBackground(it)
        }
        viewModel.leftWidthDecrement.observe(this){
            cardBg.setLeftShadowDecrement(it)
        }
        viewModel.topWidthDecrement.observe(this){
            cardBg.setTopShadowDecrement(it)
        }
        viewModel.rightWidthDecrement.observe(this){
            cardBg.setRightShadowDecrement(it)
        }
        viewModel.bottomWidthDecrement.observe(this){
            cardBg.setBottomShadowDecrement(it)
        }
        viewModel.shadowSize.value = cardBg.getShadowSize()

        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val viewPager = findViewById<ViewPager2>(R.id.viewPager)
        tabLayout.tabMode = TabLayout.MODE_SCROLLABLE

        val fragments = ArrayList<Fragment>()
        fragments.add(CornerSettingFragment())
        fragments.add(BottomMeshSettingFragment())
        fragments.add(BottomBookStyleSettingFragment())
        fragments.add(CornerSyncFragment())
        fragments.add(ShadowOffsetSettingFragment())
        fragments.add(ShadowShapeSettingFragment())
        fragments.add(ShadowSizeSettingFragment())
        fragments.add(ShadowColorSettingFragment())
        fragments.add(CardBgSettingFragment())
        fragments.add(CardListFragment())

        addTab("圆角设置",tabLayout)
        addTab("底部扭曲阴影设置",tabLayout)
        addTab("底部书本阴影设置",tabLayout)
        addTab("圆角同步设置",tabLayout)
        addTab("阴影偏移设置",tabLayout)
        addTab("阴影形状设置",tabLayout)
        addTab("阴影大小设置",tabLayout)
        addTab("阴影颜色设置",tabLayout)
        addTab("卡片背景设置",tabLayout)
        addTab("卡片列表",tabLayout)

        viewPager.offscreenPageLimit = 10
        viewPager.adapter = object:FragmentStateAdapter(this){
            override fun getItemCount(): Int {
                return 10
            }

            override fun createFragment(position: Int): Fragment {
                return fragments[position]
            }
        }

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                tab.customView?.findViewById<TextView>(android.R.id.text1)?.setTextColor(Color.parseColor("#3269F6"))
                viewPager.setCurrentItem(tab.position, false)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                tab.customView?.findViewById<TextView>(android.R.id.text1)?.setTextColor(Color.parseColor("#212121"))
            }
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        viewPager.registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                tabLayout.getTabAt(position)?.select()
            }
        })
    }

    private fun addTab(s: String, tabLayout: TabLayout) {
        val tab = tabLayout.newTab()
        tab.tag = s
        tab.customView = AppCompatTextView(this).apply {
            id = android.R.id.text1
            text = s
            setTextColor(Color.parseColor("#212121"))
            gravity = Gravity.CENTER
            textSize = 15f
        }
        tab.text = s
        tabLayout.addTab(tab)
    }
}
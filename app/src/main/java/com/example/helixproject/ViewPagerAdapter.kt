package com.example.helixproject

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    private val fragments = arrayListOf<Fragment>()
    private val titles = arrayListOf<String>()

    fun addFragment(fragment: Fragment, title: String) {
        fragments.add(fragment)
        titles.add(title)
    }

    override fun getCount() = 3

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        super.destroyItem(container, position, `object`)
    }

    override fun getItem(position: Int): Fragment {

        return when(position) {

            0       ->  Fragment1()

            1       ->  Fragment2()

            else       ->  Fragment3()

        }

    }

    //override fun getPageTitle(position: Int): CharSequence? {
    //    return titles[position]
    //}
}

package com.mandeep.singletonflow

import android.graphics.Canvas
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mandeep.singletonflow.adapter.MyAdapter
import com.mandeep.singletonflow.databinding.ActivityMain2Binding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity2 : AppCompatActivity() {

    lateinit var binding:ActivityMain2Binding
    lateinit var adapter:MyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding = ActivityMain2Binding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        val arrayList = ArrayList<String>()

         adapter = MyAdapter(this)
        val linearLayoutManager = LinearLayoutManager(this)
        binding.recyclervIew.layoutManager = linearLayoutManager
        binding.recyclervIew.adapter = adapter

        CoroutineScope(Dispatchers.Main).launch {
            for(i in 0..50)
            {
                arrayList.add(System.currentTimeMillis().toString())
                delay(300)
                adapter.setData(arrayList)
                binding.recyclervIew.scrollToPosition(arrayList.size-1)
            }
        }

        var headerView: View? = null
        var header: ConstraintLayout? = null
        var text: TextView? = null

        binding.recyclervIew.addItemDecoration(object:RecyclerView.ItemDecoration() {
            override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                super.onDrawOver(c!!, parent, state!!)

                /**Inflating and initializing views of Header Layout*/
                if (headerView == null) {
                    headerView = inflateHeaderView(parent)

                    header = headerView?.findViewById<View>(R.id.constraintLayout) as ConstraintLayout
                    text = headerView?.findViewById<View>(R.id.timeHeader) as TextView

                    fixLayoutSize(headerView!!, parent)
                }
                /**finished Header Layout*/

                var previousHeader: CharSequence = ""

                /** Getting view and its position from recycler View for getting specific time of item/child View one by one
                 *  and then drawing header if previousHeader is not same */
                for (i in 0 until parent.childCount) {


                    val child = parent.getChildAt(i)
                    val position = parent.getChildAdapterPosition(child)
                    val title: CharSequence = adapter.millisecondsToTime(arrayList[position].toLong())

                    text?.setText(title)

                    /**  drawing header View only if previous time title of previous header is not equal*/
                    if (previousHeader != title /*|| addAlphabets(arrayList)*/) {
                        /**drawing header*/
                        drawHeader(c, child, headerView!!)
                        previousHeader = title
                    }
                }
            }

            val headerOffset = resources.getDimensionPixelSize(androidx.constraintlayout.widget.R.dimen.notification_small_icon_size_as_large)

            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view!!, parent, state!!)
                val pos = parent.getChildAdapterPosition(view!!)
             //   if (addAlphabets(arrayList)) {
                    outRect.top = headerOffset
                //}
            }
        })
}

    /**drawing header on header layout using canvas*/
    fun drawHeader(c: Canvas, child: View, headerView: View) {
        c.save()
        //  if (sticky) {
        c.translate(0f, Math.max(0, child.top - headerView.height + 13).toFloat())
/*  }    else {
         c.translate(
             0f, (
                     child.top - headerView.height).toFloat()
         )
     }*/
        headerView.draw(c)
        c.restore()
    }

    /**inflating header layout here */
    fun inflateHeaderView(parent: RecyclerView): View? {
        return LayoutInflater.from(parent.context).inflate(R.layout.time_header, parent, false)
    }

    fun addAlphabets(list: ArrayList<String>): Boolean {
        var result = true
        var i = 0
        i = 0
        while (i < list.size - 1) {
            val name1 = list[i].toLong()
            val name2 = list[i + 1].toLong()
            if (adapter.millisecondsToTime(name1).equals(adapter.millisecondsToTime(name2))) {
                result = false
            } else {

            }
            i++
        }

        return result
    }

    /**
     * Measures the header view to make sure its size is greater than 0 and will be drawn
     * https://yoda.entelect.co.za/view/9627/how-to-android-recyclerview-item-decorations
     */
    fun fixLayoutSize(view: View, parent: ViewGroup) {
        val widthSpec = View.MeasureSpec.makeMeasureSpec(parent.width, View.MeasureSpec.EXACTLY)
        val heightSpec = View.MeasureSpec.makeMeasureSpec(parent.height, View.MeasureSpec.UNSPECIFIED)
        val childWidth = ViewGroup.getChildMeasureSpec(widthSpec, 0, view.layoutParams.width)
        val childHeight = ViewGroup.getChildMeasureSpec(heightSpec, 0, view.layoutParams.height)

        view.measure(childWidth, childHeight)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
    }

}
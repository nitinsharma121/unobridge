import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.viewpager.widget.PagerAdapter
import com.provider.unobridge.R
import com.provider.unobridge.data.StateData
import java.util.*

class ViewPagerAdapter(// Context object
    var context: Context, // Array of images
    var states: ArrayList<StateData>
) :
    PagerAdapter() {
    // Layout Inflater
    var mLayoutInflater: LayoutInflater
    override fun getCount(): Int {
        // return the number of images
        return states.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as CardView
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        // inflating the item.xml
        val itemView: View = mLayoutInflater.inflate(R.layout.state_item, container, false)


        val imageView = itemView.findViewById<View>(R.id.ivPic) as ImageView
        val title = itemView.findViewById<View>(R.id.tvStates) as TextView
        val description = itemView.findViewById<View>(R.id.tvDescription) as TextView
        imageView.setImageResource(states[position].image)
        title.text = states[position].stateName
        description.text ="Tap to see ${states[position].stateName} state rides"

        // Adding the View
        Objects.requireNonNull(container).addView(itemView)
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }

    // Viewpager Constructor
    init {
        states = states
        mLayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }
}
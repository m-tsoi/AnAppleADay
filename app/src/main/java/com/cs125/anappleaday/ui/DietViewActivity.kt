import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cs125.anappleaday.R
import com.cs125.anappleaday.data.recycler.DietViewAdapter

class DietViewActivity : AppCompatActivity() {
    // this activity displays what we have for this day

    private lateinit var recyclerMeals: RecyclerView
    private lateinit var dietViewAdapter: DietViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diet_view)

        // to get list of uhhh things we haev to access the firebase
        var tester: MutableList<String> = mutableListOf("cheese")
        tester.add("bread")

        recyclerMeals = findViewById<RecyclerView>(R.id.recyclerMeals)

        dietViewAdapter = DietViewAdapter(tester)
        recyclerMeals.adapter = dietViewAdapter

        recyclerMeals.layoutManager = LinearLayoutManager(this)

    }
}
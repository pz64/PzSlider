package customslider.pzy64.customslider

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import pzy64.pzslider.OnSeekListener
import pzy64.pzslider.PzSlider

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        slider.setOnSeekListener(object : OnSeekListener {
            override fun onProgressStarted(slider: PzSlider, progress: Int) {
                b.text = progress.toString()
            }
            override   fun onProgressChanged(slider: PzSlider, progress: Int) {
                a.text =progress.toString()
            }
            override fun onProgressCompleted(slider: PzSlider, progress: Int) {
                c.text = progress.toString()
            }
        })
    }
}

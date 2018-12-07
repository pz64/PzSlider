package customslider.pzy64.customslider

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import customslider.pzy64.customslider.slider.CustomSlider
import customslider.pzy64.customslider.slider.OnSeekListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        slider.setOnSeekListener(object : OnSeekListener{
            override fun onProgressChanged(slider: CustomSlider, progress: Int) {

            }
        })
    }
}

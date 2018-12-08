package pzy64.pzslider

interface OnSeekListener {
    fun onProgressChanged(slider: PzSlider, progress: Int)
    fun onProgressCompleted(slider: PzSlider, progress: Int)
    fun onProgressStarted(slider: PzSlider, progress: Int)
}

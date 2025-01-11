package com.glucode.about_you.about.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.ColorInt
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.glucode.about_you.R
import com.glucode.about_you.databinding.ViewAnswerCardBinding

class AnswerCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : CardView(context, attrs, defStyleAttr) {
    private val binding: ViewAnswerCardBinding =
        ViewAnswerCardBinding.inflate(LayoutInflater.from(context), this)
    @ColorInt
    private val selectedCardBackgroundColor: Int
    @ColorInt
    private val selectedTextColor: Int
    @ColorInt
    private val deselectedTextColor: Int

    var title: String? = null
        set(value) {
            field = value
            binding.title.text = value
        }

    init {
        val whiteColour = ContextCompat.getColor(context, R.color.white)

        // both the background and text were black so you couldnt see the selected answer
        val purpleColour = ContextCompat.getColor(context, R.color.purple_500)

        // default was black, I updated to automatically be white - The preselected answer is not visible.
        selectedCardBackgroundColor = whiteColour

        // default was black, I updated to automatically be purple - When changing the answer to a question no selection is visible.
        selectedTextColor = purpleColour

        deselectedTextColor = whiteColour
        radius = resources.getDimension(R.dimen.corner_radius_normal)
        elevation = resources.getDimension(R.dimen.elevation_normal)
        setCardBackgroundColor(null)
    }

//Method to change answers (text & background) when their selected.
    override fun setSelected(selected: Boolean) {
        super.setSelected(selected)
        if (selected) {
            setCardBackgroundColor(selectedCardBackgroundColor)
            binding.title.setTextColor(selectedTextColor)
        } else {
            setCardBackgroundColor(null)
            binding.title.setTextColor(deselectedTextColor)
        }
    }

}

package io.devmartynov.spiice.ui.shared

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.res.getStringOrThrow
import io.devmartynov.spiice.R
import io.devmartynov.spiice.databinding.ViewBannerBinding

typealias OnClick = () -> Unit

/**
 * Кастомный баннер.
 */
class BannerView(context: Context, attributes: AttributeSet? = null) : LinearLayout(context, attributes) {
    private var mode = BannerMode.SUCCESS
    private var isVisible = false
    private val defaultCloseTimeout = 3000
    private var closeTimeout = defaultCloseTimeout // TODO закрытие по таймеру
    private var binding: ViewBannerBinding

    init {
        binding = ViewBannerBinding.inflate(LayoutInflater.from(context), this, true)
        setIsVisible(isVisible = isVisible, force = true)
        context.theme.obtainStyledAttributes(
            attributes,
            R.styleable.BannerView,
            0,
            0
        ).apply {
            try {
                binding.message.text = getStringOrThrow(R.styleable.BannerView_text)
                closeTimeout = getInt(R.styleable.BannerView_close_timeout, defaultCloseTimeout)
                val mode = BannerMode.values()[getInt(R.styleable.BannerView_mode, 0)]
                setMode(mode, force = true)
            } finally {
                recycle()
            }
        }
    }

    fun setText(text: String): BannerView {
        binding.message.text = text;
        return this;
    }

    fun setMode(mode: BannerMode, force: Boolean = false): BannerView {
        if (this.mode != mode || force) {
            this.mode = mode
            val imageResource = if (mode == BannerMode.SUCCESS) {
                R.drawable.ic_expired_green
            } else {
                R.drawable.ic_expired
            }
            binding.mode.setImageResource(imageResource)
        }
        return this;
    }

    fun setCloseClick(onClick: OnClick ): BannerView {
        binding.close.setOnClickListener {
            onClick()
        }
        return this;
    }

    private fun setIsVisible(isVisible: Boolean, force: Boolean = false) {
        if (this.isVisible != isVisible || force) {
            this.isVisible = isVisible
            this.visibility = if (isVisible) View.VISIBLE else View.GONE
        }
    }

    /**
     * Показать баннер
     */
    fun show() {
        setIsVisible(true)
    }

    /**
     * Скрыть баннер
     */
    fun hide() {
        setIsVisible(false)
    }

    enum class BannerMode {
        SUCCESS, WARNING
    }
}

package com.example.mirela.rxjava.adapters

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mirela.rxjava.Myadapter
import com.example.mirela.rxjava.viewModel.SchoolViewModel

object BindingAdapter {
    @JvmStatic
    @BindingAdapter("android:items")
    fun setItems(view: RecyclerView, items: MutableLiveData<List<SchoolViewModel>>?) {
        val adapter = view.adapter
        if (adapter == null || items == null) {
            return
        }
        if (adapter is Myadapter) {
            adapter.notifChanges(items.value)
            Log.e("set items adapter ", items.value?.size.toString())
        }
    }

    @JvmStatic
    @BindingAdapter("android:text")
    fun setText(textView: TextView, @StringRes text: Int) {
        if (text != 0) {
            textView.text = textView.resources.getString(text)
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["android:url", "android:placeHolder", "android:errorPlaceHolder"], requireAll = false)
    fun setImageUrl(view: ImageView, url: String?, placeHolder: Int?, errorPlaceHolder: Int?) {
        setImageUrl(view,
            url,
            placeHolder?.run { ContextCompat.getDrawable(view.context, placeHolder) },
            errorPlaceHolder?.run { ContextCompat.getDrawable(view.context, errorPlaceHolder) })
    }

    @JvmStatic
    @BindingAdapter(value = ["android:url", "android:placeHolder", "android:errorPlaceHolder"], requireAll = false)
    fun setImageUrl(view: ImageView, url: String?, placeHolder: Drawable?, errorPlaceHolder: Drawable?) {
        if (!url.isNullOrBlank()) {
            val scaleType = view.scaleType
            var requestOptions = if (scaleType == ImageView.ScaleType.CENTER_CROP) {
                RequestOptions().centerCrop()
            } else {
                RequestOptions().centerInside()
            }
            placeHolder?.let {
                requestOptions = requestOptions.fallback(it)
            }
            errorPlaceHolder?.let {
                requestOptions.error(it)
            }
            view.visibility = View.VISIBLE
            Glide.with(view.context).load(url).apply(requestOptions.dontAnimate()).into(view)
        } else if (placeHolder != null) {
            view.visibility = View.VISIBLE
            view.setImageDrawable(placeHolder)
        } else {
            view.visibility = View.GONE
        }
    }
}
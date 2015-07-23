package com.site.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class PageScrollView extends ScrollView {
	private float a;
	private float b;
	private float c;
	private float d;

	public PageScrollView(Context paramContext, AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
	}

	public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent) {
		return false;

	}
}
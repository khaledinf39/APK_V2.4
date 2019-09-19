package com.mpos.utils;

import android.content.Context;
import android.graphics.Typeface;

public class TypefaceManager {

	/*
	 * Permissible values ​​for the "typeface" attribute.
	 */
	private final static int INTERSTATE = 0;
	
	
	/**
	 * Create typeface from assets.
	 * 
	 * @param context
	 *            The Context the view is running in, through which it can
	 *            access the current theme, resources, etc.
	 * @param typefaceValue
	 *            values ​​for the "typeface" attribute
	 * @return Roboto {@link Typeface}
	 * @throws IllegalArgumentException
	 *             if unknown `typeface` attribute value.
	 */
	public static Typeface createTypeface(Context context, int typefaceValue)
			throws IllegalArgumentException {
		Typeface typeface;
		switch (typefaceValue) {
		case INTERSTATE:
			typeface = Typeface.createFromAsset(context.getAssets(), "Interstate.ttf");
			break;
		default:
			throw new IllegalArgumentException("Unknown `typeface` attribute value "
					+ typefaceValue);
		}
		return typeface;
	}
}

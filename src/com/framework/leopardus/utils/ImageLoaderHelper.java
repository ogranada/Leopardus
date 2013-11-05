package com.framework.leopardus.utils;

import java.io.File;

import android.content.Context;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.DiscCacheUtil;
import com.nostra13.universalimageloader.core.assist.MemoryCacheUtil;
import com.nostra13.universalimageloader.utils.StorageUtils;

public class ImageLoaderHelper {

	private ImageLoaderConfiguration config;
	private static ImageLoaderHelper loader;
	private ImageLoader ildr;

	public static ImageLoaderHelper getInstance(Context ctx) {
		if (loader == null) {
			loader = new ImageLoaderHelper(ctx);
		}
		return loader;
	}

	public ImageLoaderHelper(Context ctx) {
		File cacheDir = StorageUtils.getCacheDirectory(ctx);
		ildr = ImageLoader.getInstance();
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.cacheInMemory(true)
				.cacheOnDisc(true)
				.build();
		config = new ImageLoaderConfiguration.Builder(ctx)
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				// default
				.discCache(new UnlimitedDiscCache(cacheDir))
				.defaultDisplayImageOptions(defaultOptions).writeDebugLogs()
				.build();
		ildr.init(config);

	}

	public void RemoveFromDiscCache(String uri) {
		DiscCacheUtil.removeFromCache(uri, ildr.getDiscCache());
	}

	public void RemoveFromMemoryCache(String uri) {
		MemoryCacheUtil.removeFromCache(uri, ildr.getMemoryCache());
	}
	
	public void display(String path, ImageView iv) {
		ildr.displayImage(path, iv);
	}

}

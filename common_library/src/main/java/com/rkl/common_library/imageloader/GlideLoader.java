package com.rkl.common_library.imageloader;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;
import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.rkl.common_library.util.EmptyUtils;
import com.rkl.common_library.util.FileUtils;
import com.rkl.common_library.util.LogUtils;
import com.rkl.common_library.util.StringUtils;
import java.io.File;
import static com.rkl.common_library.base.BaseApplication.mAppContext;

/**
 * Created by shy' on 2017/3/7.
 * 第三方图片加载框架封装
 * 依据开源框架Glide版本 包括本地图片加载 网络图片加载 图片缓存管理(路径以及清理)
 */
public class GlideLoader {

    private static GlideLoader ImageManagerer;
    private static final String tag = "ImageManager";
    private File cacheLocation;

    /**
     * 获取ImageManagerer的实例
     * @return  图片管理实例
     */
    public static GlideLoader getInstance() {
        if (ImageManagerer == null) {
            synchronized (GlideLoader.class) {
                if (ImageManagerer == null) {
                    ImageManagerer = new GlideLoader();
                }
            }
        }
        return ImageManagerer;
    }

    /*
    * 设置图片缓存路径和大小
    * */
    private GlideLoader() {
        GlideBuilder builder = new GlideBuilder(mAppContext);
        builder.setDiskCache(new DiskCache.Factory() {
            @Override
            public DiskCache build() {
                // 设置当前图片缓存路径,最大缓存尺寸100M
                cacheLocation = new File(mAppContext.getCacheDir(), "image_cache_dir");
                if (!cacheLocation.exists()){
                    cacheLocation.mkdirs();
                }
                return DiskLruCacheWrapper.get(cacheLocation, 1024 * 1024 * 100);
            }
        });
        if (!Glide.isSetup()) {
            Glide.setup(builder);
        }
    }

    /*
    * 传过来的三种对象 Fragment、Activity、Context
    * */
    public RequestManager setContext(Object object) {
        if (object instanceof Fragment) {
            return baseGlide((Fragment) object);
        } else if (object instanceof Activity) {
            return baseGlide((Activity) object);
        } else if (object instanceof Context) {
            return baseGlide((Context) object);
        }
        return baseGlide(mAppContext);
    }

/** ---------------  加载图片方式，可根据实际需求进行添加修改  ---------------**/
    /**
     * String参数加载
     * @param object
     * @param url
     * @return
     */
    private DrawableTypeRequest<String> baseGlide(Object object, String url) {
        return setContext(object).load(url);
    }

    /**
     * 资源文件加载
     * @param object
     * @param resourceId
     * @return
     */
    private DrawableTypeRequest<Integer> baseGlide(Object object, int resourceId) {
        return setContext(object).load(resourceId);
    }

    /**
     * 本地文件加载
     * @param object
     * @param file
     * @return
     */
    private DrawableTypeRequest<File> baseGlide(Object object, File file) {
        return setContext(object).load(file);
    }

    /**
     * Uri加载
     * @param object
     * @param uri
     * @return
     */
    private DrawableTypeRequest<Uri> baseGlide(Object object, Uri uri) {
        return setContext(object).load(uri);
    }

    //根据传入参数Activity/Fragment的生命周期保持一致，去暂停和执行图片加载
    private RequestManager baseGlide(Context context) {
        return Glide.with(context);
    }
    private RequestManager baseGlide(Activity activity) {
        return Glide.with(activity);
    }
    private RequestManager baseGlide(Fragment fragment) {
        return Glide.with(fragment);
    }
    /**
     * 基本加载，缓存处理后的图片到本地
     * @param object    Activity Fragment Context (传递对应的,方便Glide管理生命周期)
     * @param url 网络图片地址或者本地图片地址
     * @param imageView
     * @param defImgId     占位的图片资源id,没有传0
     * @param errImgId     图片加载错误显示的id,没有传0
     */
    public void displayImage(Object object, String url, ImageView imageView, int defImgId, int errImgId) {
        if (EmptyUtils.compareWithNull(object, imageView))
            return;
        baseGlide(object, url)
                .placeholder(defImgId)
                .error(errImgId)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(imageView);
    }

    /**
     * Glide原图加载
     * @param context
     * @param url
     * @param imageView
     */
    public void displayImageByRealSize(Object context, String url, final ImageView imageView){
        if (EmptyUtils.compareWithNull(context, imageView))
            return;
        baseGlide(context,url)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                        imageView.setImageBitmap(bitmap);
                    }
                });
    }

    /**
     * Glide原图加载并且显示圆角图片
     * @param context
     * @param url
     * @param imageView
     * @param radiusPx 圆角半径(px)
     */
    public void displayImageByRealSizeAndTransfrom(Object context, String url, final ImageView imageView, final int radiusPx){
        if (EmptyUtils.compareWithNull(context, imageView))
            return;
        baseGlide(context,url)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap,
                                                GlideAnimation<? super Bitmap> glideAnimation) {
                        Bitmap bitmap1 = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(), Bitmap.Config.ARGB_8888);
                        Canvas canvas = new Canvas(bitmap1);
                        RectF rect = new RectF(0,0,bitmap.getWidth(),bitmap.getHeight());
                        Paint paint = new Paint();
                        paint.setAntiAlias(true);  //抗锯齿效果
                        paint.setColor(Color.RED);
                        canvas.drawRoundRect(rect,radiusPx,radiusPx,paint);
                        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                        canvas.drawBitmap(bitmap,0,0,paint);
                        //显示圆角图片
                        imageView.setImageBitmap(bitmap1);
                    }
                });
    }

    /**
     * 设置跳过内存缓存,和磁盘缓存
     */
    public void displayImageSkipCache(Object object, String url, ImageView imageView, int defImgId, int errImgId) {
        if (EmptyUtils.compareWithNull(object, imageView))
            return;
        baseGlide(object, url)
                .placeholder(defImgId)
                .error(errImgId)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imageView);
    }

    /**
     * 自定义图片转换器,可以参考圆形图片和圆角图片的写法(保存的图片为转换后的)
     * @param object
     * @param url
     * @param imageView
     * @param trans     图片转换器
     */
    public void displayImageByTransfrom(Object object, String url, ImageView imageView, BitmapTransformation trans) {
        if (EmptyUtils.compareWithNull(object, imageView, trans))
            return;
        baseGlide(object, url)
                .transform(trans)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(imageView);
    }

    /**
     * 加载
     * @param object
     * @param url
     * @param imageView
     * @param trans
     * @param errorRes
     * @param isCache 是否做缓存操作
     */
    public void displayImageByTransfrom(Object object, String url, ImageView imageView, BitmapTransformation trans, int errorRes, boolean isCache) {
        if (EmptyUtils.compareWithNull(object, imageView, trans))
            return;
        baseGlide(object, url)
                .diskCacheStrategy(isCache?DiskCacheStrategy.RESULT:DiskCacheStrategy.NONE)
                .skipMemoryCache(isCache)
                .transform(trans)
                .error(errorRes)
                .into(imageView);
    }

    /**
    /*
    * 自定义图片转换器,无缓存加载图片
    * */
    public void displayImageWithOutCache(Object object, String url, ImageView imageView, BitmapTransformation trans) {
        if (EmptyUtils.compareWithNull(object, imageView, trans))
            return;
        baseGlide(object, url)
                .transform(trans)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);
    }




    /**
     * 本地不做缓存的图片加载
     * @param object
     * @param url
     * @param imageView
     * @param defID
     * @param errID
     */
    public void displayImageWithOutCache(Object object, String url, ImageView imageView, int defID, int errID) {
        if (EmptyUtils.compareWithNull(object, imageView))
            return;
        baseGlide(object, url)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(defID)
                .error(errID)
                .into(imageView);
    }

    /**
     * 本地不做缓存的图片加载,添加加载状态的监听
     * @param object
     * @param url
     * @param imageView
     * @param defID
     * @param errID
     */
    public void displayImageWithOutCache(Object object, String url, GlideDrawableImageViewTarget imageView, int defID, int errID) {
        if (EmptyUtils.compareWithNull(object, imageView))
            return;
        baseGlide(object, url)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(defID)
                .error(errID)
                .into(imageView);
    }

    /**
     *
     * @param object
     * @param url
     * @param imageView
     * @param failID
     * @param targetType  加载后的图片显示ScaleType
     */
    public void displayImageWithOutCache(Object object, String url, final ImageView imageView, int failID, final ImageView.ScaleType targetType) {
        if (EmptyUtils.compareWithNull(object, imageView))
            return;
        GlideDrawableImageViewTarget glideDrawableImageViewTarget=new GlideDrawableImageViewTarget(imageView){
            /**
             * {@inheritDoc}
             * If no {@link GlideAnimation} is given or if the animation does not set the
             * {@link Drawable} on the view, the drawable is set using
             * {@link ImageView#setImageDrawable(Drawable)}.
             *
             * @param resource  {@inheritDoc}
             * @param animation {@inheritDoc}
             */
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                super.onResourceReady(resource, animation);
                imageView.setScaleType(targetType);
            }
        };
        baseGlide(object, url)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(failID)
                .error(failID)
                .into(glideDrawableImageViewTarget);
    }
    /**
     *
     * @param object
     * @param url
     * @param imageView
     * @param failID
     * @param targetType  加载后的图片显示ScaleType
     */
    public void displayImageWithScaleType(Object object, String url, final ImageView imageView, int failID, final ImageView.ScaleType targetType, boolean isCache) {
        if (EmptyUtils.compareWithNull(object, imageView))
            return;
        GlideDrawableImageViewTarget glideDrawableImageViewTarget=new GlideDrawableImageViewTarget(imageView){
            /**
             * {@inheritDoc}
             * If no {@link GlideAnimation} is given or if the animation does not set the
             * {@link Drawable} on the view, the drawable is set using
             * {@link ImageView#setImageDrawable(Drawable)}.
             *
             * @param resource  {@inheritDoc}
             * @param animation {@inheritDoc}
             */
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                super.onResourceReady(resource, animation);
                imageView.setScaleType(targetType);
            }
        };
        baseGlide(object, url).skipMemoryCache(isCache).diskCacheStrategy(isCache?DiskCacheStrategy.ALL:DiskCacheStrategy.NONE).placeholder(failID).error(failID).into(glideDrawableImageViewTarget);
    }
    /**
     * 加载图片资源
     * @param object
     * @param id
     * @param imageView
     * @param defImgId
     * @param errImgId
     */
    public void displayImageById(Object object, int id, ImageView imageView, int defImgId, int errImgId) {
        if (EmptyUtils.compareWithNull(object, imageView))
            return;
        baseGlide(object, id).placeholder(defImgId).error(errImgId).into(imageView);
    }

    /**
     * 判断图片资源，本地存在加载本地，否则加载网络图片资源
     * @param object
     * @param defUrl    本地存储路径
     * @param url
     * @param imageView
     * @param defImgId
     * @param errImgId
     */
    public void displayImage(Object object, String defUrl, String url, ImageView imageView, int defImgId, int errImgId) {
        if (EmptyUtils.compareWithNull(object, imageView))
            return;
        if (!StringUtils.isEmpty(defUrl) && FileUtils.isFileExists(defUrl)) {
            displayImage(object, defUrl, imageView, defImgId, errImgId);
        } else {
            displayImage(object, url, imageView, defImgId, errImgId);
        }
    }

    /**
     * 加载图片直接转换为bitmap
     * @param object
     * @param url
     * @param simpleTarget  下载完成的对象
     */
    public void downloadImage(Object object, String url, SimpleTarget<Bitmap> simpleTarget) {
        if (EmptyUtils.compareWithNull(object, simpleTarget))
            return;
        baseGlide(object, url).asBitmap().into(simpleTarget);
    }

    /**
     * 下载图片文件,提供一个最简单的实现BaseFileTarget抽象类(文件下载完成,文件下载失败)
     * @param object
     * @param url
     * @param target
     */
    public void downloadImage4File(Object object, String url, Target<File> target) {
        if (EmptyUtils.compareWithNull(object, target))
            return;
        baseGlide(object, url).downloadOnly(target);
    }

    /**
     * 下载图片,并获取下载的状态(可以显示加载状态等)
     * @param object
     * @param url
     * @param imageView
     * @param requestListener   下载状态的listener
     * @param defImgId
     * @param errImgId
     */
    public void displayImageByStatus(Object object, String url, ImageView imageView, RequestListener<String, GlideDrawable> requestListener, int defImgId, int errImgId) {
        if (EmptyUtils.compareWithNull(object, imageView, requestListener))
            return;
        DrawableTypeRequest<String> load = baseGlide(object, url);
        load.placeholder(defImgId).error(errImgId).listener(requestListener).into(imageView);
    }


    /**
     * 照片墙
     *
     * @param context
     * @param url
     * @param view
     * @param defID
     * @param errID
     */
    public void displayPhotoWall(Context context, String url, ImageView view, int defID, int errID) {
        if (EmptyUtils.compareWithNull(context, view))
            return;
        baseGlide(context, url).centerCrop().diskCacheStrategy(DiskCacheStrategy.NONE).// 设置本地不缓存
                placeholder(defID).error(errID).crossFade().into(view);
    }

    /**
     * 加载GIf图片
     * @param object
     * @param url       Gif路径
     * @param imageView
     * @param defID
     * @param errID
     */
    public void displayImageWithGif(Object object, String url, ImageView imageView, int defID, int errID) {
        if (EmptyUtils.compareWithNull(object, imageView))
            return;
        baseGlide(object, url).asGif().placeholder(defID).error(errID).into(imageView);
    }

    /**
     * 停请求(listview滑动时可以调用等等)
     * @param object
     */
    public void pauseRequsts(Object object) {
        if (EmptyUtils.compareWithNull(object))
            return;
        setContext(object).pauseRequests();
    }

    /**
     * 恢复请求
     *
     * @param object Activity Fragment Context (传递对应的,方便Glide管理生命周期)
     */
    public void resumeRequests(Object object) {
        if (EmptyUtils.compareWithNull(object))
            return;
        setContext(object).resumeRequests();
    }

    /**
     * 根据url判断是否是gif，然后显示
     *
     * @param object
     * @param url
     * @param imageView
     * @param gifDefID
     * @param gifErrID
     * @param defID
     * @param errID
     */
    public void showGifOrOther(Object object, String url, ImageView imageView, int gifDefID, int gifErrID, int defID, int errID) {
        LogUtils.e(tag, url + "url");
        if ("gif".equals(url.substring(url.length() - 3))) {
            GlideLoader.getInstance().displayImageWithGif(this, url, imageView, gifDefID, gifErrID);
        } else {
            GlideLoader.getInstance().displayImage(this, url, imageView, defID, errID);
        }
    }

    /**
     * 手动清理内存缓存
     */
    public void clearMemoryChace(Context context) {
        if (EmptyUtils.compareWithNull(context))
            return;
        Glide.get(context).clearMemory();
    }

    /**
     * 清理本地缓存文件,慎用!!
     */
    public void clearDiskChace(final Context context) {
        if (EmptyUtils.compareWithNull(context))
            return;
        new Thread() {
            @Override
            public void run() {
                Glide.get(context).clearDiskCache();
            }
        }.start();

    }

}

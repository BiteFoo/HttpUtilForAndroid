package com.hss01248.net.builder;

import android.app.Activity;

/**
 * Created by Administrator on 2017/1/18 0018.
 */

public class ProgressBaseBuilder<T> extends BaseNetBuilder {

    public boolean isLoadingDialogHorizontal;
    public boolean updateProgress ;

    public ProgressBaseBuilder(){
        super();
        isLoadingDialogHorizontal = true;
        updateProgress = true;
    }

    public ProgressBaseBuilder<T> showLoadingDialog(Activity activity, String loadingMsg, boolean updateProgress, boolean horizontal){
        isLoadingDialogHorizontal = horizontal;
        return (ProgressBaseBuilder<T>) setShowLoadingDialog(null,loadingMsg,activity,updateProgress,horizontal);
    }



    @Override
    public ProgressBaseBuilder showLoadingDialog(Activity activity, String loadingMsg) {
        return (ProgressBaseBuilder) setShowLoadingDialog(null,loadingMsg,activity,updateProgress,isLoadingDialogHorizontal);
    }

}

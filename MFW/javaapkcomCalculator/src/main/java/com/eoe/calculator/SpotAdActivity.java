package com.eoe.calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import net.youmi.android.nm.cm.ErrorCode;
import net.youmi.android.nm.sp.SpotListener;
import net.youmi.android.nm.sp.SpotManager;

/**
 * 插屏广告演示窗口
 *
 * @author Alian Lee
 * @since 2016-11-25
 */
public class SpotAdActivity extends BaseActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_spot_ad);
		
		// 设置插屏广告
		setupSpotAd();
	}
	
	/**
	 * 设置插屏广告
	 */
	private void setupSpotAd() {
		// 设置插屏图片类型，默认竖图
		//		// 横图
		//		SpotManager.getInstance(mContext).setImageType(SpotManager
		// .IMAGE_TYPE_HORIZONTAL);
		// 竖图
		SpotManager.getInstance(mContext).setImageType(SpotManager.IMAGE_TYPE_VERTICAL);

		// 设置动画类型，默认高级动画
		//		// 无动画
		//		SpotManager.getInstance(mContext).setAnimationType(SpotManager
		//				.ANIMATION_TYPE_NONE);
		//		// 简单动画
		//		SpotManager.getInstance(mContext)
		//		                    .setAnimationType(SpotManager.ANIMATION_TYPE_SIMPLE);
		// 高级动画
		SpotManager.getInstance(mContext)
		                    .setAnimationType(SpotManager.ANIMATION_TYPE_ADVANCED);

		Button btnShowSpotAd = (Button) findViewById(R.id.btn_show_spot_ad);

		btnShowSpotAd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 展示插屏广告
				SpotManager.getInstance(mContext).showSpot(mContext, new SpotListener() {

					@Override
					public void onShowSuccess() {
						logInfo("插屏展示成功");
					}

					@Override
					public void onShowFailed(int errorCode) {
						logError("插屏展示失败");
						switch (errorCode) {
						case ErrorCode.NON_NETWORK:
							showShortToast("网络异常");
							break;
						case ErrorCode.NON_AD:
							showShortToast("暂无插屏广告");
							break;
						case ErrorCode.RESOURCE_NOT_READY:
							showShortToast("插屏资源还没准备好");
							break;
						case ErrorCode.SHOW_INTERVAL_LIMITED:
							showShortToast("请勿频繁展示");
							break;
						case ErrorCode.WIDGET_NOT_IN_VISIBILITY_STATE:
							showShortToast("请设置插屏为可见状态");
							break;
						default:
							showShortToast("请稍后再试");
							break;
						}
					}

					@Override
					public void onSpotClosed() {
						logDebug("插屏被关闭");
					}

					@Override
					public void onSpotClicked(boolean isWebPage) {
						logDebug("插屏被点击");
						logInfo("是否是网页广告？%s", isWebPage ? "是" : "不是");
					}
				});
			}
		});
	}
	

}

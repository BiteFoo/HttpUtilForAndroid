package com.hss01248.netdemo.akulaku.bean;

import java.util.List;

/**
 * Created by huangshuisheng on 2017/9/29.
 */

public class CommonConfig {

    /**
     * referrerFriendCouponAmountV2 : RM50
     * csPhoneNumber : +0322603660
     * authTitleCreditLimitParam : ["RM500","+ RM500","+ RM500","+ RM1,000","+ RM1,500","+ RM1,500"]
     * menuList : [{"id":2,"countryId":2,"icon":"https://s3-ap-northeast-1.amazonaws.com/com-silvrr-installment/config/app_menu_icon/volunteer.jpg","name":"Volunteer","url":"https://test.mall.akulaku.com/volunteer/volunteer.html","orderNo":0,"status":1,"createTime":0}]
     * aboutUsUrl : http://test.app.akulaku.com/about_us.html
     * mallIndex : https://test.mall.akulaku.com/index.html?v=20160405
     * beginnerGuidePageCreditLimitAmount : [500,500,500,1500,5000,5000]
     * contactUsUrl : http://test.app.akulaku.com/contact_us.html
     * referrerMyCouponAmountV2 : RM40
     * mallCategoryUrl : https://test.mall.akulaku.com/category.html?v=20160615
     * orderCancelFee : RM10
     */

    public String referrerFriendCouponAmountV2;
    public String csPhoneNumber;
    public String aboutUsUrl;
    public String mallIndex;
    public String contactUsUrl;
    public String referrerMyCouponAmountV2;
    public String mallCategoryUrl;
    public String orderCancelFee;
    public List<String> authTitleCreditLimitParam;
    public List<MenuListBean> menuList;
    public List<Integer> beginnerGuidePageCreditLimitAmount;

    public static class MenuListBean {
        /**
         * id : 2
         * countryId : 2
         * icon : https://s3-ap-northeast-1.amazonaws.com/com-silvrr-installment/config/app_menu_icon/volunteer.jpg
         * name : Volunteer
         * url : https://test.mall.akulaku.com/volunteer/volunteer.html
         * orderNo : 0
         * status : 1
         * createTime : 0
         */

        public int id;
        public int countryId;
        public String icon;
        public String name;
        public String url;
        public int orderNo;
        public int status;
        public int createTime;
    }
}

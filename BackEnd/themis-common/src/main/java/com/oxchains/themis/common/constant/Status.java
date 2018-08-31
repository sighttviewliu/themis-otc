package com.oxchains.themis.common.constant;

/**
 * @author ccl
 * @time 2017-11-06 14:12
 * @name Status
 * @desc:
 */
public interface Status {
    enum LoginStatus{
        LOGOUT(0,"未登录"),LOGIN(1,"已登录");
        private Integer status;
        private String name;

        LoginStatus(Integer status, String name) {
            this.status = status;
            this.name = name;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
    enum TrustStatus{
        NONE(0,"无记录"),TRUST(1,"信任"),SHIELD(2,"屏蔽");
        private Integer status;
        private String name;

        TrustStatus(Integer status, String name) {
            this.status = status;
            this.name = name;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    enum EnableStatus {
        UNENABLED(0,"不可用"),ENABLED(1,"可用");
        private Integer status;
        private String name;

        EnableStatus(Integer status, String name) {
            this.status = status;
            this.name = name;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    enum OrderStatus {
        //订单状态
        WAIT_CONFIRM(1, "待确认"),
        WAIT_PAY(2, "待付款"),
        WAIT_SEND(3, "待收货"),
        WAIT_RECIVE(4, "待收货"),
        WAIT_COMMENT(5, "待评价"),
        FINISH(6, "已完成"),
        CANCEL(7, "已取消"),
        WAIT_REFUND(8, "退款中");

        private int status;
        private String name;
        OrderStatus(int value, String name) {
            this.status = value;
            this.name = name;
        }
        public int getStatus(){
            return this.status;
        }

        public static String getName(Integer status) {
            if (status != null) {
                for (OrderStatus item : OrderStatus.values()) {
                    if (item.status == status.intValue()) {
                        return item.name;
                    }
                }
            }
            return "";
        }
    }

    enum ORDER_STATUS {
        /**
         *  订单状态
         * 10 订单创建中  11 订单已创建,待确认
         * 20 订单确认中  21 订单已确认,待托管 || 22 订单取消中  23 订单已取消(关闭交易)
         * 30 订单托管中  31 订单已托管,待付款
         * 40 订单付款中  41 订单已付款,待收币
         * 50 订单申请仲裁中 51 订单已申请仲裁 || 52 仲裁中  53 已仲裁 || 54 取消仲裁中  55 已取消仲裁
         * 60 订单完成中  61 订单已完成(完成交易)  62 买家评价  63 卖家评价 64 双方都已评价
         *
         */

        CREATING(10, "创建中"), CREATED(11, /*"已创建,待确认"*/"已创建, 待托管"),
        CONFIRMING(20, "确认中"), CONFIRMED(21, "已确认,待托管"),CANCELING(22, "取消中"), CANCELED(23, "已取消"),
        ENTRUSTING(30, "托管中"), ENTRUSTED(31, "已托管,待付款"),
        PAYING(40, "付款中"), PAYED(41, "已付款,待转币"),
        APPLYING(50, "申请仲裁中"), APPLYED(51, "已申请仲裁,执行仲裁中"),ARBITRATING(52, "仲裁中"), ARBITRATED(53, "完成仲裁"),CANCELING_ARBI(54, "取消仲裁中"), CANCELED_ARBI(55, "已取消仲裁"),
        FINISHING(60, "已放币,待收币"), FINISHED(61, "已完成"), BCOMMENT(62, "买家评价"), SCOMMENT(63, "卖家评价"),COMMENT(64, "已评价");



        private int status;
        private String name;
        ORDER_STATUS(int value, String name) {
            this.status = value;
            this.name = name;
        }
        public int getStatus(){
            return this.status;
        }

        public static String getName(Integer status) {
            if (status != null) {
                for (ORDER_STATUS item : ORDER_STATUS.values()) {
                    if (item.status == status.intValue()) {
                        return item.name;
                    }
                }
            }
            return "";
        }
    }

    enum TranStatus {
        //订单交易(POA)状态
        CREATING(10, "订单创建中"), CREATED(11, "订单已创建"),

        CONFIRMING(20,"订单确认中"), CONFIRMED(21,"订单已确认"),
        CANCELING(22,"订单取消中"),    CANCELED(23,"订单已取消"),

        UPLOADING_BUYSEC(30,"买家碎片上传中"), UPLOADED_BUYSEC(31,"买家碎片已上传"),
        UPLOADING_SELLSEC(32,"卖家碎片上传中"), UPLOADED_SELLSEC(33,"卖家碎片已上传"),

        FINISHING(40,"订单完成中"), FINISHED(41,"订单已完成"),
        ARBITRATING(42, "仲裁申请中"),ARBITRATED(43, "仲裁已申请"),
        JUDGING(44, "仲裁判决中"), JUDGED(45, "仲裁已判决");

        private int status;
        private String name;
        TranStatus(int status, String name) {
            this.status = status;
            this.name = name;
        }
        public int getStatus(){
            return this.status;
        }

        public static String getName(int status) {

            for (TranStatus item : TranStatus.values()) {
                if (item.status == status) {
                    return item.name;
                }
            }
            return "";
        }
    }

    enum ArbitrateStatus {
        //仲裁表状态
        NOARBITRATE(0,"未仲裁"),ARBITRATEING(1,"仲裁中"),ARBITRATEEND(2,"仲裁结束");
        private Integer status;
        private String name;
        ArbitrateStatus(Integer status, String description) {
            this.status = status;
            this.name = description;
        }
        public Integer getStatus() {
            return status;
        }
        public static String getName(int status) {
            for (ArbitrateStatus item : ArbitrateStatus.values()) {
                if (item.status == status) {
                    return item.name;
                }
            }
            return "";
        }

    }

    enum CommentStatus{
        GOOD(1,"好评"),NOR(2,"中评"),BAD(3,"差评");
        private Integer status;
        private String desc;

        CommentStatus(Integer status, String desc) {
            this.status = status;
            this.desc = desc;
        }
        public Integer getStatus() {

            return status;
        }

        public String getDesc() {
            return desc;
        }
    }

    enum UserTxDetailHandle{
        BELIEVE(1,"信任"),FIRST_BUY_TIME(2,"验证第一次购买时间"),DESC(3,"评价"),TX_NUM_AMOUNT(4,"交易次数和交易总量");
        private Integer status;
        private String desc;

        UserTxDetailHandle(Integer status, String desc) {
            this.status = status;
            this.desc = desc;
        }
        public Integer getStatus() {
            return status;
        }
        public String getDesc() {
            return desc;
        }
    }

    /**
     * 排序字段
     */
    enum SortField {
        CREATE_TIME("createTime"), ORDER_STATUS("orderStatus");
        private String name;

        SortField(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    enum NoticeStatus {
        //公告的类型
        BUY(1L,"购买"),SELL(2L,"出售");
        private Long status;
        private String name;

        NoticeStatus(Long status, String name) {
            this.status = status;
            this.name = name;
        }
        public Long getStatus() {
            return status;
        }
        public String getName() {
            return name;
        }
    }
    enum  VcurrencyStatus {
        //支付类型
        BTC(1L,"BTC");
        private Long id;
        private String vcurrencyName;

        VcurrencyStatus(Long id, String vcurrencyName) {
            this.id = id;
            this.vcurrencyName = vcurrencyName;
        }
        public Long getId() {
            return id;
        }
        public String getVcurrencyName() {
            return vcurrencyName;
        }
    }

    enum ChainOrderStatus {
        NORMAL(0),
        CREATED(1),
        CANCELED(2),
        CONFIRMED(3),
        SECRET_UPLOADED(4),
        IN_ARBITRATION(5),
        FINISHED(6);
        private Integer status;

        public Integer getStatus() {
            return status;
        }

        ChainOrderStatus(Integer status) {
            this.status = status;
        }
    }

    enum IdentityEnum {
        BUYER(1, "买家"),
        SELLER(2, "卖家");
        private Integer status;
        private String name;

        IdentityEnum(Integer status, String name) {
            this.status = status;
            this.name = name;
        }

        public Integer getStatus() {
            return status;
        }

        public String getName() {
            return name;
        }

        public static String getName(int status) {
            for (IdentityEnum item : IdentityEnum.values()) {
                if (item.status == status) {
                    return item.name;
                }
            }
            return "";
        }
    }

    enum JPushType {
        ZERO(0, "通知有人下订单"),
        ONE(1, "通知可以点击托管按钮"),
        TWO(2, "通知买家付款"),
        THREE(3, "通知卖家释放数字货币"),
        FOUR(4,"通知买家已释放数字货币");
        private Integer status;
        private String name;

        JPushType() {
        }

        JPushType(Integer status, String name) {
            this.status = status;
            this.name = name;
        }

        public Integer getStatus() {
            return status;
        }

        public String getName() {
            return name;
        }
    }

    enum OrderLockEnum {
        NORMAL(0, "正常"), LOCK(1, "锁定");
        private Integer status;
        private String name;

        OrderLockEnum() {
        }

        OrderLockEnum(Integer status, String name) {
            this.status = status;
            this.name = name;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}

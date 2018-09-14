package com.oxchains.themis.common.constant;

/**
 * @author ccl
 * @time 2018-03-07 11:23
 * @name Const
 * @desc:
 */
public interface Const {
    /**
     * COMMA
     */
    String SEPARATOR_COMMA = ",";

    /**
     * UNDERLINE
     */
    String SEPARATOR_UNDERLINE = "_";

    String MAIL_TYPE_TEXT = "text/plain";
    String MAIL_TYPE_HTML = "text/html";
    enum STYPE implements Const{
        WEB_LOGNAME(101,"WEB_BROWSER_LOGINNAME"),
        WEB_EMAIL(102,"WEB_BROWSER_EMAIL"),
        WEB_MOBILEPHONE(103,"WEB_BROWSER_MOBILEPHONE"),

        APP_LOGNAME(201,"APP_LOGINNAME"),
        APP_EMAIL(202,"APP_EMAIL"),
        APP_MOBILEPHONE(203,"APP_MOBILEPHONE");

        private int type;
        private String name;

        STYPE(int type, String name) {
            this.type = type;
            this.name = name;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
    enum ROLE implements Const{
        /**
         * 管理员
         */
        ADMIN(1L,"管理员"),

        /**
         * 客服
         */
        SERVICE(2L,"客服"),

        /**
         * 仲裁
         */
        ARBITRATION(3L,"仲裁"),

        /**
         * 普通用户
         */
        USER(4L,"普通用户");

        private Long id;
        private String name;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        ROLE(Long id, String name) {
            this.id=id;
            this.name=name;
        }
    }

     enum APPLYV implements Const{
         /**
          * 申请
          */
        APPLIED(1),
         /**
          * 取消
          */
         CANCELED(2),
         /**
          * 批准
          */
         APPROVED(3),
         /**
          * 拒绝
          */
         REJECTED(4);

        private Integer status;
         APPLYV(Integer status) {
             this.status = status;
         }

         public Integer getStatus() {
             return status;
         }

         public void setStatus(Integer status) {
             this.status = status;
         }
     }

     enum ENABLE implements Const{
        ENABLEED(1),DISABLED(2);

        private int enable;

         ENABLE(int enable) {
             this.enable = enable;
         }

         public int getEnable() {
             return enable;
         }

         public void setEnable(int enable) {
             this.enable = enable;
         }
     }

     enum VCURR implements Const{
        GET(0,"GET"),
        BTC(1,"BTC"),
         ETH(2,"ETH");
        private int type;
        private String name;

         VCURR(int type, String name) {
             this.type = type;
             this.name = name;
         }

         public int getType() {
             return type;
         }

         public void setType(int type) {
             this.type = type;
         }

         public String getName() {
             return name;
         }

         public void setName(String name) {
             this.name = name;
         }
     }

     enum CFIELD implements Const{
        LOGIN_NAME(1,"loginname"),EMAIL(2,"email"),MOBILE_PHONE(3,"mobilephone");
        private int fieldValue;
        private String fieldName;

         CFIELD(int fieldValue, String fieldName) {
             this.fieldValue = fieldValue;
             this.fieldName = fieldName;
         }

         public int getFieldValue() {
             return fieldValue;
         }

         public void setFieldValue(int fieldValue) {
             this.fieldValue = fieldValue;
         }

         public String getFieldName() {
             return fieldName;
         }

         public void setFieldName(String fieldName) {
             this.fieldName = fieldName;
         }
     }
    enum ORDER_TYPE {
        //公告的类型
        BUY(1,"购买"),SELL(2,"出售");
        private int type;
        private String name;

        ORDER_TYPE(int type, String name) {
            this.type = type;
            this.name = name;
        }
        public int getStatus() {
            return type;
        }
        public String getName() {
            return name;
        }

        public static String getName(Integer type) {
            if (type != null) {
                for (ORDER_TYPE item : ORDER_TYPE.values()) {
                    if (item.type == type.intValue()) {
                        return item.name;
                    }
                }
            }
            return "";
        }
    }
     enum PAYMENT {
         CASH(1L, "现金"),
         BANK_CARD(2L, "银行卡转账"),
         ALI_PAY(3L, "支付宝"),
         WECHAT(4L, "微信"),
         APPLE_PAY(5L, "Apple Pay");

         private long type;
        private String name;

         PAYMENT(long type, String name) {
             this.type = type;
             this.name = name;
         }

         public long getType() {
             return type;
         }

         public void setType(long type) {
             this.type = type;
         }

         public String getName() {
             return name;
         }

         public void setName(String name) {
             this.name = name;
         }
         public static String getName(Long type) {
             if (type != null) {
                 for (PAYMENT item : PAYMENT.values()) {
                     if (item.type == type.longValue()) {
                         return item.name;
                     }
                 }
             }
             return "";
         }
     }

    enum DIGICCY {
        GET(0L, "GET"),
        BTC(1L, "BTC"),
        ETH(2L, "ETH"),
        EOS(3L, "EOS");

        private long type;
        private String name;

        DIGICCY(long type, String name) {
            this.type = type;
            this.name = name;
        }

        public long getType() {
            return type;
        }

        public void setType(long type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
        public static String getName(Long type) {
            if (type != null) {
                for (DIGICCY item : DIGICCY.values()) {
                    if (item.type == type.longValue()) {
                        return item.name;
                    }
                }
            }
            return "";
        }
    }

    enum CURRENCY {
        CNY(1L, "CNY"),
        USD(2L, "USD");

        private long type;
        private String name;

        CURRENCY(long type, String name) {
            this.type = type;
            this.name = name;
        }

        public long getType() {
            return type;
        }

        public void setType(long type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
        public static String getName(Long type) {
            if (type != null) {
                for (CURRENCY item : CURRENCY.values()) {
                    if (item.type == type.longValue()) {
                        return item.name;
                    }
                }
            }
            return "";
        }
    }

}

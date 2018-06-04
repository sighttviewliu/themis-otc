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
    enum STYPE implements Const {
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
    enum ROLE implements Const {
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

     enum APPLYV implements Const {
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

     enum ENABLE implements Const {
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

     enum VCURR implements Const {
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

     enum CFIELD implements Const {
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
}

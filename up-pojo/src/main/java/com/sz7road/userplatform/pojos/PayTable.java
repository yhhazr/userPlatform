package com.sz7road.userplatform.pojos;

import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @author jeremy
 */
public class PayTable extends HashMap<PayTable.PayEntryKey, PayTable.PayEntry> {

    /**
     * @author jeremy
     */
    public static interface PayEntryKey extends Serializable {

        /**
         * @return 充值渠道ＩＤ。
         */
        CharSequence getChannelId();

        /**
         * @return 充值渠道支付类型
         */
        int getSubType();

        /**
         * @return 支付类型标识
         */
        String getSubTag();

    }

    /**
     * @author jeremy
     */
    public static class PayEntry implements Serializable {

        private CharSequence channelId;
        private int subType;
        private String subTag;
        private int scale;
        private byte status;

        private String channelName;
        private String subTypeName;
        private String subTagName;

        /**
         * Constructs by default.
         */
        public PayEntry() {
            super();
        }

        public CharSequence getChannelId() {
            return channelId;
        }

        public void setChannelId(CharSequence channelId) {
            this.channelId = channelId;
        }

        public int getSubType() {
            return subType;
        }

        public void setSubType(int subType) {
            this.subType = subType;
        }

        public String getSubTag() {
            return subTag;
        }

        public void setSubTag(String subTag) {
            this.subTag = subTag;
        }

        public int getScale() {
            return scale;
        }

        public void setScale(int scale) {
            this.scale = scale;
        }

        public byte getStatus() {
            return status;
        }

        public void setStatus(byte status) {
            this.status = status;
        }

        public String getChannelName() {
            return channelName;
        }

        public void setChannelName(String channelName) {
            this.channelName = channelName;
        }

        public String getSubTypeName() {
            return subTypeName;
        }

        public void setSubTypeName(String subTypeName) {
            this.subTypeName = subTypeName;
        }

        public String getSubTagName() {
            return subTagName;
        }

        public void setSubTagName(String subTagName) {
            this.subTagName = subTagName;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            PayEntry payEntry = (PayEntry) o;

            if (channelId.charAt(0) != payEntry.channelId.charAt(0)) return false;
            if (subType != payEntry.subType) return false;
            if (!subTag.equals(payEntry.subTag)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = (int) channelId.charAt(0);
            result = 31 * result + (int) subType;
            result = 31 * result + Strings.nullToEmpty(subTag).hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "PayTable{" +
                    "channelId=" + channelId +
                    ", scale=" + scale +
                    ", status=" + status +
                    ", channelName='" + channelName + '\'' +
                    ", subTypeName='" + subTypeName + '\'' +
                    ", subTagName='" + subTagName + '\'' +
                    ", subTag='" + subTag + '\'' +
                    ", subType=" + subType +
                    '}';
        }
    }


    public static PayEntryKey getKey(final CharSequence channelId, final int subType, final String subTag) {
        return new PayEntryKey() {
            @Override
            public CharSequence getChannelId() {
                return channelId;
            }

            @Override
            public int getSubType() {
                return subType;
            }

            @Override
            public String getSubTag() {
                return Strings.nullToEmpty(subTag);
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;

                PayEntryKey key = (PayEntryKey) o;

                if (getChannelId().charAt(0) != key.getChannelId().charAt(0)) return false;
                if (getSubType() != key.getSubType()) return false;
                if (!getSubTag().equals(key.getSubTag())) return false;

                return true;
            }

            @Override
            public int hashCode() {
                int result = (int) getChannelId().charAt(0);
                result = 31 * result + (int) getSubType();
                result = 31 * result + Strings.nullToEmpty(getSubTag()).hashCode();
                return result;
            }

            @Override
            public String toString() {
                return "PayEntryKey{channelId=" + getChannelId() +
                        ", subType=" + getSubType() +
                        ", subTag=" + getSubTag() +
                        "}";
            }

        };
    }

}

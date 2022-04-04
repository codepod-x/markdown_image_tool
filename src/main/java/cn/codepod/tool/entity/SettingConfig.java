package cn.codepod.tool.entity;

import cn.codepod.tool.config.component.SettingConfigComponent;

import java.util.Objects;

/**
 * @author zhanglei
 */
public class SettingConfig {
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
    private boolean enable;
    private String filenameFormat;
    private String styleSuffix;

    public SettingConfig() {
        this.filenameFormat = "image/{DATE}_{MD_NAME}_{PIC_NAME}";
        this.endpoint = "oss-cn-chengdu.aliyuncs.com";
        this.enable = false;
    }

    public String getFilenameFormat() {
        return filenameFormat;
    }

    public void setFilenameFormat(String filenameFormat) {
        this.filenameFormat = filenameFormat;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getStyleSuffix() {
        return styleSuffix;
    }

    public void setStyleSuffix(String styleSuffix) {
        this.styleSuffix = styleSuffix;
    }

    public void applyConfig(SettingConfigComponent component) {
        this.endpoint = component.getEndpoint();
        this.enable = component.getEnable();
        this.accessKeyId = component.getAccessKeyId();
        this.accessKeySecret = component.getAccessKeySecret();
        this.bucketName = component.getBucketName();
        this.filenameFormat = component.getFilenameFormat();
        this.styleSuffix = component.getStyleSuffix();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SettingConfig config = (SettingConfig) o;
        return enable == config.enable && Objects.equals(endpoint, config.endpoint) && Objects.equals(accessKeyId, config.accessKeyId) && Objects.equals(accessKeySecret, config.accessKeySecret) && Objects.equals(bucketName, config.bucketName) && Objects.equals(filenameFormat, config.filenameFormat) && Objects.equals(styleSuffix, config.styleSuffix);
    }

    @Override
    public int hashCode() {
        return Objects.hash(endpoint, accessKeyId, accessKeySecret, bucketName, enable, filenameFormat, styleSuffix);
    }
}

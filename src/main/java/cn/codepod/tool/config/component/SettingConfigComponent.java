package cn.codepod.tool.config.component;

import cn.codepod.tool.entity.SettingConfig;

import javax.swing.*;

/**
 * @author zhanglei
 */
public class SettingConfigComponent {
    private JTextField endpoint;
    private JTextField accessKeyId;
    private JTextField accessKeySecret;
    private JTextField bucketName;
    private JCheckBox enable;
    private JPanel mainPanel;
    private JTextField filenameFormat;
    private JTextField styleSuffix;

    public String getEndpoint() {
        return endpoint.getText();
    }

    public String getAccessKeyId() {
        return accessKeyId.getText();
    }

    public String getAccessKeySecret() {
        return accessKeySecret.getText();
    }

    public String getBucketName() {
        return bucketName.getText();
    }

    public boolean getEnable() {
        return enable.isSelected();
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public String getFilenameFormat() {
        return filenameFormat.getText();
    }

    public String getStyleSuffix() {
        return styleSuffix.getText();
    }

    public void applyConfig(SettingConfig config) {
        this.enable.setSelected(config.isEnable());
        this.endpoint.setText(config.getEndpoint());
        this.accessKeyId.setText(config.getAccessKeyId());
        this.accessKeySecret.setText(config.getAccessKeySecret());
        this.bucketName.setText(config.getBucketName());
        this.filenameFormat.setText(config.getFilenameFormat());
        this.styleSuffix.setText(config.getStyleSuffix());
    }
}

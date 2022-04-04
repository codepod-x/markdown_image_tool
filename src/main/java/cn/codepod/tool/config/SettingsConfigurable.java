package cn.codepod.tool.config;

import cn.codepod.tool.config.component.SettingConfigComponent;
import cn.codepod.tool.entity.SettingConfig;
import cn.codepod.tool.service.SettingStateService;
import com.intellij.openapi.options.SearchableConfigurable;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author zhanglei
 */
public class SettingsConfigurable implements SearchableConfigurable {

    private SettingConfigComponent component;

    public SettingsConfigurable() {
    }

    @Override
    public @NotNull @NonNls String getId() {
        return "cn.codepod.tool.config.SettingsConfigurable";
    }

    @Override
    public String getDisplayName() {
        return "Markdown Image Tool";
    }

    @Override
    public @Nullable JComponent createComponent() {
        component = new SettingConfigComponent();
        return component.getMainPanel();
    }

    @Override
    public boolean isModified() {
        SettingConfig state = SettingStateService.getInstance().getState();
        SettingConfig config = new SettingConfig();
        config.applyConfig(component);
        return !config.equals(state);
    }

    @Override
    public void apply() {
        SettingConfig state = SettingStateService.getInstance().getState();
        state.applyConfig(component);
    }

    @Override
    public void reset() {
        SettingConfig state = SettingStateService.getInstance().getState();
        component.applyConfig(state);
    }

    @Override
    public void disposeUIResources() {
        component = null;
    }
}

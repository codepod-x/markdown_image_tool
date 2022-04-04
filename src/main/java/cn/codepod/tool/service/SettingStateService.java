package cn.codepod.tool.service;

import cn.codepod.tool.entity.SettingConfig;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;

/**
 * @author zhanglei
 */
@State(
        name = "cn.codepod.tool.service.SettingStateService",
        storages = @Storage("markdownImageTool.xml")
)
public final class SettingStateService implements PersistentStateComponent<SettingConfig> {

    private final SettingConfig config = new SettingConfig();

    public static SettingStateService getInstance() {
        return ApplicationManager.getApplication().getService(SettingStateService.class);
    }

    @Override
    public @NotNull SettingConfig getState() {
        return this.config;
    }

    @Override
    public void loadState(@NotNull SettingConfig state) {
        XmlSerializerUtil.copyBean(state, this.config);
    }
}

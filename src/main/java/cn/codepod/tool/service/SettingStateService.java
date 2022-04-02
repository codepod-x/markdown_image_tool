package cn.codepod.tool.state;

import com.intellij.openapi.components.PersistentStateComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SettingState implements PersistentStateComponent<SettingState> {
    @Nullable
    @Override
    public SettingState getState() {
        return null;
    }

    @Override
    public void loadState(@NotNull SettingState state) {

    }
}

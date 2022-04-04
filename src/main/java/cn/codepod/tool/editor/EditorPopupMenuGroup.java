package cn.codepod.tool.editor;

import cn.codepod.tool.entity.SettingConfig;
import cn.codepod.tool.service.SettingStateService;
import cn.codepod.tool.util.MarkdownUtil;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.editor.Editor;
import org.jetbrains.annotations.NotNull;

/**
 * @author zhanglei
 * @date 2022/4/4 14:46
 */
public class EditorPopupMenuGroup extends DefaultActionGroup {
    @Override
    public void update(@NotNull AnActionEvent event) {
        Editor editor = event.getData(CommonDataKeys.EDITOR);
        SettingConfig config = SettingStateService.getInstance().getState();
        event.getPresentation().setEnabledAndVisible(MarkdownUtil.isMarkdownFile(editor) && config.isEnable());
    }
}

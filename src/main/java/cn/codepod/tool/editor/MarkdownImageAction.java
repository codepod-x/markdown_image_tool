package cn.codepod.tool.editor;

import cn.codepod.tool.entity.MarkdownImage;
import cn.codepod.tool.entity.SettingConfig;
import cn.codepod.tool.service.SettingStateService;
import cn.codepod.tool.util.MarkdownUtil;
import cn.codepod.tool.util.NotificationUtil;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.util.text.StringUtil;
import org.jetbrains.annotations.NotNull;

/**
 * @author zhanglei
 * @date 2022/4/4 21:35
 */
public abstract class MarkdownImageAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        try {
            Editor editor = event.getData(CommonDataKeys.EDITOR);
            if (editor == null) {
                return;
            }
            Caret caret = editor.getCaretModel().getCurrentCaret();
            caret.selectLineAtCaret();
            String selectedText = caret.getSelectedText();
            int offset = caret.getOffset();
            if (StringUtil.isEmpty(selectedText)) {
                caret.setSelection(offset, offset);
                return;
            }
            MarkdownImage markdownImage = MarkdownUtil.getMarkDownImage(selectedText);

            if (markdownImage == null) {
                caret.setSelection(offset, offset);
                return;
            }
            imageProcess(event, editor, caret, markdownImage);
        } catch (Exception e) {
            e.printStackTrace();
            NotificationUtil.show(NotificationType.ERROR, "An error happened: " + e.getMessage());
        }
    }

    @Override
    public void update(@NotNull AnActionEvent event) {
        boolean enable;
        Editor editor = event.getData(CommonDataKeys.EDITOR);
        enable = MarkdownUtil.isMarkdownFile(editor);
        SettingConfig config = SettingStateService.getInstance().getState();
        enable = enable && config.isEnable();
        event.getPresentation().setEnabledAndVisible(enable);
    }


    /**
     * imageProcess
     *
     * @param event         event
     * @param editor        editor
     * @param caret         caret
     * @param markdownImage markdownImage
     */
    protected abstract void imageProcess(@NotNull AnActionEvent event, Editor editor, Caret caret, MarkdownImage markdownImage);
}

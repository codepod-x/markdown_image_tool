package cn.codepod.tool.editor;

import cn.codepod.tool.entity.MarkdownImage;
import cn.codepod.tool.util.NotificationUtil;
import cn.codepod.tool.util.OssUtil;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.MessageConstants;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

/**
 * @author zhanglei
 * @date 2022/4/4 14:43
 */
public class MarkdownImageDeleteAction extends MarkdownImageAction {

    @Override
    protected void imageProcess(@NotNull AnActionEvent event, Editor editor, Caret caret, MarkdownImage markdownImage) {

        int offset = caret.getOffset();

        int status = Messages.showOkCancelDialog(String.format("Delete file %s ?", markdownImage.getDescription()), "Delete Image", "Yes", "No", null);

        if (status == MessageConstants.OK) {
            OssUtil.delete(markdownImage);
            NotificationUtil.show("The selected file(s) is deleted.");

            WriteCommandAction.runWriteCommandAction(event.getProject(), null, null, () -> {
                String text = caret.getSelectedText();
                text = text.replace(markdownImage.getFull(), "");
                Document document = editor.getDocument();
                int start = caret.getSelectionStart();
                int end = caret.getSelectionEnd();
                document.replaceString(start, end, text);
            });
        }
        caret.setSelection(offset, offset);
    }
}
